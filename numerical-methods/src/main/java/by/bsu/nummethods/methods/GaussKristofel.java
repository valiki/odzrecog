package by.bsu.nummethods.methods;

import by.bsu.nummethods.IFunction;
/**
 * 
 * @author Valiantsin Shukaila
 *
 */
public class GaussKristofel implements IFunction {

	@Override
	public double getYValue(double xValue) {
		return simpleInitFunction(xValue);
	}
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static double simpleInitFunction(double x){
		double a = 1.0/3.0;
		double b = 1.0;
		double power1 = 2.0;
		double power2 =  -1.0/2.0;
		double c = 1.0/9.0;
		double result=a*1/Math.pow(b-Math.pow(x,power1 ),power2)/(c+Math.pow(x,power1));
		return result;
	}
	/**
	 * 
	 * @param n
	 * @return
	 */
	public static double executeGauusChebishevMethod(int n){
		double result=0;
		for(int i=1;i<n+1;i++){
			result+=functionWithoutChebishevMnogitel(countXi(i, n))*countCi(n);
		}
		return result;
	}
	/**
	 * 
	 * @param i
	 * @param n
	 * @return
	 */
	public static double countXi(int i,int n){
		double number = n;
		double cur = i;
		double a = 2.0;
		double result = Math.cos((a*cur-1)/Math.pow(a, number)*Math.PI);
		return result;
	}
	/**
	 * 
	 * @param n
	 * @return
	 */
	public static double countCi(int n){
		double number = n;
		double result = Math.PI/number;
		return result;
	}
	public static double functionWithoutChebishevMnogitel(double x){
		double a = 1.0/3.0;
		double power1 = 2.0;
		double c = 1.0/9.0;
		double result=a*1/(c+Math.pow(x,power1));
		return result;
	}
}
