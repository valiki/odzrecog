package by.bsu.nummethods.filter;

import by.bsu.commons.NumberUtil;
import by.bsu.nummethods.exception.ChisMethodException;

/**
 * Passes only low frequencies
 * 
 * @author _Trims
 *
 */
public class LinearLowPassFilter implements IFilter{

	/**
	 * time interval
	 */
	private double dt;
	
	/**
	 * time constant
	 */
	private double rc;
	
	/**
	 * creates filter with params
	 * @param dt time interval
	 * @param rc time constant
	 */
	public LinearLowPassFilter(double dt,double rc) {
		this.dt= dt;
		this.rc = rc;
	}
	
	@Override
	public double[] filter(double[] input) {
		double[] result = new double[input.length];
		if(NumberUtil.equalsZero(dt)|| NumberUtil.equalsZero(rc)){
			throw new ChisMethodException("time interval and time contant for low pass linear filter should be not zero");
		}
		double alpha = dt/(rc+dt);
		result[0] = input[0];
		for(int i=1;i<input.length;i++){
			result[i] = alpha * input[i] + (1-alpha) * result[i-1];//alpha*yi+(1-alpha)*y(i-1)    
		}
		return result;
	}

}
