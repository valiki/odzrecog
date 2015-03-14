package by.sunnycore.recognition.image.cluster.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.util.ClusteringUtil;
import by.sunnycore.recognition.image.util.DataUtil;
import by.sunnycore.recognition.image.util.ImageUtil;
import by.sunnycore.recognition.image.util.Point;
import by.sunnycore.recognition.image.util.TestUtil;

public abstract class AbstractTeachableClusterizationMethodTest {
	
	private Logger logger = Logger.getLogger(AbstractTeachableClusterizationMethodTest.class);

	private String[] classNames = new String[] {"field","forest","water","road"};
	
	public List<ObjectCluster[]> enhanceData(List<ObjectCluster[]> data){
		for(ObjectCluster[] clusters:data){
			for(int i=0;i<clusters.length;i++){
				int[][] clusterPoints = clusters[i].getClusterPoints();
				int[][] enhancedPixels = enhancePixels(clusterPoints);
				clusters[i].setClusterPoints(enhancedPixels);
			}
		}
		return data;
	}

	public int[][] enhancePixels(int[][] clusterPoints) {
		double[][] dClusterPoints = DataUtil.intToDouble(clusterPoints);
		dClusterPoints = ClusteringUtil.addParamBands(dClusterPoints, new SpectralAngleCounter());
		int[][] enhancedPixels = DataUtil.doubleToInt(dClusterPoints);
		return enhancedPixels;
	}
	
	public List<ObjectCluster[]> loadTeachData() throws IOException{
		URL r = MaximumLikelyHoodMethodTest.class.getClassLoader().getResource("images/teach");
		String folder = r.getFile();
		File f = new File(folder);
		final Map<String,List<File>> teachDataNumberForClasses = new HashMap<>();
		for(String c:classNames){
			List<File> files = teachDataNumberForClasses.get(c);
			if(files==null){
				files = new ArrayList<>();
				teachDataNumberForClasses.put(c, files);
			}
		}
		f.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				String name = pathname.getName();
				for(String className:classNames){
					if(name.contains(className)){
						List<File> files = teachDataNumberForClasses.get(className);
						files.add(pathname);
						return true;
					}
				}
				return false;
			}
		});
		int maxTeachData = 0;
		for(String className:teachDataNumberForClasses.keySet()){
			List<File> files = teachDataNumberForClasses.get(className);
			if(files.size()>maxTeachData){
				maxTeachData = files.size();
			}
		}
		for(String className:classNames){
			List<File> list = teachDataNumberForClasses.get(className);
			while( list.size() < maxTeachData ){
				int index = (int)(Math.random()*(list.size()-1));
				list.add(list.get(index));
			}
		}
		List<ObjectCluster[]> teachData = new ArrayList<>();
		int clusterNumber = 0;
		for(String key:classNames){
			List<File> list = teachDataNumberForClasses.get(key);
			for(int i=0;i<list.size();i++){
				ObjectCluster[] teachSet;
				if (i == teachData.size()) {
					teachSet = new ObjectCluster[classNames.length];
					teachData.add(teachSet);
				}else{
					teachSet = teachData.get(i);
				}
				File file = list.get(i);
				BufferedImage img = ImageUtil.loadImage(file);
				int[][] pixels = ImageUtil.imageTORGBRawArray(img);
				ObjectCluster objectCluster = new ObjectCluster();
				pixels = filterLightedPixels(pixels);
				objectCluster.setClusterPoints(pixels);
				teachSet[clusterNumber]=objectCluster;
			}
			clusterNumber++;
		}
		return teachData;
	}

	public int[][] filterLightedPixels(int[][] pixels) {
		return pixels;
		/*int number = 0;
		for(int j=0;j<pixels[0].length;j++){
			for(int k=0;k<pixels.length;k++){
				if(pixels[k][j]>253){
					continue;
				}
			}
			number++;
		}
		int[][] filteredPixels = new int[pixels.length][number];
		number = 0;
		for(int j=0;j<pixels[0].length;j++){
			for(int k=0;k<pixels.length;k++){
				if(pixels[k][j]>253){
					continue;
				}else{
					filteredPixels[k][number]=pixels[k][j];
				}
			}
			number++;
		}
		return filteredPixels;*/
	}
	
	protected ObjectCluster[] loadTestTEachData() {
		ObjectCluster[] clusters = TestUtil.loadCLustersFromFile(6);
		EuklidDistanceCounter dc = new EuklidDistanceCounter();
		for(ObjectCluster c:clusters){
			int[][] clusterPoints = c.getClusterPoints();
			int[] center = c.getClusterCenter();
			short[] centerS = new short[center.length];
			for(int i=0;i<center.length;i++){
				centerS[i]=(short) center[i];
			}
			double maxLength = 0f;
			Set<Point> uniquiePoints = new HashSet<>();
			for(int i=0;i<clusterPoints[0].length;i++){
				int[] pointData = new int[clusterPoints.length];
				short[] pointShort = new short[clusterPoints.length];
				for(int j=0;j<clusterPoints.length;j++){
					pointData[j]=clusterPoints[j][i];
					pointShort[j]=(short) pointData[j];
				}
				uniquiePoints.add(new Point(pointData));
				double d = dc.countDistance(centerS, pointShort);
				if(d > maxLength){
					maxLength = d;
				}
			}
			int numberOfUniquePoints = uniquiePoints.size();
			//numberOfUniquePoints = (numberOfUniquePoints>50000)?50000:numberOfUniquePoints;
			int[][] uniqueClusterPoints = new int[clusterPoints.length][numberOfUniquePoints];
			List<Point> uPointsList = new ArrayList<>(uniquiePoints);
			int i = 0;
			for(Point point:uPointsList){
				int[] data = point.getData();
				short[] pointShort = new short[clusterPoints.length];
				for(int j=0;j<data.length;j++){
					pointShort[j]=(short) data[j];
				}
				/*if(dc.countDistance(centerS, pointShort)>150){
					continue;
				}*/
				for(int j=0;j<data.length;j++){
					uniqueClusterPoints[j][i]=data[j];
				}
				i++;
				if(i==uniqueClusterPoints[0].length){
					break;
				}
			}
			c.setClusterPoints(uniqueClusterPoints);
		}
		return clusters;
	}
}
