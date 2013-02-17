package by.sunnycore.recognition.image.filter.impl;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.MedianFilterDescriptor;
import javax.media.jai.operator.MedianFilterShape;

import by.sunnycore.recognition.image.filter.ImageFilter;


public class JAIMedianFilter implements ImageFilter {

	private static final int DEFAULT_CORE_SIZE = 5;
	private int medianFIlterCoreSize;
	private MedianFilterShape coreShape;
	
	
	public JAIMedianFilter() {
		this(DEFAULT_CORE_SIZE);
	}

	public JAIMedianFilter(int coreSize) {
		this(coreSize,MedianFilterDescriptor.MEDIAN_MASK_SQUARE);
	}
	
	public JAIMedianFilter(int coreSize,MedianFilterShape coreShape) {
		this.medianFIlterCoreSize = coreSize;
		this.coreShape = coreShape;
	}

	@Override
	public BufferedImage filter(BufferedImage image) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(coreShape);
		pb.add(medianFIlterCoreSize);
		RenderedOp result = JAI.create("MedianFilter", pb);
		return result.getAsBufferedImage();
	}

}
