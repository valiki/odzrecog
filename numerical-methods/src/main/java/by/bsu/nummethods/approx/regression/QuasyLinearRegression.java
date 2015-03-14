package by.bsu.nummethods.approx.regression;

import org.apache.log4j.Logger;

import by.bsu.nummethods.IInterpolation;
import by.bsu.nummethods.IRegression;

public class QuasyLinearRegression implements IInterpolation,IRegression {
	private double[][] polinomsStepenValues;
	private int stepenPolinoma = 4;
	private double[] ck;
	private double[] x;
	private double[] y;
	private double xsr;
	private double ysr;
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public double[] getResults(double[] xIn) {
		double[] y = new double[xIn.length];
		for (int i = 0; i < xIn.length; i++) {
			double x = xIn[i];
			y[i] = getOneValue(x);
		}
		return y;
	}

	private double getOneValue(double x) {
		double y1 = 0;
		for (int j = 0; j < stepenPolinoma; j++) {
			y1 += ck[j] * polinom(x, j);
		}
		return y1;
	}

	@Override
	public void initializeData(double[] x, double[] y) {
		this.x = x;
		this.y = y;
		polinomsStepenValues = new double[stepenPolinoma][x.length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < stepenPolinoma; j++) {
				polinomsStepenValues[j][i] = polinom(x[i], j);
			}
		}
		ck = new double[stepenPolinoma];
		for (int i = 0; i < stepenPolinoma; i++) {
			double sumChisl = 0;
			double sumZnam = 0;
			for (int j = 0; j < x.length; j++) {
				sumChisl += y[j] * polinomsStepenValues[i][j];
				sumZnam += Math.pow(polinomsStepenValues[i][j], 2);
			}
			ck[i] = sumChisl / sumZnam;
		}
		double xsrsum = 0;
		double ysrsum = 0;
		for(int i=0;i<x.length;i++){
			xsrsum+=x[i];
			ysrsum+=y[i];
		}
		xsr=xsrsum/x.length;
		ysr=ysrsum/y.length;
		
	}

	private double polinom(double x, int power) {
		if (power < -1) {
			logger.error("power is lower than -1:" + power);
		}
		if (power == -1) {
			return 0;
		} else 
		if (power == 0) {
			return 1;
		}else
		{
			double ak1 = 0;
			double bk1 = 0;
			double ak1Chisl = 0;
			double ak1Znam = 0;
			double bk1Chisl = 0;
			double bk1Znam = 0;
			for (int i = 0; i < this.x.length; i++) {
				ak1Chisl += this.x[i]
						* Math.pow(polinom(this.x[i], power - 1), 2);
				ak1Znam += Math.pow(polinom(this.x[i], power - 1), 2);
				bk1Chisl += Math.pow(polinom(this.x[i], power - 1), 2);
				bk1Znam += Math.pow(polinom(this.x[i], power - 2), 2);
			}
			ak1 = ak1Chisl / ak1Znam;
			if(bk1Znam==0){
				bk1=1;
			}else{
				bk1 = bk1Chisl / bk1Znam;
			}
			double result = (x - ak1) * polinom(x, power - 1) - bk1
					* polinom(x, power - 2);
			return result;
		}
	}

	public int getStepenPolinoma() {
		return stepenPolinoma;
	}

	public void setStepenPolinoma(int stepenPolinoma) {
		this.stepenPolinoma = stepenPolinoma;
	}

	@Override
	public double getKorrelationKoef() {
		double korelChisl = 0;
		double korelZnam = 0;
		for(int i=0;i<x.length;i++){
			korelChisl+=Math.pow(getOneValue(x[i])-ysr, 2);
			korelZnam+=Math.pow(y[i]-ysr, 2);
		}
		return Math.sqrt(korelChisl/korelZnam);
	}

}
