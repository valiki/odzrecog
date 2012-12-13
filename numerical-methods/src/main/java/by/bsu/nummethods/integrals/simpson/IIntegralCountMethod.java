package by.bsu.nummethods.integrals.simpson;

import by.bsu.nummethods.IFunction;

public interface IIntegralCountMethod {
	public double countIntegral(double x0,double x1,IFunction function,double maxError);
}
