package by.sunnycore.recognition.image.filter.impl;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import by.sunnycore.recognition.image.filter.ImageFilter;


public class RGBToGrayImageFilter implements ImageFilter{

	@Override
	public BufferedImage filter(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);  
		Graphics g = grayImage.getGraphics();  
		g.drawImage(image, 0, 0, null);  
		g.dispose(); 
		return grayImage;
	}

}
