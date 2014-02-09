import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class Matrix<T>
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
}
