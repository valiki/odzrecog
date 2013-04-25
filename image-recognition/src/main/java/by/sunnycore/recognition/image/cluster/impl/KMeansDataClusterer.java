package by.sunnycore.recognition.image.cluster.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import by.sunnycore.recognition.image.cluster.DataClusterer;
import by.sunnycore.recognition.image.cluster.DistanceCounter;

/**
 * Does clusterization using KMeans clustering algorithm
 * 
 * @author Valiantsin Shukaila
 *
 */
public class KMeansDataClusterer implements DataClusterer{
	private static final int THREADS_NUMBER = 16;
	
	private static final short EMPTY_DOT_MARKER = Short.MIN_VALUE;
	private int clustersNumber;
	private int maxDotsInCluster;
	private int dimensionsNumber;
	private short[][] clusterCenters;
	
	private ThreadPoolExecutor executor;
	private List<Future<?>> futureList;
	
	public short[][] getClusterCenters() {
		return clusterCenters;
	}

	private short coordMinValue;
	private short coordMaxValue;
	
	private short[] result;// cluster j i
	private short[] dataToUse;
	private int[] pointsInClusters;
	/**
	 * Object that counts distance between two dots
	 */
	private DistanceCounter distanceCounter = new EuklidDistanceCounter();

	private BlockingQueue<Runnable> workQueue;
	
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
		initData(data);
		int iterationNumber = 0;
		while(true){
			long millis = System.currentTimeMillis();
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
			System.out.println("one iteration is done in "+(System.currentTimeMillis()-millis));
		}
		short[][][] returnResult = truncateArray(dataToUse);
		System.out.println("Clustering execution time: "+(System.currentTimeMillis()-time)+"millis");
		return returnResult;
	}
	
	/**
	 * processes data initalization important for clustering and optimizations
	 * @param data
	 */
	private void initData(short[][] data) {
		pointsInClusters = new int[clustersNumber];
		checkClusterCenters(data);
		maxDotsInCluster = data[0].length;
		dimensionsNumber = data.length;
		result = new short[clustersNumber*maxDotsInCluster*dimensionsNumber];
		assignDotsToClusters(data);
		dataToUse = new short[result.length];
		copyData(result, dataToUse);
		clusterCenters = recalculateClusterCenters(dataToUse);
	}
	
	/**
	 * Initializes executor threads that will execute jobs concurrently in separate threads
	 */
	private void initExecutors(){
		futureList = new ArrayList<>();
		workQueue = new LinkedBlockingQueue<Runnable>();
		if(executor == null || executor.isShutdown()){
			executor = new ThreadPoolExecutor(THREADS_NUMBER, THREADS_NUMBER, 500, TimeUnit.SECONDS, workQueue);
		}
	}
	
	/**
	 * truncates initiali clustering array to the array that doesn't contain 
	 * empty slots and has size that fits number of points in the cluster
	 * @param data
	 * @return
	 */
	private short[][][] truncateArray(short[] data){
		short[][][] result = new short[clustersNumber][dimensionsNumber][1];
		//later 1 will be replaced with number of points in cluster
		for(int i=0;i<clustersNumber;i++){
			for(int j=0;j<dimensionsNumber;j++){
				result[i][j] = new short[pointsInClusters[i]];
				int k=0;
				for(int n=0;n<maxDotsInCluster;n++){
					int currentIndex = i*maxDotsInCluster*dimensionsNumber+n*dimensionsNumber+j;
					if(data[currentIndex] != EMPTY_DOT_MARKER){
						result[i][j][k]=data[currentIndex];
						k++;
					}
				}
			}
		}
		return result;
	}
	
	private void copyData(short[] from,short[] to){
		System.arraycopy(from, 0, to, 0, from.length);
	}
	
	/**
	 * assigns dots to the clusters depending on the cluster centers and data dots
	 * 
	 * @param data
	 * @param clusterCenters
	 * @return
	 */
	public void assignDotsToClusters(final short[][] data){
		initExecutors();
		nullOutArray(result);
		final int threadsNumber = THREADS_NUMBER;
		int lastEnd = 0;
		for(int i=0;i<threadsNumber;i++){
			final int currentStart;
			final int currentEnd;
			if (i<threadsNumber-1) {
				currentStart = data[0].length / threadsNumber * i;
				currentEnd = data[0].length / threadsNumber * (i + 1);
			}else{
				currentStart = lastEnd;
				currentEnd = data[0].length;
			}
			lastEnd = currentEnd;
			addJobToExecutor(data, currentStart, currentEnd,i);
		}
		executeExecutors();
	}

	private void addJobToExecutor(final short[][] data, final int currentStart,
			final int currentEnd,int index) {
			Runnable r = new Runnable() {
				
				private int start = currentStart;
				private int end = currentEnd;
				
				@Override
				public void run() {
					assignDotsGapToClusters(data,start,end);
				}
			};
			addJobToExecutor(r);
	}
	
	private void assignDotsGapToClusters(final short[][] data,int start,int end) {
		for(int i=start;i<end;i++){
			short[] dot = new short[data.length];//3
			for(int j=0;j<data.length;j++){//3
				dot[j]=data[j][i];
			}
			int cluster = calculateDotCluster(dot);
			for(int j=0;j<dot.length;j++){//3
				result[cluster*maxDotsInCluster*dimensionsNumber + i*dimensionsNumber + j] = dot[j];
			}
		}
	}
	
	private void nullOutArray(short[] array){
		for(int i=0;i<array.length;i++){
			array[i]=EMPTY_DOT_MARKER;
		}
	}
	
	/**
	 * assigns dots to the clusters depending on the cluster centers and data dots
	 * 1000*3/60
	 * @param data
	 * @param clusterCenters
	 * @return
	 */
	public void assignDotsToClusters(final short[] data){
		initExecutors();
		nullOutArray(result);
		final int threadsNumber = THREADS_NUMBER;
		int lastEnd = 0;
		for(int i=0;i<threadsNumber;i++){
			final int currentStart;
			final int currentEnd;
			if (i<threadsNumber-1) {
				currentStart = maxDotsInCluster / threadsNumber * i;
				currentEnd = maxDotsInCluster / threadsNumber * (i + 1);
			}else{
				currentStart = lastEnd;
				currentEnd = maxDotsInCluster;
			}
			lastEnd = currentEnd;
			addJobToExecutor(data, currentStart, currentEnd,i);
		}
		executeExecutors();
	}
	
	private void executeExecutors() {
		for(Future<?> future:futureList){
			try {
				future.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void addJobToExecutor(final short[] data,
			final int currentStart, final int currentEnd,int index) {
		Runnable r = new Runnable() {
			
			private int start = currentStart;
			private int end = currentEnd;
			
			@Override
			public void run() {
				assignDotsGapToClusters(data,start,end);
			}

		};
		addJobToExecutor(r);
	}
	
	private void assignDotsGapToClusters(final short[] data,int start,int end) {
		for(int i=0;i<clustersNumber;i++){
			int startingIndex = i*maxDotsInCluster*dimensionsNumber;
			for(int k=start;k<end;k++){
				int dimensionsFix = k*dimensionsNumber;
				int currentIndex = startingIndex+dimensionsFix;
				if(data[currentIndex]==EMPTY_DOT_MARKER){
					continue;
				}
				short[] dot = new short[dimensionsNumber];
				for(int j=0;j<dimensionsNumber;j++){
					dot[j]=data[currentIndex+j];
				}
				int cluster = calculateDotCluster(dot);
				int newIndex = cluster*maxDotsInCluster*dimensionsNumber+dimensionsFix;
				for(int j=0;j<dimensionsNumber;j++){
					result[newIndex+j]=data[currentIndex+j];
				}
			}
		}
	}
	
	public short[][] recalculateClusterCenters(final short[] clusters){
		//init concurrent executors
		initExecutors();
		final short[][] newClusterCenters = new short[clusterCenters.length][clusterCenters[0].length];
		for(int i=0;i<clustersNumber;i++){
			final int clusterIndex = i;
			Runnable r = new Runnable() {
				
				@Override
				public void run() {
					//calculate new cluster center
					short[] center = calculateClusterCenter(clusters,clusterIndex);
					newClusterCenters[clusterIndex]=center;
				}
			};
			addJobToExecutor(r);
		}
		//execute calculations concurrently
		executeExecutors();
		return newClusterCenters;
	}

	private void addJobToExecutor(Runnable r) {
		Future<?> future = executor.submit(r);
		futureList.add(future);
	}
	
	public short[] calculateClusterCenter(short[] clusters,int clusterIndex){
		int[] clusterCenter = new int[dimensionsNumber];
		//init cluster center dimensions with 0 values
		for(int i=0;i<clusterCenter.length;i++){
			clusterCenter[i]=0;
		}
		int pointsNumber = 0;
		int start = clusterIndex*maxDotsInCluster*dimensionsNumber;
		int end = start+maxDotsInCluster*dimensionsNumber;
		for(int i=start;i<end;i=i+dimensionsNumber){
			//if current dot was not initialized in array
			if(clusters[i]==EMPTY_DOT_MARKER){
				continue;
			}
			pointsNumber++;
			for(int j=0;j<dimensionsNumber;j++){
				clusterCenter[j]+=clusters[i+j];
			}
		}
		short[] center = new short[clusterCenter.length];
		if (pointsNumber>0) {
			for (int i = 0; i < clusterCenter.length; i++) {
				int value = clusterCenter[i] / pointsNumber;
				center[i] = (short)value;
			}
		}
		pointsInClusters[clusterIndex]=pointsNumber;
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
		for(int i=0;i<clustersNumber;i++){
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

	public void destroy(){
		executor.shutdownNow();
	}
	
}
