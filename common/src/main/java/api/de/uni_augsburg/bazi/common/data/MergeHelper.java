package de.uni_augsburg.bazi.common.data;

import java.util.List;
import java.util.Map;

/**
 * Created by Marco on 03.03.14.
 */
public class MergeHelper
{
	@SuppressWarnings("unchecked")
	public static <T> T merge(Object v1, Object v2)
	{
		if (v1 == null) return (T) v2;
		if (v2 == null) return (T) v1;

		// Map
		if (v1 instanceof MapData.ProxyData) v1 = ((MapData.ProxyData) v1).delegate();
		if (v2 instanceof MapData.ProxyData) v2 = ((MapData.ProxyData) v2).delegate();
		if (v1 instanceof Map && v2 instanceof Map)
		{
			Map m1 = (Map) v1, m2 = (Map) v2;
			m1.replaceAll((k, v) -> merge(v, m2.get(k)));
			m2.forEach(m1::putIfAbsent);
			return (T) m1;
		}

		// Data instance
		if (v1 instanceof Map) v1 = new MapData((Map) v1);
		if (v2 instanceof Map) v2 = new MapData((Map) v2);
		if (v1 instanceof Data && v2 instanceof Data)
		{
			return (T) ((Data) v1).merge((Data) v2);
		}

		// List
		if (v1 instanceof List && v2 instanceof List)
		{
			List l1 = (List) v1, l2 = (List) v2;
			for (int i = 0; i < Math.min(l1.size(), l2.size()); i++)
				l1.set(i, merge(l1.get(i), l2.get(i)));
			for (int i = l1.size(); i < l2.size(); i++)
				l1.add(l2.get(i));
			return (T) l1;
		}

		return (T) v2;
	}
}
