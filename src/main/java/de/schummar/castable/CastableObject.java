package de.schummar.castable;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.*;

public class CastableObject extends SimpleMapProperty<String, Castable> implements Castable
{

	public CastableObject()
	{
		super(FXCollections.observableMap(new LinkedHashMap<>()));
		addListener(changeListener);
	}


	private final List<InvalidationListener> deepListeners = new ArrayList<>();
	@Override public void addDeepListener(InvalidationListener invalidationListener)
	{
		deepListeners.add(invalidationListener);
	}
	@Override public void removeDeepListener(InvalidationListener invalidationListener)
	{
		deepListeners.remove(invalidationListener);
	}
	private InvalidationListener deepListener = observable -> {
		deepListeners.forEach(l -> l.invalidated(this));
	};
	private MapChangeListener<String, Castable> changeListener = change -> {
		if (change.wasAdded()) change.getValueAdded().addDeepListener(deepListener);
		else change.getValueRemoved().removeDeepListener(deepListener);
	};


	@Override public Castable copy()
	{
		CastableObject copy = new CastableObject();
		forEach((k, v) -> copy.put(k, v.copy()));
		return copy;
	}
	@Override public void overwrite(Castable castable)
	{
		CastableObject that = castable.asCastableObject();
		clear();
		putAll(that);
	}
	@Override public CastableObject asCastableObject() { return this; }


	@Override public String toString() { return getValue().toString(); }


	public <T> T cast(Class<T> c)
	{
		if (!c.isInterface()) throw new RuntimeException(c + " is no interface.");

		@SuppressWarnings("unchecked")
		T t = (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{c}, invocationHandler);
		return t;
	}


	private final InvocationHandler invocationHandler = (proxy, method, args) -> {
		if (method.isAnnotationPresent(Attribute.class))
			return get(method);
		if (method.isDefault())
			return invokeDefault(proxy, method, args);
		if (method.getDeclaringClass() == Object.class)
			return method.invoke(CastableObject.this, args);
		return null;
	};


	Object get(Method method) throws Exception
	{
		if (method.getReturnType().isAssignableFrom(Property.class))
			return getProperty(method);
		return getProperty(method).getValue();
	}

	private final Map<Method, Property> propertyCache = new HashMap<>();
	Property getProperty(Method method) throws Exception
	{
		Property property = propertyCache.get(method);
		if (property == null)
		{
			Converter<?> adapter = getAdapter(method);
			Castable castable = getObj(method, adapter);
			property = new CastBinding<>(castable, adapter);
			propertyCache.put(method, property);
		}
		return property;
	}


	private static Map<Method, Converter> adapterCache = new HashMap<>();
	static Converter getAdapter(Method method) throws Exception
	{
		Converter adapter = adapterCache.get(method);
		if (adapter != null) return adapter;

		Attribute attribute = method.getAnnotation(Attribute.class);
		if (attribute.adapter() != Converter.class) adapter = attribute.adapter().newInstance();
		else
		{
			Type type = method.getReturnType().isAssignableFrom(Property.class)
				? ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0]
				: method.getGenericReturnType();
			Converter contentAdapter = null;
			if (attribute.contentAdapter() != Converter.class) contentAdapter = attribute.contentAdapter().newInstance();
			adapter = Converters.get(type, contentAdapter);
		}
		adapterCache.put(method, adapter);
		return adapter;
	}

	Castable getObj(Method method, Converter adapter)
	{
		String name = method.getName().toLowerCase();
		if (name.endsWith("property")) name = name.substring(0, name.indexOf("property"));
		return get(name, adapter);
	}

	public Castable get(Object key, Converter<?> adapter)
	{
		Castable o = get(key);
		if (o == null && adapter != null) put(key.toString(), o = adapter.applyInverse(null));
		return o;
	}


	private static final Constructor<MethodHandles.Lookup> lookupConstructor;
	static
	{
		Constructor<MethodHandles.Lookup> temp = null;
		try { temp = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class); }
		catch (NoSuchMethodException ignore) { }
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
}
