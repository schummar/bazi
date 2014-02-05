package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		Optional<Locale> locale = Optional.empty();
		Optional<Path> in = Optional.empty(), out = Optional.empty();
		Optional<String> content = Optional.empty();
		Optional<Format> format = Optional.empty();

		for (int i = 0; i < args.length; i++)
		{
			if (i == args.length - 1 && !args[i].startsWith("-"))
			{
				content = Optional.of(args[i]);
				break;
			}
			switch (args[i])
			{
				case "-l":
					if (++i >= args.length) LOG.warn(Resources.get("params.missing_value"));
					else locale = readLocale(args[i]);
					break;

				case "-in":
					if (++i >= args.length) LOG.warn(Resources.get("params.missing_value"));
					in = readIn(args[i]);
					break;

				case "-out":
					if (++i >= args.length) LOG.warn(Resources.get("params.missing_value"));
					out = readOut(args[i]);
					break;

				case "-f":
					if (++i >= args.length) LOG.warn(Resources.get("params.missing_value"));
					format = readFormat(args[i]);
					break;
			}
		}

		LOG.info("Language: {}", locale.orElse(Locale.getDefault()));
		Resources.setLocale(locale.orElse(Locale.getDefault()));
		in.ifPresent(x -> LOG.info("Input file: {}", x));
		out.ifPresent(x -> LOG.info("Output file: {}", x));

		BaziFile baziFile = BaziFile.load(in.get());
		AlgorithmSwitch.calculate(baziFile, format.orElse(Format.JSON));
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
