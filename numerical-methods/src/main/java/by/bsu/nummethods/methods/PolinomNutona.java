package by.bsu.nummethods.methods;

import org.apache.log4j.Logger;

import by.bsu.nummethods.IFunction;
/**
 * 
 * @author Valiantsin Shukaila
 * @category This class is used to interpolate function entered as massiv of values using Newton interpolation formula
 */

public final class PolinomNutona implements IFunction{
	private static Logger logger = Logger.getLogger(PolinomNutona.class);
	
	private double[] initXAll;
	private double[] initYAll;
	/**
	 * @param y1 the bigger number
	 * @param y0 the smaller number
	 * @return return difference between y1 and y0 
	 */
	public double[] getInitXAll() {
		return initXAll;
	}
	public void setInitXAll(double[] initXAll) {
		this.initXAll = initXAll;
	}
	public double[] getInitYAll() {
		return initYAll;
	}
	public void setInitYAll(double[] initYAll) {
		this.initYAll = initYAll;
	}
	public static double difference(double y1,double y0){
		return y1-y0;
	}
	/**
	 * 
	 * @param n the power of difference
	 * @param initNumbersAll the array of values which difference we are counting
	 * @return the difference of n power using initNumbersAll array values
	 */
	public static double differencePowerNY0(int n,double[] initNumbersAll) {
		double[] initNumbers =new double[n+1];
		for(int i=0;i<n+1;i++){
			initNumbers[i]=initNumbersAll[i];
		}
		do{
			double[] outNumbers = new double[initNumbers.length-1];
			for(int j=0;j<outNumbers.length;j++){
				try {
					outNumbers[j] = difference(initNumbers[j+1], initNumbers[j]);
				} catch (IndexOutOfBoundsException e) {
					logger.error("The difference of entered power \n can not be counted using entered massiv of numbers", e);
				}
			}
			initNumbers = outNumbers.clone();
		}while(initNumbers.length>1);
		return initNumbers[0];
	}
	/**
	 * 
	 * @param initXAll the points entered to interpolate function at
	 * @param initYAll the values of function at entered points
	 * @param x the point where we need to find the value of a function. It should be between min(initXAll) and max(initXAll)
	 * @return the value of function at entered point
	 */
	public static double functionUsingPolinomNutona(double[] initXAll,double[] initYAll, double x){
		double result=0;
		double slagaemoe=1;
		double h=initXAll[1]-initXAll[0];
		result+=initYAll[0];
		for(int i=1;i<initYAll.length;i++){
				slagaemoe*=differencePowerNY0(i, initYAll)*(x-initXAll[i-1])*1/(h*i);
			result+=slagaemoe;
		}
		return result;
	}
	
	@Override
	public double getYValue(double XValue) {
		double res=0;
		try {
			res= functionUsingPolinomNutona(initXAll, initYAll, XValue);
		} catch (NullPointerException e) {
			logger.error("two massivs of numbers should be initialized", e);
		}
		return res;
	}
}
