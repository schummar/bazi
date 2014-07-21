package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Function;

public class CastableObject extends SimpleMapProperty<String, Castable> implements Castable, Data
{
	public static CastableObject create(Object o)
	{
		if (o instanceof Data) return ((Data) o).src();
		try
		{
			@SuppressWarnings("unchecked")
			Converter<Object> converter = (Converter<Object>) Converters.get(o.getClass(), null);
			return converter.pack(o).asCastableObject();
		}
		catch (Exception ignore)
		{
		}
		return null;
	}


	public CastableObject()
	{
		super(FXCollections.observableMap(new LinkedHashMap<>()));
		addListener(deepListener);
		addListener(changeListener);
	}

	private final List<InvalidationListener> deepListeners = new ArrayList<>();

	@Override
	public void addDeepListener(InvalidationListener invalidationListener)
	{
		deepListeners.add(invalidationListener);
	}

	@Override
	public void removeDeepListener(InvalidationListener invalidationListener)
	{
		deepListeners.remove(invalidationListener);
	}

	private InvalidationListener deepListener = observable -> new ArrayList<>(deepListeners).forEach(l -> l.invalidated(this));
	private MapChangeListener<String, Castable> changeListener = change -> {
		if (change.wasAdded()) change.getValueAdded().addDeepListener(deepListener);
		else change.getValueRemoved().removeDeepListener(deepListener);
	};


	@Override
	public CastableObject src()
	{
		return this;
	}

	@Override
	public CastableObject copy()
	{
		CastableObject copy = new CastableObject();
		forEach((k, v) -> copy.put(k, v.copy()));
		return copy;
	}

	public <T extends Data> T cast(Class<T> c)
	{
		if (c.isInstance(this)) return c.cast(this);
		if (!c.isInterface()) throw new RuntimeException(c + " is no interface.");

		@SuppressWarnings("unchecked")
		T t = (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{c}, invocationHandler);
		return t;
	}

	@Override
	public void merge(Data that)
	{
		merge((Castable) that.src());
	}


	@Override
	public void merge(Castable castable)
	{
		CastableObject that = castable.asCastableObject();
		that.forEach(
			(k, v) -> {
				if (containsKey(k)) get(k).merge(v);
				else put(k, v);
			}
		);
	}

	@Override
	public void overwrite(Castable castable)
	{
		CastableObject that = castable.asCastableObject();
		//entrySet().removeIf(e -> !that.containsKey(e.getKey()));
		that.forEach(
			(k, v) -> {
				if (containsKey(k)) get(k).overwrite(v);
				else put(k, v);
			}
		);
	}

	@Override public boolean isCastableObject() { return true; }
	@Override public CastableObject asCastableObject() { return this; }


	@Override
	public String toString()
	{
		return getValue().toString();
	}


	private final InvocationHandler invocationHandler = (proxy, method, args) -> {
		if (method.isAnnotationPresent(Attribute.class))
			return get(method);
		if (method.getDeclaringClass().isAssignableFrom(CastableObject.class))
			return method.invoke(CastableObject.this, args);
		if (method.isDefault())
			return invokeDefault(proxy, method, args);
		return null;
	};


	public Object get(Method method) throws Throwable
	{
		if (method.getReturnType().isAssignableFrom(CProperty.class))
			return getProperty(method);
		return getProperty(method).getValue();
	}

	private final Map<Method, Property<?>> propertyCache = new HashMap<>();

	public <T> Property<T> getProperty(Method method)
	{
		@SuppressWarnings("unchecked")
		Property<T> property = (Property<T>) propertyCache.get(method);
		if (property == null)
		{
			String name = method.getName().toLowerCase();
			if (name.endsWith("property")) name = name.substring(0, name.indexOf("property"));
			Converter<T> converter = Converters.get(method);
			String def = method.getAnnotation(Attribute.class).def();
			Castable castable = getObj(name, def);
			Function<String, String> validator = converter.createValidator(def(converter, def));
			property = new CProperty<>(castable, converter, validator);
			//propertyCache.put(method, property);
		}
		return property;
	}

	public <T> Property<T> getProperty(String name, Converter<T> converter)
	{
		return getProperty(name, converter, null);
	}

	public <T> Property<T> getProperty(String name, Converter<T> converter, String def)
	{
		Function<String, String> validator = converter.createValidator(def(converter, def));
		return new CProperty<>(getObj(name, def), converter, validator);
	}

	private <T> T def(Converter<T> converter, String def)
	{
		return def == null || Attribute.NULL.equals(def) ? null : converter.unpack(new CastableString(def));
	}

	private Castable getObj(String name, String def)
	{
		Castable o = get(name);
		if (o == null)
		{
			o = (Attribute.NULL.equals(def))
				? new CastableUninitialized()
				: new CastableString(def);
			put(name, o);
		}
		return o;
	}


	private static final Constructor<MethodHandles.Lookup> lookupConstructor;

	static
	{
		Constructor<MethodHandles.Lookup> temp = null;
		try
		{
			temp = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
		}
		catch (NoSuchMethodException ignore)
		{
		}
		lookupConstructor = temp;
		if (lookupConstructor != null && !lookupConstructor.isAccessible())
			lookupConstructor.setAccessible(true);
	}

	private Object invokeDefault(Object obj, Method method, Object... args) throws Throwable
	{
		final Class<?> declaringClass = method.getDeclaringClass();
		return lookupConstructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
			.unreflectSpecial(method, declaringClass)
			.bindTo(obj)
			.invokeWithArguments(args);
	}


	@Override
	public int hashCode()
	{
		return System.identityHashCode(this);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Data) obj = ((Data) obj).src();
		return this == obj;
	}
}
