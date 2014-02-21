package de.uni_augsburg.bazi.cl;

import com.google.common.io.Files;
import de.uni_augsburg.bazi.common.MapData;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.Version;
import de.uni_augsburg.bazi.common.format.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;


class BAZIImpl
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BAZIImpl.class);


	public static void main(String[] args)
	{
		LOGGER.info("BAZI under development...");
		LOGGER.info("The current Version is: {}", Version.getCurrentVersionName());

		PluginManager pluginManager = new PluginManager();
		pluginManager.load();

		Optional<Locale> locale = readValue(args, "-l", Locale::forLanguageTag);
		Optional<Path> in = readValue(args, "-i", BAZIImpl::readIn);
		Optional<Path> out = readValue(args, "-o", BAZIImpl::readOut);
		Optional<Format> inFormat = readValue(args, "-if", s -> Format.create(s, pluginManager));
		Optional<Format> outFormat = readValue(args, "-of", s -> Format.create(s, pluginManager));

		locale.ifPresent(Resources::setLocale);
		in.ifPresent(x -> LOGGER.info("Input file: {}", x));
		out.ifPresent(x -> LOGGER.info("Output file: {}", x));

		if (!inFormat.isPresent() && in.isPresent())
		{
			String name = Files.getFileExtension(in.get().toString());
			inFormat = Optional.ofNullable(Format.create(name, pluginManager));
		}
		if (!outFormat.isPresent() && out.isPresent())
		{
			String name = Files.getFileExtension(out.get().toString());
			outFormat = Optional.ofNullable(Format.create(name, pluginManager));
		}

		MapData data;
		if (in.isPresent())
			try (InputStream stream = new FileInputStream(in.get().toFile()))
			{
				data = new MapData(inFormat.get().deserialize(stream));
			}
			catch (IOException e)
			{
				LOGGER.error(e.getMessage());
				return;
			}
		else if (args.length > 0 || !args[args.length - 1].startsWith("-"))
			try (InputStream stream = new ByteArrayInputStream(args[args.length].getBytes(StandardCharsets.UTF_8)))
			{
				data = new MapData(inFormat.get().deserialize(stream));
			}
			catch (IOException e)
			{
				LOGGER.error(e.getMessage());
				return;
			}
		else
		{
			LOGGER.error(Resources.get("params.noinput"));
			return;
		}

		System.out.println(data);
	}


	private static <T> Optional<T> readValue(String[] args, String key, Function<String, T> converter)
	{
		int i = 0;
		while (i < args.length && !args[i].equals(key))
			i++;

		if (++i >= args.length || args[i].startsWith("-"))
		{
			LOGGER.warn(Resources.get("params.missing_value"));
			return Optional.empty();
		}

		return Optional.ofNullable(converter.apply(args[i]));
	}


	private static Path readIn(String s)
	{
		try
		{
			Path path = FileSystems.getDefault().getPath(s);
			if (path.toFile().exists()) return path;
			LOGGER.warn(Resources.get("params.file_doesnt_exist: {}"), s);
		}
		catch (Exception e)
		{
			LOGGER.warn(Resources.get("params.invalid_path: {}"), s);
		}
		return null;
	}


	private static Path readOut(String s)
	{
		try
		{
			return FileSystems.getDefault().getPath(s);
		}
		catch (Exception e)
		{
			LOGGER.warn(Resources.get("params.invalid_path: {}"), s);
			return null;
		}
	}
}
