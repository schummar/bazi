import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Matrix<T, U> implements Iterable<Matrix<T,U>.Field>
{
	private final int rows, cols;
	private boolean transposed = false;
	private final List<List<T>> values;
	private final List<U> rowBorder, colBorder;

	public Matrix(int rows, int cols, T identity, U borderIdentity)
	{
		this.rows = rows;
		this.cols = cols;
		values = new ArrayList<>();
		rowBorder = new ArrayList<>();
		colBorder = new ArrayList<>();
		for (int row = 0; row < rows; row++)
		{
			List<T> rowList = new ArrayList<>(cols);
			for (int col=0; col < cols;col++)
				rowList.add(identity);
			values.add(rowList);
			rowBorder.add(borderIdentity);
		}
	}

	public T get(int row, int col)
	{
		return transposed ? values.get(col).get(row) : values.get(row).get(col);
	}

	public void set(int row, int col, T value)
	{
		if (transposed)
			values.get(col).set(row, value);
		else
			values.get(row).set(col, value);
	}

	public U rowBorder(int row) { return transposed ? colBorder.get(row) : rowBorder.get(row); }
	public U colBorder(int col) { return transposed ? rowBorder.get(col) : colBorder.get(col); }

	public int rows() { return transposed ? cols : rows; }
	public int cols() { return transposed ? rows : cols; }
	public List<Field> row(int row)
	{
		List<Field> rowList = 
		if (!transposed) return values.get(row);
		return values.stream().map(list -> list.get(row)).collect(Collectors.toList());
	}
	public List<Field> col(int col)
	{
		if (transposed) return values.get(col);
		return values.stream().map(list -> list.get(col)).collect(Collectors.toList());
	}

	@Override
	public Iterator<Field> iterator()
	{
		return null;
	}
	public class Field
	{
		private final int row, col;
		public Field(int row, int col)
		{
			this.row = row;
			this.col = col;
		}
		public int row() { return row; }
		public int col() { return col; }
		public T get() { return Matrix.this.get(row,col); }
		public U rowBorder() { return Matrix.this.rowBorder(row); }
		public U colBorder() { return Matrix.this.colBorder(row); }
	}
}
