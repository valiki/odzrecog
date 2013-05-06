package by.sunnycore.recognition.image.cluster;

public interface DistanceCounter {

	double countDistance(short[] dot1,short[] dot2);
	
	double countDistance(double[] dot1,double[] dot2);
}
