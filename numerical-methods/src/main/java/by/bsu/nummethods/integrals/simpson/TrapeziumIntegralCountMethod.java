package by.bsu.nummethods.integrals.simpson;

import by.bsu.nummethods.IFunction;
/**
 * counts integral using the trapeze numerical method
 * 
 * @author Valentine Shukaila
 *
 */
public class TrapeziumIntegralCountMethod implements IIntegralCountMethod{

	@Override
	public double countIntegral(double x0, double x1,
			IFunction f,double maxError) {
		double l = x1-x0;
		double h = maxError*100;
		double s = 0;
		double x =x0;
		int numSteps = (int) Math.floor(l/h);
		for(int i=0;i<numSteps;i++){
			s+=Math.abs(f.getYValue(x+h)+f.getYValue(x))/2*h;
			x+=h;
		}
		return s;
	}

}
