package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import by.bsu.nummethods.IFunction;
import by.bsu.nummethods.integrals.simpson.IIntegralCountMethod;
import by.bsu.nummethods.integrals.simpson.TrapeziumIntegralCountMethod;
import by.sunnycore.recognition.image.IImageTransformer;
import by.sunnycore.recognition.image.util.JAIUtil;

/**
 * makes image histogram equalization, Makes image more contrast and removed
 * over dark and over light places
 * 
 * @author Valentine Shukaila
 * 
 */
public class JAIHistogramEqualizationTransformer implements IImageTransformer {

	private static final double MAX_ERROR = 0.000005;

	@Override
	public BufferedImage transform(BufferedImage source) {
		PlanarImage planarImage = JAIUtil.createRGBHistogramImage(source);
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(planarImage);
		Histogram histogram = (Histogram) planarImage.getProperty("histogram");
		// Get the band count.
		int numBands = histogram.getNumBands();
		// Create an equalization CDF.
		float[][] CDFeq = new float[numBands][];
		for (int b = 0; b < numBands; b++) {
			int binCount = histogram.getNumBins(b);
			double h = getCumulativeFunctionValue(175);
			CDFeq[b] = new float[binCount];
			for (int i = 0; i < binCount; i++) {
				if (i!=255) {
					CDFeq[b][i] = (float) (getCumulativeFunctionValue(i+1-100) / h);
				}else{
					CDFeq[b][i] = 1;
				}
			}
		}
		pb.add(CDFeq);
		// Create a histogram-equalized image.
		PlanarImage eq = JAI.create("matchcdf", pb);
		return eq.getAsBufferedImage();
	}
	
	private double getCumulativeFunctionValue(int i){
		double x = (double)i/64;
		IFunction gaussFunction = new IFunction() {
			
			@Override
			public double getYValue(double xValue) {
				return 1/Math.sqrt(2*Math.PI)*Math.exp(-(xValue*xValue)/2);
			}
		};
		IIntegralCountMethod integral = new TrapeziumIntegralCountMethod();
		double countIntegral = integral.countIntegral(-(double)127/64, x, gaussFunction, MAX_ERROR);
		return countIntegral;
	}

}
