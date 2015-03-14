package by.bsu.nummethods.integrals.simpson;

import org.junit.Test;

import by.bsu.nummethods.IFunction;

public class TrapeziumIntegralCountMethodTest {
	
	@Test
	public void test(){
		IIntegralCountMethod method = new TrapeziumIntegralCountMethod();
		IFunction function = new IFunction() {
			
			@Override
			public double getYValue(double xValue) {
				return 2/Math.sqrt(2*Math.PI)*Math.exp(-(xValue*xValue)/2);
			}
		};
		double result = method.countIntegral(0, 3.99, function, 0.000005);
		System.out.println(result);
	}
}
