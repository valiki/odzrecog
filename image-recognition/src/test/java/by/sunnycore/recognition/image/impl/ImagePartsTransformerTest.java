package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import by.sunnycore.recognition.image.transformer.ImageTransformer;
import by.sunnycore.recognition.image.transformer.impl.ImagePartsTransformer;
import by.sunnycore.recognition.image.transformer.impl.JAIHistogramNormalizationTransformer;
import by.sunnycore.recognition.test.TestUtil;

public class ImagePartsTransformerTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ImageTransformer transformer = new ImagePartsTransformer(3, 5, new JAIHistogramNormalizationTransformer());
		BufferedImage image = TestUtil.loadImage();
		BufferedImage result = transformer.transform(image);
		TestUtil.saveImageWithNewName(result, "\\.bmp", "_partially_equalized.png");
	}

}
