package by.bsu.nummethods.integrals.simpson;

import org.apache.log4j.Logger;

import by.bsu.nummethods.IIntegral;
import by.bsu.nummethods.exception.NotEqualNumberOfXAndY;
import by.bsu.nummethods.exception.NotEvenNumberOfPointsException;

public class SimpsonMethod implements IIntegral {
	private Logger logger = Logger.getLogger(SimpsonMethod.class);

	@Override
	public double countIntegralValue(double[][] points) {
		int xLength = points[0].length;
		if (xLength != points[1].length) {
			String msg = "number of X and Y values is not equal";
			logger.error(msg);
			throw new NotEqualNumberOfXAndY(msg);
		}
		if ((xLength % 2) != 0) {
			String msg = "simson method requires that number of point will be even, but it is not:"+xLength;
			logger.error(msg);
			throw new NotEvenNumberOfPointsException(msg);
		}
		double h = points[0][1] - points[0][0];
		double a = points[1][0];
		double b = points[1][points.length-1];
		double sum = a + b;
		for (int i = 1; i < xLength - 1; i++) {
			double y = points[1][i];
			if (i % 2 == 0) {
				sum += (2 * y);
			} else {
				sum += (4 * y);
			}
		}
		sum = sum * h / 3;
		return sum;
	}

}
