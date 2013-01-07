package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import by.sunnycore.recognition.image.filter.IImageFilter;
import by.sunnycore.recognition.image.filter.RGBToGrayImageFilter;
import by.sunnycore.recognition.test.TestUtil;

public class RGBToGrayImageFilterTest {
	
	@Test
	public void test() throws IOException{
		BufferedImage image = TestUtil.loadImage("images/0020_median.png");
		IImageFilter filter = new RGBToGrayImageFilter();
		BufferedImage res = filter.filter(image);
		TestUtil.saveImageWithNewName(res, "\\.bmp", "_gray"+".png");
	}
	
}
