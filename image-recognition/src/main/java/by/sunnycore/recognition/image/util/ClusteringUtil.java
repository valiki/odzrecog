package by.sunnycore.recognition.image.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.math.plot.Plot3DPanel;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.cluster.ParamCounter;

public class ClusteringUtil {

	public final static Color[] COLORLIST = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.PINK, Color.CYAN, Color.GRAY, Color.MAGENTA, Color.YELLOW, Color.LIGHT_GRAY};
	
	public static int[][] markDotsWithTooLightedValues(BufferedImage source,String newName) {
		PixelGrabber pixelGrabber = new PixelGrabber(source, 0, 0, source.getWidth(), source.getHeight(), true);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		int width = source.getWidth();
		int height = source.getHeight();
		return markDotsWithTooLightedValues(width, height, pixelGrabber, pixels, newName);
	}

	public static int[][] markDotsWithTooLightedValues(int width, int height,
			PixelGrabber pixelGrabber, int[] pixels,String newName) {
		final DirectColorModel colorModel = (DirectColorModel) pixelGrabber
				.getColorModel();
		int[][] pointsRGB = new int[3][];// create array for the RGB points for
											// clusterization
		int numberOfPixels = pixels.length;
		pointsRGB[0] = new int[numberOfPixels];
		pointsRGB[1] = new int[numberOfPixels];
		pointsRGB[2] = new int[numberOfPixels];
		for (int i = 0; i < numberOfPixels; i++) {
			pointsRGB[0][i] = ImageUtil.getRedRaw(pixels[i], colorModel);
			pointsRGB[1][i] = ImageUtil.getGreenRaw(pixels[i], colorModel);
			pointsRGB[2][i] = ImageUtil.getBlue(pixels[i], colorModel);
			if (pointsRGB[0][i] == 255 || pointsRGB[1][i] == 255
					|| pointsRGB[2][i] == 255) {
				pointsRGB[0][i] = 255;
				pointsRGB[1][i] = 0;
				pointsRGB[2][i] = 0;
			}
		}
		BufferedImage newImage = ImageUtil.createRGBImageFrom3Channels(width,
				height, pointsRGB[0], pointsRGB[1], pointsRGB[2]);
		try {
			TestUtil.saveImageWithNewName(newImage, "\\.bmp", newName+".png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pointsRGB;
	}

	public static BufferedImage buildChart(int[][] pixels) {
		Plot3DPanel chart = new Plot3DPanel();
		chart.setSize(480, 640);
		int l = pixels[0].length;
		l = (l < 2500) ? l : 2500;
		double[][] datasetPoints = new double[3][l];
		for (int i = 0; i < l; i++) {
			datasetPoints[0][i] = pixels[0][i];
			datasetPoints[1][i] = pixels[1][i];
			datasetPoints[2][i] = pixels[2][i];
		}
		chart.addScatterPlot("Points", datasetPoints[0], datasetPoints[1],
				datasetPoints[2]);
		chart.getAxis(0).setLegend("R");
		chart.getAxis(1).setLegend("G");
		chart.getAxis(2).setLegend("B");
		BufferedImage b = ImageUtil.createImageFromPanel(chart, 480, 640);
		return b;
	}

	public static BufferedImage buildChart(final ObjectCluster[] clusters) {
		Plot3DPanel chart = new Plot3DPanel();
		chart.setSize(480, 640);
		for (int k = 0; k < clusters.length; k++) {
			ObjectCluster cluster = clusters[k];
			int[][] points = cluster.getClusterPoints();
			int l = points[0].length;
			l = (l < 50000) ? l : 50000;
			double[][] datasetPoints = new double[3][l];
			for (int i = 0; i < l; i++) {
				datasetPoints[0][i] = points[0][i];
				datasetPoints[1][i] = points[1][i];
				datasetPoints[2][i] = points[2][i];
			}
			chart.addScatterPlot("Cluster " + k, datasetPoints[0],
					datasetPoints[1], datasetPoints[2]);
			chart.setAxisLabels("R","G","B");
		}
		/*
		 * JFrame frame = new JFrame("a plot panel"); frame.setBounds(0, 0, 480,
		 * 640); frame.setContentPane(chart); frame.setVisible(true);
		 */

		BufferedImage b = ImageUtil.createImageFromPanel(chart, 480, 640);
		return b;
	}

	public static BufferedImage markClustersOnSourceImage(ObjectCluster[] clusters,BufferedImage source) {
		PixelGrabber pixelGrabber = new PixelGrabber(source, 0, 0, source.getWidth(), source.getHeight(), true);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		int width = source.getWidth();
		int height = source.getHeight();
		final DirectColorModel colorModel = (DirectColorModel) pixelGrabber.getColorModel();
		return markClustersOnSourceImage(clusters, pixels, colorModel, width, height);
	}
	
	public static BufferedImage markClustersOnSourceImage(ObjectCluster[] clusters,int[] sourcePixels,ColorModel colorModel,int width, int height) {
		Map<Integer, ObjectCluster> classificationMap = buildClassificationMap(clusters);
		for (int i = 0; i < sourcePixels.length; i++) {
			int pixel = sourcePixels[i];
			int alpha = colorModel.getAlpha(pixel) << 24;
			pixel = pixel - alpha;
			ObjectCluster cluster = classificationMap.get(pixel);
			if (cluster != null) {
				int center = cluster.getClusterColor();
				sourcePixels[i] = center;
			}else{
				sourcePixels[i] = 0;
			}
		}
		BufferedImage newImg1 = ImageUtil.createImage(sourcePixels, width, height);
		return newImg1;
	}

	private static Map<Integer, ObjectCluster> buildClassificationMap(ObjectCluster[] clusters) {
		Map<Integer, ObjectCluster> classificationMap = new HashMap<Integer, ObjectCluster>();
		int clustersNumber = clusters.length;
		for (int i = 0; i < clustersNumber; i++) {
			ObjectCluster objectCluster = clusters[i];
			int[] c = objectCluster.getClusterCenter();
			System.out.println(c[0] + "," + c[1] + "," + c[2]);
			int[][] points = objectCluster.getClusterPoints();
			Color newColor = getNewColor(i);
			System.out.println("Cluster "+i+" "+newColor);
			objectCluster.setClusterColor(newColor.getRGB());
			for (int j = 0; j < points[0].length; j++) {
				int[] pointRGB = new int[] { points[0][j], points[1][j], points[2][j] };
				int point = (pointRGB[0] << 16) + (pointRGB[1] << 8) + (pointRGB[2]);
				classificationMap.put(point, objectCluster);
			}
		}
		return classificationMap;
	}
	
	public static ObjectCluster fetchUnclasifiedPixels(ObjectCluster[] clusters,int[] sourcePixels,ColorModel colorModel){
		Map<Integer, ObjectCluster> classificationMap = buildClassificationMap(clusters);
		List<Integer> unclassified = new LinkedList<>();
		for (int i = 0; i < sourcePixels.length; i++) {
			int pixel = sourcePixels[i];
			int alpha = colorModel.getAlpha(pixel) << 24;
			pixel = pixel - alpha;
			ObjectCluster cluster = classificationMap.get(pixel);
			if (cluster != null) {
				int center = cluster.getClusterColor();
				sourcePixels[i] = center;
			}else{
				unclassified.add(sourcePixels[i]);
				sourcePixels[i] = 0;
			}
		}
		ObjectCluster cluster = new ObjectCluster();
		int size = unclassified.size();
		int[][] clusterPoints = new int[3][size];
		Iterator<Integer> iterator = unclassified.iterator();
		int i=0;
		while(iterator.hasNext()){
			Integer point = iterator.next();
			int pointInt = point;
			clusterPoints[0][i]=ImageUtil.getRedRaw(pointInt, colorModel);
			clusterPoints[1][i]=ImageUtil.getGreenRaw(pointInt, colorModel);
			clusterPoints[2][i]=ImageUtil.getBlue(pointInt, colorModel);
			i++;
		}
		cluster.setClusterPoints(clusterPoints);
		return cluster;
	}
	
    public static Color getNewColor(int index) {
        return COLORLIST[index];
    }
    
    /**
     * Adds specific counted param as an additional band to the data array
     * @param data
     * @param paramCounter
     * @return
     */
    public static double[][] addParamBands(double[][] data,ParamCounter paramCounter){
    	double[][] newData = new double[data.length*2][data[0].length];
    	double[][] baseVectors = new double[data.length][data.length];
    	double[] baseVector = new double[data.length];
		int currentBand = 0;
    	for(int i=0;i<data.length;i++){
    		baseVector[i]=1;
    		for(int j=0;j<data.length;j++){
    			if(j == currentBand){
    				baseVectors[i][j] = 1;
    			}else{
    				baseVectors[i][j] = 0;
    			}
    		}
    		currentBand++;
    	}
    	for(int i=0;i<newData[0].length;i++){
    		double[] v1 = new double[data.length];
    		for(int j=0;j<data.length;j++){
    			newData[j][i]=data[j][i];
    			v1[j]=data[j][i];
    		}
    		for(int j=0;j<data.length;j++){
    			newData[j+data.length][i]=paramCounter.count(v1, baseVectors[j]);
    		}
    	}
    	return newData;
    }

}
