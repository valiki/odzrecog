package by.sunnycore.recognition.image.cluster.impl;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.Logger;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.util.ClusteringUtil;
import by.sunnycore.recognition.image.util.DataUtil;
import by.sunnycore.recognition.image.util.ImageUtil;
import by.sunnycore.recognition.image.util.TestUtil;

public class RectangleMethodClustererTest extends AbstractTeachableClusterizationMethodTest{
	
	private Logger logger = Logger.getLogger(RectangleMethodClustererTest.class);
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		RectangleMethodClustererTest test = new RectangleMethodClustererTest();
		test.executeRectangleMethod();
	}
	
	public void executeRectangleMethod() throws IOException{
		logger.debug("Creating Rectangle Method clusterer");
		RectangleMethodClusterer m = new RectangleMethodClusterer();
		logger.debug("Loading Teach data from disk.");
		List<ObjectCluster[]> teachData = loadTeachData();
		teachData = enhanceData(teachData);
		logger.debug("Start Teaching Clusterer");
		long time = System.currentTimeMillis();
		m.teach(teachData);
		logger.info("Teaching took "+(System.currentTimeMillis()-time)+"ms");
		BufferedImage image = TestUtil.loadImage("images/0048.bmp");
		int[][] rgb = ImageUtil.imageTORGBRawArray(image);
		rgb = filterLightedPixels(rgb);
		rgb = enhancePixels(rgb);
		short[][] data = new short[rgb.length][rgb[0].length];
		for(int i=0;i<rgb.length;i++){
			for(int j=0;j<rgb[0].length;j++){
				data[i][j] = (short) rgb[i][j];
			}
		}
		short[][][] result = m.cluster(data);
		RealMatrix[] mathExpectations = m.getMathExpectations();
		short[][] clusterCenters = new short[mathExpectations.length][];
		for(int i=0;i<mathExpectations.length;i++){
			RealMatrix matrix = mathExpectations[i];
			double[][] center = matrix.getData();
			clusterCenters[i] = new short[center.length];
			for(int j=0;j<center.length;j++){
				clusterCenters[i][j]= (short) center[j][0];
			}
		}
		ObjectCluster[] objectClusters = DataUtil.shortToObjectClusters(result, clusterCenters);
		PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), true);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		int width = image.getWidth();
		int height = image.getHeight();
		final ColorModel colorModel = pixelGrabber.getColorModel();
		BufferedImage markedImage = ClusteringUtil.markClustersOnSourceImage(objectClusters, pixels,colorModel,width,height);
		//objectClusters = addUnclassifiedToObjectClusters(objectClusters, pixels, colorModel);
		
		BufferedImage chart = ClusteringUtil.buildChart(objectClusters);
		TestUtil.saveImageWithNewName(markedImage, "\\.bmp", "_rectangle.png");
		TestUtil.saveImageWithNewName(chart, "\\.bmp", "_rectangle_chart.png");
	}

	private ObjectCluster[] addUnclassifiedToObjectClusters(
			ObjectCluster[] objectClusters, int[] pixels,
			final ColorModel colorModel) {
		ObjectCluster unclassified = ClusteringUtil.fetchUnclasifiedPixels(objectClusters, pixels, colorModel);
		ObjectCluster[] wUnclassifiedClusters = new ObjectCluster[objectClusters.length+1];
		for(int i=0;i<objectClusters.length;i++){
			wUnclassifiedClusters[i]=objectClusters[i];
		}
		wUnclassifiedClusters[objectClusters.length]=unclassified;
		return wUnclassifiedClusters;
	}

}
