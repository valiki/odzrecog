package by.sunnycore.recognition.image.cluster.impl;

import by.sunnycore.recognition.image.cluster.ParamCounter;

/**
 * Class to count spectral angle of some specific point
 * 
 * @author Valiantsin Shukaila
 *
 */
public class SpectralAngleCounter implements ParamCounter{

	/**
	 * Counts angle between two vectors
	 */
	@Override
	public double count(double[] v1, double[] v2) {
		double multiplication = 0;
		double length1 = 0;
		double length2 = 0;
		for(int i=0;i<v1.length;i++){
			multiplication+=v1[i]*v2[i];
			length1+=Math.pow(v1[i], 2);
			length2+=Math.pow(v2[i], 2);
		}
		length1 = Math.sqrt(length1);
		length2 = Math.sqrt(length2);
		return Math.acos( multiplication/(length1*length2) )*90;
	}	

}
