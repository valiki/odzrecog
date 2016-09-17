package by.bsu.nummethods.filter;

/**
 * релаксационный фильтр из оригинальной программы ФСС
 * 
 * @author user
 *
 */
public class RelaxationFilterStartToEnd implements IFilter {

	private double weight;

	public RelaxationFilterStartToEnd(final double weight) {
		this.weight = weight;
	}
	
	@Override
	public double[] filter(double[] input) {
		if (input.length == 0) {
			return input;
		}
		double[] result = new double[input.length];
		for (int i=1;i<input.length;i++) {
			result[i] = input[i-1]*weight + input[i]*(1-weight);
		}
		return result;
	}
	
	//---------------------------------------------------------------------
		//релаксационный фильтр с проходом от начала к концу
		//---------------------------------------------------------------------
//	void RelaxFrontFilter(const double* Source, double* Result, int Size, double Weight)
//	{
//		if (Size == 0)
//			return;
//		double* p_Result = Result;
//		*Result++ = *Source++;
//		for (int i = 1; i < Size; i++, Source++, p_Result++, Result++)
//			*Result = *p_Result*Weight + *Source*(1-Weight);
//	}

}
