package de.uni_augsburg.bazi.common.data;

import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Attribute<T>
{
	private final String name;
	private final String guiName;
	private final boolean labelEditable;
	private final Function<T, String> toString;
	private final Function<String, T> fromString;
	private final BinaryOperator<T> addOperator;
	private final T neutralElement;
	public Attribute(String name, String guiName, boolean labelEditable, Function<T, String> toString, Function<String, T> fromString, BinaryOperator<T> addOperator, T neutralElement)
	{
		this.name = name;
		this.guiName = guiName;
		this.labelEditable = labelEditable;
		this.toString = toString;
		this.fromString = fromString;
		this.addOperator = addOperator;
		this.neutralElement = neutralElement;
	}

	public static Attribute<String> create(String name, String guiName, boolean editable)
	{
		return new Attribute<>(name, guiName, editable, Object::toString, Function.identity(), null, null);
	}
	public static <T> Attribute<T> create(String name, String guiName, boolean editable, Function<String, T> fromString, BinaryOperator<T> addOperator, T neutralElement)
	{
		return new Attribute<>(name, guiName, editable, Object::toString, fromString, addOperator, neutralElement);
	}
	public static <T> Attribute<T> create(String name, String guiName, boolean editable, Function<T, String> toString, Function<String, T> fromString, BinaryOperator<T> addOperator, T neutralElement)
	{
		return new Attribute<>(name, guiName, editable, toString, fromString, addOperator, neutralElement);
	}


	public String getName()
	{
		return name;
	}
	public String getGuiName()
	{
		return guiName;
	}
	public boolean isLabelEditable()
	{
		return labelEditable;
	}
	public T getNeutralElement()
	{
		return neutralElement;
	}
	public Function<T, String> getToString()
	{
		return toString;
	}
	public Function<String, T> getFromString()
	{
		return fromString;
	}
	public BinaryOperator<T> getAddOperator()
	{
		return addOperator;
	}
}
