package by.sunnycore.recognition.domain;

import java.io.Serializable;

public class RecorgnizedObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7553563897879514822L;
	/**
	 * actual pixels of the object
	 */
	private int[] pixels;
	/**
	 * the width of the object in pixels
	 */
	private int width;
	/**
	 * the height of the object in pixels
	 */
	private int height;
	/**
	 * the x coordinate of the object top left pixel on the original image
	 */
	private int x;
	/**
	 * the y coordinate of the object top left pixel on the original image
	 */
	private int y;
	/**
	 * the source image where the object was recognized
	 */
	private SourceImage image;
	/**
	 * the border of the object.<br/>
	 * The coordinates of each pixel of the border<br/>
	 * the first dimension of the array is x coordinate, the second is y
	 * coordinate
	 */
	private int[][] objectBorder;
	
	/**
	 * the cluster to which the object correspond
	 */
	private ObjectCluster cluster;

	public int[] getPixels() {
		return pixels;
	}

	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public SourceImage getImage() {
		return image;
	}

	public int[][] getObjectBorder() {
		return objectBorder;
	}

	public ObjectCluster getCluster() {
		return cluster;
	}

	public void setImage(SourceImage image) {
		this.image = image;
	}

	public void setObjectBorder(int[][] objectBorder) {
		this.objectBorder = objectBorder;
	}

	public void setCluster(ObjectCluster cluster) {
		this.cluster = cluster;
	}
}
