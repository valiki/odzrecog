package by.sunnycore.recognition.image.splitter.impl;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;

import org.springframework.stereotype.Component;

import by.sunnycore.recognition.domain.Channel;
import by.sunnycore.recognition.domain.ImageChannel;
import by.sunnycore.recognition.image.splitter.ChannelImageSplitter;
import by.sunnycore.recognition.image.util.ImageUtil;
/**
 * splits an RGB image into 3 images where each of them is in only one RGB color component
 * @author Valentine Shukaila
 *
 */
@Component("rgbChannelSplitter")
public class RGBChannelImageSplitter implements ChannelImageSplitter{

	@Override
	public ImageChannel[] split(BufferedImage source) {
		int width = source.getWidth();
		int height = source.getHeight();
		PixelGrabber pixelGrabber = new PixelGrabber(source, 0, 0, width, height, true);
		pixelGrabber.startGrabbing();
		final ColorModel colorModel = pixelGrabber.getColorModel();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		int[] redPixels = ImageUtil.getRedPixels(width, height, colorModel, pixels);
		BufferedImage red = ImageUtil.createImage(redPixels,width,height);
		int[] greenPixels = ImageUtil.getGreenPixels(width, height, colorModel, pixels);
		BufferedImage green = ImageUtil.createImage(greenPixels,width, height);
		int[] bluePixels = ImageUtil.getBluePixels(width, height, colorModel, pixels);
		BufferedImage blue = ImageUtil.createImage(bluePixels,width, height);
		ImageChannel[] result = new ImageChannel[3];
		result[0]=new ImageChannel(red,Channel.RED,redPixels);
		result[1]=new ImageChannel(green, Channel.GREEN,greenPixels);
		result[2]=new ImageChannel(blue, Channel.BLUE,bluePixels);
		return result;
	}
	
}
