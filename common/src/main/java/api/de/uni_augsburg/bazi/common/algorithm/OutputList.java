package de.uni_augsburg.bazi.common.algorithm;

import de.uni_augsburg.bazi.common.data.Data;

import java.util.List;

public interface OutputList extends Data
{
	List<? extends Data> results();
}
