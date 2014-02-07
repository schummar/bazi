import de.uni_augsburg.bazi.math.BMath;
import de.uni_augsburg.bazi.math.Real;

import java.util.Arrays;

public class ASAlgorithm
{
	public static Object calculate(Matrix<BipropOutput.Party> matrix) throws InterruptedException
	{
		boolean isRowStep = true;

		Real[] rowDivisors = new Real[matrix.rows()];
		Real[] colDivisors = new Real[matrix.cols()];
		Arrays.fill(rowDivisors, BMath.ONE);
		Arrays.fill(colDivisors, BMath.ONE);

		while(true)
		{
			if (Thread.interrupted())
				throw new InterruptedException();

			matrix.forEach((i,j)
		}


		return null;
	}
}
