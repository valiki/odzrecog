package by.sunnycore.recognition.image.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.cluster.impl.ExtendedKMeansClusterer;
import by.sunnycore.recognition.image.util.ClusteringUtil;
import by.sunnycore.recognition.image.util.ImageUtil;
import by.sunnycore.recognition.test.TestUtil;

public class ExtendedKMeanClustererTest {
    private static final String SERIALIZED_CLUSTERS_PREFIX = "c:/Users/Val/Documents/GitHub/odzrecog/image-recognition/clusters/clusters";
	private static final String SERIALIZED_CLUSTERS_FILE = SERIALIZED_CLUSTERS_PREFIX+".zip";

	public static void main(String[] args) {
		try {
			ExtendedKMeanClustererTest t = new ExtendedKMeanClustererTest();
			t.test();
		} catch (IOException e) {
			
		}
	}
	
	public void test() throws IOException{
		BufferedImage source = TestUtil.loadImage();
		PixelGrabber pixelGrabber = new PixelGrabber(source, 0, 0, source.getWidth(), source.getHeight(), true);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		final DirectColorModel colorModel = (DirectColorModel) pixelGrabber.getColorModel();
		BufferedImage newImg = ImageUtil.createImage(pixels, source.getWidth(), source.getHeight());
		TestUtil.saveImageWithNewName(newImg, "\\.bmp", "_test.png");
		//clusterImageIntoFile(source);
		ObjectCluster[] clusters = TestUtil.loadCLustersFromFile(4);
		BufferedImage img = ClusteringUtil.markClustersOnSourceImage(clusters, source);
		//ClusteringUtil.markDotsWithTooLightedValues(source,"_4_clusters");
		//Map<Integer,ObjectCluster> classificationMap = new HashMap<Integer, ObjectCluster>();
		//int clustersNumber = clusters.length;
		/*for(int i=0;i<clustersNumber;i++){
			ObjectCluster objectCluster = clusters[i];
			int[] c = objectCluster.getClusterCenter();
			System.out.println(c[0]+","+c[1]+","+c[2]);
			int[][] points = objectCluster.getClusterPoints();
			Color newColor = getNewColor(i);
			objectCluster.setClusterColor(newColor.getRGB());
			for(int j=0;j<points[0].length;j++){
				int[] pointRGB = new int[]{points[0][j],points[1][j],points[2][j]};
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
				int center = cluster.getClusterColor();
				pixels[i]=center;
			}
		}*/
		//BufferedImage newImg1 = ImageUtil.createImage(pixels, source.getWidth(), source.getHeight());
		//showResultImageOnFrame(newImg1);
		
		TestUtil.saveImageWithNewName(img, "\\.bmp", "_kmeans_4_.png");
		
		
		//BufferedImage chart = buildChart(clusters);
		//int[][] pointsRGB = ClusteringUtil.markDotsWithTooLightedValues(source.getWidth(), source.getHeight(), pixelGrabber, pixels);
		//BufferedImage chart = ClusteringUtil.buildChart(pointsRGB);
		//TestUtil.saveImageWithNewName(chart, "\\.bmp", "_chart.png");
	}

	public void clusterImageIntoFile(BufferedImage source) throws IOException, FileNotFoundException {
		ExtendedKMeansClusterer clusterer = new ExtendedKMeansClusterer();
		ObjectCluster[] clusters = clusterer.cluster(source);
		TestUtil.writeObjectIntoZip(clusters);
	}
	
	private void showResultImageOnFrame(BufferedImage newImg1) {
		JFrame frame = new JFrame("clustered image");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JLabel(new ImageIcon(newImg1)));
		frame.setSize(newImg1.getWidth(), newImg1.getHeight());
		/*BufferStrategy bs = frame.getBufferStrategy();
		if(bs==null){
			frame.createBufferStrategy(4);
			bs = frame.getBufferStrategy();
		}
		Graphics gc = bs.getDrawGraphics();
		gc.drawImage(newImg1, 0, 0, newImg1.getWidth(), newImg1.getHeight(), frame);
		//gc.dispose();
		bs.show();*/
	}
}
