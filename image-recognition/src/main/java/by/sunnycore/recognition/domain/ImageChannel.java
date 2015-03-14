package by.sunnycore.recognition.domain;

import java.awt.image.BufferedImage;

/**
 * composite object that contains the image data and the channel in which this
 * image is represented
 * 
 * @author Valentine Shukaila
 * 
 */
public class ImageChannel {
	private BufferedImage data;
	private int[] pixelData;
	private Channel channel;

	public ImageChannel(BufferedImage data,Channel channel,int[] pixelData) {
		this.data=data;
		this.channel=channel;
		this.pixelData = pixelData;
	}
	
	public BufferedImage getData() {
		return data;
	}

	/**
	 * @return the pixelData
	 */
	public int[] getPixelData() {
		return pixelData;
	}
	
	public Channel getChannel() {
		return channel;
	}
}
