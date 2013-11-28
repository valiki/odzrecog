package by.bsu.nummethods.approx.splyne;

import org.apache.log4j.Logger;

import by.bsu.nummethods.IFunction;
import by.bsu.nummethods.IInterpolation;
import by.bsu.nummethods.gauss.GaussMethod;

/**
 * 
 * This class counts value of function in a point
 *         using cube splain method of interpolation
 * 
 * @author Valiantsin Shukaila 
 * 
 */
public class CubeSplineMethod implements IInterpolation, IFunction {

	private static Logger logger = Logger.getLogger(CubeSplineMethod.class);

	private double[] initXAll;
	private double[] initYAll;

	public CubeSplineMethod() {
	}
	
	public CubeSplineMethod(double[] xData, double[] yData){
		this.initXAll = xData;
		this.initYAll = yData;
	}
	/**
	 * 
	 * @param fx
	 * @param xInit
	 * @param x
	 * @return
	 */
	public static double executeCubeSplain(double[] fx, double[] xInit, double x) {
		double result = 0;

		int i = countI(x, xInit);
		double[] q = secondDerivativs(xInit, fx);
		double[] hi = new double[xInit.length];
		countH(xInit, hi);
		result = q[i - 1] * Math.pow((xInit[i] - x), 3) / (6 * hi[i]) + q[i]
				* Math.pow((x - xInit[i - 1]), 3) / (6 * hi[i])
				+ (fx[i - 1] / hi[i] - q[i - 1] * hi[i] / 6) * (xInit[i] - x)
				+ (fx[i] / hi[i] - q[i] * hi[i] / 6) * (x - xInit[i - 1]);
		return result;

	}

	/**
	 * 
	 * @param x
	 * @param xInit
	 * @return
	 */
	public static int countI(double x, double[] xInit) {
		int i = 1;
		for (int j = 1; j < xInit.length; j++) {
			if (Math.abs(x) > Math.abs(xInit[j - 1])) {
				i = j;
			}
		}
		return i;
	}

	/**
	 * 
	 * @param xInit
	 * @param fx
	 * @return
	 */
	public static double[] secondDerivativs(double[] xInit, double[] fx) {
		double[] result = new double[xInit.length];
		result[0] = 0;
		result[xInit.length - 1] = 0;

		double[] hi = new double[xInit.length];
		countH(xInit, hi);

		double[][] koefficientsOfEquation = new double[fx.length - 2][fx.length - 1];
		countCoefficientsOfDerivativs(hi, fx, koefficientsOfEquation);

		double[] res = new double[xInit.length - 2];
		GaussMethod.executeGauss(koefficientsOfEquation, res);
		addQ0andQn(res, result);
		return result;
	}

	/**
	 * 
	 * @param res
	 * @param result
	 */
	public static void addQ0andQn(double[] res, double[] result) {
		for (int i = 1; i < result.length - 1; i++) {
			result[i] = res[i - 1];
		}
	}

	/**
	 * 
	 * @param xInit
	 * @param hi
	 */
	public static void countH(double[] xInit, double[] hi) {
		for (int i = 1; i < hi.length; i++) {
			hi[i] = xInit[i] - xInit[i - 1];
		}
	}

	/**
	 * 
	 * @param hi
	 * @param fx
	 * @param koefficientsOfEquation
	 */
	public static void countCoefficientsOfDerivativs(double[] hi, double[] fx,
			double[][] koefficientsOfEquation) {
		for (int i = 1; i < fx.length - 1; i++) {
			if (i == 1) {
				koefficientsOfEquation[i - 1][i - 1] = (hi[i] + hi[i + 1]) / 3;
				koefficientsOfEquation[i - 1][i] = hi[i + 1] / 6;
			} else if (i == fx.length - 2) {
				koefficientsOfEquation[i - 1][i - 2] = hi[i] / 6;
				koefficientsOfEquation[i - 1][i - 1] = (hi[i] + hi[i + 1]) / 3;
			} else {
				koefficientsOfEquation[i - 1][i - 2] = hi[i] / 6;
				koefficientsOfEquation[i - 1][i - 1] = (hi[i] + hi[i + 1]) / 3;
				koefficientsOfEquation[i - 1][i] = hi[i + 1] / 6;
			}
			koefficientsOfEquation[i - 1][koefficientsOfEquation[0].length - 1] = (fx[i + 1] - fx[i])
					/ hi[i + 1] - (fx[i] - fx[i - 1]) / hi[i];
		}
	}

	/**
	 * 
	 */
	@Override
	public double getYValue(double XValue) {
		double res = 0;
		try {
			res = executeCubeSplain(initYAll, initXAll, XValue);
		} catch (NullPointerException e) {
			logger.error("two massivs of numbers should be initialized", e);
		}
		return res;
	}

	@Override
	public double[] getResults(double[] xArray) {
		double[] result = new double[xArray.length];
		for(int i=0;i<xArray.length;i++){
			result[i]=getYValue(xArray[i]);
		}
		return result;
	}

	@Override
	public void initializeData(double[] xData, double[] yData) {
		initXAll = xData;
		initYAll = yData;
	}

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
}
