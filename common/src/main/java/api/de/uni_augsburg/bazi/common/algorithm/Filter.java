package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public interface Filter extends Plugin.Instance
{
	boolean applicableGlobally();
	boolean applicableTo(Algorithm algorithm);
	List<Object> getInputAttributes();
	void preprocess(Data in);
	void postprocess(Data in, Data out);
}
