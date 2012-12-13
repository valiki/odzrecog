package by.bsu.nummethods.gauss;

import by.bsu.nummethods.IFunctionXY;
/**
 * the typical gauss function for two variables x and y
 * @author Valiantsin Shukaila
 *
 */
public class GaussFunction implements IFunctionXY{
	
	private double sigma;

	public GaussFunction(double sigma) {
		this.sigma = sigma;
	}
	
	@Override
	public double getFuncValue(double x, double y) {
		return 1/(2*Math.PI*sigma*sigma)*Math.exp(-(x*x+y*y)/(2*sigma*sigma));
	}

}
