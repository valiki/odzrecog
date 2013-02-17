package by.sunnycore.recognition.image.transformer.impl;

import java.awt.image.BufferedImage;

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;

import by.sunnycore.recognition.image.transformer.ImageTransformer;


/**
 * processes image resize 
 * 
 * @author val
 *
 */
public class JAIResizeImageTransformer implements ImageTransformer{

	private static final int DEFAULT_MAX_WIDTH = 640;
	private int maxWidth;
	
	public JAIResizeImageTransformer() {
		this(DEFAULT_MAX_WIDTH);
	}
	
	public JAIResizeImageTransformer(int maxWidth){
		this.maxWidth = maxWidth;
	}
	
	@Override
	public BufferedImage transform(BufferedImage source) {
		ParameterBlockJAI pb = new ParameterBlockJAI("scale");
		pb.addSource(source);
		int width = source.getWidth();
		float scale = 1.0f;
		if(width>maxWidth){
			scale = (float)maxWidth/(float)width;
		}else{
			return source;
		}
		pb.setParameter("xScale", scale);
		pb.setParameter("yScale", scale);
		pb.setParameter("interpolation", javax.media.jai.Interpolation.getInstance(Interpolation.INTERP_BICUBIC));
		RenderedOp result = JAI.create("scale", pb);
		return result.getAsBufferedImage();
	}

}
