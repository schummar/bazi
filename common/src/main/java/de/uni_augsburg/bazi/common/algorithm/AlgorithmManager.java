package de.uni_augsburg.bazi.common.algorithm;

import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlgorithmManager
{
	private AlgorithmManager() {}

	private static final Logger LOGGER = LoggerFactory.getLogger(AlgorithmManager.class);

	private static boolean loading = false, loaded = false;
	private static final List<Plugin<? extends Algorithm>> plugins = new ArrayList<>();
	private static final List<Filter> filters = new ArrayList<>();

	synchronized public static void load()
	{
		if (loading || loaded) return;
		loading = true;
		new Thread(
			new Runnable()
			{
				@Override public void run()
				{
					plugins.addAll(PluginManager.getPluginsOfInstanceType(Algorithm.class));
					PluginManager.getPluginsOfInstanceType(Filter.class).forEach(
						fp -> {
							try { fp.tryInstantiate(Data.create(Plugin.Params.class)).ifPresent(filters::add);}
							catch (Exception e) { LOGGER.warn(e.getMessage()); }
						}
					);

					synchronized (AlgorithmManager.class)
					{
						loading = false;
						loaded = true;
						AlgorithmManager.class.notifyAll();
					}
				}
			}
		).start();
	}
	synchronized public static void ensureLoaded()
	{
		load();
		while (!loaded) try { AlgorithmManager.class.wait(); }
		catch (InterruptedException e) { LOGGER.error(e.getMessage()); }
	}

	/**
	 * Create an instance of the given type and with the given parameters.
	 * @param <T> the type the instance must be a subtype of.
	 * @param type the class of the type the instance must be a subtype of.
	 * @param data the parameters for instantiation.
	 * @return an Optional of the instance if any, an empty Optional else.
	 */
	public static <T extends Data> Optional<Algorithm<? extends T>> tryInstantiate(Class<T> type, Data data)
	{
		ensureLoaded();

		for (Plugin<? extends Algorithm> plugin : plugins)
		{
			Optional<? extends Algorithm> t = plugin.tryInstantiate(data.cast(Plugin.Params.class));
			if (t.isPresent())
			{
				if (!type.isAssignableFrom(t.get().dataType())) continue;
				@SuppressWarnings("unchecked")
				Algorithm<? extends T> algorithm = t.get();
				for (Filter filter : filters) algorithm = filter.decorate(algorithm);
				return Optional.ofNullable(algorithm);
			}
		}
		return Optional.empty();
	}
}
