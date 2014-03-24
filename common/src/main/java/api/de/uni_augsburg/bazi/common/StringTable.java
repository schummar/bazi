package de.uni_augsburg.bazi.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/** A simple way to work with a string table. */
public class StringTable
{
	/** Identifiers for columns. */
	public static class Key
	{}


	/** The default number of spaces between two columns. */
	public static final int DEFAULT_MARGIN = 2;

	private final List<Column> columns = new ArrayList<>();
	private final List<String> titles = new ArrayList<>(), footnotes = new ArrayList<>();


	/**
	 * A list of titles that will be printed before the table.
	 * @return a list of titles that will be printed before the table.
	 */
	public List<String> titles()
	{
		return titles;
	}

	/**
	 * A list of footnotes that will be printed after the table.
	 * @return a list of footnotes that will be printed after the table.
	 */
	public List<String> footnotes()
	{
		return footnotes;
	}


	/**
	 * Get the column with the given index.
	 * Creates it and all columns before if needed.
	 * @param col the column index.
	 * @return the column.
	 */
	public Column col(int col)
	{
		while (col >= columns.size()) columns.add(new Column());
		return columns.get(col);
	}

	/**
	 * Get the column with the given index and sets its identifier to the given key.
	 * Creates it and all columns before if needed.
	 * @param col the column index.
	 * @param key the column identifier.
	 * @return the column.
	 */
	public Column col(int col, Key key)
	{
		Column column = col(col);
		column.key(key);
		return column;
	}

	/**
	 * Get a new column.
	 * @return the column.
	 */
	public Column col()
	{
		return col(columns.size());
	}

	/**
	 * Get a new column and set its identifier to the given key.
	 * @param key the column identifier.
	 * @return the column.
	 */
	public Column col(Key key)
	{
		return col(columns.size(), key);
	}


	/**
	 * Get all columns.
	 * @return all columns.
	 */
	public List<Column> cols()
	{
		return columns;
	}

	/**
	 * Get all columns with the given key.
	 * @param key the key.
	 * @return all columns with the given key.
	 */
	public List<Column> cols(Key key)
	{
		return columns.stream()
			.filter(c -> Objects.equals(c.key(), key))
			.collect(Collectors.toList());
	}


	/**
	 * The number of columns.
	 * @return the number of columns.
	 */
	public int width()
	{
		return columns.size();
	}

	/**
	 * The number of lines.
	 * @return the number of lines.
	 */
	public int height()
	{
		return columns.stream().mapToInt(List::size).max().orElse(0);
	}

	/**
	 * The maximum number of characters in the column.
	 * @param col the index of the column.
	 * @return the maximum number of characters in the column.
	 */
	public int colWidth(int col)
	{
		return col(col).stream()
			.filter(s -> s != null)
			.mapToInt(String::length)
			.max().orElse(0);
	}


	/**
	 * Print the table with default margins.
	 * @return the table as string.
	 */
	@Override public String toString()
	{
		return toString(DEFAULT_MARGIN);
	}

	/**
	 * Print the table with the given margin.
	 * @param margin the number of spaces between two columns.
	 * @return the table as string.
	 */
	public String toString(int margin)
	{
		List<Function<String, String>> paddings = new ArrayList<>();
		for (int col = 0; col < width(); col++)
		{
			int colWidth = colWidth(col) + margin;
			Alignment colAlignment = col(col).alignment() != null
				? col(col).alignment()
				: (col == 0 ? Alignment.LEFT : Alignment.RIGHT);
			paddings.add(s -> colAlignment.pad(s, colWidth));
		}

		StringBuilder s = new StringBuilder();
		titles.forEach(t -> s.append("*").append(t).append("\n"));
		for (int row = 0; row < height(); row++)
		{
			for (int col = 0; col < width(); col++)
			{
				String value = col(col).getOrDefault(row, "");
				s.append(paddings.get(col).apply(value));
			}
			s.append("\n");
		}
		footnotes.forEach(f -> s.append("*").append(f).append("\n"));
		return s.toString();
	}


	/**
	 * Add a new column.
	 * @param col the new columns.
	 */
	public void add(List<String> col)
	{
		columns.add(new Column(col));
	}


