package de.uni_augsburg.bazi.cl;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import de.uni_augsburg.bazi.common.format.FileFormat;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.Version;
import de.uni_augsburg.bazi.monoprop.DivisorPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;


class BAZIImpl
{
	private static final Logger LOG = LoggerFactory.getLogger(BAZIImpl.class);

	public static void main(String[] args)
	{
		LOG.info("BAZI under development...");
		LOG.info("The current Version is: {}", Version.getCurrentVersionName());

		PluginManager pluginManager = new PluginManager();
		pluginManager.load();
		System.out.println(pluginManager.find(DivisorPlugin.class, p -> true));


		Optional<Locale> locale = Optional.empty();
		Optional<Path> in = Optional.empty(), out = Optional.empty();
		String content = "";
		Optional<FileFormat> inFormat = Optional.empty(), outFormat = Optional.empty();
		for (int i = 0; i < args.length; i++)
		{
			if (i == args.length - 1 && !args[i].startsWith("-"))
			{
				content = args[i].trim();
				break;
			}
			switch (args[i])
			{
				case "-l":
					if (++i >= args.length) LOG.warn(Resources.get("params.missing_value"));
					else locale = readLocale(args[i]);
					break;

				case "-i":
					if (++i >= args.length) LOG.warn(Resources.get("params.missing_value"));
					in = readIn(args[i]);
					break;

				case "-o":
					if (++i >= args.length) LOG.warn(Resources.get("params.missing_value"));
					out = readOut(args[i]);
					break;

				case "-if":
					if (++i >= args.length) LOG.warn(Resources.get("params.missing_value"));
					inFormat = readFormat(args[i]);
					break;

				case "-of":
					if (++i >= args.length) LOG.warn(Resources.get("params.missing_value"));
					outFormat = readFormat(args[i]);
					break;
			}
		}
		locale.ifPresent(Resources::setLocale);
		in.ifPresent(x -> LOG.info("Input file: {}", x));
		out.ifPresent(x -> LOG.info("Output file: {}", x));


		BaziFile baziFile = in.isPresent()
			? BaziFile.load(in.get(), inFormat.orElse(Format.JSON))
			: BaziFile.load(content, inFormat.orElse(Format.JSON));


		String result = AlgorithmSwitch.calculate(baziFile, outFormat.orElse(Format.PLAIN));
		if (out.isPresent())
		{
			File file = out.get().toFile();
			try
			{
				Files.write(result, file, Charsets.UTF_8);
			}
			catch (IOException e)
			{
				LOG.error("Could not write to {}", file.getPath());
			}
		}
		else
			System.out.println(result);
	}


	private static Optional<Locale> readLocale(String s)
	{
		Locale locale = Locale.forLanguageTag(s);
		return Optional.of(locale);
	}


	private static Optional<Path> readIn(String s)
	{
		try
		{
			Path path = FileSystems.getDefault().getPath(s);
			if (path.toFile().exists()) return Optional.of(path);

			LOG.warn(Resources.get("params.file_doesnt_exist: {}"), s);
		}
		catch (Exception e)
		{
			LOG.warn(Resources.get("params.invalid_path: {}"), s);
		}
		return Optional.empty();
	}


	private static Optional<Path> readOut(String s)
	{
		try
		{
			Path path = FileSystems.getDefault().getPath(s);
			return Optional.of(path);
		}
		catch (Exception e)
		{
			LOG.warn(Resources.get("params.invalid_path: {}"), s);
			return Optional.empty();
		}
	}


	public static Optional<Format> readFormat(String s)
	{
		try
		{
			return Optional.of(Format.valueOf(s.toUpperCase()));
		}
		catch (Exception e)
		{
			LOG.warn(Resources.get("params.invalid_format"), s);
		}
		return Optional.empty();
	}
}
