package by.sunnycore.recognition.image.cluster;

import by.sunnycore.recognition.image.cluster.impl.EuklidDistanceCounter;

/**
 * Does clusterization using KMeans clustering algorithm
 * 
 * @author Valiantsin Shukaila
 *
 */
public class KMeansDataClusterer implements DataClusterer{

	private static final short EMPTY_DOT_MARKER = Short.MIN_VALUE;
	private int clustersNumber;
	private short[][] clusterCenters;
	public short[][] getClusterCenters() {
		return clusterCenters;
	}

	private short coordMinValue;
	private short coordMaxValue;
	
	private short[][][] result;
	private short[][][] dataToUse;
	private int[] pointsInClusters;
	private int currentPointsIncluster;
	/**
	 * Object that counts distance between two dots
	 */
	private DistanceCounter distanceCounter = new EuklidDistanceCounter();
	
	/**
	 * 
	 * @param clustersNumber
	 */
	public KMeansDataClusterer(int clustersNumber) {
		this(clustersNumber,null,(short)0,(short)0);
	}
	
	/**
	 * 
	 * @param clustersNumber
	 */
	public KMeansDataClusterer(int clustersNumber,short coordMinValue,short coordMaxValue) {
		this(clustersNumber,null,coordMinValue,coordMaxValue);
	}
	
	/**
	 * 
	 * @param clustersNumber
	 * @param clustersCenters
	 */
	public KMeansDataClusterer(int clustersNumber,short[][] clustersCenters,short coordMinValue,short coordMaxValue){
		this.clustersNumber = clustersNumber;
		this.clusterCenters = clustersCenters;
		this.coordMinValue = coordMinValue;
		this.coordMaxValue = coordMaxValue;
	}
	
	@Override
	public short[][][] cluster(short[][] data) {
		long time = System.currentTimeMillis();
		pointsInClusters = new int[clustersNumber];
		checkClusterCenters(data);
		int iterationNumber = 0;
		result = new short[clustersNumber][data.length][data[0].length];
		assignDotsToClusters(data);
		dataToUse = new short[clustersNumber][data.length][data[0].length];
		copyData(result, dataToUse);
		clusterCenters = recalculateClusterCenters(dataToUse);
		while(true){
			iterationNumber++;
			assignDotsToClusters(dataToUse);
			short[][] newCenters = recalculateClusterCenters(result);
			double maxDistance = -1;
			for(int i=0;i<newCenters.length;i++){
				double distance = distanceCounter.countDistance(newCenters[i], clusterCenters[i]);
				if(distance > maxDistance){
					maxDistance = distance;
				}
			}
			clusterCenters = newCenters;
			copyData(result, dataToUse);
			System.out.println("Done iteration number "+iterationNumber+", centers changed max by "+maxDistance);
			if(maxDistance<0.5){
				break;
			}
		}
		short[][][] returnResult = truncateArray(dataToUse);
		System.out.println("Clustering execution time: "+(System.currentTimeMillis()-time)+"millis");
		return returnResult;
	}
	
