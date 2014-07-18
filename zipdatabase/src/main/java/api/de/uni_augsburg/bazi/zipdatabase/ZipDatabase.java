package de.uni_augsburg.bazi.zipdatabase;

import de.uni_augsburg.bazi.common.database.Tree;
import de.uni_augsburg.bazi.common.prefs.Prefs;
import de.uni_augsburg.bazi.common.util.ResourceBundleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipDatabase
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ZipDatabase.class);

	private static final String BASENAME = "names";


	private final File file;
	private final ClassLoader loader;
	private final Tree tree = new Tree("");
	private ResourceBundle lang = null;

	public ZipDatabase(File file)
	{
		this.file = file;
		ClassLoader loader = null;
		try
		{
			loader = new URLClassLoader(new URL[]{new URL("jar:file:" + file.getCanonicalPath() + "!/")});
		}
		catch (IOException e)
		{
			LOGGER.error(e.getMessage());
		}
		this.loader = loader;
	}

	public void open()
	{
		try (ZipFile zip = new ZipFile(file))
		{
			lang = ResourceBundleHelper.getUTF8Bundle(BASENAME, Prefs.locale(), loader);

			Enumeration<? extends ZipEntry> entries = zip.entries();
			while (entries.hasMoreElements())
			{
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory()) continue;

				Path path = Paths.get(entry.getName());
				String name = translate(path.toString(), () -> withoutEnding(path.getFileName()));
				String ending = ending(path.getFileName());
				Tree parent = tree.search(path.getParent(), true);
				parent.children.add(new Tree(name, ending, () -> zip.getInputStream(entry)));
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

	private String translate(String key, Supplier<String> def)
	{
		try
		{
			return lang.getString(key);
		}
		catch (Exception ignore) {}
		return def.get();
	}

	private String withoutEnding(Path path)
	{
		String s = path.toString();
		int i = s.lastIndexOf(".");
		return i == -1 ? s : s.substring(0, i);
	}
	private String ending(Path path)
	{
		String s = path.toString();
		int i = s.lastIndexOf(".");
		return i == -1 ? "" : s.substring(i + 1);
	}
}
