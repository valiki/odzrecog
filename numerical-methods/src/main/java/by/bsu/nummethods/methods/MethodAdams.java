package by.bsu.nummethods.methods;

import by.bsu.nummethods.IFunction;
import by.bsu.nummethods.IFunctionXY;

public class MethodAdams implements IFunction {
	private IFunctionXY function;
	private double xStart;
	private double xEnd;
	private double yStart;
	private double h;
	private MethodRungeKutta4Power runge;
	public MethodAdams(){
		
	}
	public MethodAdams(double xStart,double xEnd,double yStart,double h){
		runge = new MethodRungeKutta4Power();
		runge.setFunction(function);
		runge.setXEnd(xEnd);
		runge.setXStart(xStart);
		runge.setYStart(yStart);
		this.h = h;
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.yStart = yStart;
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
	public double getH() {
		return h;
	}
	public void setH(double h) {
		this.h = h;
	}
	public double executeAddamsMethod(double xI,double yI){
		double result = yI;
		runge.setYStart(yI);
		result+= this.h/24*( 55*yI+59*runge.getYValue(xI-h)+37*runge.getYValue(xI-2*h)-9*runge.getYValue(xI-3*h) );
		return result;
	}
	@Override
	public double getYValue(double xValue) {
		double xEnd = this.xEnd;
		double ySt = this.yStart;
		while(xValue<xEnd){
			ySt = executeAddamsMethod(xEnd, ySt);
		}
		return ySt;
	}
	public IFunctionXY getFunction() {
		return function;
	}
	public void setFunction(IFunctionXY function) {
		this.function = function;

	}

}



