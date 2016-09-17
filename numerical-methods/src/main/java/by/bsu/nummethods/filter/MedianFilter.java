package by.bsu.nummethods.filter;

public class MedianFilter implements IFilter {

	private int depth;

	public MedianFilter(int depth) {
		this.depth = depth;
		// глубина должна быть нечетной
		if (depth % 2 != 1) {
			throw new IllegalArgumentException("Depth should be not even");
		}
	}

	@Override
	public double[] filter(double[] input) {
		if (input.length == 0) {
			return input;
		}
		double[] result = new double[input.length];

		// смещение
		int m = depth - 1 / 2;

		double m_value;
		for (int r = 0; r < input.length - depth; r++) {
			m_value = 0;
			for (int i = r; i < r + depth; i++)
				m_value += input[i];
			m_value = m_value / (double) depth;
			result[r + m] = (int) (m_value + 0.5);
		}
		for (int r = input.length - depth + m; r < input.length; r++) {
			result[r] = result[input.length - depth + m - 1];
		}
		for (int r = 0; r < m; r++) {
			result[r] = result[m];
		}

		return result;
	}

	// void MediumFilter(unsigned short *Source, unsigned short *Result, int
	// width, unsigned char depth)
	// {
	// //глубина должна быть нечетной
	// if (depth % 2 != 1) depth++;
	// //смещение
	// unsigned char m;
	// double m_value;
	// m = (depth - 1) / 2;
	// for (int r = 0; r < width - depth; r++)
	// {
	// m_value = 0;
	// for (int i = r; i < r + depth; i++)
	// m_value += Source[i];
	// m_value = m_value / (double) depth;
	// Result[r + m] = (int)(m_value + 0.5);
	// }
	// for (int r = width - depth + m; r < width; r++)
	// Result[r] = Result[width - depth + m - 1];
	// for (int r = 0; r < m; r++)
	// {
	// Result[r] = Result[m];
	// }
	//
	// }
}
