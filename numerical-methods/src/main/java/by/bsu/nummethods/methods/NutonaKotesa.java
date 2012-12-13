package by.bsu.nummethods.methods;

import by.bsu.nummethods.IFunction;
/**
 * 
 * @author Valiantsin Shukaila
 *
 */
public class NutonaKotesa implements IFunction {
	/**
	 * 
	 */
	@Override
	public double getYValue(double XValue) {
		
		return fourthDerivative(XValue);
	}
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static double simpleInitFunction(double x){
		double a = 1.0/3.0;
		double result = -1.2*Math.pow(x, a)+Math.pow(Math.cos(x), 3.0);
		return result;
	}
	public static double fourthDerivative(double x){
		double power=11.0/3.0;
		double result = 1.18519/Math.pow(x, power)+21*Math.pow(Math.cos(x),3.0)-60*Math.cos(x)*Math.pow(Math.sin(x),2.0);
		return result;
	}
	/**
	 * 
	 * @param M4
	 * @param e
	 * @param xMax
	 * @param xMin
	 * @return
	 */
	public static double countH(double M4,double e,double xMax,double xMin){
		double power = 1.0/4.0;
		double result = Math.pow(180*e/(xMax-xMin)/M4,power);
		return result;	
	}
	/**
	 * 
	 * @param h
	 * @param xMax
	 * @param xMin
	 * @return
	 */
	public static int countNumberOfBonds(double h,double xMax,double xMin){
		int result;
		double promej = Math.ceil((xMax-xMin)/h);
		result = (int) promej;
		while((result%2)!=0){
			result++;
		}
		return result;
	}
	/**
	 * 
	 * @param xMin
	 * @param xMax
	 * @param e
	 * @return
	 */
	public static double countMetodSimpsona(double xMin,double xMax,double e,double M4){
		double result=0;
		double h=countH(M4, e, xMax, xMin);
		double n = countNumberOfBonds(h, xMax, xMin);
		for(int i=0;i<n+1;i++){
			if(i==0){
				result+=simpleInitFunction(xMin);
			}else	
			if(i==n){
				result+=simpleInitFunction(xMax);
			}else
			if(i%2==0){
				result+=2*simpleInitFunction(xMin+h*i);
			}else
			if(i%2!=0){
				result+=4*simpleInitFunction(xMin+h*i);
			}
		}
		result*=h/3.0;
		return result;
	}
}
