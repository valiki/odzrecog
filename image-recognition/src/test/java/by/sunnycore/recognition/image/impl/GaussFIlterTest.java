package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import by.sunnycore.recognition.image.IImageFilter;
import by.sunnycore.recognition.test.TestUtil;

public class GaussFIlterTest {

	@Test
	public void test() throws IOException{
		BufferedImage src = TestUtil.loadImage();
		IImageFilter filter = new GaussFilter();
		BufferedImage result = filter.filter(src);
		String name = "_gauss_filter";
		TestUtil.saveImageWithNewName(result, "\\.bmp", name+".png");
	}
	
}
