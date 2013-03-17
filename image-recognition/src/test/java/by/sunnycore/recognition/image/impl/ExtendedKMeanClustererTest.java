package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.math.plot.Plot3DPanel;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.cluster.impl.ExtendedKMeansClusterer;
import by.sunnycore.recognition.image.util.ImageUtil;
import by.sunnycore.recognition.test.TestUtil;

public class ExtendedKMeanClustererTest {

	private static final String SERIALIZED_CLUSTERS_FILE = "c:/Users/Val/Documents/GitHub/odzrecog/image-recognition/src/main/resources/clusters.ser";

	public static void main(String[] args) {
		try {
			ExtendedKMeanClustererTest t = new ExtendedKMeanClustererTest();
			t.test();
		} catch (IOException e) {
			
		}
	}
	
	public void test() throws IOException{
		BufferedImage source = TestUtil.loadImage();
		/*IImageFilter filter = new JAIMedianFilter(10,MedianFilterDescriptor.MEDIAN_MASK_X);
		BufferedImage median = filter.filter(source);
		TestUtil.saveImageWithNewName(median, "\\.bmp", "_median.png");*/
		/*JAIResizeImageTransformer resizeImageTransformer = new JAIResizeImageTransformer(200);
		BufferedImage resized = resizeImageTransformer.transform(source);
		TestUtil.saveImageWithNewName(resized, "\\.bmp", "_resized.png");*/
		PixelGrabber pixelGrabber = new PixelGrabber(source, 0, 0, source.getWidth(), source.getHeight(), true);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		final DirectColorModel colorModel = (DirectColorModel) pixelGrabber.getColorModel();
		BufferedImage newImg = ImageUtil.createImage(pixels, source.getWidth(), source.getHeight());
		TestUtil.saveImageWithNewName(newImg, "\\.png", "_test.png");
		clusterImageIntoFile(source);
		ObjectCluster[] clusters = loadCLustersFromFile();
		Map<Integer,ObjectCluster> classificationMap = new HashMap<Integer, ObjectCluster>();
		for(int i=0;i<clusters.length;i++){
			ObjectCluster objectCluster = clusters[i];
			int[] c = objectCluster.getClusterCenter();
			System.out.println(c[0]+","+c[1]+","+c[2]);
			for(int j=0;j<objectCluster.getClusterPoints().length;j++){
				int[] pointRGB = objectCluster.getClusterPoints()[j];
				int point = (pointRGB[0]<<16)+(pointRGB[1]<<8)+(pointRGB[2]);
				classificationMap.put(point,objectCluster);
			}
		}
		for(int i=0;i<pixels.length;i++){
			int pixel = pixels[i];
			int alpha = colorModel.getAlpha(pixel)<<24;
			pixel = pixel-alpha;
			ObjectCluster cluster = classificationMap.get(pixel);
			if(cluster!=null){
				int[] center = cluster.getClusterCenter();
				pixels[i]=(center[0]<<16)+(center[1]<<8)+(center[2])+alpha;
			}
		}
		BufferedImage newImg1 = ImageUtil.createImage(pixels, source.getWidth(), source.getHeight());
		TestUtil.saveImageWithNewName(newImg1, "\\.png", "_result.png");
		
		BufferedImage chart = buildChart(clusters);
		TestUtil.saveImageWithNewName(chart, "\\.png", "_chart.png");
		
		System.out.println("clusters size is: "+clusters.length);
	}

	private BufferedImage buildChart(final ObjectCluster[] clusters){
		Plot3DPanel chart = new Plot3DPanel();
		chart.setSize(480, 640);
		for(int k=0;k<clusters.length;k++){
			ObjectCluster cluster = clusters[k];
			int[][] points = cluster.getClusterPoints();
			int l = points.length/10;
			l = (l<5000)?l:5000;
			double[][] datasetPoints = new double[3][l];
			for(int i=0;i<l;i++){
				datasetPoints[0][i]=points[i][0];
				datasetPoints[1][i]=points[i][1];
				datasetPoints[2][i]=points[i][2];
			}
			chart.addScatterPlot("Cluster "+k, datasetPoints[0],datasetPoints[1],datasetPoints[2]);
		}
/*		JFrame frame = new JFrame("a plot panel");
		frame.setBounds(0, 0, 480, 640);
		frame.setContentPane(chart);
		frame.setVisible(true);*/
		
		BufferedImage b = ImageUtil.createImageFromPanel(chart,480,640);
		return b;
	}
	
	private ObjectCluster[] loadCLustersFromFile(){
		ObjectCluster[] clusters = null;
		try(FileInputStream fileInputStream = new FileInputStream(SERIALIZED_CLUSTERS_FILE);
			ObjectInputStream oInputStream = new ObjectInputStream(fileInputStream)){
			Object one = oInputStream.readObject();
			clusters = (ObjectCluster[]) one;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return clusters;
	}
	
	private void clusterImageIntoFile(BufferedImage source) throws IOException,
			FileNotFoundException {
		ExtendedKMeansClusterer clusterer = new ExtendedKMeansClusterer();
		ObjectCluster[] clusters = clusterer.cluster(source);
		try (FileOutputStream fileStream = new FileOutputStream(SERIALIZED_CLUSTERS_FILE);
			 ObjectOutputStream os = new ObjectOutputStream(fileStream);) {
			os.writeObject(clusters);
		}
	}
}
