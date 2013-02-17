package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import by.sunnycore.recognition.image.transformer.impl.JAIHistogramEqualizationTransformer;
import by.sunnycore.recognition.test.TestUtil;

public class JAIHistogramEqualizationTransformerTest extends AbstractImageHistogramTransformerTest{
	
	@Test
	public void testTransform() throws IOException{
		JAIHistogramEqualizationTransformer transformer = new JAIHistogramEqualizationTransformer();
		BufferedImage image = TestUtil.loadImage();
		BufferedImage res = transformer.transform(image);
		String name = "_equalized";
		TestUtil.saveImageWithNewName(res, "\\.bmp", name+".png");
		buildHistograms(res,name);
	}
}
