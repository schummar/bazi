package de.uni_augsburg.bazi.common;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum PluginManager
{
	INSTANCE;

	private final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);

	private final List<Plugin<?>> plugins = new ArrayList<>();


	private PluginManager()
	{
		try
		{
			Reflections reflections = new Reflections("de.uni_augsburg.bazi");
			for (Class<? extends Plugin> c : reflections.getSubTypesOf(Plugin.class))
			{
				if (c.isInterface() || Modifier.isAbstract(c.getModifiers())) continue;
				try
				{
					Plugin<?> plugin = c.getConstructor().newInstance();
					plugins.add(plugin);
				}
				catch (NoSuchMethodException | SecurityException e)
				{
					LOGGER.warn(e.getMessage());
				}
			}

			LOGGER.info("loaded plugins:\n{}", plugins.toString().replaceAll(",", ",\n"));
		}
		catch (Exception e) {e.printStackTrace();}
	}


	public <T extends Plugin.Instance> List<Plugin<? extends T>> getPluginsOfInstanceType(Class<T> type)
	{
		List<Plugin<? extends T>> list = new ArrayList<>();
		for (Plugin<?> plugin : plugins)
			if (type.isAssignableFrom(plugin.getInstanceType()))
			{
				@SuppressWarnings("unchecked")
				Plugin<? extends T> cast = (Plugin<? extends T>) plugin;
				list.add(cast);
			}
		return list;
	}

	public <T extends Plugin.Instance> Optional<T> tryInstantiate(Class<T> type, Plugin.Params params)
	{
		for (Plugin<? extends T> plugin : getPluginsOfInstanceType(type))
		{
			Optional<? extends T> t = plugin.tryInstantiate(params);
			if (t.isPresent()) return Optional.ofNullable(t.get());
		}
		return Optional.empty();
	}

	public class NoSuchPluginException extends RuntimeException
	{}
}
