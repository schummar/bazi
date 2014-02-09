package de.uni_augsburg.bazi.cl;

import de.uni_augsburg.bazi.common.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StringTable
{
	public static final int DEFAULT_MARGIN = 2;

	private final Map<Pos, String> values = new HashMap<>();
	private final Map<Integer, Alignment> alignments = new HashMap<>();
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
		return col(col).getFilledFields().stream().mapToInt(field -> field.get().length()).max().getAsInt();
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
			Alignment colAlignment = alignments.getOrDefault(col, col == 0 ? Alignment.LEFT : Alignment.RIGHT);
			paddings.add(s -> colAlignment.pad(s, colWidth));
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

	public StringTable append(StringTable that)
	{
		for (Map.Entry<Pos, String> entry : that.values.entrySet())
			values.put(new Pos(entry.getKey().x() + width, entry.getKey().y()), entry.getValue());
		width += that.width;
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

	public String get(int col, int row)
	{
		return field(col, row).get();
	}

	public StringTable set(int col, int row, Object value)
	{
		field(col, row).set(value);
		return this;
	}

	public StringTable remove(int col, int row)
	{
		field(col, row).remove();
		return this;
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
				remove();
				return;
			}

			values.put(pos, value.toString());
			width = Math.max(width, pos.x() + 1);
			height = Math.max(height, pos.y() + 1);
		}

		public void remove()
		{
			values.remove(pos);
			if (pos.x().equals(width - 1))
				width = values.keySet().stream().mapToInt(Pos::x).max().getAsInt();
			if (pos.y().equals(height - 1))
				height = values.keySet().stream().mapToInt(Pos::y).max().getAsInt();
		}
	}

	public class Column
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

		public Field last()
		{
			int last = values.keySet().stream().mapToInt(Pos::y).max().getAsInt();
			return field(last);
		}

		public List<Field> getFilledFields()
		{
			List<Field> list = new ArrayList<>();
			for (int row = 0; row < height; row++)
				if (get(row) != null)
					list.add(field(row));
			return list;
		}

		public String get(int row)
		{
			return field(row).get();
		}

		public Column set(int row, Object value)
		{
			field(row).set(value);
			return this;
		}

		public Column remove(int row)
		{
			field(row).remove();
			return this;
		}

		public Column append(Object value)
		{
			int last = values.keySet().stream()
				.filter(pos -> pos.x() == col)
				.mapToInt(Pos::y)
				.max()
				.orElse(-1);
			set(last + 1, value);
			return this;
		}

		public void setAligment(Alignment aligment)
		{
			alignments.put(col, aligment);
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
