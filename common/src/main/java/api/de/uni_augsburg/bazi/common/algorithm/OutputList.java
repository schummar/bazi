package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.data.Data;

import java.util.List;

/**
 * Created by Marco on 12.03.2014.
 */
public interface OutputList extends Data
{
	List<? extends Data> results();
}
