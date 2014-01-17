package de.uni_augsburg.bazi.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.google.common.primitives.Primitives;

public interface Model
{
	public static interface Definition extends toJsonMixin
	{
		// public
	}


	// public static interface Definition1<T0> extends Definition
	// {
	// public static void test()
	// {}
	// }
	//
	// public static interface Model1<T0, Def extends Definition1<T0>> extends Model
	// {
	// public Def fill(T0 t0);
	// }
	//
	// @SuppressWarnings("unchecked") public static <T0, Def extends Definition1<T0>> Def create(Class<Def> definition, T0 t0)
	// {
	// return (Def) Proxy.newProxyInstance(Model.class.getClassLoader(), new Class<?>[] { definition }, new Handler(definition, t0));
	// }
	//
	// public static interface Definition2<T0, T1> extends Definition
	// {}
	//
	// public static interface Model2<T0, T1, Def extends Definition2<T0, T1>> extends Model
	// {
	// public Def fill(T0 t0, T1 t1);
	// }
	//
	// // @SuppressWarnings("unchecked") public static <T0, T1, Def extends Definition2<T0, T1>> Model2<T0, T1, Def> of(Class<Def> definition)
	// // {
	// // return (t0, t1) -> (Def) Proxy.newProxyInstance(Model.class.getClassLoader(), new Class<?>[] { definition }, new Handler(definition, t0, t1));
	// // }


	public static class Handler implements InvocationHandler
	{
		private final HashMap<String, Object> data;

		public Handler(Class<?> clazz, Object... data)
		{
			this.data = new HashMap<>();

			Method[] methods = clazz.getMethods();
			if (methods.length != data.length)
				throw new WrongData();
			for (int i = 0; i < methods.length; i++)
			{
				Class<?> returnType = methods[i].getReturnType();
				returnType = Primitives.wrap(returnType);

				if (methods[i].getParameterCount() > 0
						|| !returnType.isAssignableFrom(data[i].getClass()))
					throw new WrongData();

				System.out.println(methods[i].getName() + " -> " + data[i]);
				this.data.put(methods[i].getName(), data[i]);
			}
		}

		@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
		{
			return data.get(method.getName());
		}

		public static class WrongData extends RuntimeException
		{
			private static final long serialVersionUID = 1L;
		}
	}

	@SuppressWarnings("hiding") static interface M<X>
	{}

	// static interface ID<T>

	static interface X
	{
		public Object create();
	}

	static interface A<T0> extends X
	{
		public default X with(T0 t)
		{
			return null;
		}
	}

	static interface B<T0, T1> extends A<T0>
	{
		@SuppressWarnings("unchecked") @Override public default A<T1> with(T0 t)
		{
			return (A<T1>) this;
		}
	}

	static interface C<T0, T1, T2> extends B<T0, T1>
	{
		@SuppressWarnings("unchecked") @Override public default B<T1, T2> with(T0 t)
		{
			return (B<T1, T2>) this;
		}
	}

	public class N<R, Y>
	{

	}

	public static <T extends X> T test(Class<T> c)
	{
		// return () -> null;
		return null;
	}

	static interface D extends B<String, Integer>
	{}

	public static void main(String[] args)
	{
		test(D.class);
	}
}
