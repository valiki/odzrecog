package by.sunnycore.recognition.image.cluster.impl;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.cluster.ImageClusterer;
import by.sunnycore.recognition.image.util.ImageUtil;
import by.sunnycore.recognition.image.util.JavaMlUtil;

/**
 * 
 * the KMeans custerer with extended mechanism of defining number of
 * clusters.</br> The defining number of clusters is based on the minimizing the
 * validity measure which is intraLenght/interLength
 * 
 * @author val
 * 
 */
public class ExtendedKMeansClusterer implements ImageClusterer {

	private static final int MIN_CLUSTERS_NUMBER = 3;
	private static final int MAX_CLUSTERS_NUMBER = 25;

	private Logger logger = Logger.getLogger(ExtendedKMeansClusterer.class);
	
	@Override
	public ObjectCluster[] cluster(BufferedImage source) {
		PixelGrabber pixelGrabber = new PixelGrabber(source, 0, 0, source.getWidth(), source.getHeight(), true);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		final ColorModel colorModel = pixelGrabber.getColorModel();
		int[][] pointsRGB = new int[3][];//create array for the RGB points for clusterization
		int numberOfPixels = pixels.length;
		pointsRGB[0] = new int[numberOfPixels];
		pointsRGB[1] = new int[numberOfPixels];
		pointsRGB[2] = new int[numberOfPixels];
		for(int i=0;i<numberOfPixels;i++){
			pointsRGB[0][i]=ImageUtil.getRedRaw(pixels[i], colorModel);
			pointsRGB[1][i]=ImageUtil.getGreenRaw(pixels[i], colorModel);
			pointsRGB[2][i]=ImageUtil.getBlue(pixels[i], colorModel);
		}
		ObjectCluster[] theBestClusters = null;
		double minValidity = Double.MAX_VALUE;
		logger.info("the number of points are: "+numberOfPixels);
		for(int i=MAX_CLUSTERS_NUMBER;i>MIN_CLUSTERS_NUMBER;i--){
			//System.out.println("starting k means clustering iteration with "+i+" number of clusters");
			logger.info("starting k means clustering iteration with "+i+" number of clusters");
			ObjectCluster[] clusters = doKMeansIteration(pointsRGB, i);
			logger.info("done k means clustering with "+i+" number of clusters");
			//System.out.println("done k means clustering with "+i+" number of clusters");
			double validity = countValidity(clusters, numberOfPixels);
			//System.out.println("the validity is"+validity);
			logger.info("the validity is "+validity);
			if(validity < minValidity){
				minValidity = validity;
				theBestClusters = clusters;
			}
		}
		return theBestClusters;
	}

	/**
	 * does one itreation of the clusterization 
	 * that uses the KMeans clusterization and clusterNumber number of clusters
	 * 
	 * @param pointsRGB
	 * @param clusterNumber
	 */
	private ObjectCluster[] doKMeansIteration(int[][] pointsRGB, int clusterNumber) {
		try {
			KMeans clusterer = new KMeans(clusterNumber);
			logger.info(" creating dataset from array of pixels");
			Dataset clusterDataSet = JavaMlUtil.arrayToDataSet(pointsRGB);
			logger.info("clustering using clusterer");
			Dataset[] clusters = clusterer.cluster(clusterDataSet);
			ObjectCluster[] result = new ObjectCluster[clusterNumber];
			//the coordinates of the cluster centers
			Field centrodsField = KMeans.class.getDeclaredField("centroids");
			centrodsField.setAccessible(true);
			Instance[] centroids = (Instance[]) centrodsField.get(clusterer);
			logger.info("dataset to object clusters");
			for(int i=0;i<result.length;i++){
				ObjectCluster objCluster = new ObjectCluster();
				result[i]=objCluster;
				Dataset cluster = clusters[i];
				int[][] clusterPoints = JavaMlUtil.dataSetToArray(cluster);
				objCluster.setClusterPoints(clusterPoints);
				Instance center = centroids[i];
				int[] centerInt = JavaMlUtil.instanceToArray(center);
				objCluster.setClusterCenter(centerInt);
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * counts minimal distance between cluster centers
	 * 
	 * formula is 1/N*(sum by clusters of sum by points of Norm(x-center of cluster))
	 *  
	 * @param clusters the clusters that were clusterized in prev iteration
	 * @param numOfpoints number of all pixels of the image
	 * @return
	 */
	private long countInterLength(ObjectCluster[] clusters) {
		long mininalLength = Long.MAX_VALUE;
		for(int i=0;i<clusters.length-1;i++){
			ObjectCluster iCluster = clusters[i];
			int[] iClusterCenter = iCluster.getClusterCenter();
			for(int j=i;j<clusters.length;j++){
				if(i==j){
					continue;
				}
				ObjectCluster jCluster = clusters[j];
				int[] jClusterCenter = jCluster.getClusterCenter();
				long normValue = 0;
				for(int k=0;k<jClusterCenter.length;k++){
					normValue+=Math.pow(iClusterCenter[k]-jClusterCenter[k], 2);
				}
				if(normValue<mininalLength){
					mininalLength = normValue;
				}
			}
		}
		return mininalLength;
	}
	
	/**
	 * counts average distance between points and it's cluster centers<br/>
	 * 
	 * formula is 1/N*(sum by clusters of sum by points of Norm(x-center of cluster))
	 * @param clusters the clusters that were clusterized in prev iteration
	 * @param numOfpoints number of all pixels of the image
	 * @return
	 */
	private long countIntraLength(ObjectCluster[] clusters,int numOfpoints) {
		long result = 0;
		for(ObjectCluster cluster:clusters){
			int[][] clusterPixels = cluster.getClusterPoints();
			int[] clusterCenter = cluster.getClusterCenter();
			for(int k=0;k<clusterPixels.length;k++){
				long normValue = 0;
				int[] dot = clusterPixels[k];
				for(int i=0;i<clusterCenter.length;i++){
					normValue+=(Math.pow(dot[i]-clusterCenter[i], 2));
				}
				result+=normValue;
			}
		}
		result = result/(long)numOfpoints;
		return result;
	}

	/**
	 * counts the validity of the current clusterization
	 * 
	 * @return
	 */
	private double countValidity(ObjectCluster[] clusters,int numberOfPixels) {
		return (double)countIntraLength(clusters,numberOfPixels)/(double)countInterLength(clusters);
	}

}
