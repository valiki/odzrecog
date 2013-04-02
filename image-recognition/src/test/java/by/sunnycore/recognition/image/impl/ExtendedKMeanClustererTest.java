package by.sunnycore.recognition.image.impl;

import java.awt.Color;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.math.plot.Plot3DPanel;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.cluster.impl.ExtendedKMeansClusterer;
import by.sunnycore.recognition.image.util.ImageUtil;
import by.sunnycore.recognition.test.TestUtil;

public class ExtendedKMeanClustererTest {

    public final static Color[] COLORLIST = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA};
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
		TestUtil.saveImageWithNewName(newImg, "\\.bmp", "_test.png");
		//clusterImageIntoFile(source);
		ObjectCluster[] clusters = loadCLustersFromFile(6);
		Map<Integer,ObjectCluster> classificationMap = new HashMap<Integer, ObjectCluster>();
		int clustersNumber = clusters.length;
		for(int i=0;i<clustersNumber;i++){
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
		}
		BufferedImage newImg1 = ImageUtil.createImage(pixels, source.getWidth(), source.getHeight());
		showResultImageOnFrame(newImg1);
		
		TestUtil.saveImageWithNewName(newImg1, "\\.bmp", "_result.png");
		
		BufferedImage chart = buildChart(clusters);
		TestUtil.saveImageWithNewName(chart, "\\.png", "_chart.png");
		
		System.out.println("clusters size is: "+clustersNumber);
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

    protected Color getNewColor(int index) {
        return COLORLIST[index];
    }
	
	private BufferedImage buildChart(final ObjectCluster[] clusters){
		Plot3DPanel chart = new Plot3DPanel();
		chart.setSize(480, 640);
		for(int k=0;k<clusters.length;k++){
			ObjectCluster cluster = clusters[k];
			int[][] points = cluster.getClusterPoints();
			int l = points[0].length/10;
			l = (l<5000)?l:5000;
			double[][] datasetPoints = new double[3][l];
			for(int i=0;i<l;i++){
				datasetPoints[0][i]=points[0][i];
				datasetPoints[1][i]=points[1][i];
				datasetPoints[2][i]=points[2][i];
			}
			chart.addScatterPlot("Cluster "+k, datasetPoints[0],datasetPoints[1],datasetPoints[2]);
		}
		/*JFrame frame = new JFrame("a plot panel");
		frame.setBounds(0, 0, 480, 640);
		frame.setContentPane(chart);
		frame.setVisible(true);*/
		
		BufferedImage b = ImageUtil.createImageFromPanel(chart,480,640);
		return b;
	}
	
	private ObjectCluster[] loadCLustersFromFile(){
		ObjectCluster[] clusters = loadCLustersFromFile(0);
		return clusters;
	}
	
	private ObjectCluster[] loadCLustersFromFile(int number){
		ObjectCluster[] clusters = readObjectsFromZip(number);
		return clusters;
	}

	private ObjectCluster[] readObjectsFromZip(int number) {
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
	
	private void clusterImageIntoFile(BufferedImage source) throws IOException,
			FileNotFoundException {
		ExtendedKMeansClusterer clusterer = new ExtendedKMeansClusterer();
		ObjectCluster[] clusters = clusterer.cluster(source);
		writeObjectIntoZip(clusters);
	}

	private void writeObjectIntoZip(ObjectCluster[] clusters) {
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
	
	public String getClustersFileName(int number){
		return SERIALIZED_CLUSTERS_PREFIX+"-"+number+".zip";
	}
}
