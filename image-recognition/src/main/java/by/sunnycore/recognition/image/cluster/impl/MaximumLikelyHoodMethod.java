package by.sunnycore.recognition.image.cluster.impl;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.cluster.DataClusterer;
import by.sunnycore.recognition.image.cluster.TeachableMethod;
import by.sunnycore.recognition.image.util.DataUtil;

/**
 * The clusterization is done using maximum likelyhood method
 * The main idea is to build square forms of type (Fij-Mk)^T*Rk^-1*(Fij-Mk) 
 * where Fij is the pixel, Mk is math expectation of the k cluster,^T is transposing matrix, Rk is the Covariance matrix of k cluster,
 *  ^-1 is matrix inversion.
 *  And compare these forms determinant with each other, the smallest will show to which cluster our pixels is most close
 */
public class MaximumLikelyHoodMethod implements DataClusterer,TeachableMethod{

	//covariance matrixes for each of the class
	private RealMatrix[] correlationMatrixes;
	private RealMatrix[] inverseCorrelationMaxtrixes;
	
	//vector of math expectation
	//first dimension is the number of bins
	private RealMatrix[] mathExpectations;

	private int clustersNumber;
	
	/**
	 * The clusterization is done using maximum likelyhood method
	 * The main idea is to build square forms of type (Fij-Mk)^T*Rk^-1*(Fij-Mk) 
	 * where Fij is the pixel, Mk is math expectation of the k cluster,^T is transposing matrix, Rk is the Covariance matrix of k cluster,
	 *  ^-1 is matrix inversion.
	 *  And compare these forms determinant with each other, the smallest will show to which cluster our pixels is most close
	 */
	@Override
	public short[][][] cluster(short[][] data) {
		//iterate over all pixels to cluster each of them
		short[][][] result = new short[clustersNumber][data.length][data[0].length];
		for(int i=0;i<data[0].length;i++){
			//verctor with pixel coordinates fij this is how it is called in the formula
			short[] fij = new short[data.length];
			for(int j=0;j<data.length;j++){
				fij[j]=data[j][i];
			}
			int cluster = clusterOnePixel(fij);
			for(int j=0;j<fij.length;j++){
				result[cluster][j][i]=(short) fij[j];
			}
		}
		return result;
	}
	
	public int clusterOnePixel(short[] pixel){
		double[] pixelD = new double[pixel.length];
		for(int i=0;i<pixelD.length;i++){
			pixelD[i]=pixel[i];
		}
		double smallestDeterminant = Double.MAX_VALUE;
		int cluster = -1;
		for(int i=0;i<clustersNumber;i++){
			RealMatrix squareForm = countSquareForm(pixelD, i);
			double determinant = new LUDecomposition(squareForm).getDeterminant();
			if(determinant < smallestDeterminant){
				smallestDeterminant = determinant;
				cluster = i;
			}
		}
		return cluster;
	}

	private RealMatrix countSquareForm(double[] pixelD, int i) {
		RealMatrix fij = new Array2DRowRealMatrix(pixelD);
		RealMatrix fijMinusMk = fij.subtract(mathExpectations[i]);
		RealMatrix squareForm = fijMinusMk.transpose().multiply(inverseCorrelationMaxtrixes[i]).multiply(fijMinusMk);
		return squareForm;
	}
	
	@Override
	public void teach(List<ObjectCluster[]> teachData){
		preInit(teachData);
		buildCorrelationMatrix(teachData);
		mathExpectations = MathUtil.buildMathExpectations(teachData);
	}

	/**
	 * initializes arrays with necessary dimensions
	 * @param teachData
	 */
	private void preInit(List<ObjectCluster[]> teachData) {
		if(teachData.size()>0){
			ObjectCluster[] clusters = teachData.get(0);
			clustersNumber = clusters.length;
			correlationMatrixes = new RealMatrix[clustersNumber];
			inverseCorrelationMaxtrixes = new RealMatrix[clustersNumber];
			mathExpectations = new RealMatrix[clustersNumber];
		}
	}

	/**
	 * build array of the covariance matrixes Rijk where k is the number of the cluster.
	 * Different Matrix for each cluster
	 * 
	 * @param teachData
	 */
	private void buildCorrelationMatrix(List<ObjectCluster[]> teachData) {
		//the teach step is to create vector of math expectations
		for(ObjectCluster[] teachSet:teachData){
			int clusterNumber = 0;
			for(ObjectCluster cluster:teachSet){
				int[][] clusterPoints = cluster.getClusterPoints();
				double[][] doublePoints = DataUtil.intToDoubleTransponded(clusterPoints);
				PearsonsCorrelation correlation = new PearsonsCorrelation(doublePoints);
				//Covariance cov = new Covariance(doublePoints);
				RealMatrix correlationMatrix = correlation.getCorrelationMatrix();//cov.getCovarianceMatrix();
				if(correlationMatrixes[clusterNumber]==null){
					correlationMatrixes[clusterNumber]=correlationMatrix;
				}else{
					//store the sum of the matrixes of the teach sets
					//at the we will divide matrix by the teach sets number 
					correlationMatrixes[clusterNumber] = correlationMatrixes[clusterNumber].add(correlationMatrix);
				}
				clusterNumber++;
			}
		}
		for(int i=0;i<correlationMatrixes.length;i++){
			MathUtil.divideMatrixByNumber(correlationMatrixes[i], teachData.size());
			inverseCorrelationMaxtrixes[i] = new LUDecomposition(correlationMatrixes[i],1e-30).getSolver().getInverse();
		}
	}
	
	public RealMatrix[] getMathExpectations() {
		return mathExpectations;
	}
	
}
