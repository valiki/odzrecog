package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.PlanarImage;

import by.sunnycore.recognition.image.IImageFilter;

/**
 * adds borders to the objects on the image
 * 
 * @author Valentine Shukaila
 * 
 */
public class JAIFreiChenEdgeImageFilter implements IImageFilter {

	@Override
	public BufferedImage filter(BufferedImage source) {
		float[] freichen_h_data = { 1.0F, 0.0F, -1.0F, 1.414F, 0.0F, -1.414F,
				1.0F, 0.0F, -1.0F };
		float[] freichen_v_data = { -1.0F, -1.414F, -1.0F, 0.0F, 0.0F, 0.0F,
				1.0F, 1.414F, 1.0F };

		KernelJAI kern_h = new KernelJAI(3, 3, freichen_h_data);
		KernelJAI kern_v = new KernelJAI(3, 3, freichen_v_data);

		ParameterBlock pb = new ParameterBlock();
		pb.addSource(source);
		pb.add(kern_h);
		pb.add(kern_v);
		// Create the Gradient operation.
		PlanarImage im1 = (PlanarImage) JAI.create("gradientmagnitude", pb);
		return im1.getAsBufferedImage();
	}

}
