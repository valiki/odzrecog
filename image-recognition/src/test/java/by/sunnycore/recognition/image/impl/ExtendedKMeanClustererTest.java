package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.media.jai.operator.MedianFilterDescriptor;

import org.junit.Test;

import by.sunnycore.recognition.domain.ObjectCluster;
import by.sunnycore.recognition.image.IImageFilter;
import by.sunnycore.recognition.test.TestUtil;

public class ExtendedKMeanClustererTest {
	@Test
	public void test() throws IOException{
		BufferedImage source = TestUtil.loadImage();
		IImageFilter filter = new JAIMedianFilter(10,MedianFilterDescriptor.MEDIAN_MASK_X);
		BufferedImage median = filter.filter(source);
		TestUtil.saveImageWithNewName(median, "\\.bmp", "_median.png");
		JAIResizeImageTransformer resizeImageTransformer = new JAIResizeImageTransformer(200);
		BufferedImage resized = resizeImageTransformer.transform(median);
		TestUtil.saveImageWithNewName(resized, "\\.bmp", "_resized.png");
		ExtendedKMeansClusterer clusterer = new ExtendedKMeansClusterer();
		ObjectCluster[] clusters = clusterer.cluster(resized);
		System.out.println("clusters size is: "+clusters.length);
	}
}
