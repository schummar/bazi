package de.uni_augsburg.bazi.common.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.uni_augsburg.bazi.common.data.CropHelper.crop;

/**
 * Created by Marco on 03.03.14.
 */
public class CopyHelper
{
	@SuppressWarnings("unchecked")
	public static <T> T copy(Object v)
	{
		// Map
		if (v instanceof MapData.ProxyData) v = ((MapData.ProxyData) v).delegate();
		if (v instanceof Map)
		{
			Map map = (Map) v;
			Map newMap = new LinkedHashMap();
			map.forEach((k, w) -> newMap.put(k, copy(w)));
			return (T) newMap;
		}

		// Data instance
		if (v instanceof Data)
		{
			return (T) ((Data) v).copy();
		}

		// List
		if (v instanceof List)
		{
			return (T) ((List) v).stream().map(CopyHelper::copy).collect(Collectors.toList());
		}

		// Hopefully immutable
		return (T) v;
	}

	public static <T extends Data> T copy(Object v, Class<T> type)
	{
		return crop(copy(v), type);
	}
}
