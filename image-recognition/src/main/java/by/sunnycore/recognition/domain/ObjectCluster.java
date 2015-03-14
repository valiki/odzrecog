package by.sunnycore.recognition.domain;

import java.io.Serializable;

/**
 * defines the cluster of the objects using which we can process object
 * recognition
 * 
 * @author Valentine Shukaila
 * 
 */
public class ObjectCluster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3265263694428303410L;

	/**
	 * the colors that lie in current cluster
	 */
	private int[][] clusterPoints;

	private int[] clusterCenter;
	
	private int clusterColor;

	public int getClusterColor() {
		return clusterColor;
	}

	public void setClusterColor(int clusterColor) {
		this.clusterColor = clusterColor;
	}

	public int[][] getClusterPoints() {
		return clusterPoints;
	}

	public void setClusterPoints(int[][] clusterPoints) {
		this.clusterPoints = clusterPoints;
	}

	public int[] getClusterCenter() {
		return clusterCenter;
	}

	public void setClusterCenter(int[] clusterCenter) {
		this.clusterCenter = clusterCenter;
	}

}
