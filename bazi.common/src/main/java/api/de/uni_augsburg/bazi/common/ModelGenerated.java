package de.uni_augsburg.bazi.common;


public class ModelGenerated
{
	// public static void main(String[] args)
	// {
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i <= 20; i++)
	// {
	// sb.append("public <");
	// for (int j = 0; j < i; j++)
	// sb.append("X").append(j).append(j < i - 1 ? "," : "");
	// sb.append("> Build").append(i).append("<T");
	// for (int j = 0; j < i; j++)
	// sb.append(",T").append(j);
	// sb.append("> override(");
	// for (int j = 0; j < i; j++)
	// sb.append("Class<X").append(j).append("> x").append(j).append(j < i - 1 ? "," : "");
	// sb.append("){\n");
	// sb.append("return _override(");
	// for (int j = 0; j < i; j++)
	// sb.append("x").append(j).append(j < i - 1 ? "," : "");
	// sb.append(");}\n");
	// }
	// System.out.println(sb);
	// }

	// public static void main(String[] args)
	// {
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i <= 20; i++)
	// {
	// sb.append("Build").append(i).append("<T");
	// for (int j = 0; j < i; j++)
	// sb.append(",T").append(j);
	// sb.append(">, Model").append(i).append("<T");
	// for (int j = 0; j < i; j++)
	// sb.append(",T").append(j);
	// sb.append(">,\n");
	// }
	// for (int i = 0; i < 20; i++)
	// {
	// sb.append("public T New(");
	// for (int j = 0; j < i; j++)
	// sb.append("T").append(j).append(" t").append(j).append(j < i - 1 ? "," : "");
	// sb.append("){\n");
	// sb.append("return _New(");
	// for (int j = 0; j < i; j++)
	// sb.append("t").append(j).append(j < i - 1 ? "," : "");
	// sb.append(");}\n");
	// }
	// System.out.println(sb);
	// }

	// public static void main(String[] args)
	// {
	// StringBuilder s = new StringBuilder();
	// for (int i = 0; i <= 20; i++)
	// {
	// s.append("/** @see Model */\n")
	// .append("public static interface Build").append(i).append("<T");
	// for (int j = 0; j < i; j++)
	// s.append(",T").append(j);
	// s.append(">{\n");
	// s.append("/** Add a field with the given type and name to the model.\n");
	// s.append("* @see Model */\n");
	// s.append("public <X> Build").append(i + 1).append("<T");
	// for (int j = 0; j < i; j++)
	// s.append(",T").append(j);
	// s.append(",X> add(Class<X> type, String name);\n");
	// s.append("/** Check consistency and finish model.\n");
	// s.append("* @see Model */\n");
	// s.append("public Model").append(i).append("<T");
	// for (int j = 0; j < i; j++)
	// s.append(",T").append(j);
	// s.append("> create();\n");
	// s.append("/** Change the interface of the model.\n");
	// s.append("* @see Model */\n");
	// s.append("public <X> Build").append(i).append("<X");
	// for (int j = 0; j < i; j++)
	// s.append(",T").append(j);
	// s.append("> extend(Class<X> type);\n");
	// s.append("/** Change the types of all the models fields.\n");
	// s.append("* @see Model */\n");
	// s.append("public <");
	// for (int j = 0; j < i; j++)
	// s.append("X").append(j).append(j < i - 1 ? "," : "");
	// s.append("> Build").append(i).append("<T");
	// for (int j = 0; j < i; j++)
	// s.append(",T").append(j);
	// s.append("> override(");
	// for (int j = 0; j < i; j++)
	// s.append("Class<X").append(j).append("> type").append(j).append(j < i - 1 ? "," : "");
	// s.append(");}\n");
	//
	// s.append("/** @see Model */\n");
	// s.append("public static interface Model").append(i).append("<T");
	// for (int j = 0; j < i; j++)
	// s.append(",T").append(j);
	// s.append("> extends Build").append(i).append("<T");
	// for (int j = 0; j < i; j++)
	// s.append(",T").append(j);
	// s.append(">{\n");
	// s.append("/** Create an instance of the models interface.\n");
	// s.append("* @see Model */\n");
	// s.append("public T New(");
	// for (int j = 0; j < i; j++)
	// s.append("T").append(j).append(" t").append(j).append(j < i - 1 ? "," : "");
	// s.append(");}\n");
	// }
	// System.out.println(s.toString());
	// }


	static abstract class ImplGenerated<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>
			implements Build0<T>, Model0<T>,
			Build1<T, T0>, Model1<T, T0>,
			Build2<T, T0, T1>, Model2<T, T0, T1>,
			Build3<T, T0, T1, T2>, Model3<T, T0, T1, T2>,
			Build4<T, T0, T1, T2, T3>, Model4<T, T0, T1, T2, T3>,
			Build5<T, T0, T1, T2, T3, T4>, Model5<T, T0, T1, T2, T3, T4>,
			Build6<T, T0, T1, T2, T3, T4, T5>, Model6<T, T0, T1, T2, T3, T4, T5>,
			Build7<T, T0, T1, T2, T3, T4, T5, T6>, Model7<T, T0, T1, T2, T3, T4, T5, T6>,
			Build8<T, T0, T1, T2, T3, T4, T5, T6, T7>, Model8<T, T0, T1, T2, T3, T4, T5, T6, T7>,
			Build9<T, T0, T1, T2, T3, T4, T5, T6, T7, T8>, Model9<T, T0, T1, T2, T3, T4, T5, T6, T7, T8>,
			Build10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>, Model10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>,
			Build11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, Model11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
			Build12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>, Model12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
			Build13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>, Model13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
			Build14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>, Model14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
			Build15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>, Model15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
			Build16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>, Model16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
			Build17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>, Model17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
			Build18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
			Model18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
			Build19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
			Model19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
			Build20<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>,
			Model20<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>

	{
		protected abstract T _New(Object... data);
		protected abstract <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16, X17, X18, X19> ImplGenerated<T, X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16, X17, X18, X19> _override(
				Class<?>... types);
		@Override @SuppressWarnings({ "unchecked", "rawtypes" }) public abstract ImplGenerated create();
		@Override @SuppressWarnings({ "unchecked", "rawtypes" }) public abstract <X> ImplGenerated extend(Class<X> type);
		@Override @SuppressWarnings({ "unchecked", "rawtypes" }) public abstract <X> ImplGenerated add(Class<X> type, String name);


		@Override public T New()
		{
			return _New();
		}
		@Override public T New(T0 t0)
		{
			return _New(t0);
		}
		@Override public T New(T0 t0, T1 t1)
		{
			return _New(t0, t1);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2)
		{
			return _New(t0, t1, t2);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3)
		{
			return _New(t0, t1, t2, t3);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4)
		{
			return _New(t0, t1, t2, t3, t4);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5)
		{
			return _New(t0, t1, t2, t3, t4, t5);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
		}
		@Override public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18,
				T19 t19)
		{
			return _New(t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
		}


		@Override public <X0> Build1<T, T0> override(Class<X0> x0)
		{
			return _override(x0);
		}
		@Override public <X0, X1> Build2<T, T0, T1> override(Class<X0> x0, Class<X1> x1)
		{
			return _override(x0, x1);
		}
		@Override public <X0, X1, X2> Build3<T, T0, T1, T2> override(Class<X0> x0, Class<X1> x1, Class<X2> x2)
		{
			return _override(x0, x1, x2);
		}
		@Override public <X0, X1, X2, X3> Build4<T, T0, T1, T2, T3> override(Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3)
		{
			return _override(x0, x1, x2, x3);
		}
		@Override public <X0, X1, X2, X3, X4> Build5<T, T0, T1, T2, T3, T4> override(Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3, Class<X4> x4)
		{
			return _override(x0, x1, x2, x3, x4);
		}
		@Override public <X0, X1, X2, X3, X4, X5> Build6<T, T0, T1, T2, T3, T4, T5> override(Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3, Class<X4> x4, Class<X5> x5)
		{
			return _override(x0, x1, x2, x3, x4, x5);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6> Build7<T, T0, T1, T2, T3, T4, T5, T6> override(Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3, Class<X4> x4,
				Class<X5> x5, Class<X6> x6)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7> Build8<T, T0, T1, T2, T3, T4, T5, T6, T7> override(Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3, Class<X4> x4,
				Class<X5> x5, Class<X6> x6, Class<X7> x7)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8> Build9<T, T0, T1, T2, T3, T4, T5, T6, T7, T8> override(Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3,
				Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9> Build10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> override(Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3,
				Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8, Class<X9> x9)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10> Build11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> override(Class<X0> x0, Class<X1> x1, Class<X2> x2,
				Class<X3> x3, Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8, Class<X9> x9, Class<X10> x10)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11> Build12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> override(Class<X0> x0, Class<X1> x1,
				Class<X2> x2, Class<X3> x3, Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8, Class<X9> x9, Class<X10> x10, Class<X11> x11)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12> Build13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> override(Class<X0> x0, Class<X1> x1,
				Class<X2> x2, Class<X3> x3, Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8, Class<X9> x9, Class<X10> x10, Class<X11> x11, Class<X12> x12)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13> Build14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> override(Class<X0> x0,
				Class<X1> x1, Class<X2> x2, Class<X3> x3, Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8, Class<X9> x9, Class<X10> x10, Class<X11> x11,
				Class<X12> x12, Class<X13> x13)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14> Build15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> override(
				Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3, Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8, Class<X9> x9, Class<X10> x10, Class<X11> x11,
				Class<X12> x12, Class<X13> x13, Class<X14> x14)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15> Build16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> override(
				Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3, Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8, Class<X9> x9, Class<X10> x10, Class<X11> x11,
				Class<X12> x12, Class<X13> x13, Class<X14> x14, Class<X15> x15)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16> Build17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> override(
				Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3, Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8, Class<X9> x9, Class<X10> x10, Class<X11> x11,
				Class<X12> x12, Class<X13> x13, Class<X14> x14, Class<X15> x15, Class<X16> x16)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16, X17> Build18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> override(
				Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3, Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8, Class<X9> x9, Class<X10> x10, Class<X11> x11,
				Class<X12> x12, Class<X13> x13, Class<X14> x14, Class<X15> x15, Class<X16> x16, Class<X17> x17)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16, X17, X18> Build19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> override(
				Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3, Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8, Class<X9> x9, Class<X10> x10, Class<X11> x11,
				Class<X12> x12, Class<X13> x13, Class<X14> x14, Class<X15> x15, Class<X16> x16, Class<X17> x17, Class<X18> x18)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17, x18);
		}
		@Override public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16, X17, X18, X19> Build20<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> override(
				Class<X0> x0, Class<X1> x1, Class<X2> x2, Class<X3> x3, Class<X4> x4, Class<X5> x5, Class<X6> x6, Class<X7> x7, Class<X8> x8, Class<X9> x9, Class<X10> x10, Class<X11> x11,
				Class<X12> x12, Class<X13> x13, Class<X14> x14, Class<X15> x15, Class<X16> x16, Class<X17> x17, Class<X18> x18, Class<X19> x19)
		{
			return _override(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17, x18, x19);
		}
	}


	/** @see Model */
	public static interface Build0<T>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build1<T, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model0<T> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build0<X> extend(Class<X> type);
	}

	/** @see Model */
	public static interface Model0<T> extends Build0<T>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New();
	}

	/** @see Model */
	public static interface Build1<T, T0>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build2<T, T0, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model1<T, T0> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build1<X, T0> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0> Build1<T, T0> override(Class<X0> type0);
	}

	/** @see Model */
	public static interface Model1<T, T0> extends Build1<T, T0>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0);
	}

	/** @see Model */
	public static interface Build2<T, T0, T1>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build3<T, T0, T1, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model2<T, T0, T1> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build2<X, T0, T1> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1> Build2<T, T0, T1> override(Class<X0> type0, Class<X1> type1);
	}

	/** @see Model */
	public static interface Model2<T, T0, T1> extends Build2<T, T0, T1>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1);
	}

	/** @see Model */
	public static interface Build3<T, T0, T1, T2>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build4<T, T0, T1, T2, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model3<T, T0, T1, T2> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build3<X, T0, T1, T2> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2> Build3<T, T0, T1, T2> override(Class<X0> type0, Class<X1> type1, Class<X2> type2);
	}

	/** @see Model */
	public static interface Model3<T, T0, T1, T2> extends Build3<T, T0, T1, T2>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2);
	}

	/** @see Model */
	public static interface Build4<T, T0, T1, T2, T3>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build5<T, T0, T1, T2, T3, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model4<T, T0, T1, T2, T3> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build4<X, T0, T1, T2, T3> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3> Build4<T, T0, T1, T2, T3> override(Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3);
	}

	/** @see Model */
	public static interface Model4<T, T0, T1, T2, T3> extends Build4<T, T0, T1, T2, T3>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3);
	}

	/** @see Model */
	public static interface Build5<T, T0, T1, T2, T3, T4>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build6<T, T0, T1, T2, T3, T4, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model5<T, T0, T1, T2, T3, T4> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build5<X, T0, T1, T2, T3, T4> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4> Build5<T, T0, T1, T2, T3, T4> override(Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3, Class<X4> type4);
	}

	/** @see Model */
	public static interface Model5<T, T0, T1, T2, T3, T4> extends Build5<T, T0, T1, T2, T3, T4>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4);
	}

	/** @see Model */
	public static interface Build6<T, T0, T1, T2, T3, T4, T5>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build7<T, T0, T1, T2, T3, T4, T5, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model6<T, T0, T1, T2, T3, T4, T5> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build6<X, T0, T1, T2, T3, T4, T5> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5> Build6<T, T0, T1, T2, T3, T4, T5> override(Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3, Class<X4> type4, Class<X5> type5);
	}

	/** @see Model */
	public static interface Model6<T, T0, T1, T2, T3, T4, T5> extends Build6<T, T0, T1, T2, T3, T4, T5>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
	}

	/** @see Model */
	public static interface Build7<T, T0, T1, T2, T3, T4, T5, T6>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build8<T, T0, T1, T2, T3, T4, T5, T6, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model7<T, T0, T1, T2, T3, T4, T5, T6> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build7<X, T0, T1, T2, T3, T4, T5, T6> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6> Build7<T, T0, T1, T2, T3, T4, T5, T6> override(Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3, Class<X4> type4,
				Class<X5> type5, Class<X6> type6);
	}

	/** @see Model */
	public static interface Model7<T, T0, T1, T2, T3, T4, T5, T6> extends Build7<T, T0, T1, T2, T3, T4, T5, T6>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);
	}

	/** @see Model */
	public static interface Build8<T, T0, T1, T2, T3, T4, T5, T6, T7>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build9<T, T0, T1, T2, T3, T4, T5, T6, T7, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model8<T, T0, T1, T2, T3, T4, T5, T6, T7> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build8<X, T0, T1, T2, T3, T4, T5, T6, T7> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7> Build8<T, T0, T1, T2, T3, T4, T5, T6, T7> override(Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3, Class<X4> type4,
				Class<X5> type5, Class<X6> type6, Class<X7> type7);
	}

	/** @see Model */
	public static interface Model8<T, T0, T1, T2, T3, T4, T5, T6, T7> extends Build8<T, T0, T1, T2, T3, T4, T5, T6, T7>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);
	}

	/** @see Model */
	public static interface Build9<T, T0, T1, T2, T3, T4, T5, T6, T7, T8>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model9<T, T0, T1, T2, T3, T4, T5, T6, T7, T8> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build9<X, T0, T1, T2, T3, T4, T5, T6, T7, T8> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8> Build9<T, T0, T1, T2, T3, T4, T5, T6, T7, T8> override(Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3,
				Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8);
	}

	/** @see Model */
	public static interface Model9<T, T0, T1, T2, T3, T4, T5, T6, T7, T8> extends Build9<T, T0, T1, T2, T3, T4, T5, T6, T7, T8>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8);
	}

	/** @see Model */
	public static interface Build10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build10<X, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9> Build10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> override(Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3,
				Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8, Class<X9> type9);
	}

	/** @see Model */
	public static interface Model10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> extends Build10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9);
	}

	/** @see Model */
	public static interface Build11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build11<X, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10> Build11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> override(Class<X0> type0, Class<X1> type1, Class<X2> type2,
				Class<X3> type3, Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8, Class<X9> type9, Class<X10> type10);
	}

	/** @see Model */
	public static interface Model11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends Build11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10);
	}

	/** @see Model */
	public static interface Build12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build12<X, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11> Build12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> override(Class<X0> type0, Class<X1> type1,
				Class<X2> type2, Class<X3> type3, Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8, Class<X9> type9, Class<X10> type10, Class<X11> type11);
	}

	/** @see Model */
	public static interface Model12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> extends Build12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11);
	}

	/** @see Model */
	public static interface Build13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build13<X, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12> Build13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> override(Class<X0> type0, Class<X1> type1,
				Class<X2> type2, Class<X3> type3, Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8, Class<X9> type9, Class<X10> type10,
				Class<X11> type11, Class<X12> type12);
	}

	/** @see Model */
	public static interface Model13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> extends Build13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12);
	}

	/** @see Model */
	public static interface Build14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build14<X, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13> Build14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> override(Class<X0> type0,
				Class<X1> type1, Class<X2> type2, Class<X3> type3, Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8, Class<X9> type9, Class<X10> type10,
				Class<X11> type11, Class<X12> type12, Class<X13> type13);
	}

	/** @see Model */
	public static interface Model14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> extends Build14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);
	}

	/** @see Model */
	public static interface Build15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build15<X, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14> Build15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> override(Class<X0> type0,
				Class<X1> type1, Class<X2> type2, Class<X3> type3, Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8, Class<X9> type9, Class<X10> type10,
				Class<X11> type11, Class<X12> type12, Class<X13> type13, Class<X14> type14);
	}

	/** @see Model */
	public static interface Model15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> extends
			Build15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14);
	}

	/** @see Model */
	public static interface Build16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build16<X, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15> Build16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> override(
				Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3, Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8, Class<X9> type9,
				Class<X10> type10, Class<X11> type11, Class<X12> type12, Class<X13> type13, Class<X14> type14, Class<X15> type15);
	}

	/** @see Model */
	public static interface Model16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> extends
			Build16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15);
	}

	/** @see Model */
	public static interface Build17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build17<X, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16> Build17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> override(
				Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3, Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8, Class<X9> type9,
				Class<X10> type10, Class<X11> type11, Class<X12> type12, Class<X13> type13, Class<X14> type14, Class<X15> type15, Class<X16> type16);
	}

	/** @see Model */
	public static interface Model17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> extends
			Build17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16);
	}

	/** @see Model */
	public static interface Build18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build18<X, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16, X17> Build18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> override(
				Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3, Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8, Class<X9> type9,
				Class<X10> type10, Class<X11> type11, Class<X12> type12, Class<X13> type13, Class<X14> type14, Class<X15> type15, Class<X16> type16, Class<X17> type17);
	}

	/** @see Model */
	public static interface Model18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> extends
			Build18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17);
	}

	/** @see Model */
	public static interface Build19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>
	{
		/** Add a field with the given type and name to the model.
		 * @see Model */
		public <X> Build20<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, X> add(Class<X> type, String name);
		/** Check consistency and finish model.
		 * @see Model */
		public Model19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build19<X, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16, X17, X18> Build19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> override(
				Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3, Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8, Class<X9> type9,
				Class<X10> type10, Class<X11> type11, Class<X12> type12, Class<X13> type13, Class<X14> type14, Class<X15> type15, Class<X16> type16, Class<X17> type17, Class<X18> type18);
	}

	/** @see Model */
	public static interface Model19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> extends
			Build19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18);
	}

	/** @see Model */
	public static interface Build20<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>
	{
		/** Check consistency and finish model.
		 * @see Model */
		public Model20<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> create();
		/** Change the interface of the model.
		 * @see Model */
		public <X> Build20<X, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> extend(Class<X> type);
		/** Change the types of all the models fields.
		 * @see Model */
		public <X0, X1, X2, X3, X4, X5, X6, X7, X8, X9, X10, X11, X12, X13, X14, X15, X16, X17, X18, X19> Build20<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> override(
				Class<X0> type0, Class<X1> type1, Class<X2> type2, Class<X3> type3, Class<X4> type4, Class<X5> type5, Class<X6> type6, Class<X7> type7, Class<X8> type8, Class<X9> type9,
				Class<X10> type10, Class<X11> type11, Class<X12> type12, Class<X13> type13, Class<X14> type14, Class<X15> type15, Class<X16> type16, Class<X17> type17, Class<X18> type18,
				Class<X19> type19);
	}

	/** @see Model */
	public static interface Model20<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> extends
			Build20<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19);
	}

}
