package by.sunnycore.recognition.image.cluster.impl;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.cluster.DataClusterer;
import by.sunnycore.recognition.image.cluster.TeachableMethod;

/**
 * Clusters data using Spectral Angle Clustering method
 * 
 * @author Valiantsin Shukaila
 * 
 */
public class SpectralAngleClusterer implements DataClusterer,TeachableMethod {

	private double[][] mathExpectations;
	
	private double[] averageSpectralAngles;

	private SpectralAngleCounter spectralAngleCounter;

	private int clustersNumber;
	
	@Override
	public short[][][] cluster(short[][] data) {
		short[][][] result = new short[mathExpectations.length][data.length][data[0].length];
		for(int i=0;i<data[0].length;i++){
			double[] point = new double[data.length];
			for(int j=0;j<data.length;j++){
				point[j]=data[j][i];
			}
			int cluster = clusterOnePoint(point);
			if (cluster!=-1) {
				for (int j = 0; j < point.length; j++) {
					result[cluster][j][i] = (short) point[j];
				}
			}
		}
		return result;
	}
	
	public int clusterOnePoint(double[] point){
		int cluster = -1;
		double lastAngle = Double.MAX_VALUE;
		for(int i=0;i<averageSpectralAngles.length;i++){
			double angle = spectralAngleCounter.count(mathExpectations[i], point);
			double averageAngle = averageSpectralAngles[i];
			if(Math.abs(angle) < averageAngle){
				if (cluster==-1) {
					cluster = i;
					lastAngle = angle;
				}else{
					//means that cluster lies in few angles and it is unclassified
					//do not add unclassified yet
					//cluster = -1;
					//break;
					if(angle < lastAngle){
						cluster = i;
					}
				}
			}
		}
		return cluster;
	}

	@Override
	public void teach(List<ObjectCluster[]> teachData) {
		spectralAngleCounter = new SpectralAngleCounter();
		clustersNumber = teachData.get(0).length;
		mathExpectations = new double[clustersNumber][];
		averageSpectralAngles = new double[clustersNumber];
		int[][] data = teachData.get(0)[0].getClusterPoints();
		double[][] baseVectors = new double[data.length][data.length];
    	double[] baseVector = new double[data.length];
		int currentBand = 0;
    	for(int i=0;i<data.length;i++){
    		baseVector[i]=1;
    		for(int j=0;j<data.length;j++){
    			if(j == currentBand){
    				baseVectors[i][j] = 1;
    			}else{
    				baseVectors[i][j] = 0;
    			}
    		}
    		currentBand++;
    	}
		
		
		for(int k=0;k<teachData.size();k++){
			ObjectCluster[] teachSet = teachData.get(k);
			double[][] angles = new double[teachSet.length][data.length];
			for(int j=0;j<teachSet.length;j++){
				ObjectCluster cluster = teachSet[j];
				int[][] clusterPoints = cluster.getClusterPoints();
				double[] angle = new double[data.length];
				for(int i=0;i<angle.length;i++){
					angle[i]=0;
				}
				for(int l=0;l<clusterPoints[0].length;l++){
					double[] curAngle = new double[data.length];
					double[] curPoint = new double[data.length];
					for(int s=0;s<clusterPoints.length;s++){
						curPoint[s]=clusterPoints[s][l];
					}
					for(int s=0;s<curAngle.length;s++){
						angle[s]+=spectralAngleCounter.count(curPoint, baseVectors[s]);
					}
				}
				for(int i=0;i<angle.length;i++){
					angle[i]/=clusterPoints[0].length;
				}
				for(int i=0;i<angles[j].length;i++){
					angles[j][i]+=angle[i];
				}
			}
		}
		for(ObjectCluster[] clusters:teachData){
			double averageSpectralAngle = 0;
			for(int k=0;k<clusters.length;k++){
				ObjectCluster cluster = clusters[k];
				int[][] points = cluster.getClusterPoints();
				for(int i=0;i<points[0].length;i++){
					double[] point = new double[points.length];
					for(int j=0;j<points.length;j++){
						point[j]=points[j][i];
					}
					double angle = spectralAngleCounter.count(point, mathExpectations[k]);
					averageSpectralAngle+=Math.abs(angle);
				}
				averageSpectralAngle/=points[0].length;
				averageSpectralAngles[k]+=averageSpectralAngle;
			}
		}
		for(int i=0;i<averageSpectralAngles.length;i++){
			averageSpectralAngles[i]/=teachData.size();
		}
	}

	public double[][] getMathExpectations() {
		return mathExpectations;
	}

}
