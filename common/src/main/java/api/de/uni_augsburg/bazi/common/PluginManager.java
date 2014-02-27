package de.uni_augsburg.bazi.common;

import de.uni_augsburg.bazi.common.util.MList;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Optional;

public enum PluginManager
{
	INSTANCE;

	private final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);

	private final MList<Plugin<?>> plugins = new MList<>();


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
					LOGGER.info("loaded " + c);
				}
				catch (NoSuchMethodException | SecurityException e)
				{
					LOGGER.error(e.getMessage());
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
	}


	public <T> MList<Plugin<? extends T>> getPluginsOfInstanceType(Class<T> type)
	{
		MList<Plugin<? extends T>> list = new MList<>();
		for (Plugin<?> plugin : plugins)
			if (type.isAssignableFrom(plugin.getInstanceType()))
			{
				@SuppressWarnings("unchecked")
				Plugin<? extends T> cast = (Plugin<? extends T>) plugin;
				list.add(cast);
			}
		return list;
	}

	public <T> Optional<T> tryInstantiate(Class<T> type, Plugin.Params params)
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
