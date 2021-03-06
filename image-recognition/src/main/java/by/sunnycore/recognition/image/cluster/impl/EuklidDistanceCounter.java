package by.sunnycore.recognition.image.cluster.impl;

import by.sunnycore.recognition.image.cluster.DistanceCounter;

/**
 * counts Euklid distance between 2 dots
 * @author Val
 *
 */
public class EuklidDistanceCounter implements DistanceCounter{

	@Override
	public double countDistance(short[] dot1, short[] dot2) {
		double sum = 0;
		for(int i=0;i<dot1.length;i++){
			double l = dot1[i] - dot2[i];
			sum+= l * l;
		}
		return Math.sqrt(sum);
	}

	@Override
	public double countDistance(double[] dot1, double[] dot2) {
		double sum = 0;
		for(int i=0;i<dot1.length;i++){
			double l = dot1[i] - dot2[i];
			sum+= l * l;
		}
		return Math.sqrt(sum);
	}

}
