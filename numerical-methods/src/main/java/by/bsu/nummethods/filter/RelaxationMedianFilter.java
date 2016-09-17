package by.bsu.nummethods.filter;

public class RelaxationMedianFilter implements IFilter {

	private RelaxationFilterStartToEnd relaxFromFilter;
	private RelaxationFilterEndToStart relaxationFilterEndToStart;
	
	public RelaxationMedianFilter(double weight) {
		this.relaxFromFilter = new RelaxationFilterStartToEnd(weight);
		this.relaxationFilterEndToStart = new RelaxationFilterEndToStart(weight);
	}

	@Override
	public double[] filter(double[] input) {
		if (input.length == 0) {
			return input;
		}
		double[] result = new double[input.length];

		final double[] relaxFilterStartToEnd = relaxFromFilter.filter(input);
		final double[] relaxFilterEndToStart = relaxationFilterEndToStart.filter(input);
		for (int i = 0; i < input.length; i++) {
			result[i] = (relaxFilterStartToEnd[i]+relaxFilterEndToStart[i])/2;
		}
		return result;
	}
	
	//---------------------------------------------------------------------
	//усредененный медианный фильтр
	//---------------------------------------------------------------------
//	void RelaxMedium(const double* Source, double* Result, int Size, double Weight)
//	{
//		if (Size == 0)
//			return;
//		RelaxFrontFilter(Source,Result,Size,Weight);
//		double* res = new double[Size];
//		RelaxBackFilter(Source,res,Size,Weight);
//		double* p_res = res;
//		for (int i = 0; i < Size; i++, p_res++, Result++)
//			*Result = (*Result+*p_res)/2;
//		delete[] res;
//	}
	
}
