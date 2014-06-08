package by.sunnycore.recognition.image.transformer.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;

import by.sunnycore.recognition.image.transformer.ImageTransformer;
/**
 * Transforms all white pixels to black
 * 
 * @author Val
 *
 */
public class InvertColorsImageTransformer implements ImageTransformer{

	@Override
	public BufferedImage transform(BufferedImage source) {
		BufferedImage output = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                int rgba = source.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                                255 - col.getGreen(),
                                255 - col.getBlue());
                output.setRGB(x, y, col.getRGB());
            }
        }
		return output;
	}

}
