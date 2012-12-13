package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import by.sunnycore.recognition.image.IImageTransformer;
import by.sunnycore.recognition.image.util.JAIUtil;

public class JAIHistogramPiecewiseLinearMappingTransformer implements IImageTransformer {

	@Override
	public BufferedImage transform(BufferedImage source) {
		PlanarImage planarImage = JAIUtil.createRGBHistogramImage(source);
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(planarImage);
		Histogram histogram = (Histogram) planarImage.getProperty("histogram");
		// Get the band count.
		int numBands = histogram.getNumBands();
		// Create a piecewise-mapped image emphasizing low values.
		float[][][] bp = new float[numBands][2][];
		for (int b = 0; b < numBands; b++) {
			bp[b][0] = new float[] { 0.0F, 12.5F, 32.0F, 64.0F, 255.0F };
			bp[b][1] = new float[] { 0.0F, 32.0F, 64.0F, 112.0F, 255.0F };
		}
		pb.add(bp);
		// Create a histogram-equalized image.
		PlanarImage eq = JAI.create("piecewise", pb);
		return eq.getAsBufferedImage();
	}

}