	/**
	 * Remove all columns with the given key.
	 * @param key the key.
	 */
	public void removeAll(Key key)
	{
		columns.removeIf(c -> Objects.equals(c.key(), key));
	}


	/**
	 * Appends another StringTable to this one.
	 * @param that another StringTable.
	 * @return this.
	 */
	public StringTable append(StringTable that)
	{
		that.columns.forEach(col -> columns.add(new Column(col)));
		return this;
	}


	/**
	 * Switch columns and rows.
	 * @return a transposed version of this table.
	 */
	public StringTable transposed()
	{
		StringTable that = new StringTable();
		for (int i = 0; i < height(); i++)
		{
			Column col = that.col();
			for (int j = 0; j < width(); j++)
				col.add(col(j).get(i));
		}
		return that;
	}


	/** A column of a StringTable. */
	public class Column extends ArrayList<String>
	{
		private Key key = null;
		private Alignment alignment = null;

		/** Default Constructor */
		public Column()
		{}

		/**
		 * Constructor with initializer.
		 * @param c a collection of Strings whose values are added to this column.
		 */
		public Column(Collection<? extends String> c)
		{
			super(c);
		}

		/**
		 * This column's key.
		 * @return this column's key.
		 */
		public Key key()
		{
			return key;
		}

		/**
		 * This column's key.
		 * @param key this column's key.
		 */
		public void key(Key key)
		{
			this.key = key;
		}

		/**
		 * Get the value with the given index.
		 * @param index the index.
		 * @return the value or null if the index is greater or equal to this column's size.
		 */
		@Override public String get(int index)
		{
			return getOrDefault(index, null);
		}

		/**
		 * Get the value with the given index or a default value.
		 * @param index the index.
		 * @param def the default value.
		 * @return the value or the default value if the index is greater or equal to this column's size.
		 */
		public String getOrDefault(int index, String def)
		{
			if (index < size() && super.get(index) != null)
				return super.get(index);
			return def;
		}

		/**
		 * Get the last value in this column.
		 * @return the last value in this column.
		 */
		public String get()
		{
			return get(size() - 1);
		}

		/**
		 * Set the last value in this column.
		 * @param s the last value in this column.
		 */
		public void set(String s)
		{
			set(size() - 1, s);
		}

		/** Remove the last value in this column. */
		public void remove()
		{
			remove(size() - 1);
		}

		/**
		 * This columns alignment.
		 * @return this columns alignment.
		 */
		public Alignment alignment()
		{
			return alignment;
		}

		/**
		 * This columns alignment.
		 * @param aligment this columns alignment.
		 */
		public void alignment(Alignment aligment)
		{
			this.alignment = aligment;
		}

		/**
		 * Insert a new column after this one.
		 * @return the new column.
		 */
		public Column insertAfter()
		{
			return insertAfter(null);
		}

		/**
		 * Insert a new column after this one and set its key to the given one.
		 * @param key the key for the new column.
		 * @return the new column.
		 */
		public Column insertAfter(Key key)
		{
			int index = columns.indexOf(this);
			Column col = new Column();
			col.key(key);
			columns.add(index + 1, col);
			return col;
		}

		/**
		 * Insert a new column before this one.
		 * @return the new column.
		 */
		public Column inserBefore()
		{
			return inserBefore(null);
		}

		/**
		 * Insert a new column before this one and set its key to the given one.
		 * @param key the key for the new column.
		 * @return the new column.
		 */
		public Column inserBefore(Key key)
		{
			int index = columns.indexOf(this);
			Column col = new Column();
			col.key(key);
			columns.add(index, col);
			return col;
		}

		/** Remove this column from its StringTable. */
		public void delete()
		{
			columns.remove(this);
		}
	}


	/** The Alignment with which fields of a StringTable are printed. */
	public enum Alignment
	{
		/** Alignt left. */
		LEFT
			{
				@Override
				public String pad(String s, int to)
				{
					while (s.length() < to) s += " ";
					return s;
				}
			},

		/** Alignt right. */
		RIGHT
			{
				@Override
				public String pad(String s, int to)
				{
					while (s.length() < to) s = " " + s;
					return s;
				}
			};
		public abstract String pad(String s, int to);
	}
}
