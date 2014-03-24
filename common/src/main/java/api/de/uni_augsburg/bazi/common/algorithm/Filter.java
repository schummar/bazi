package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.List;

/** A Filter serves as a preprocessor or as a postprocessor for some algorithms. */
public interface Filter extends Plugin.Instance
{
	/**
	 * Whether this filter should be applied before and after the first algorithm call.
	 * @return true iff this filter should be applied before and after the first algorithm call.
	 */
	boolean applicableGlobally();

	/**
	 * Tells whether this filter should be applied to the given algorithm.
	 * @param algorithm the algorithm in question.
	 * @return true iff this filter should be applied to <b>algorithm</b>.
	 */
	boolean applicableTo(Algorithm algorithm);

	/**
	 * Since the inputs are generic this method gives information about attributes that can or must be included and their types.
	 * @return a list of Attributes this filter uses in its input data.
	 */
	List<Object> getInputAttributes();

	/**
	 * Apply this filter to the input data.
	 * @param in the input data to be filtered
	 */
	void preprocess(Data in);

	/**
	 * Apply this filter to the output data.
	 * @param in the input data just in case this filter needs input data that was lost when applying the wrapped algorithm.
	 * @param out the output data to be filtered.
	 */
	void postprocess(Data in, Data out);
}
