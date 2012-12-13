package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

import by.sunnycore.recognition.image.IImageTransformer;
import by.sunnycore.recognition.image.util.JAIUtil;

public class JAIHistogramNormalizationTransformer implements IImageTransformer {

	@Override
	public BufferedImage transform(BufferedImage source) {
		PlanarImage planarImage = JAIUtil.createRGBHistogramImage(source);
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(planarImage);
		Histogram histogram = (Histogram) planarImage.getProperty("histogram");
		// Get the band count.
		int numBands = histogram.getNumBands();
		// Create a normalization CDF.
		double[] mean = new double[] { 128.0, 128.0, 128.0 };
		double[] stDev = new double[] { 64.0, 64.0, 64.0 };
		float[][] _CDFnorm = new float[numBands][];
		for (int b = 0; b < numBands; b++) {
			int binCount = histogram.getNumBins(b);
			_CDFnorm[b] = new float[binCount];
			double mu = mean[b];
			double twoSigmaSquared = 2.0 * stDev[b] * stDev[b];
			_CDFnorm[b][0] = (float) Math.exp(-mu * mu / twoSigmaSquared);
			for (int i = 1; i < binCount; i++) {
				double deviation = i - mu;
				_CDFnorm[b][i] = _CDFnorm[b][i - 1]
						+ (float) Math.exp(-deviation * deviation
								/ twoSigmaSquared);
			}
		}
		for (int b = 0; b < numBands; b++) {
			int binCount = histogram.getNumBins(b);
			double CDFnormLast = _CDFnorm[b][binCount - 1];
			for (int i = 0; i < binCount; i++) {
				_CDFnorm[b][i] /= CDFnormLast;
			}
		}
		pb.add(_CDFnorm);
		// Create a histogram-normalized image.
		RenderedOp nm = JAI.create("matchcdf", pb);
		return nm.getAsBufferedImage();
	}

}
