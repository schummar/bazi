package de.uni_augsburg.bazi.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** Provides dynamic proxy factories for interfaces.
 * Once correctly defined they allow typesafe one-line instantiation of that interface.
 * 
 * <pre>
 * public static void main(String[] args)
 * {
 * 	Data d = Data.MODEL.New(&quot;abc&quot;, 10);
 * 	System.out.println(d.name());
 * 	System.out.println(d.value());
 * }
 * 
 * public static interface Data
 * {
 * 	public static final Model2&lt;Data, String, Integer&gt; MODEL = Model.create(Data.class, &quot;name&quot;, &quot;value&quot;);
 * 
 * 	public String name();
 * 	public int value();
 * }
 * </pre> */
public class Model extends ModelGenerated
{
	public static void main(String[] args)
	{
		Data d = Data.MODEL.New("abc", 10);
		System.out.println(d.name());
		System.out.println(d.value());
	}

	public static interface Data
	{
		public static final Model2<Data, String, Integer> MODEL = Model.create(Data.class)
				.add("name", "value")
				.build();

		public String name();
		public int value();
	}


	public static <T> ModelBuilder<T> create(Class<T> type)
	{
		return new Impl<>(type);
	}

	public static interface ModelBuilder<T>
	{
		/** Adds the methods of that interface to this model.
		 * @see Model */
		public ModelBuilder<T> extend(Model0<? super T> that);
		/** Adds these methods to this model.
		 * @see Model */
		public ModelBuilder<T> add(String... fields);
		/** Sets these default values for this model. (Same order as defined)
		 * @see Model */
		public ModelBuilder<T> defaults(Object... values);
		/** Builds the final Model.
		 * @see Model */
		public <X extends Model0<T>> X build();
	}

	private static class Impl<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>
			extends ImplGenerated<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>
			implements ModelBuilder<T>
	{
		private final Class<T> type;
		private final List<String> fields = new ArrayList<>();
		private final List<String> declaredFields = new ArrayList<>();
		private final Map<String, Object> defaultValues = new HashMap<>();

		public Impl(Class<T> type)
		{
			this.type = type;
		}

		@Override public List<String> getFields()
		{
			return fields;
		}

		@Override public Map<String, Object> getDefaultValues()
		{
			return defaultValues;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" }) @Override public Impl build()
		{
			return this;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" }) @Override public Impl add(String... fields)
		{
			for (String field : fields)
			{
				try
				{
					Method method = type.getMethod(field);
					if (Modifier.isStatic(method.getModifiers())
							|| method.getParameterCount() > 0)
						throw new NoFittingMethod();
				}
				catch (NoSuchMethodException | SecurityException e)
				{
					throw new NoFittingMethod();
				}
			}
			declaredFields.addAll(Arrays.asList(fields));
			this.fields.addAll(Arrays.asList(fields));
			return this;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" }) @Override public Impl defaults(Object... values)
		{
			for (int i = 0; i < values.length; i++)
			{
				if (declaredFields.size() <= i)
					throw new TooMuchData();

				defaultValues.put(declaredFields.get(i), values[i]);
			}
			return this;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" }) @Override public Impl extend(Model0<? super T> that)
		{
			fields.addAll(that.getFields());
			defaultValues.putAll(that.getDefaultValues());
			return this;
		}

		@Override @SuppressWarnings("unchecked") protected T _New(Object... data)
		{
			return (T) Proxy.newProxyInstance(Model.class.getClassLoader(), new Class<?>[] { type }, new Handler(data));
		}

		private class Handler implements InvocationHandler
		{
			private final Map<String, Object> data = new HashMap<>(defaultValues);

			public Handler(Object... data)
			{
				for (int i = 0; i < data.length; i++)
				{
					if (fields.size() <= i)
						throw new TooMuchData();

					if (data[i] == null)
						continue;

					this.data.put(fields.get(i), data[i]);
				}
			}
			@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
			{
				return data.get(method.getName());
			}
		}
	}

	public static class NoFittingMethod extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
	}

	public static class TooMuchData extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
	}

	public static class IsAlreadyFinished extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
	}
}
