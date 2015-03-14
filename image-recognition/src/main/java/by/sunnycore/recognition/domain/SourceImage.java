package by.sunnycore.recognition.domain;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * 
 * @author Valentine Shukaila
 * 
 */
public class SourceImage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -315890298769543874L;
	private BufferedImage origin;
	/**
	 * the size of the square side in pixels. <br/>
	 * This square is the tracked as one pixel during the recognition process
	 */
	private int regionSize;
	/**
	 * the pixels data of the image. Each pixel has value of the average color
	 * of the pixels in the region square
	 */
	private int[][] data;

	public BufferedImage getOrigin() {
		return origin;
	}

	public int getRegionSize() {
		return regionSize;
	}

	public int[][] getData() {
		return data;
	}

	public void setOrigin(BufferedImage origin) {
		this.origin = origin;
	}

	public void setRegionSize(int regionSize) {
		this.regionSize = regionSize;
	}

	public void setData(int[][] data) {
		this.data = data;
	}
}
