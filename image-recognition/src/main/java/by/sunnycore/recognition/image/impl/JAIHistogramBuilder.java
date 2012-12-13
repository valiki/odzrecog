package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;

import javax.media.jai.Histogram;
import javax.media.jai.PlanarImage;

import org.springframework.stereotype.Component;

import by.sunnycore.recognition.image.IHistogramBuilder;
import by.sunnycore.recognition.image.util.JAIUtil;
/**
 * builds histogram from the image for the visualization
 * 
 * @author Valentine Shukaila
 *
 */
@Component("jaiHistogramBuilder")
public class JAIHistogramBuilder implements IHistogramBuilder{

	@Override
	public int[][] buildHistogram(BufferedImage image) {
		PlanarImage op = JAIUtil.createRGBHistogramImage(image);
        Histogram histogram = (Histogram) op.getProperty("histogram");
        return histogram.getBins();
	}

}
