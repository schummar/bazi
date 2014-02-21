package de.uni_augsburg.bazi.common;

import de.uni_augsburg.bazi.common.util.MList;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class PluginManager
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);


	public interface Plugin {}

	private final List<Plugin> plugins = new ArrayList<>();

	public void load()
	{
		try
		{
			Reflections reflections = new Reflections();
			for (Class<? extends Plugin> c : reflections.getSubTypesOf(Plugin.class))
			{
				if (c.isInterface() || Modifier.isAbstract(c.getModifiers())) continue;
				try
				{
					Plugin plugin = c.getConstructor().newInstance();
					plugins.add(plugin);
					LOGGER.info("loaded " + plugin);
				}
				catch (NoSuchMethodException | SecurityException e)
				{
					LOGGER.error(e.getMessage());
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
	}


	public <T extends Plugin> List<T> findAll(Class<T> type)
	{
		return plugins.stream()
			.filter(type::isInstance)
			.map(type::cast)
			.collect(MList.collector());
	}

	public <T extends Plugin, R> R create(Class<T> pluginType, Function<T, R> creator)
	{
		for (T plugin : findAll(pluginType))
		{
			R r = creator.apply(plugin);
			if (r != null) return r;
		}
		return null;
	}

	public class NoSuchPluginException extends RuntimeException
	{}
}
