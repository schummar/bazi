package de.uni_augsburg.bazi.common.algorithm;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.Plugin;

/** A Filter serves as a preprocessor or as a postprocessor for some algorithms. */
public interface Filter extends Plugin.Instance
{
	<T extends Data> Algorithm<T> decorate(Algorithm<T> algorithm);
}
