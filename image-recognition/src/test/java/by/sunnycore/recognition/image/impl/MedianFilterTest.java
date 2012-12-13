package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.media.jai.operator.MedianFilterDescriptor;

import org.junit.Test;

import by.sunnycore.recognition.image.IImageFilter;
import by.sunnycore.recognition.test.TestUtil;

public class MedianFilterTest {
	@Test
	public void test() throws IOException{
		BufferedImage source = TestUtil.loadImage();
		IImageFilter filter = new JAIMedianFilter(10,MedianFilterDescriptor.MEDIAN_MASK_X);
		BufferedImage result = filter.filter(source);
		String name = "_median";
		TestUtil.saveImageWithNewName(result, "\\.bmp", name+".png");
	}
}
