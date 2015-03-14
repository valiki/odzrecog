package by.sunnycore.recognition.image.cluster.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.cluster.DataClusterer;
import by.sunnycore.recognition.image.cluster.DistanceCounter;
import by.sunnycore.recognition.image.cluster.TeachableMethod;

/**
 * Clusters data using Rectagles Method
 * 
 * @author Valiantsin Shukaila
 * 
 */
public class RectangleMethodClusterer implements DataClusterer, TeachableMethod {
	/**
	 * The values of dispersion for each cluster. they also define clusters size and 2*dispersion
	 */
	private RealMatrix[] dispersions;

	/**
	 * math expectations for each clusters. they also define clusters centers
	 */
	private RealMatrix[] mathExpectations;
	
	private DistanceCounter counter = new EuklidDistanceCounter();

	private DistanceCounter[] counters;
	
	public RealMatrix[] getDispersions() {
		return dispersions;
	}

	public RealMatrix[] getMathExpectations() {
		return mathExpectations;
	}

	private int clustersNumber;
	
	@Override
	public short[][][] cluster(short[][] data) {
		short[][][] result = new short[clustersNumber][data.length][data[0].length];
		for(int i=0;i<data[0].length;i++){
			double[] point = new double[data.length];
			for(int j=0;j<data.length;j++){
				point[j]=data[j][i];
			}
			int cluster = clusterOnePixel(point);
			if (cluster!=-1) {
				for (int j = 0; j < point.length; j++) {
					result[cluster][j][i] = (short) point[j];
				}
			}
		}
		return result;
	}

	public int clusterOnePixel(double[] pixel){
		int cluster = -1;
		for(int i=0;i<clustersNumber;i++){
			double[][] mathExpectationArray = ((Array2DRowRealMatrix)mathExpectations[i]).getData();
			double[][] dispersionArray = ((Array2DRowRealMatrix)dispersions[i]).getData();
			double[] center = new double[mathExpectationArray.length];
			for(int j=0;j<mathExpectationArray.length;j++){
				center[j]=mathExpectationArray[j][0];
			}
			boolean liesInside = true;
			double[] lastCenter = new double[mathExpectationArray.length];
			double lastDistance = 0;
			for(int j=0;j<pixel.length;j++){
				double mathExpBin = mathExpectationArray[j][0];
				double dispersionBin = dispersionArray[j][0];
				//make sure that pixel lies in the square of size 2*dispersion.
				//according to normal distribution 97% dots should be inside this gap
				if(pixel[j] > (mathExpBin+2*dispersionBin) ||
				   pixel[j] < (mathExpBin-2*dispersionBin)){
					liesInside = false;
				}
			}
			if(liesInside==true){
				//mean the dot is inside the square of necessary size
				if (cluster==-1) {
					cluster = i;
					for(int j=0;j<mathExpectationArray.length;j++){
						lastCenter[j]=center[j];
					}
					lastDistance = counters[i].countDistance(center, pixel);
					//System.out.println("euklid distance versus mahalanobis: "+lastDistance+", "+length);
				}else{
					//if this is scond cluster where the dots lies in
					//mean it lies inside two clusters and we can not classifiy it
					//cluster = -1;
					//break;
					double distance = counters[i].countDistance(center, pixel);
					if(distance<lastDistance){
						cluster = i;
					}
					for(int j=0;j<mathExpectationArray.length;j++){
						lastCenter[j]=center[j];
					}
					lastDistance = distance;
				}
			}
		}
		return cluster;
	}
	
	@Override
	public void teach(List<ObjectCluster[]> teachData) {
		counters = new DistanceCounter[teachData.get(0).length];
		for(int i=0;i<counters.length;i++){
			List<ObjectCluster> clusterData = new ArrayList<>();
			for(int j=0;j<teachData.size();j++){
				clusterData.add(teachData.get(j)[i]);
			}
			counters[i]=new EuklidDistanceCounter();//new MahalanobisDistanceCounter(clusterData);
		}
		clustersNumber = teachData.get(0).length;
		init();
		mathExpectations = MathUtil.buildMathExpectations(teachData);
		dispersions = MathUtil.buildDispersions(teachData, mathExpectations);
	}
	
	private void init(){
		mathExpectations = new RealMatrix[clustersNumber];
		dispersions = new RealMatrix[clustersNumber];
	}

}
