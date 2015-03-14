package by.sunnycore.recognition.image.cluster.impl;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.cluster.DistanceCounter;
import by.sunnycore.recognition.image.cluster.TeachableMethod;
import by.sunnycore.recognition.image.util.DataUtil;

public class MahalanobisDistanceCounter implements DistanceCounter{

	//обратная матрица ковариации
	private RealMatrix unCovMatrix;
	
	public MahalanobisDistanceCounter(List<ObjectCluster> teachClusterData) {
		buildCorrelationMatrix(teachClusterData);
	}
	
	@Override
	public double countDistance(double[] dot1, double[] dot2) {
		Array2DRowRealMatrix dot1Matrix = new Array2DRowRealMatrix(dot1);
		Array2DRowRealMatrix dot2Matrix = new Array2DRowRealMatrix(dot2);
		Array2DRowRealMatrix subtract = dot1Matrix.subtract(dot2Matrix);
		RealMatrix multiply = subtract.transpose().multiply(unCovMatrix).multiply(subtract);
		double[][] data = multiply.getData();
		return Math.sqrt(data[0][0]);
	}
	
	/**
	 * build array of the covariance matrixes Rijk where k is the number of the cluster.
	 * Different Matrix for each cluster
	 * 
	 * @param teachData
	 */
	private void buildCorrelationMatrix(List<ObjectCluster> teachData) {
		RealMatrix covMatrix = null;
		//the teach step is to create vector of math expectations
		for(ObjectCluster cluster:teachData){
			int[][] clusterPoints = cluster.getClusterPoints();
			double[][] doublePoints = DataUtil.intToDoubleTransponded(clusterPoints);
			Covariance cov = new Covariance(doublePoints);
			//Covariance cov = new Covariance(doublePoints);
			RealMatrix covvariationMatrix = cov.getCovarianceMatrix();
			if(covMatrix==null){
				covMatrix=covvariationMatrix;
			}else{
				//store the sum of the matrixes of the teach sets
				//at the we will divide matrix by the teach sets number 
				covMatrix = covMatrix.add(covvariationMatrix);
			}
		}
		MathUtil.divideMatrixByNumber(covMatrix, teachData.size());
		unCovMatrix = new LUDecomposition(covMatrix,1e-30).getSolver().getInverse();
	}

	@Override
	public double countDistance(short[] dot1, short[] dot2) {
		Array2DRowRealMatrix dot1Matrix = new Array2DRowRealMatrix(DataUtil.shortToDouble(dot1));
		Array2DRowRealMatrix dot2Matrix = new Array2DRowRealMatrix(DataUtil.shortToDouble(dot2));
		Array2DRowRealMatrix subtract = dot1Matrix.subtract(dot2Matrix);
		RealMatrix multiply = subtract.transpose().multiply(unCovMatrix).multiply(subtract);
		double[][] data = multiply.getData();
		return Math.sqrt(data[0][0]);
	}

}
