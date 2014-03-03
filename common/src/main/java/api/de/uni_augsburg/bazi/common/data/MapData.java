package de.uni_augsburg.bazi.common.data;

import com.google.common.base.Defaults;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.uni_augsburg.bazi.common.data.Getter.asGetter;
import static de.uni_augsburg.bazi.common.data.Setter.asSetter;

public class MapData extends LinkedHashMap<String, Object> implements InvocationHandler, Data
{
	public interface ProxyData
	{
		MapData delegate();
	}


	public static MapData fromDataInterface(Data obj)
	{
		if (obj instanceof MapData) return (MapData) obj;
		if (obj instanceof ProxyData) return ((ProxyData) obj).delegate();

		MapData data = new MapData();
		for (Getter getter : Getter.getters(obj.getClass()))
			data.put(getter.key(), getter.invoke(obj));
		return data;
	}


	private final Map<Class<?>, Data> proxies = new HashMap<>();

	public MapData() {}
	public MapData(Map<?, ?> m) { m.forEach((k, v) -> put(k.toString(), v)); }


	@Override public <T extends Data> T cast(Class<? extends T> type)
	{
		if (!type.isInterface()) throw new RuntimeException("no interface!");

		Object proxy = proxies.get(type);
		if (proxy == null)
			proxy = Proxy.newProxyInstance(
				MapData.class.getClassLoader(),
				new Class<?>[]{ProxyData.class, type},
				this
			);

		@SuppressWarnings("unchecked")
		T t = (T) proxy;

		proxies.put(type, t);
		return t;
	}


	@Override public Data merge(Data value)
	{
		return MergeHelper.merge(this, value);
	}


	@Override public MapData copy()
	{
		return new MapData(CopyHelper.copy(this));
	}


	@Override public <T extends Data> T copy(Class<T> type)
	{
		return CopyHelper.copy(this, type);
	}


	@Override public <T extends Data> T crop(Class<T> type)
	{
		return CropHelper.crop(this, type);
	}


	@Override public Map<String, Object> serialize()
	{
		return this;
	}


	@SuppressWarnings("unchecked")
	@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		if (overriddenBy(method, getClass().getMethod("cast", Class.class)))
			return cast((Class<? extends Data>) args[0]);

		if (overriddenBy(method, getClass().getMethod("merge", Data.class)))
			return merge((Data) args[0]);

		if (overriddenBy(method, getClass().getMethod("copy")))
			return copy();

		if (overriddenBy(method, getClass().getMethod("copy", Class.class)))
			return copy((Class<? extends Data>) args[0]);

		if (overriddenBy(method, getClass().getMethod("crop", Class.class)))
			return crop((Class<? extends Data>) args[0]);

		if (overriddenBy(method, getClass().getMethod("serialize")))
			return serialize();

		if (overriddenBy(method, Object.class.getMethod("toString")))
			return toString();

		if (overriddenBy(method, ProxyData.class.getMethod("delegate")))
			return this;

		Getter getter = asGetter(method);
		if (getter != null)
		{
			if (!containsKey(getter.key())) return Defaults.defaultValue(method.getReturnType());
			Object value = CastHelper.cast(get(getter.key()), getter.type());
			put(getter.key(), value);
			return value;
		}

		Setter setter = asSetter(method);
		if (setter != null)
		{
			put(setter.key(), args[0]);
			return null;
		}

		return Defaults.defaultValue(method.getReturnType());
	}


	private static boolean overriddenBy(Method m0, Method m1)
	{
		if (!m0.getName().equals(m1.getName())
			|| !m0.getReturnType().isAssignableFrom(m1.getReturnType())
			|| m0.getParameterCount() != m1.getParameterCount())
			return false;

		for (int i = 0; i < m0.getParameterCount(); i++)
			if (!m1.getParameterTypes()[i].isAssignableFrom(m0.getParameterTypes()[i]))
				return false;
		return true;
	}


	@Override public String toString()
	{
		if (values().stream().anyMatch(
			v ->
				v instanceof Data || v instanceof List || v instanceof Map
		))
			return "{\n  " + entrySet().stream().map(Object::toString).collect(Collectors.joining(",\n")).replaceAll("\n", "\n  ") + "\n}";
		return super.toString();
	}


}
