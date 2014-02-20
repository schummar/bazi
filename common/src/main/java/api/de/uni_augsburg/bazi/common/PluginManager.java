package de.uni_augsburg.bazi.common;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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
				try
				{
					plugins.add(c.getConstructor().newInstance());
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
		List<T> list = new ArrayList<>();

		for (Plugin plugin : plugins)
			if (type.isAssignableFrom(plugin.getClass()))
				list.add(type.cast(plugin));

		return list;
	}


	public <T extends Plugin> T find(Class<T> type, Predicate<T> predicate)
	{
		for (Plugin plugin : plugins)
			if (type.isAssignableFrom(plugin.getClass()))
			{
				T t = type.cast(plugin);
				if (predicate.test(t)) return t;
			}
		throw new NoSuchPluginException();
	}

	public class NoSuchPluginException extends RuntimeException {}
}
