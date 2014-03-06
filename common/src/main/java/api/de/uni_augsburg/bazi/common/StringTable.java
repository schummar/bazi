package de.uni_augsburg.bazi.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class StringTable
{
	public static final int DEFAULT_MARGIN = 2;

	private final List<Column> columns = new ArrayList<>();


	public Column col(int col)
	{
		while (col >= columns.size()) columns.add(new Column());
		return columns.get(col);
	}
	public Column col()
	{
		return col(columns.size());
	}

	public int width()
	{
		return columns.size();
	}

	public int height()
	{
		return columns.stream().mapToInt(List::size).max().orElse(0);
	}

	public int colWidth(int col)
	{
		return col(col).stream()
			.filter(s -> s != null)
			.mapToInt(String::length)
			.max().orElse(0);
	}

	@Override
	public String toString()
	{
		return toString(DEFAULT_MARGIN);
	}

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
		for (int row = 0; row < height(); row++)
		{
			for (int col = 0; col < width(); col++)
			{
				String value = col(col).getOrDefault(row, "");
				s.append(paddings.get(col).apply(value));
			}
			if (row < height() - 1) s.append("\n");
		}
		return s.toString();
	}

	public StringTable append(StringTable that)
	{
		that.columns.forEach(col -> columns.add(new Column(col)));
		return this;
	}

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

	public class Column extends ArrayList<String>
	{
		private Alignment alignment = null;

		public Column(Collection<? extends String> c) { super(c); }
		public Column() { }

		@Override public String get(int index)
		{
			return getOrDefault(index, null);
		}

		public String getOrDefault(int index, String def)
		{
			if (index < size() && super.get(index) != null)
				return super.get(index);
			return def;
		}

		public String get() { return get(size() - 1); }
		public void set(String s)
		{
			set(size() - 1, s);
		}

		public void remove()
		{
			remove(size() - 1);
		}

		public Alignment alignment() { return alignment; }
		public void alignment(Alignment aligment) { this.alignment = aligment; }

		public Column insertAfter()
		{
			int index = columns.indexOf(this);
			Column col = new Column();
			columns.add(index + 1, col);
			return col;
		}

		public Column inserBefore()
		{
			int index = columns.indexOf(this);
			Column col = new Column();
			columns.add(index, col);
			return col;
		}
	}


	public enum Alignment
	{
		LEFT
			{
				@Override
				public String pad(String s, int to)
				{
					while (s.length() < to) s += " ";
					return s;
				}
			},
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
