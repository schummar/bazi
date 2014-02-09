package de.uni_augsburg.bazi.biprop;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

class Matrix<T>
{
	private List<List<T>> rows;
	private List<List<T>> cols;

	public Matrix(int rows, int cols, BiFunction<Integer, Integer, T> supplier)
	{
		this.rows = new ArrayList<>(rows);
		for (int row = 0; row < rows; row++) this.rows.add(new ArrayList<>());
		this.cols = new ArrayList<>(cols);
		for (int col = 0; col < cols; col++) this.cols.add(new ArrayList<>());

		for (int row = 0; row < rows; row++)
			for (int col = 0; col < cols; col++)
			{
				T t = supplier.apply(row, col);
				this.rows.get(row).add(t);
				this.cols.get(col).add(t);
			}
	}

	public T get(int row, int col)
	{
		return rows.get(row).get(col);
	}

	public void set(int row, int col, T value)
	{
		rows.get(row).set(col, value);
		cols.get(col).set(row, value);
	}

	public int rows() { return rows.size(); }
	public int cols() { return cols.size(); }
	public List<T> row(int row) { return rows.get(row); }
	public List<T> col(int col) { return cols.get(col); }

	public void transpose()
	{
		List<List<T>> temp = rows;
		rows = cols;
		cols = temp;
	}

	public String toString(Function<T, String> mapper)
	{
		List<Function<String, String>> paddings = new ArrayList<>();
		for (int col = 0; col < cols(); col++)
		{
			int colWidth = col(col).stream()
				.map(mapper)
				.map(String::length)
				.max(Integer::compare)
				.orElse(0) + (col < cols() - 1 ? 2 : 0);
			paddings.add(s -> pad(s, colWidth));
		}

		StringBuilder s = new StringBuilder();
		for (int row = 0; row < rows(); row++)
		{
			if (row == 0) s.append("/");
			else if (row == rows() - 1) s.append("\\");
			else s.append("|");
			for (int col = 0; col < cols(); col++)
			{
				String value = mapper.apply(get(row, col));
				s.append(paddings.get(col).apply(value));
			}
			if (row == 0) s.append("\\");
			else if (row == rows() - 1) s.append("/");
			else s.append("|");
			if (row < rows() - 1) s.append("\n");
		}
		return s.toString();
	}

	private String pad(String s, int length)
	{
		while (s.length() < length) s += " ";
		return s;
	}
}
