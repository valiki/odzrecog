package by.sunnycore.recognition.image.transformer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class BitonalImageTransformer implements IImageTransformer{

	@Override
	public BufferedImage transform(BufferedImage source) {
		BufferedImage im =  new BufferedImage(source.getWidth(),source.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g2d = im.createGraphics();
		g2d.drawImage(source,0,0,null); 
		return im;
	}

}
