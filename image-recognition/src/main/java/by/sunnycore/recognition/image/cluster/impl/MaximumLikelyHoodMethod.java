package by.sunnycore.recognition.image.cluster.impl;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.cluster.DataClusterer;

public class MaximumLikelyHoodMethod implements DataClusterer{

	//covariance matrixes for each of the class
	private RealMatrix[] covarianceMatrixes;
	
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
		RealMatrix rInverse = new LUDecomposition(covarianceMatrixes[i]).getSolver().getInverse();
		RealMatrix squareForm = fijMinusMk.transpose().multiply(rInverse).multiply(fijMinusMk);
		return squareForm;
	}
	
	public void teach(List<ObjectCluster[]> teachData){
		preInit(teachData);
		buildCovarianceMatrix(teachData);
		buildMathExpectations(teachData);
	}

	/**
	 * Builds math expectations
	 * 
	 * @param teachData
	 */
	private void buildMathExpectations(List<ObjectCluster[]> teachData) {
		for(int i=0;i<teachData.size();i++){
			ObjectCluster[] teachSet = teachData.get(i);
			for(int j=0;j<teachSet.length;j++){
				ObjectCluster cluster = teachSet[j];
				RealMatrix mathExpecationsForCluster = countMaxExpectationsForCluster(cluster);
				//sum the mathExpecation vectors for clusters from all teach sets
				//and later to divide by the number of teach sets
				if (mathExpectations[j]==null) {
					mathExpectations[j] = mathExpecationsForCluster;
				}else{
					mathExpectations[j] = mathExpectations[j].add(mathExpecationsForCluster);
				}
			}
		}
		//normalize vector values by dividing it by the number of teach sets
		for(RealMatrix r:mathExpectations){
			divideMatrixByNumber(r, teachData.size());
		}
	}

	/**
	 * counts math expectations for the cluster as the average value of all pixels in the cluster
	 * 
	 * @param cluster
	 * @return
	 */
	private RealMatrix countMaxExpectationsForCluster(ObjectCluster cluster) {
		int[][] clusterPoints = cluster.getClusterPoints();
		double[] maxExpectationsForCluster = new double[clusterPoints.length];
		for(int k=0;k<clusterPoints.length;k++){
			for(int l=0;l<clusterPoints[k].length;l++){
				//first sum all values on the channell
				maxExpectationsForCluster[k]=maxExpectationsForCluster[k]+clusterPoints[k][l];
			}
			//divide by the pixels number to get 
			maxExpectationsForCluster[k]=maxExpectationsForCluster[k]/clusterPoints[k].length;
		}
		RealMatrix maxExpecationsForCluster = new Array2DRowRealMatrix(maxExpectationsForCluster);
		return maxExpecationsForCluster;
	}

	/**
	 * initializes arrays with necessary dimensions
	 * @param teachData
	 */
	private void preInit(List<ObjectCluster[]> teachData) {
		if(teachData.size()>0){
			ObjectCluster[] clusters = teachData.get(0);
			clustersNumber = clusters.length;
			covarianceMatrixes = new RealMatrix[clustersNumber];
			mathExpectations = new RealMatrix[clustersNumber];
		}
	}

	/**
	 * build array of the covariance matrixes Rijk where k is the number of the cluster.
	 * Different Matrix for each cluster
	 * 
	 * @param teachData
	 */
	private void buildCovarianceMatrix(List<ObjectCluster[]> teachData) {
		//the teach step is to create vector of math expectations
		for(ObjectCluster[] teachSet:teachData){
			int clusterNumber = 0;
			for(ObjectCluster cluster:teachSet){
				int[][] clusterPoints = cluster.getClusterPoints();
				double[][] doubleClusterPoints = new double[clusterPoints.length][clusterPoints[0].length];
				double[][] transpondedPoints = new double[clusterPoints[0].length][clusterPoints.length];
				for(int i=0;i<doubleClusterPoints.length;i++){
					for(int j=0;j<doubleClusterPoints[i].length;j++){
						transpondedPoints[j][i]=doubleClusterPoints[i][j];
					}
				}
				Covariance cov = new Covariance(transpondedPoints);
				RealMatrix covarianceMatrix = cov.getCovarianceMatrix();
				if(covarianceMatrixes[clusterNumber]==null){
					covarianceMatrixes[clusterNumber]=covarianceMatrix;
				}else{
					//store the sum of the matrixes of the teach sets
					//at the we will divide matrix by the teach sets number 
					covarianceMatrixes[clusterNumber].add(covarianceMatrix);
				}
				clusterNumber++;
			}
		}
		for(RealMatrix r:covarianceMatrixes){
			divideMatrixByNumber(r, teachData.size());
		}
	}

	/**
	 * Divides matrix values by the number
	 * @param matrixes 
	 * @param divisionNumber
	 */
	private void divideMatrixByNumber(RealMatrix matrix,double divisionNumber) {
		//now normalize the matrix values
		//divide them by the number of teach sets because we were summing matrixes in teach sets
		for(int i=0;i<matrix.getRowDimension();i++){
			for(int j=0;j<matrix.getColumnDimension();j++){
				matrix.multiplyEntry(i, j, 1/divisionNumber);
			}
		}
	}
	
}
