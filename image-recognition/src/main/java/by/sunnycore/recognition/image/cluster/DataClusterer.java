package by.sunnycore.recognition.image.cluster;

/**
 * Clusters data into clusters using implementation algorithm
 * 
 * @author Val
 * 
 */
public interface DataClusterer {

	/**
	 * clusters data array using implementation algorithm
	 * 
	 * @param multidimensional
	 *            data array where where 1st index is dimensions indexes, 2nd
	 *            index is point number
	 * @return returns multidimensional array where 1st index is cluster index
	 *         2nd index is dimensions index and third index is point number
	 *         index
	 */
	public double[][][] cluster(double[][] data);
}
