package by.sunnycore.recognition.image.cluster.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.Logger;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.util.ClusteringUtil;
import by.sunnycore.recognition.image.util.DataUtil;
import by.sunnycore.recognition.image.util.ImageUtil;
import by.sunnycore.recognition.test.TestUtil;

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
		logger.debug("Start Teaching Clusterer");
		long time = System.currentTimeMillis();
		m.teach(teachData);
		logger.info("Teaching took "+(System.currentTimeMillis()-time)+"ms");
		BufferedImage image = TestUtil.loadImage("images/0048_cont.bmp");
		int[][] rgb = ImageUtil.imageTORGBRawArray(image);
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
		BufferedImage markedImage = ClusteringUtil.markClustersOnSourceImage(objectClusters, image);
		TestUtil.saveImageWithNewName(markedImage, "\\.bmp", "_rectangle.png");
	}

}
