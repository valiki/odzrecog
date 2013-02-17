package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import by.sunnycore.recognition.image.filter.ImageFilter;
import by.sunnycore.recognition.image.filter.impl.JAIFreiChenEdgeImageFilter;
import by.sunnycore.recognition.image.filter.impl.JAIPrewittEdgeImageFilter;
import by.sunnycore.recognition.image.filter.impl.JAIRobertsEdgeImageFilter;
import by.sunnycore.recognition.test.TestUtil;

public class JAIEdgeImageTransformerTest {
	@Test
	public void test() throws IOException {
		BufferedImage image = TestUtil.loadImage("images/0020_gray.png");
		ImageFilter transformer1 = new JAIRobertsEdgeImageFilter();
		ImageFilter transformer2 = new JAIPrewittEdgeImageFilter();
		ImageFilter transformer3 = new JAIFreiChenEdgeImageFilter();
		BufferedImage res1 = transformer1.filter(image);
		BufferedImage res2 = transformer2.filter(image);
		BufferedImage res3 = transformer3.filter(image);
		String name = "_gray_roberts";
		TestUtil.saveImageWithNewName(res1, "\\.bmp", name+".png");
		name="_gray_prewitt";
		TestUtil.saveImageWithNewName(res2, "\\.bmp", name+".png");
		name="_gray_freichen";
		TestUtil.saveImageWithNewName(res3, "\\.bmp", name+".png");
	}
}
