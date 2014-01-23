package de.uni_augsburg.bazi.common;

import java.util.List;
import java.util.Map;


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
	// s.append("/** @see Model */\n");
	// s.append("public static interface Model").append(i).append("<T");
	// for (int j = 0; j < i; j++)
	// s.append(",T").append(j);
	// s.append(">");
	//
	// if (i > 0)
	// {
	// s.append("extends Model").append(i - 1).append("<T");
	// for (int j = 0; j < i - 1; j++)
	// s.append(",T").append(j);
	// s.append(">");
	// }
	//
	// s.append("{\n");
	//
	// s.append("/** Create an instance of the models interface.\n");
	// s.append("* @see Model */\n");
	// s.append("public T New(");
	// for (int j = 0; j < i; j++)
	// s.append("T").append(j).append(" t").append(j).append(j < i - 1 ? "," : "");
	// s.append(");\n");
	//
	// if (i == 0)
	// {
	// s.append("public List<String> getFields();\n");
	// s.append("public Map<String,Object> getDefaultValues();\n");
	// }
	//
	// s.append("}");
	// }
	// System.out.println(s.toString());
	// }


	static abstract class ImplGenerated<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>
			implements
			Model0<T>,
			Model1<T, T0>,
			Model2<T, T0, T1>,
			Model3<T, T0, T1, T2>,
			Model4<T, T0, T1, T2, T3>,
			Model5<T, T0, T1, T2, T3, T4>,
			Model6<T, T0, T1, T2, T3, T4, T5>,
			Model7<T, T0, T1, T2, T3, T4, T5, T6>,
			Model8<T, T0, T1, T2, T3, T4, T5, T6, T7>,
			Model9<T, T0, T1, T2, T3, T4, T5, T6, T7, T8>,
			Model10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>,
			Model11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
			Model12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
			Model13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
			Model14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
			Model15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
			Model16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
			Model17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
			Model18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
			Model19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
			Model20<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>

	{
		protected abstract T _New(Object... data);


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
	}

	/** @see Model */
	public static interface Model0<T>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New();
		public List<String> getFields();
		public Map<String, Object> getDefaultValues();
	}

	/** @see Model */
	public static interface Model1<T, T0> extends Model0<T>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0);
	}

	/** @see Model */
	public static interface Model2<T, T0, T1> extends Model1<T, T0>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1);
	}

	/** @see Model */
	public static interface Model3<T, T0, T1, T2> extends Model2<T, T0, T1>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2);
	}

	/** @see Model */
	public static interface Model4<T, T0, T1, T2, T3> extends Model3<T, T0, T1, T2>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3);
	}

	/** @see Model */
	public static interface Model5<T, T0, T1, T2, T3, T4> extends Model4<T, T0, T1, T2, T3>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4);
	}

	/** @see Model */
	public static interface Model6<T, T0, T1, T2, T3, T4, T5> extends Model5<T, T0, T1, T2, T3, T4>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
	}

	/** @see Model */
	public static interface Model7<T, T0, T1, T2, T3, T4, T5, T6> extends Model6<T, T0, T1, T2, T3, T4, T5>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);
	}

	/** @see Model */
	public static interface Model8<T, T0, T1, T2, T3, T4, T5, T6, T7> extends Model7<T, T0, T1, T2, T3, T4, T5, T6>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);
	}

	/** @see Model */
	public static interface Model9<T, T0, T1, T2, T3, T4, T5, T6, T7, T8> extends Model8<T, T0, T1, T2, T3, T4, T5, T6, T7>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8);
	}

	/** @see Model */
	public static interface Model10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> extends Model9<T, T0, T1, T2, T3, T4, T5, T6, T7, T8>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9);
	}

	/** @see Model */
	public static interface Model11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends Model10<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10);
	}

	/** @see Model */
	public static interface Model12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> extends Model11<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11);
	}

	/** @see Model */
	public static interface Model13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> extends Model12<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12);
	}

	/** @see Model */
	public static interface Model14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> extends Model13<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);
	}

	/** @see Model */
	public static interface Model15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> extends
			Model14<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14);
	}

	/** @see Model */
	public static interface Model16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> extends
			Model15<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15);
	}

	/** @see Model */
	public static interface Model17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> extends
			Model16<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16);
	}

	/** @see Model */
	public static interface Model18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> extends
			Model17<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17);
	}

	/** @see Model */
	public static interface Model19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> extends
			Model18<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18);
	}

	/** @see Model */
	public static interface Model20<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> extends
			Model19<T, T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>
	{
		/** Create an instance of the models interface.
		 * @see Model */
		public T New(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19);
	}
}
