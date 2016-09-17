package by.bsu.nummethods.filter;

public class MovingAverageFilter implements IFilter {

	private int windowSize;
	
	public MovingAverageFilter(int windowSize) {
		this.windowSize = windowSize;
	}
	
	@Override
	public double[] filter(double[] input) {
		
		double[] result = input.clone();
	
		all:for (int i = 0; i < result.length; i = i + windowSize/2) {
			double movingAverage = 0;
			for (int j=i,k=0;j<i+windowSize;j++,k++) {
				if (j>result.length - 1) {
					break all;
				}
				result[j] = (movingAverage + result[j]) / (k + 1);
				movingAverage += result[j];
			}
		}
		return result;
	}

}
