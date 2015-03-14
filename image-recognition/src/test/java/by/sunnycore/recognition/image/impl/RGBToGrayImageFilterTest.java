package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import by.sunnycore.recognition.image.filter.ImageFilter;
import by.sunnycore.recognition.image.filter.impl.RGBToGrayImageFilter;
import by.sunnycore.recognition.image.util.TestUtil;

public class RGBToGrayImageFilterTest {
	
	@Test
	public void test() throws IOException{
		BufferedImage image = TestUtil.loadImage("images/0020_median.png");
		ImageFilter filter = new RGBToGrayImageFilter();
		BufferedImage res = filter.filter(image);
		TestUtil.saveImageWithNewName(res, "\\.bmp", "_gray"+".png");
	}
	
}
