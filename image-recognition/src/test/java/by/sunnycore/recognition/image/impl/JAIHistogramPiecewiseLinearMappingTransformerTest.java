package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

import by.sunnycore.recognition.test.TestUtil;

public class JAIHistogramPiecewiseLinearMappingTransformerTest extends AbstractImageHistogramTransformerTest{
	@Test
	public void testTransform() throws IOException{
		BufferedImage image = TestUtil.loadImage();
		JAIHistogramPiecewiseLinearMappingTransformer transformer = new JAIHistogramPiecewiseLinearMappingTransformer();
		BufferedImage res = transformer.transform(image);
		String name = "_picewise";
		TestUtil.saveImageWithNewName(res, "\\.bmp", name+".png");
		buildHistograms(res, name);
	}
}
