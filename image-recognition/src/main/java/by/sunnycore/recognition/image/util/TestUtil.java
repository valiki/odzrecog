package by.sunnycore.recognition.image.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import by.sunnycore.recognition.domain.ObjectCluster;

public class TestUtil {

	private static final String IMAGE_PATH = "images/D2010-07-25T11-12-37.nef";
	private static final String SERIALIZED_CLUSTERS_PREFIX = "/Users/user/Documents/phd/repos/phd/fssviewer/static-resources/clusters/clusters";
	private static final String SERIALIZED_CLUSTERS_FILE = SERIALIZED_CLUSTERS_PREFIX+".zip";
	
	public static String getClustersFileName(int number){
		return SERIALIZED_CLUSTERS_PREFIX+"-"+number+".zip";
	}
	
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
		path = path.substring(0, path.indexOf("."));
		String newpath = path.replaceAll(what, to);
		if(newpath.equals(path)){
			newpath = newpath+to;
		}
		System.out.println(newpath);
		ImageUtil.saveImage((BufferedImage) image, new File(newpath), "png");
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
	
	public static ObjectCluster[] loadCLustersFromFile(){
		ObjectCluster[] clusters = loadCLustersFromFile(0);
		return clusters;
	}
	
	public static ObjectCluster[] loadCLustersFromFile(int number){
		ObjectCluster[] clusters = readObjectsFromZip(number);
		return clusters;
	}

	public static ObjectCluster[] readObjectsFromZip(int number) {
		ObjectCluster[] clusters = null;
		FileInputStream fileStream = null;
		ZipInputStream zip = null;
		ObjectInputStream os = null;
		try {
			fileStream = new FileInputStream((number)>0?getClustersFileName(number):SERIALIZED_CLUSTERS_FILE);
			zip = new ZipInputStream(fileStream);
			zip.getNextEntry();
			os = new ObjectInputStream(zip);
			Object one = os.readObject();
			clusters = (ObjectCluster[]) one;
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
		return clusters;
	}
	
	public static void writeObjectIntoZip(ObjectCluster[] clusters) {
		FileOutputStream fileStream = null;
		ZipOutputStream zip = null;
		ObjectOutputStream os = null;
		try {
			fileStream = new FileOutputStream(SERIALIZED_CLUSTERS_FILE);
			zip = new ZipOutputStream(fileStream);
			zip.putNextEntry(new ZipEntry("clusters.ser"));
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
}
