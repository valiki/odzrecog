package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import by.sunnycore.recognition.test.TestUtil;

public class BitonalImageTransformerTest extends AbstractImageHistogramTransformerTest{
	@Test
	public void testTransform() throws IOException{
		BufferedImage image = TestUtil.loadImage("images/0020_equalized.png");
		BitonalImageTransformer transformer = new BitonalImageTransformer();
		BufferedImage res = transformer.transform(image);
		String name = "_bw";
		TestUtil.saveImageWithNewName(res, "\\.bmp", name+".png");
		buildHistograms(res,name);
	}
}
