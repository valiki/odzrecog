package by.sunnycore.recognition.image.impl;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import by.sunnycore.recognition.test.TestUtil;

public class JAIHistogramBuilderTest {
	
	@Test
	public void testBuildHistogram() throws IOException, URISyntaxException{
		BufferedImage image = TestUtil.loadImage();
		JAIHistogramBuilder jaiHistogramBuilder = new JAIHistogramBuilder();
		int[][] buildHistogram = jaiHistogramBuilder.buildHistogram(image);
		assertEquals(3, buildHistogram.length);
		assertEquals(buildHistogram[0].length, 256);
		BufferedImage[] result = TestUtil.buildHistogramCharts(buildHistogram);
		TestUtil.saveImageWithNewName(result[0], "\\.bmp", "_histogram_red.png");
		TestUtil.saveImageWithNewName(result[1], "\\.bmp", "_histogram_green.png");
		TestUtil.saveImageWithNewName(result[2], "\\.bmp", "_histogram_blue.png");
	}

	

}
