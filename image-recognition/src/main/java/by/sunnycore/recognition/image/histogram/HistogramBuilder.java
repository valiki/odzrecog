package by.sunnycore.recognition.image.histogram;

import java.awt.image.BufferedImage;

public interface HistogramBuilder {
	int[][] buildHistogram(BufferedImage image);
}
