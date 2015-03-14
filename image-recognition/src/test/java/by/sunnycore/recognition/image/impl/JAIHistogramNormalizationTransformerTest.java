package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import by.sunnycore.recognition.image.transformer.impl.JAIHistogramNormalizationTransformer;
import by.sunnycore.recognition.image.util.TestUtil;

public class JAIHistogramNormalizationTransformerTest extends AbstractImageHistogramTransformerTest{

	@Test
	public void testTransform() throws IOException{
		JAIHistogramNormalizationTransformer transformer = new JAIHistogramNormalizationTransformer();
		BufferedImage image = TestUtil.loadImage();
		BufferedImage res = transformer.transform(image);
		String name = "_normalized";
		TestUtil.saveImageWithNewName(res, "\\.bmp", name+".png");
		buildHistograms(res, name);
	}
	
}
