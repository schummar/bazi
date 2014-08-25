package de.schummar.castable;

public class Test {
    public static void main(String[] args) throws Exception {
        CastableObject o = new CastableObject();
        A a = o.cast(A.class);
        CProperty<String> aa = a.a();

        aa.addListener((x, y, z) -> System.out.println(String.format("%s -> %s", y, z)));

        a.a().setValue("1");

        CastableObject o1 = new CastableObject();
        o1.put("a", new CastableString("2"));
        o.overwrite(o1);
    }

    private interface A extends Data {
        @Attribute
        CProperty<String> a();
    }
}
