package by.bsu.nummethods;

public interface IInterpolation {
	public double[] getResults(double[] xArray);
	/**
	 * 
	 * @param xData array of x values
	 * @param yData array of y values
	 */
	public void initializeData(double[] xData,double[] yData);
}
