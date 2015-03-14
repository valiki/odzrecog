package by.sunnycore.recognition.image.cluster.impl;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.util.DataUtil;

public class MathUtil {

	/**
	 * Builds math expectations
	 * 
	 * @param teachData
	 */
	public static RealMatrix[] buildMathExpectations(List<ObjectCluster[]> teachData) {
		RealMatrix[] mathExpectations = new RealMatrix[teachData.get(0).length];
		for(int i=0;i<teachData.size();i++){
			ObjectCluster[] teachSet = teachData.get(i);
			for(int j=0;j<teachSet.length;j++){
				ObjectCluster cluster = teachSet[j];
				RealMatrix mathExpecationsForCluster = MathUtil.countMaxExpectationsForCluster(cluster);
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
			MathUtil.divideMatrixByNumber(r, teachData.size());
		}
		return mathExpectations;
	}
	
	/**
	 * counts math expectations for the cluster as the average value of all
	 * pixels in the cluster
	 * 
	 * @param cluster
	 * @return
	 */
	public static RealMatrix countMaxExpectationsForCluster(ObjectCluster cluster) {
		int[][] clusterPoints = cluster.getClusterPoints();
		double[] maxExpectationsForCluster = new double[clusterPoints.length];
		for (int k = 0; k < clusterPoints.length; k++) {
			for (int l = 0; l < clusterPoints[k].length; l++) {
				// first sum all values on the channell
				maxExpectationsForCluster[k] = maxExpectationsForCluster[k]
						+ clusterPoints[k][l];
			}
			// divide by the pixels number to get
			maxExpectationsForCluster[k] = maxExpectationsForCluster[k] / clusterPoints[k].length;
		}
		RealMatrix maxExpecationsForCluster = new Array2DRowRealMatrix(
				maxExpectationsForCluster);
		return maxExpecationsForCluster;
	}

	/**
	 * Divides matrix values by the number
	 * @param matrixes 
	 * @param divisionNumber
	 */
	public static RealMatrix divideMatrixByNumber(RealMatrix matrix,double divisionNumber) {
		//now normalize the matrix values
		//divide them by the number of teach sets because we were summing matrixes in teach sets
		for(int i=0;i<matrix.getRowDimension();i++){
			for(int j=0;j<matrix.getColumnDimension();j++){
				matrix.multiplyEntry(i, j, 1/divisionNumber);
			}
		}
		return matrix;
	}
	
	/**
	 * Divides matrix values by the number
	 * @param matrixes 
	 * @param divisionNumber
	 */
	public static RealMatrix powerMatrixByNumber(RealMatrix matrix,double powerNumber) {
		//now normalize the matrix values
		//divide them by the number of teach sets because we were summing matrixes in teach sets
		for(int i=0;i<matrix.getRowDimension();i++){
			for(int j=0;j<matrix.getColumnDimension();j++){
				double entry = matrix.getEntry(i, j);
				entry = Math.pow(entry, powerNumber);
				matrix.setEntry(i, j, entry);
			}
		}
		return matrix;
	}
	
	/**
	 * Divides matrix values by the number
	 * @param matrixes 
	 * @param divisionNumber
	 */
	public static RealMatrix absMatrix(RealMatrix matrix) {
		//now normalize the matrix values
		//divide them by the number of teach sets because we were summing matrixes in teach sets
		for(int i=0;i<matrix.getRowDimension();i++){
			for(int j=0;j<matrix.getColumnDimension();j++){
				double entry = matrix.getEntry(i, j);
				entry = Math.abs(entry);
				matrix.setEntry(i, j, entry);
			}
		}
		return matrix;
	}
	
	public static RealMatrix[] buildDispersions(List<ObjectCluster[]> data,RealMatrix[] mathExpectations){
		RealMatrix[] dispersions = new RealMatrix[mathExpectations.length];
		for(int l=0;l<data.size();l++){
			ObjectCluster[] clusters = data.get(l);
			for(int i=0;i<clusters.length;i++){
				ObjectCluster cluster = clusters[i];
				int[][] points = cluster.getClusterPoints();
				double[][] dPoints = DataUtil.intToDouble(points);
				double[] dispersion = new double[dPoints.length];
				for(int j=0;j<dispersion.length;j++){
					dispersion[j]=0;
				}
				RealMatrix exp = mathExpectations[i];
				double[][] center = ((Array2DRowRealMatrix)exp).getData();
				
				for(int j=0;j<dPoints[0].length;j++){
					for(int k=0;k<dPoints.length;k++){
						dispersion[k]+=Math.pow(dPoints[k][j]-center[k][0],2);
					}
				}
				for(int j=0;j<dispersion.length;j++){
					dispersion[j]=Math.sqrt(dispersion[j]/dPoints[0].length);
				}
				if(dispersions[i]==null){
					dispersions[i] = new Array2DRowRealMatrix(dispersion);
				}else{
					dispersions[i] = dispersions[i].add(new Array2DRowRealMatrix(dispersion));
				}
			}
		}
		for(int l=0;l<dispersions.length;l++){
			dispersions[l] = divideMatrixByNumber(dispersions[l], data.size());
		}
		return dispersions;
	}
	
	/**
	 * Divides matrix values by the number
	 * @param matrixes 
	 * @param divisionNumber
	 */
	public static RealMatrix sqrtMatrix(RealMatrix matrix) {
		//now normalize the matrix values
		//divide them by the number of teach sets because we were summing matrixes in teach sets
		for(int i=0;i<matrix.getRowDimension();i++){
			for(int j=0;j<matrix.getColumnDimension();j++){
				double entry = matrix.getEntry(i, j);
				matrix.setEntry(i, j, Math.sqrt(entry));
			}
		}
		return matrix;
	}
	
}
