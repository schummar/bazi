package de.uni_augsburg.bazi.common.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

public class Tree
{
	public final String name, ending;
	public final ObservableList<Tree> children = FXCollections.observableArrayList();
	public final Loader loader;
	public Tree()
	{
		this.name = "/";
		this.ending = "";
		this.loader = null;
	}
	public Tree(String name)
	{
		this.name = name;
		this.ending = "";
		this.loader = null;
	}
	public Tree(String name, String ending, Loader loader)
	{
		this.name = name;
		this.ending = ending;
		this.loader = loader;
	}


	public boolean isLeaf() { return loader != null; }

	public InputStream load() throws IOException { return loader.load(); }

	public Tree search(Path path, boolean create)
	{
		if (path == null || path.getNameCount() == 0) return this;

		String name = path.getName(0).toString();
		Optional<Tree> child = children.stream()
			.filter(c -> c.name.equals(name))
			.findAny();

		if (create && !child.isPresent())
		{
			child = Optional.of(new Tree(name));
			children.add(child.get());
		}
		if (!child.isPresent()) return null;

		Path tail = path.getNameCount() == 1 ? null : path.subpath(1, path.getNameCount());
		return child.get().search(tail, create);
	}


	public interface Loader
	{
		InputStream load() throws IOException;
	}

	@Override public String toString()
	{
		StringBuilder s = new StringBuilder();
		if (isLeaf()) s.append(String.format("%s (%s)", name, ending));
		else
		{
			s.append("+").append(name);
			children.forEach(
				c -> s.append("\n|-- ").append(c.toString().replaceAll("\n", "\n|   "))
			);
		}
		return s.toString();
	}
}
