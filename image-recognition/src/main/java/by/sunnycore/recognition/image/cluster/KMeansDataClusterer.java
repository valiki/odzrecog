package by.sunnycore.recognition.image.cluster;

import by.sunnycore.recognition.image.cluster.impl.EuklidDistanceCounter;

/**
 * Does clusterization using KMeans clustering algorithm
 * 
 * @author Valiantsin Shukaila
 *
 */
public class KMeansDataClusterer implements DataClusterer{

	private int clustersNumber;
	private double[][] clusterCenters;
	private double coordMinValue;
	private double coordMaxValue;
	
	/**
	 * Object that counts distance between two dots
	 */
	private DistanceCounter distanceCounter = new EuklidDistanceCounter();
	
	/**
	 * 
	 * @param clustersNumber
	 */
	public KMeansDataClusterer(int clustersNumber) {
		this(clustersNumber,null,0,0);
	}
	
	/**
	 * 
	 * @param clustersNumber
	 */
	public KMeansDataClusterer(int clustersNumber,double coordMinValue,double coordMaxValue) {
		this(clustersNumber,null,coordMinValue,coordMaxValue);
	}
	
	/**
	 * 
	 * @param clustersNumber
	 * @param clustersCenters
	 */
	public KMeansDataClusterer(int clustersNumber,double[][] clustersCenters,double coordMinValue,double coordMaxValue){
		this.clustersNumber = clustersNumber;
		this.clusterCenters = clustersCenters;
		this.coordMinValue = coordMinValue;
		this.coordMaxValue = coordMaxValue;
	}
	
	@Override
	public double[][][] cluster(double[][] data) {
		checkClusterCenters(data);
		double[][][] result = null;
		while(true){
			result = assignDotsToClusters(data);
			double[][] newCenters = recalculateClusterCenters(result);
			double maxDistance = -1;
			for(int i=0;i<newCenters.length;i++){
				double distance = distanceCounter.countDistance(newCenters[i], clusterCenters[i]);
				if(distance > maxDistance){
					maxDistance = distance;
				}
			}
			clusterCenters = newCenters;
			if(maxDistance<0.5){
				break;
			}
		}
		return null;
	}
	
	/**
	 * assigns dots to the clusters depending on the cluster centers and data dots
	 * 
	 * @param data
	 * @param clusterCenters
	 * @return
	 */
	public double[][][] assignDotsToClusters(double[][] data){
		double[][][] result = new double[clustersNumber][data.length][data[0].length];
		for(int i=0;i<data.length;i++){
			int cluster = calculateDotCluster(data[i]);
			result[cluster][i]=data[i];
		}
		//result will have a lot of empty array point slots
		//we need to remove empty points from it
		double[][][] newResult = new double[clustersNumber][data.length][data[0].length];
		int k = 0;
		for(int i=0;i<result.length;i++){
			for(int j=0;j<result[i].length;j++){
				if(result[i][j]!=null){
					newResult[i][k]=result[i][j];
					k++;
				}
			}
		}
		return newResult;
	}
	public double[][] recalculateClusterCenters(double[][][] clusters){
		double[][] newClusterCenters = new double[clusterCenters.length][clusterCenters[0].length];
		for(int i=0;i<clusters.length;i++){
			//calculate new cluster center
			double[] center = calculateClusterCenter(clusters[i]);
			newClusterCenters[i]=center;
		}
		return newClusterCenters;
	}
	
	public double[] calculateClusterCenter(double[][] clusterPoints){
		double[] clusterCenter = new double[clusterPoints[0].length];
		//init cluster center dimensions with 0 values
		for(int i=0;i<clusterCenter.length;i++){
			clusterCenter[i]=0;
		}
		for(int i=0;i<clusterPoints.length;i++){
			for(int j=0;j<clusterPoints[i].length;j++){
				clusterCenter[j]+=clusterPoints[i][j];
			}
		}
		for(int i=0;i<clusterCenter.length;i++){
			clusterCenter[i]/=clusterPoints.length;
		}
		return clusterCenter;
	}
	/**
	 * calculates to which cluster current dot corresponds
	 * 
	 * @param dot
	 * @return the number of the cluster starting from 0
	 */
	public int calculateDotCluster(double[] dot){
		double minimalDist = Double.MAX_VALUE;
		int cluster = Integer.MAX_VALUE;
		for(int i=0;i<clusterCenters.length;i++){
			double dist = distanceCounter.countDistance(dot, clusterCenters[i]);
			if(dist<minimalDist){
				minimalDist = dist;
				cluster = i;
			}
		}
		return cluster;
	}
	
	/**
	 * counts new cluster centers as the average coordinates of all dots in the cluster
	 * 
	 * @param clusteredDots
	 * @return
	 */
	public double[][] countNewClusterCenters(double[][][] clusteredDots){
		return null;
	}
	
	/**
	 * checks whether the centers of the clusters are populated. if not generates them.
	 * 
	 * @param data
	 */
	private void checkClusterCenters(double[][] data) {
		checkCoordinatesMinMaxValue(data);
		if(clusterCenters==null){
			//define clusters centers as random values
			//the number of dimensions in point coordinates
			int dimensionsNumber = data[0].length;
			clusterCenters = new double[clustersNumber][dimensionsNumber];
			for(int i=0;i<clusterCenters.length;i++){
				for(int j=0;j<clusterCenters[i].length;j++){
					clusterCenters[i][j]=generateRandomCoordinate(coordMinValue,coordMaxValue);
				}
			}
		}
	}

	private void checkCoordinatesMinMaxValue(double[][] data) {
		if(coordMinValue==coordMaxValue){
			double minValue = Double.MAX_VALUE;
			double maxValue = Double.MIN_VALUE;
			for(int i=0;i<data.length;i++){
				for(int j=0;j<data[i].length;j++){
					if(data[i][j]<minValue){
						minValue = data[i][j];
					}
					if(data[i][j]>maxValue){
						maxValue = data[i][j];
					}
				}
			}
			coordMinValue = minValue;
			coordMaxValue = maxValue;
		}
	}
	
	/**
	 * Generates random coorodinates in bounds of min and max value
	 * @param min minimal random value
	 * @param max maximaum random value
	 * @return random number between min and max
	 */
	public double generateRandomCoordinate(double min,double max) {
		double randomGap = Math.random()*(max-min);
		return min+randomGap;
	}

}
