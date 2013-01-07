package by.sunnycore.recognition.image.histogram;

import java.awt.image.BufferedImage;

public interface IHistogramBuilder {
	int[][] buildHistogram(BufferedImage image);
}
