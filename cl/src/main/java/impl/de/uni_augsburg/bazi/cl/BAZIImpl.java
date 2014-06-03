package de.uni_augsburg.bazi.cl;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.Version;
import de.uni_augsburg.bazi.common.algorithm.Algorithm;
import de.uni_augsburg.bazi.common.algorithm.Options;
import de.uni_augsburg.bazi.common.format.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.LogManager;


class BAZIImpl
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BAZIImpl.class);

	public static void main(String[] args)
	{
		boolean debug = keyExists(args, "-debug");
		LogManager.getLogManager().getLogger("").setLevel(debug ? Level.ALL : Level.WARNING);

		try {start(args);}
		catch (Exception e)
		{
			LOGGER.error("", e);
		}
	}

	public static void start(String[] args)
	{
		LOGGER.info("BAZI under development...");
		LOGGER.info("The current Version is: {}", Version.getCurrentVersionName());

		if (args.length == 0 || keyExists(args, "-help"))
		{
			System.out.println(Resources.get("usage"));
			System.exit(0);
		}

		PluginManager.load();

		// read args
		Optional<Locale> locale = readValue(args, "-l", Locale::forLanguageTag);
		Optional<Path> in = readValue(args, "-i", BAZIImpl::readIn);
		Optional<Path> out = readValue(args, "-o", BAZIImpl::readOut);
		Optional<Format> inFormat = readValue(args, "-if", s -> PluginManager.tryInstantiate(Format.class, params(s)).get());
		Optional<Format> outFormat = readValue(args, "-of", s -> PluginManager.tryInstantiate(Format.class, params(s)).get());


		// backup plans
		if (!inFormat.isPresent())
		{
			String name = in.isPresent()
				? Files.getFileExtension(in.get().toString())
				: "json";
			inFormat = PluginManager.tryInstantiate(Format.class, params(name));
		}
		if (!outFormat.isPresent())
		{
			String name = out.isPresent()
				? Files.getFileExtension(out.get().toString())
				: "plain";
			outFormat = PluginManager.tryInstantiate(Format.class, params(name));
		}


		// anything missing?
		if (!inFormat.isPresent()) throw new RuntimeException(Resources.get("params.missing_in_format"));
		if (!outFormat.isPresent()) throw new RuntimeException(Resources.get("params.missing_out_format"));


		// input from file or args
		InputStream inputStream;
		if (in.isPresent()) try
		{
			inputStream = new FileInputStream(in.get().toFile());
		}
		catch (IOException e)
		{
			throw new RuntimeException(Resources.get("params.file_read", in.get()));
		}
		else if (args.length > 0 || !args[args.length - 1].startsWith("-"))
		{
			inputStream = new ByteArrayInputStream(args[args.length - 1].getBytes(Charsets.UTF_8));
		}
		else throw new RuntimeException(Resources.get("params.noinput"));


		// infos
		locale.ifPresent(Resources::setLocale);
		in.ifPresent(x -> LOGGER.info("input file: {}", x));
		inFormat.ifPresent(x -> LOGGER.info("input format: {}", x));
		out.ifPresent(x -> LOGGER.info("output file: {}", x));
		outFormat.ifPresent(x -> LOGGER.info("output format: {}", x));


		// now the actual work begins
		BaziFile baziFile = inFormat.get().deserialize(inputStream).asCastableObject().cast(BaziFile.class);
		if (baziFile.output() != null)
			outFormat.get().configure(baziFile.output());

		Algorithm algorithm = baziFile.algorithm();
		if (algorithm == null) throw new RuntimeException(Resources.get("input.no_such_algorithm", baziFile.algorithm()));

		algorithm.apply(baziFile, new Options());
		outFormat.get().serialize(baziFile.src(), System.out);
	}

	public interface BaziFile extends Data
	{
		public Algorithm algorithm();
		public Data output();
	}


	private static boolean keyExists(String[] args, String key)
	{
		key = key.toLowerCase();
		for (String s : args)
			if (s.toLowerCase().equals(key))
				return true;
		return false;
	}

	private static <T> Optional<T> readValue(String[] args, String key, Function<String, T> converter)
	{
		key = key.toLowerCase();
		int i = 0;
		while (i < args.length && !args[i].toLowerCase().equals(key))
			i++;
		i++;

		if (i > args.length) return Optional.empty();

		if (i >= args.length || args[i].startsWith("-"))
		{
			LOGGER.warn(Resources.get("params.missing_value", key));
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

	private static Plugin.Params params(String name)
	{
		Plugin.Params params = Data.create(Plugin.Params.class);
		params.name(name);
		return params;
	}
}
