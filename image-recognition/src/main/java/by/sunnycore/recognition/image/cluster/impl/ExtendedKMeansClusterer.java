package by.sunnycore.recognition.image.cluster.impl;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.cluster.ImageClusterer;
import by.sunnycore.recognition.image.util.DataUtil;
import by.sunnycore.recognition.image.util.ImageUtil;

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
	private static final int MAX_CLUSTERS_NUMBER = 10;

	private Logger logger = Logger.getLogger(ExtendedKMeansClusterer.class);
	
	@Override
	public ObjectCluster[] cluster(BufferedImage source) {
		int[][] pointsRGB = ImageUtil.imageTORGBRawArray(source);
		int numberOfPixels = pointsRGB[0].length;
		ObjectCluster[] theBestClusters = null;
		double minValidity = Double.MAX_VALUE;
		logger.info("the number of points are: "+numberOfPixels);
		for(int i=MAX_CLUSTERS_NUMBER;i>MIN_CLUSTERS_NUMBER;i--){
			//System.out.println("starting k means clustering iteration with "+i+" number of clusters");
			logger.info("starting k means clustering iteration with "+i+" number of clusters");
			ObjectCluster[] clusters = doKMeansIteration(pointsRGB, i);
			logger.info("done k means clustering with "+i+" number of clusters");
			//serialize clusters into file so that we wont recount them again
			serializeClustersIntoFile(clusters, i);
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

	private void serializeClustersIntoFile(ObjectCluster[] clusters,int index){
		String name = "/Users/user/Documents/phd/repos/phd/fssviewer/static-resources/clusters/clusters-"+index+".zip";
		FileOutputStream fileStream = null;
		ZipOutputStream zip = null;
		ObjectOutputStream os = null;
		try {
			fileStream = new FileOutputStream(name);
			zip = new ZipOutputStream(fileStream);
			zip.putNextEntry(new ZipEntry("clusters-"+index+".ser"));
			os = new ObjectOutputStream(zip);
			os.writeObject(clusters);
			zip.closeEntry();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (zip != null) {
				try {
					zip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileStream != null) {
				try {
					fileStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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
			KMeansDataClusterer clusterer = new KMeansDataClusterer(clusterNumber);
			logger.info(" creating dataset from array of pixels");
			logger.info("clustering using clusterer");
			short[][] data = DataUtil.intToShort(pointsRGB);
			short[][][] res = clusterer.cluster(data);
			logger.info("dataset to object clusters");
			short[][] clusterCenters = clusterer.getClusterCenters();
			ObjectCluster[] result = DataUtil.shortToObjectClusters(res, clusterCenters);
			clusterer.destroy();
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
	private double countInterLength(ObjectCluster[] clusters) {
		double mininalLength = Double.MAX_VALUE;
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
	private double countIntraLength(ObjectCluster[] clusters,int numOfpoints) {
		double result = 0;
		for(ObjectCluster cluster:clusters){
			int[][] clusterPixels = cluster.getClusterPoints();
			int[] clusterCenter = cluster.getClusterCenter();
			for(int k=0;k<clusterPixels[0].length;k++){
				long normValue = 0;
				int[] dot = extractDot(clusterPixels, k);
				for(int i=0;i<clusterCenter.length;i++){
					normValue+=(Math.pow(dot[i]-clusterCenter[i], 2));
				}
				result+=normValue;
			}
		}
		result = result/(double)numOfpoints;
		return result;
	}

	private int[] extractDot(int[][] clusterPixels,int dotIndex){
		int[] dot = new int[clusterPixels.length];
		for(int j=0;j<clusterPixels.length;j++){
			dot[j]=clusterPixels[j][dotIndex];
		}
		return dot;
	}
	/**
	 * counts the validity of the current clusterization
	 * 
	 * @return
	 */
	private double countValidity(ObjectCluster[] clusters,int numberOfPixels) {
		return countIntraLength(clusters,numberOfPixels)/countInterLength(clusters);
	}

}