	private short[][][] truncateArray(short[][][] data){
		short[][][] result = new short[clustersNumber][data[0].length][1];
		//later 1 will be replaced with number of points in cluster
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[i].length;j++){
				result[i][j] = new short[pointsInClusters[i]];
				int k=0;
				for(int n=0;n<data[i][j].length;n++){
					if(data[i][j][n] != EMPTY_DOT_MARKER){
						result[i][j][k]=data[i][j][n];
						k++;
					}
				}
			}
		}
		return result;
	}
	
	private void copyData(short[][][] from,short[][][] to){
		for(int i=0;i<from.length;i++){
			for(int j=0;j<from[i].length;j++){
				System.arraycopy(from[i][j], 0, to[i][j], 0, from[i][j].length);
			}
		}
	}
	
	/**
	 * assigns dots to the clusters depending on the cluster centers and data dots
	 * 
	 * @param data
	 * @param clusterCenters
	 * @return
	 */
	public void assignDotsToClusters(short[][] data){
		nullOutArray(result);
		for(int i=0;i<data[0].length;i++){
			short[] dot = new short[data.length];
			for(int j=0;j<data.length;j++){
				dot[j]=data[j][i];
			}
			int cluster = calculateDotCluster(dot);
			for(int j=0;j<dot.length;j++){
				result[cluster][j][i]=dot[j];
			}
		}
	}
	
	private void nullOutArray(short[][][] array){
		for(int i=0;i<array.length;i++){
			for(int j=0;j<array[i].length;j++){
				for(int k=0;k<array[i][j].length;k++){
					array[i][j][k]=EMPTY_DOT_MARKER;
				}
			}
		}
	}
	
	/**
	 * assigns dots to the clusters depending on the cluster centers and data dots
	 * 
	 * @param data
	 * @param clusterCenters
	 * @return
	 */
	public void assignDotsToClusters(short[][][] data){
		nullOutArray(result);
		int notEmptyDots = 0;
		for(int i=0;i<data.length;i++){
			for (int j = 0; j < data[i][0].length; j++) {
				if (data[i][0][j] == EMPTY_DOT_MARKER) {
					continue;
				}else{
					notEmptyDots++;
				}
				short[] dot = new short[data[i].length];
				for (int k = 0; k < data[i].length; k++) {
					dot[k] = data[i][k][j];
				}
				int cluster = calculateDotCluster(dot);
				for (int k = 0; k < dot.length; k++) {
					result[cluster][k][j] = dot[k];
				}
			}
		}
		System.out.println("number of not empty dots "+notEmptyDots);
	}
	
	public short[][] recalculateClusterCenters(short[][][] clusters){
		short[][] newClusterCenters = new short[clusterCenters.length][clusterCenters[0].length];
		for(int i=0;i<clusters.length;i++){
			//calculate new cluster center
			short[] center = calculateClusterCenter(clusters[i]);
			pointsInClusters[i]=currentPointsIncluster;
			newClusterCenters[i]=center;
		}
		return newClusterCenters;
	}
	
	public short[] calculateClusterCenter(short[][] clusterPoints){
		int[] clusterCenter = new int[clusterPoints.length];
		//init cluster center dimensions with 0 values
		for(int i=0;i<clusterCenter.length;i++){
			clusterCenter[i]=0;
		}
		int pointsNumber = 0;
		for(int i=0;i<clusterPoints[0].length;i++){
			//if current dot was not initialized in array
			if(clusterPoints[0][i]==EMPTY_DOT_MARKER){
				continue;
			}
			pointsNumber++;
			for(int j=0;j<clusterPoints.length;j++){
				clusterCenter[j]+=clusterPoints[j][i];
			}
		}
		short[] center = new short[clusterCenter.length];
		if (pointsNumber>0) {
			for (int i = 0; i < clusterCenter.length; i++) {
				int value = clusterCenter[i] / pointsNumber;
				center[i] = (short)value;
			}
		}
		currentPointsIncluster = pointsNumber;
		return center;
	}
	/**
	 * calculates to which cluster current dot corresponds
	 * 
	 * @param dot
	 * @return the number of the cluster starting from 0
	 */
	public int calculateDotCluster(short[] dot){
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
	 * checks whether the centers of the clusters are populated. if not generates them.
	 * 
	 * @param data
	 */
	private void checkClusterCenters(short[][] data) {
		checkCoordinatesMinMaxValue(data);
		if(clusterCenters==null){
			//define clusters centers as random values
			//the number of dimensions in point coordinates
			int dimensionsNumber = data.length;
			clusterCenters = new short[clustersNumber][dimensionsNumber];
			for(int i=0;i<clusterCenters.length;i++){
				for(int j=0;j<clusterCenters[i].length;j++){
					clusterCenters[i][j]=generateRandomCoordinate(coordMinValue,coordMaxValue);
				}
			}
		}
	}

	private void checkCoordinatesMinMaxValue(short[][] data) {
		if(coordMinValue==coordMaxValue){
			short minValue = Short.MAX_VALUE;
			short maxValue = EMPTY_DOT_MARKER;
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
	public short generateRandomCoordinate(short min,short max) {
		short randomGap = (short) (Math.random()*(max-min));
		return (short) (min+randomGap);
	}

}
