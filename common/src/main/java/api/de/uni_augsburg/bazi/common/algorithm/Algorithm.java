package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.Data;

import java.util.List;

/**
 * Created by Marco on 21.02.14.
 */
public interface Algorithm
{
	List<Object> getInputAttributes();
	Data apply(Data in);
}
