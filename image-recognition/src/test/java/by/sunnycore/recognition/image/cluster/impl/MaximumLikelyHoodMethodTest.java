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

public class MaximumLikelyHoodMethodTest extends AbstractTeachableClusterizationMethodTest{
	
	private Logger logger = Logger.getLogger(MaximumLikelyHoodMethodTest.class);
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		MaximumLikelyHoodMethodTest t = new MaximumLikelyHoodMethodTest();
		t.executeMaximumLikelyHoodMethod();
	}
	
	
	
	public void executeMaximumLikelyHoodMethod() throws IOException{
		logger.debug("Creating Maximum LikelyHood Method Clusterer.");
		MaximumLikelyHoodMethod m = new MaximumLikelyHoodMethod();
		logger.debug("Loading Teach data from disk.");
		/*ObjectCluster[] clusters = loadTestTEachData();
		List<ObjectCluster[]> teachData = new ArrayList<>();
		teachData.add(clusters);*/
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
		BufferedImage markedImage = ClusteringUtil.markClustersOnSourceImage(objectClusters, image);
		TestUtil.saveImageWithNewName(markedImage, "\\.bmp", "_max_like.png");
		ClusteringUtil.buildChart(objectClusters);
	}

}
