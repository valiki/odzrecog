package by.bsu.nummethods.approx.regression;

import org.apache.log4j.Logger;

import by.bsu.nummethods.IInterpolation;
import by.bsu.nummethods.IRegression;
import by.bsu.nummethods.exception.NotEqualNumberOfXAndY;
import by.bsu.nummethods.exception.NotInitializedDataException;

public class LinearRegression implements IInterpolation,IRegression {
	private Logger logger = Logger.getLogger(LinearRegression.class);
	private double[] x;
	private double[] y;
	private double xsr;
	private double ysr;
	private double b1;
	
	@Override
	public double[] getResults(double[] xIn) {
		if(x==null || y==null){
			String msg = "first data should be initialized";
			logger.error(msg);
			throw new NotInitializedDataException(msg);
		}
		int length = xIn.length;
		double results[] = new double[length];
		for(int i=0;i<length;i++){
			results[i] = ysr+b1*(xIn[i]-xsr);
		}
		return results;
	}

	@Override
	public void initializeData(double[] x, double[] y) {
		this.x = x;
		this.y = y;
		if (x.length != y.length) {
			String msg = "number of x and y coordinates should be equal";
			logger.error(msg);
			throw new NotEqualNumberOfXAndY(msg);
		}
		double xSum = 0;
		double ySum = 0;
		for (int i = 0; i < x.length; i++) {
			xSum += x[i];
			ySum += y[i];
		}
		xsr = xSum / x.length;
		ysr = ySum / y.length;
		double b1chisl = 0;
		double b1znam = 0;
		for (int i = 0; i < x.length; i++) {
			b1chisl += (x[i] - xsr) * (y[i] - ysr);
			b1znam += Math.pow(x[i] - xsr, 2);
		}
		b1=b1chisl/b1znam;
	}

	@Override
	public double getKorrelationKoef() {
		double korelChisl = 0;
		double korelZnam1 = 0;
		double korelZnam2 = 0;
		for(int i=0;i<x.length;i++){
			korelChisl+=(x[i]-xsr)*(y[i]-ysr);
			korelZnam1+=Math.pow(x[i]-xsr, 2);
			korelZnam2+=Math.pow(y[i]-ysr, 2);
		}
		return korelChisl/Math.sqrt(korelZnam1*korelZnam2);
	}

}
