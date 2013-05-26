package by.sunnycore.recognition.image.util;

import by.sunnycore.recognition.domain.ObjectCluster;

public class DataUtil {

	public static double[][] intToDouble(int[][] data){
		double[][] doublePoints = new double[data.length][data[0].length];
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[i].length;j++){
				doublePoints[i][j]=data[i][j];
			}
		}
		return doublePoints;
	}
	
	public static short[][] doubleToShort(double[][] data){
		short[][] doublePoints = new short[data.length][data[0].length];
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[i].length;j++){
				doublePoints[i][j]=(short) data[i][j];
			}
		}
		return doublePoints;
	}
	
	public static int[][] doubleToInt(double[][] data){
		int[][] doublePoints = new int[data.length][data[0].length];
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[i].length;j++){
				doublePoints[i][j]=(int) data[i][j];
			}
		}
		return doublePoints;
	}
	
	public static double[][] intToDoubleTransponded(int[][] data){
		double[][] doublePoints = new double[data[0].length][data.length];
		for(int i=0;i<data[0].length;i++){
			for(int j=0;j<data.length;j++){
				doublePoints[i][j]=data[j][i];
			}
		}
		return doublePoints;
	}
	
	public static short[][] intToShort(int[][] pointsRGB) {
		short[][] data = new short[pointsRGB.length][pointsRGB[0].length];
		for(int i=0;i<pointsRGB.length;i++){
			for(int j=0;j<pointsRGB[0].length;j++){
				data[i][j] = (short) pointsRGB[i][j];
			}
		}
		return data;
	}
	
	public static int[][] shortToInt(short[][] data){
		int[][] data1 = new int[data.length][data[0].length];
		for(int i=0;i<data.length;i++){
			data1[i] = shortToInt(data[i]);
		}
		return data1;
	}
	
	public static int[] shortToInt(short[] data){
		int[] data1 = new int[data.length];
		for(int i=0;i<data.length;i++){
			data1[i] = (int) data[i];
		}
		return data1;
	}
	
	public static double[] shortToDouble(short[] data){
		double[] data1 = new double[data.length];
		for(int i=0;i<data.length;i++){
			data1[i] = (double) data[i];
		}
		return data1;
	}
	
	public static ObjectCluster[] shortToObjectClusters(short[][][] data,short[][] clusterCenters){
		ObjectCluster[] result = new ObjectCluster[data.length];
		for(int i=0;i<data.length;i++){
			ObjectCluster objCluster = new ObjectCluster();
			result[i]=objCluster;
			int[][] clusterPoints = DataUtil.shortToInt(data[i]);
			objCluster.setClusterPoints(clusterPoints);
/*				Instance center = centroids[i];*/
			int[] centerInt = DataUtil.shortToInt(clusterCenters[i]);
			objCluster.setClusterCenter(centerInt);
		}
		return result;
	}

}
