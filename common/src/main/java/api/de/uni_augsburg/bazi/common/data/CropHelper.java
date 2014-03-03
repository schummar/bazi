package de.uni_augsburg.bazi.common.data;

import java.lang.reflect.Type;
import java.util.*;

import static de.uni_augsburg.bazi.common.data.CastHelper.*;

/**
 * Created by Marco on 03.03.14.
 */
@SuppressWarnings("unchecked")
public class CropHelper
{
	public static <T> T crop(Object value, Type type)
	{
		Class<?> raw = raw(type);

		// Data instance
		if (Data.class.isAssignableFrom(raw))
		{
			Set<Getter> getters = Getter.getters(raw);
			Data data = cast(value, type);
			if (data instanceof MapData.ProxyData)
			{
				MapData md = ((MapData.ProxyData) data).delegate();
				Iterator<Map.Entry<String, Object>> i = md.entrySet().iterator();
				while (i.hasNext())
				{
					Map.Entry<String, Object> next = i.next();
					Optional<Getter> getter = getters.stream().filter(g -> g.key().equals(next.getKey())).findAny();
					if (!getter.isPresent()) i.remove();
					else next.setValue(crop(next.getValue(), getter.get().type()));
				}
				return (T) data;
			}
			else return (T) data.crop((Class<? extends Data>) raw);
		}

		// List
		else if (List.class.isAssignableFrom(raw))
		{
			List list = cast(value, raw);
			list.replaceAll(v -> crop(v, param(type, 0)));
			return (T) list;
		}

		// Map
		else if (Map.class.isAssignableFrom(raw))
		{
			Map map = cast(value, raw);
			map.replaceAll((k, v) -> crop(v, param(type, 1)));
			return (T) map;
		}

		return (T) value;
	}
}
