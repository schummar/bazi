package de.uni_augsburg.bazi.cl;

import com.google.common.io.Files;
import de.uni_augsburg.bazi.common.*;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.format.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
		try {start(args);}
		catch (Exception e) {LOGGER.error(e.getMessage());}
	}

	public static void start(String[] args)
	{
		LOGGER.info("BAZI under development...");
		LOGGER.info("The current Version is: {}", Version.getCurrentVersionName());

		PluginManager pluginManager = new PluginManager();
		pluginManager.load();


		// read args
		Optional<Locale> locale = readValue(args, "-l", Locale::forLanguageTag);
		Optional<Path> in = readValue(args, "-i", BAZIImpl::readIn);
		Optional<Path> out = readValue(args, "-o", BAZIImpl::readOut);
		Optional<Format> inFormat = readValue(args, "-if", s -> pluginManager.tryInstantiate(Format.class, () -> s).get());
		Optional<Format> outFormat = readValue(args, "-of", s -> pluginManager.tryInstantiate(Format.class, () -> s).get());


		// backup plans
		if (!inFormat.isPresent())
		{
			String name = in.isPresent()
				? Files.getFileExtension(in.get().toString())
				: "json";
			inFormat = pluginManager.tryInstantiate(Format.class, () -> name);
		}
		if (!outFormat.isPresent())
		{
			String name = out.isPresent()
				? Files.getFileExtension(out.get().toString())
				: "json";
			outFormat = pluginManager.tryInstantiate(Format.class, () -> name);
		}


		// anything missing?
		if (!inFormat.isPresent()) throw new RuntimeException(Resources.get("params.missing_in_format"));
		if (!outFormat.isPresent()) throw new RuntimeException(Resources.get("params.missing_out_format"));


		// input from file or args
		String inputString;
		if (in.isPresent()) try
		{
			inputString = Files.toString(in.get().toFile(), StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			throw new RuntimeException(Resources.get("params.file_read", in.get()));
		}
		else if (args.length > 0 || !args[args.length - 1].startsWith("-"))
		{
			inputString = args[args.length - 1];
		}
		else throw new RuntimeException(Resources.get("params.noinput"));


		// infos
		locale.ifPresent(Resources::setLocale);
		in.ifPresent(x -> LOGGER.info("input file: {}", x));
		inFormat.ifPresent(x -> LOGGER.info("input format: {}", x));
		out.ifPresent(x -> LOGGER.info("output file: {}", x));
		outFormat.ifPresent(x -> LOGGER.info("output format: {}", x));


		// now the actual work begins
		BaziFile baziFile = new MapData(inFormat.get().deserialize(inputString)).cast(BaziFile.class);
		System.out.println(baziFile);

		Optional<Algorithm> algorithm = pluginManager.tryInstantiate(Algorithm.class, baziFile.algorithm());
		if (!algorithm.isPresent()) throw new RuntimeException(Resources.get("input.no_such_algorithm", baziFile.algorithm()));

		Data result = algorithm.get().apply(baziFile);
		System.out.println(result);
	}

	public interface BaziFile extends Data
	{
		public Plugin.Params algorithm();
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
