package de.uni_augsburg.bazi.common.data;

import com.google.common.base.Defaults;
import de.uni_augsburg.bazi.common.plain.PlainSupplier;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

import static de.uni_augsburg.bazi.common.data.CastHelper.raw;
import static de.uni_augsburg.bazi.common.data.Getter.asGetter;
import static de.uni_augsburg.bazi.common.data.Setter.asSetter;

/**
 * Implementation of the {@link Data} interface as String-Object-Map.
 * @see Data
 */
public class MapData extends LinkedHashMap<String, Object> implements InvocationHandler, Data
{
	/** Each proxy created by a MapData object also implements ProxyData. */
	public interface ProxyData
	{
		/**
		 * The MapData object behind this proxied data.
		 * @return the MapData object behind this proxied data.
		 */
		MapData delegate();
	}


	/**
	 * Creates a MapData representation of any Data object.
	 * If <b>obj</b> is not already a MapData instance all its getters are read according to the
	 * {@link Data}-convention and the values put in a new MapData instance.
	 * @param obj the source object.
	 * @return a MapData representation of <b>obj</b>.
	 */
	public static MapData fromDataInterface(Data obj)
	{
		if (obj instanceof MapData) return (MapData) obj;
		if (obj instanceof ProxyData) return ((ProxyData) obj).delegate();

		MapData data = new MapData(obj);
		for (Getter getter : Getter.getters(obj.getClass()))
			data.put(getter.key(), getter.invoke(obj));
		return data;
	}


	private final Object id;
	private final Map<Class<?>, Data> proxies = new HashMap<>();
	private PlainSupplier plain = null;


	/** Default constructor. */
	public MapData()
	{
		id = this;
	}

	/**
	 * Constructor with initial data.
	 * @param m a map with initial data for this MapData. Keys will be used by calling {@link Object#toString()}.
	 */
	public MapData(Map<?, ?> m)
	{
		id = this;
		m.forEach((k, v) -> put(k.toString(), v));
	}

	private MapData(Object id)
	{
		this.id = id;
	}


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
		return CopyHelper.copy(this);
	}


	@Override public <T extends Data> T copy(Class<T> type)
	{
		return CopyHelper.copy(this, type);
	}


	@Override public <T extends Data> T crop(Class<T> type)
	{
		return CropHelper.crop(this, type);
	}


	@Override public MapData toMapData()
	{
		return this;
	}

	@Override public PlainSupplier plain() { return plain; }
	@Override public MapData plain(PlainSupplier plain)
	{
		this.plain = plain;
		return this;
	}


	/**
	 * Handles invokations by a proxy of this object.
	 * If the invoked method is declared by {@link Data} it is simply invoked on this.
	 * If it is a getter or setter according to the {@link Data}-convention
	 * the attribute is returned from / put in this Map.
	 */
	@SuppressWarnings("unchecked")
	@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		if (method.getDeclaringClass().isAssignableFrom(MapData.class))
			return method.invoke(this, args);

		if (method.getDeclaringClass().equals(ProxyData.class))
			return this;


		Getter getter = asGetter(method);
		if (getter != null)
		{
			if (!containsKey(getter.key()))
			{
				if (List.class.isAssignableFrom(raw(getter.type())))
				{
					put(getter.key(), new ArrayList<>());
				}
				else if (Map.class.isAssignableFrom(raw(getter.type())))
				{
					put(getter.key(), new HashMap<>());
				}
				else
				{
					String def = getter.def();
					if (def != null) put(getter.key(), def);
				}
			}
			if (!containsKey(getter.key()))
				return Defaults.defaultValue(method.getReturnType());

			Object value = CastHelper.cast(get(getter.key()), getter.type());
			put(getter.key(), value);
			return value;
		}

		Setter setter = asSetter(method);
		if (setter != null)
		{
			if (args[0] == null) remove(setter.key());
			else put(setter.key(), args[0]);
			return null;
		}

		return Defaults.defaultValue(method.getReturnType());
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


	/**
	 * Whether that object represents the same data as this one.
	 * If that is a ProxyData instance check if its delegate is equal to this.
	 * If that is a MapData instance check if they originated from the same data set. (via {@link #copy()}, etc.)
	 * If that is another Data instance check if this was created by copying that.
	 * @return true iff that object represents the same data as this one.
	 */
	@Override public boolean equals(Object o)
	{
		if (o instanceof ProxyData) o = ((ProxyData) o).delegate();
		if (o instanceof MapData) return id == ((MapData) o).id;
		if (o instanceof Data) return id == o;
		return super.equals(o);
	}

	@Override public int hashCode()
	{
		return System.identityHashCode(id);
	}
}
