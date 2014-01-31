package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.common.Resources;
import de.uni_augsburg.bazi.common.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;


class BAZIImpl
{
	private static final Logger LOG = LoggerFactory.getLogger(BAZIImpl.class);

	public static void main(String[] args)
	{
		LOG.info("BAZI under development...");
		LOG.info("The current Version is: {}", Version.getCurrentVersionName());

		Optional<Language> language = Optional.empty();
		Optional<Path> in = Optional.empty(), out = Optional.empty();
		Optional<String> content = Optional.empty();

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
					else language = readLanguage(args[i]);
					break;

				case "-in":
					if (++i >= args.length) LOG.warn(Resources.get("params.missing_value"));
					in = readIn(args[i]);
					break;

				case "-out":
					if (++i >= args.length) LOG.warn(Resources.get("params.missing_value"));
					out = readOut(args[i]);
					break;
			}
		}

		if (language.isPresent()) LOG.info("Language: {}", language.get().getName());
		in.ifPresent(path -> LOG.info("Input file: {}", path));
		out.ifPresent(path -> LOG.info("Output file: {}", path));

		BaziFile baziFile = BaziFile.load(in.get());
		AlgorithmSwitch.calculate(baziFile);
	}

	private static Optional<Language> readLanguage(String s)
	{
		try
		{
			Language language = Language.valueOf(s.trim().toUpperCase());
			return Optional.of(language);
		}
		catch (IllegalArgumentException e)
		{
			return Optional.empty();
		}
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
}
