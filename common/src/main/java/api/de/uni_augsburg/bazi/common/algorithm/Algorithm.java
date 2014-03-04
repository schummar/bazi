package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.data.Data;

import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public interface Algorithm extends Plugin.Instance
{
	List<Object> getInputAttributes();
	Data apply(Data in);
}
