package by.bsu.nummethods.methods;

import by.bsu.nummethods.IFunction;
import by.bsu.nummethods.IFunctionXY;

public class MethodRungeKutta4Power implements IFunction{
	/**
	 * 
	 */
	private double xStart;
	private double xEnd;
	private double yStart;
	private double h;
	private IFunctionXY function;
	/**
	 * 
	 * @return
	 */
	public IFunctionXY getFunction() {
		return function;
	}
	/**
	 * 
	 * @param function
	 */
	public void setFunction(IFunctionXY function){
		this.function = function;
	}
	/**
	 * 
	 * @param h
	 * @param xI
	 * @param yI
	 * @return
	 */
	public double executeRungeMethod(double xI,double yI){
		double result=yI;
		result+=h/6*(countKi(xI, yI, h,0)+2*countKi(xI, yI, h, 1)+2*countKi(xI, yI, h, 2)+countKi(xI, yI, h, 3));
		return result;
	}
	/**
	 * 
	 * @param xI
	 * @param yI
	 * @param h
	 * @param i
	 * @return
	 */
	public double countKi(double xI,double yI,double h,int i){
		double kI=0;
		switch(i){
			case 0:{
				kI=function.getFuncValue(xI, yI);
				break;
				}
			case 1:{
				kI=function.getFuncValue(xI+h/2, yI+h*countKi(xI, yI, h, 0)/2);
				break;
			}
			case 2:{
				kI = function.getFuncValue(xI+h/2*i, yI+h*countKi(xI, yI, h, 1)/2);
				break;
			}
			case 3:{
				kI = function.getFuncValue(xI+h, yI+h*countKi(xI, yI, h, 2));
				break;
			}
			default:break;
		}
		return kI;
	}
	@Override
	public double getYValue(double xValue) {
		double xSt = this.xStart;
		double ySt = this.yStart;
		if(xValue >= this.xStart){
			double i=1;
			while(xValue > i*this.h+xStart){
				ySt = executeRungeMethod(i*h+xSt, ySt);
			}
		}
		return ySt;
	}
	public double getXStart() {
		return xStart;
	}
	public void setXStart(double start) {
		xStart = start;
	}
	public double getXEnd() {
		return xEnd;
	}
	public void setXEnd(double end) {
		xEnd = end;
	}
	public double getYStart() {
		return yStart;
	}
	public void setYStart(double start) {
		yStart = start;
	}
}
