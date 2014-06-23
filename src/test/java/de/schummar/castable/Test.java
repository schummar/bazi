package de.schummar.castable;

import javafx.beans.property.Property;

import java.util.Arrays;
import java.util.List;

public class Test
{
	public static void main(String[] args) throws Exception
	{
		CastableObject m = new CastableObject();
		/*m.addDeepListener(System.out::println);

		Party p = m.cast(Party.class);
		p.name("Die Partei");
		p.vote(100.0);
		p.seat(50);
		p.sub().name("Sub");
		p.tests().addAll(Arrays.asList("a", "b", "c"));
		p.parties().add(null);
		p.parties().get(0).name("parties_0");

		P p1 = m.cast(P.class);
		p1.bla("jdlfkfdj");
		p1.parties().get(0).bla("sjfgsdf");


		System.out.println(p.parties());*/

		A a = m.cast(A.class);
		System.out.println(Arrays.asList(m, a));
		a.a().setValue(10);
		System.out.println(Arrays.asList(m, a));
		a.a().asStringProperty(20).setValue("abc");
		System.out.println(Arrays.asList(m, a.a()));


	}

	public static interface P extends Party
	{
		@Attribute Property<String> blaProperty();
		default String bla() { return blaProperty().getValue(); }
		default void bla(String bla) { blaProperty().setValue(bla); }

		@Override @Attribute List<? extends P> parties();
	}

	private interface A extends Data
	{
		@Attribute CProperty<Integer> a();
	}
}
