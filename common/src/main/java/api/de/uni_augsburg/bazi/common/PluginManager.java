package de.uni_augsburg.bazi.common;

import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Filter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum PluginManager
{
	INSTANCE;

	private final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);

	private final List<Plugin<?>> plugins = new ArrayList<>();
	private final List<Filter> filters = new ArrayList<>();


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
			LOGGER.info(
				"loaded plugins:\n{}", plugins.stream()
					.map(p -> p.getClass().toString())
					.sorted()
					.collect(Collectors.joining(",\n"))
			);


			getPluginsOfInstanceType(Filter.class)
				.forEach(p -> p.tryInstantiate(() -> null).ifPresent(filters::add));

			LOGGER.info(
				"loaded filters:\n{}", filters.stream()
					.map(f -> f.getClass().toString())
					.sorted()
					.collect(Collectors.joining(",\n"))
			);
		}
		catch (Exception e) {e.printStackTrace();}
	}


	public <T extends Plugin.Instance> List<Plugin<? extends T>> getPluginsOfInstanceType(Class<T> type)
	{
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

	public <T extends Plugin.Instance> Optional<T> tryInstantiate(Class<T> type, Plugin.Params params)
	{
		for (Plugin<? extends T> plugin : getPluginsOfInstanceType(type))
		{
			Optional<? extends T> t = plugin.tryInstantiate(params);
			if (t.isPresent()) return Optional.ofNullable(t.get());
		}
		return Optional.empty();
	}

	public List<Filter> getGlobalFilters()
	{
		return filters.stream()
			.filter(Filter::applicableGlobally)
			.collect(Collectors.toList());
	}

	public List<Filter> getFiltersFor(Algorithm algorithm)
	{
		return filters.stream()
			.filter(f -> f.applicableTo(algorithm))
			.collect(Collectors.toList());
	}

	public class NoSuchPluginException extends RuntimeException
	{}
}
