package by.sunnycore.recognition.image;

import java.awt.image.BufferedImage;

public interface IHistogramBuilder {
	int[][] buildHistogram(BufferedImage image);
}
