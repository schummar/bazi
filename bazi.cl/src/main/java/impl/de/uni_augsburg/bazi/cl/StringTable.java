package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.common.Tuple;

import java.util.*;
import java.util.function.Function;

public class StringTable
{
	public static final int DEFAULT_MARGIN = 2;

	private final Map<Pos, String> values = new HashMap<>();
	private int width = 0, height = 0;

	public Field field(int col, int row)
	{
		return new Field(new Pos(col, row));
	}

	public Column col(int col)
	{
		return new Column(col);
	}

	public int width()
	{
		return width;
	}

	public int height()
	{
		return height;
	}

	public int colWidth(int col)
	{
		return col(col).toList().stream().mapToInt(field -> field.get().length()).max().getAsInt();
	}

	@Override
	public String toString()
	{
		return toString(DEFAULT_MARGIN);
	}

	public String toString(int margin)
	{
		List<Function<String, String>> paddings = new ArrayList<>();
		for (int col = 0; col < width; col++)
		{
			int colWidth = colWidth(col) + (col < width - 1 ? margin : 0);
			paddings.add(
				s -> {
					while (s.length() < colWidth) s += " ";
					return s;
				}
			);
		}

		StringBuilder s = new StringBuilder();
		for (int row = 0; row < height; row++)
		{
			for (int col = 0; col < width; col++)
			{
				String value = values.getOrDefault(new Pos(col, row), "");
				s.append(paddings.get(col).apply(value));
			}
			if (row < height - 1) s.append("\n");
		}
		return s.toString();
	}

	public StringTable add(StringTable that)
	{
		values.putAll(that.values);
		width = Math.max(width, that.width);
		height = Math.max(height, that.height);
		return this;
	}

	public StringTable transposed()
	{
		StringTable that = new StringTable();
		for (Map.Entry<Pos, String> entry : values.entrySet())
			that.field(entry.getKey().y(), entry.getKey().x()).set(entry.getValue());
		return that;
	}


	public class Field
	{
		private final Pos pos;

		public Field(Pos pos)
		{
			this.pos = pos;
		}

		public String get()
		{
			return values.get(pos);
		}

		public void set(Object value)
		{
			if (value == null)
			{
				delete();
				return;
			}

			values.put(pos, value.toString());
			width = Math.max(width, pos.x() + 1);
			height = Math.max(height, pos.y() + 1);
		}

		public void delete()
		{
			values.remove(pos);
			if (pos.x().equals(width - 1))
				width = values.keySet().stream().mapToInt(Pos::x).max().getAsInt();
			if (pos.y().equals(height - 1))
				height = values.keySet().stream().mapToInt(Pos::y).max().getAsInt();
		}
	}

	public class Column implements Iterable<Field>
	{
		private final int col;

		public Column(int col)
		{
			this.col = col;
		}

		public Field field(int row)
		{
			return StringTable.this.field(col, row);
		}

		public List<Field> toList()
		{
			List<Field> list = new ArrayList<>();
			for (int row = 0; row < height; row++)
				list.add(StringTable.this.field(col, row));
			return list;
		}

		@Override
		public Iterator<Field> iterator()
		{
			return toList().iterator();
		}
	}

	private class Pos extends Tuple<Integer, Integer>
	{
		public Pos(int x, int y)
		{
			super(x, y);
		}

		public Pos transposed()
		{
			return new Pos(y(), x());
		}

		@Override
		public Integer x() { return super.x(); }

		@Override
		public Integer y() { return super.y(); }
	}
}
