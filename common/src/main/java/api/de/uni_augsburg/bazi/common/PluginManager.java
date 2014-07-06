package de.uni_augsburg.bazi.common;

import de.schummar.castable.Data;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utitlity class that manages plugins.
 * Call {@link PluginManager#load()} once to find all plugin in the classpath (slow!).
 * After that they can be queried.
 */
public class PluginManager
{
	private PluginManager() {}

	private static final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);

	private static boolean loading = false, loaded = false;
	private static final List<Plugin<?>> plugins = new ArrayList<>();


	/** Find and cache all plugins in the classpath. */
	synchronized public static void load()
	{
		if (loading || loaded) return;
		loading = true;

		new Thread(
			new Runnable()
			{
				@Override public void run()
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
						LOGGER.info(
							"loaded plugins:\n{}", plugins.stream()
								.map(p -> p.getClass().toString())
								.sorted()
								.collect(Collectors.joining(",\n"))
						);
					}
					catch (Exception e) { LOGGER.warn(e.getMessage()); }
					finally
					{
						synchronized (PluginManager.class)
						{
							loading = false;
							loaded = true;
							PluginManager.class.notifyAll();
						}
					}
				}
			}
		).start();
	}

	synchronized public static void ensureLoaded()
	{
		load();
		while (!loaded) try {PluginManager.class.wait();}
		catch (InterruptedException e) { LOGGER.error(e.getMessage()); }
	}


	/**
	 * Find all plugin in the cache that instantiate a subtype of <b>type</b>.
	 * @param <T> the type whose subtypes should be found.
	 * @param type the class of the type whose subtypes should be found.
	 * @return a list of all plugins that instantiate a subtype of <b>type</b>.
	 */
	public static <T extends Plugin.Instance> List<Plugin<? extends T>> getPluginsOfInstanceType(Class<T> type)
	{
		ensureLoaded();

		List<Plugin<? extends T>> list = new ArrayList<>();
		plugins.stream().filter(plugin -> type.isAssignableFrom(plugin.getInstanceType())).forEach(
			plugin -> {
				@SuppressWarnings("unchecked")
				Plugin<? extends T> cast = (Plugin<? extends T>) plugin;
				list.add(cast);
			}
		);
		return list;
	}


	/**
	 * Create an instance of the given type and with the given parameters.
	 * @param <T> the type the instance must be a subtype of.
	 * @param type the class of the type the instance must be a subtype of.
	 * @param data the parameters for instantiation.
	 * @return an Optional of the instance if any, an empty Optional else.
	 */
	public static <T extends Plugin.Instance> Optional<T> tryInstantiate(Class<T> type, Data data)
	{
		for (Plugin<? extends T> plugin : getPluginsOfInstanceType(type))
		{
			Optional<? extends T> t = plugin.tryInstantiate(data.cast(Plugin.Params.class));
			if (t.isPresent()) return Optional.ofNullable(t.get());
		}
		return Optional.empty();
	}
}
