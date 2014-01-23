package de.uni_augsburg.bazi.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.primitives.Primitives;


/** Provides dynamic proxy factories for interfaces.
 * Once correctly defined they allow typesafe one-line instantiation of that interface.
 * 
 * <pre>
 * public static interface Data
 * {
 * 	public static final Model2&lt;Data, String, Integer&gt; MODEL = Model.New(Data.class)
 * 			.add(String.class, &quot;name&quot;)
 * 			.add(Integer.class, &quot;value&quot;)
 * 			.create();
 * 
 * 	public String name();
 * 	public int value();
 * }
 * 
 * public static void main(String[] args)
 * {
 * 	Data d = Data.MODEL.New(&quot;abc&quot;, 10);
 * 	System.out.println(d.name());
 * 	System.out.println(d.value());
 * }
 * </pre> */
public class Model extends ModelGenerated
{
	public static <T> Build0<T> New(Class<T> type)
	{
		return new Impl<>(type, new ArrayList<Field<?>>());
	}


	private static class Impl<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>
			extends ImplGenerated<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>
	{
		private final Class<T> type;
		private List<Field<?>> fields = new ArrayList<>();

		public Impl(Class<T> type, List<Field<?>> fields)
		{
			this.type = type;
			this.fields = fields;
		}

		@Override @SuppressWarnings("unchecked") protected T _New(Object... data)
		{
			return (T) Proxy.newProxyInstance(Model.class.getClassLoader(), new Class<?>[] { type }, new Handler(data));
		}

		@Override @SuppressWarnings({ "unchecked", "rawtypes" }) protected <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16, X17, X18, X19> Impl<T, X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16, X17, X18, X19> _override(
				Class<?>... types)
		{
			List<Field<?>> fields = new ArrayList<>(types.length);
			for (int i = 0; i < types.length; i++)
				fields.add(Field(types[i], this.fields.get(i).name()));
			return new Impl(type, fields);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" }) @Override public <X> Impl add(Class<X> type, String name)
		{
			List<Field<?>> fields = new ArrayList<>(this.fields);
			fields.add(Field(type, name));
			return new Impl(this.type, fields);
		}

		@SuppressWarnings({ "rawtypes" }) @Override public Impl create()
		{
			for (Field<?> field : fields)
			{
				try
				{
					Method method = type.getMethod(field.name());
					Class<?> returnType = Primitives.wrap(method.getReturnType());
					if (Modifier.isStatic(method.getModifiers())
							|| method.getParameterCount() > 0
							|| !returnType.isAssignableFrom(field.type()))
						throw new NoFittingMethod();
				}
				catch (NoSuchMethodException | SecurityException e)
				{
					throw new NoFittingMethod();
				}
			}
			return this;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" }) @Override public <X> Impl extend(Class<X> type)
		{
			return new Impl(type, fields);
		}


		private class Handler implements InvocationHandler
		{
			private final Map<String, Object> data = new HashMap<>();

			public Handler(Object... data)
			{
				for (int i = 0; i < data.length; i++)
				{
					if (data[i] == null)
						continue;

					this.data.put(fields.get(i).name(), data[i]);
				}
			}
			@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
			{
				return data.get(method.getName());
			}
		}
	}


	/** Defines a getter of an interface by type and name. */
	public static <T> Field<T> Field(Class<T> type, String name)
	{
		return new Field<T>(type, name);
	}

	/** Defines a getter of an interface by type and name. */
	public static class Field<T>
	{
		private final Class<T> type;
		private final String name;

		/** Defines a getter of an interface by type and name. */
		public Field(Class<T> type, String name)
		{
			this.type = type;
			this.name = name;
		}
		public Class<T> type()
		{
			return type;
		}
		public String name()
		{
			return name;
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

	public static class WrongType extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
	}
}
