package by.bsu.nummethods.filter;

public class RelaxationFilterEndToStart implements IFilter {

	private double weight;

	public RelaxationFilterEndToStart(double weight) {
		this.weight = weight;
	}
	
	@Override
	public double[] filter(double[] input) {
		if (input.length == 0) {
			return input;
		}
		double[] result = new double[input.length];

		for (int i = input.length - 2; i > 0; i--) {
			result[i] = input[i] * weight + input[i + 1] * (1 - weight);
		}
		return result;
	}

	// //---------------------------------------------------------------------
	// //релаксационный фильтр с проходом от конца к началу
	// //---------------------------------------------------------------------
	// void RelaxBackFilter(const double* Source, double* Result, int Size,
	// double Weight)
	// {
	// if (Size == 0)
	// return;
	// Source += Size-1;
	// Result += Size-1;
	// double* p_Result = Result;
	// *Result-- = *Source--;
	// for (int i = 1; i < Size; i++, Source--, p_Result--, Result--)
	// *Result = *p_Result*Weight + *Source*(1-Weight);
	// }

}
