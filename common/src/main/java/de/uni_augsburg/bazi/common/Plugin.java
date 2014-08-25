package de.uni_augsburg.bazi.common;

import de.schummar.castable.Attribute;
import de.schummar.castable.Convert;
import de.schummar.castable.Data;
import javafx.beans.property.Property;

import java.util.List;
import java.util.Optional;

/**
 * A class that implement Plugin can be found by the PluginManager at runtime
 * and then used to create instances of type <b>T</b>.
 * (Abstract factory pattern)
 * @param <T> the subtype this plugin creates.
 */
public interface Plugin<T extends Plugin.Instance>
{
	/**
	 * The class of the type this plugin creates.
	 * @return the class of the type this plugin creates.
	 */
	Class<? extends T> getInstanceType();

	/**
	 * The list of Attributes this plugin uses in creating instances of <b>T</b>.
	 * Since the parameters are generic this method gives information about attributes that can or must be included and their types.
	 * @return the list of Attributes this plugin uses in creating instances of <b>T</b>.
	 */
	List<Object> getParamAttributes();

	/**
	 * Create an instance of <b>T</b> if the parameters fit. (E.g. the name equals the name of this plugin's instance)
	 * @return an Optional of the new instance if the parameters fit, an empty Optional else.
	 */
	Optional<? extends T> tryInstantiate(Params data);


	/** Minimal interface for plugin parameters. */
	public interface Params extends Data
	{
		/**
		 * The name of the plugin instance.
		 * @return the name of the plugin instance.
		 */
		@Attribute(def = "") Property<String> nameProperty();
		default String name() { return nameProperty().getValue(); }
		default void name(String v) { nameProperty().setValue(v); }
	}

	/** Classes that implement this interface can be created by plugins. */
	@Convert(value = PluginConverter.class, forSubClasses = true)
	public interface Instance
	{}
}
