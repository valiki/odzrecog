package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import by.sunnycore.recognition.image.filter.ImageFilter;
import by.sunnycore.recognition.image.filter.impl.GaussFilter;
import by.sunnycore.recognition.image.util.TestUtil;

public class GaussFIlterTest {

	@Test
	public void test() throws IOException{
		BufferedImage src = TestUtil.loadImage();
		ImageFilter filter = new GaussFilter();
		BufferedImage result = filter.filter(src);
		String name = "_gauss_filter";
		TestUtil.saveImageWithNewName(result, "\\.bmp", name+".png");
	}
	
}
