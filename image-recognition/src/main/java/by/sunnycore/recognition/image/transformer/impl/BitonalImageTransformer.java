package by.sunnycore.recognition.image.transformer.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import by.sunnycore.recognition.image.transformer.ImageTransformer;


public class BitonalImageTransformer implements ImageTransformer{

	@Override
	public BufferedImage transform(BufferedImage source) {
		BufferedImage im =  new BufferedImage(source.getWidth(),source.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g2d = im.createGraphics();
		g2d.drawImage(source,0,0,null); 
		return im;
	}

}
