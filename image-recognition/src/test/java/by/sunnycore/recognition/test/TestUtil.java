package by.sunnycore.recognition.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import by.sunnycore.recognition.image.util.ChartUtil;
import by.sunnycore.recognition.image.util.ImageUtil;

public class TestUtil {

	private static final String IMAGE_PATH = "images/0020_equalized.png";
	
	public static BufferedImage loadImage() throws IOException{
		return loadImage(IMAGE_PATH);
	}
	
	public static BufferedImage loadImage(String path) throws IOException{
		BufferedImage image = null;
		try {
			URL resource = TestUtil.class.getClassLoader().getResource(path);
			image = ImageUtil.loadImage(new File(resource.toURI()));
		} catch (URISyntaxException e) {}
		return image;
	}
	
	public static String getImagePathString(){
		return getImagePath().getPath();
		
	}
	
	public static URI getImagePath(){
		try {
			URL resource = TestUtil.class.getClassLoader().getResource(IMAGE_PATH);
			return resource.toURI();
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	public static void saveImageWithNewName(BufferedImage image,String what,String to) throws IOException{
		String path = getImagePathString();
		path = path.replaceAll(what, to);
		System.out.println(path);
		ImageUtil.saveImage((BufferedImage) image, new File(path), "png");
	}
	
	public static BufferedImage[] buildHistogramCharts(int[][] buildHistogram) {
		double[][][] data = new double[buildHistogram.length][][];
		for(int i=0;i<data.length;i++){
			data[i]= new double[2][buildHistogram[0].length];
		}
		for(int i=0;i<buildHistogram.length;i++){
			for(int j=0;j<buildHistogram[i].length;j++){
				data[i][0][j]=j;
				data[i][1][j]=buildHistogram[i][j];
			}
		}
		BufferedImage[] result = new BufferedImage[data.length];
		String[] colors;
		if (data.length==3) {
			String[] c={"Red","Green","Blue"};
			colors = c;
		}else{
			String[] c={"Black","White"};
			colors = c;
		}
		for(int i=0;i<data.length;i++){
			result[i]= ChartUtil.buildChart(data[i], "Histogram"+colors[i], "colour", "number of dots", 400, 400,"line");
		}
		return result;
	}
	
}
