package by.sunnycore.recognition.image.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

/**
 * an utility class for image processing
 * 
 * @author Valentine Shukaila
 * 
 */
public class ImageUtil {
	
	private static Logger logger = Logger.getLogger(ImageUtil.class);
	/**
	 * return the red component of the pixel of the image  in java representation than can be summed up with other components to get the summed RGB valued
	 * 
	 * @param pixel
	 *            the pixel number
	 * @param colorModel
	 * @return
	 */
	public static int getRed(final int pixel, final ColorModel colorModel) {
		return colorModel.getRed(pixel);//<<16;
	}

	/**
	 * return the green component of the pixel of the image in java representation than can be summed up with other components to get the summed RGB valued
	 * 
	 * @param pixel
	 *            the pixel number
	 * @param colorModel
	 * @return
	 */
	public static int getGreen(final int pixel, final ColorModel colorModel) {
		return colorModel.getGreen(pixel);//<<8;
	}

	/**
	 * return the blue component of the pixel of the image  in java representation than can be summed up with other components to get the summed RGB valued
	 * 
	 * @param pixel
	 *            the pixel number
	 * @param colorModel
	 * @return
	 */
	public static int getBlue(final int pixel, final ColorModel colorModel) {
		return colorModel.getBlue(pixel);
	}
	
	/**
	 * return the red component of the pixel of the image
	 * 
	 * @param pixel
	 *            the pixel number
	 * @param colorModel
	 * @return
	 */
	public static int getRedRaw(final int pixel, final ColorModel colorModel) {
		return colorModel.getRed(pixel);
	}

	/**
	 * return the green component of the pixel of the image
	 * 
	 * @param pixel
	 *            the pixel number
	 * @param colorModel
	 * @return
	 */
	public static int getGreenRaw(final int pixel, final ColorModel colorModel) {
		return colorModel.getGreen(pixel);
	}

	/**
	 * return the blue component of the pixel of the image
	 * 
	 * @param pixel
	 *            the pixel number
	 * @param colorModel
	 * @return
	 */
	public static int getBlueRaw(final int pixel, final ColorModel colorModel) {
		return colorModel.getBlue(pixel);
	}

	public static int[] getRedPixels(final int width, final int height,
			final ColorModel colorModel,final int[] pixels) {
		int[] result = new int[height * width];
		for (int i = 0; i < result.length; i++) {
			result[i] = getRed(pixels[i], colorModel);
		}
		return result;
	}

	public static int[] getGreenPixels(final int width, final int height,
			final ColorModel colorModel,final int[] pixels) {
		int[] result = new int[height * width];
		for (int i = 0; i < result.length; i++) {
			result[i] = getGreen(pixels[i], colorModel);
		}
		return result;
	}

	public static int[] getBluePixels(final int width, final int height,
			final ColorModel colorModel,final int[] pixels) {
		int[] result = new int[height * width];
		for (int i = 0; i < result.length; i++) {
			result[i] = getBlue(pixels[i], colorModel);
		}
		return result;
	}

	/**
	 * creates image from pixels
	 * 
	 * @param pixels
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage createImage(final int[] pixels, final int width,
			final int height,int type) {
		BufferedImage bufImg = new BufferedImage(width, height, type);
		bufImg.setRGB(0, 0, width, height, pixels, 0, width);
		return bufImg;
	}
	
	/**
	 * creates image from pixels
	 * 
	 * @param pixels
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage createImage(final int[] pixels, final int width,
			final int height) {
		return createImage(pixels, width, height,BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * create image in red RGB channel
	 * 
	 * @param width
	 * @param height
	 * @param colorModel
	 * @return
	 */
	public static BufferedImage createRedImage(final int width, final int height,
			final ColorModel colorModel,final int[] pixels) {
		int[] redPixels = getRedPixels(width, height, colorModel,pixels);
		return createImage(redPixels, width, height);
	}

	/**
	 * create image in green RGB channel
	 * 
	 * @param width
	 * @param height
	 * @param colorModel
	 * @return
	 */
	public static BufferedImage createGreenImage(final int width, final int height,
			final ColorModel colorModel,final int[] pixels) {
		int[] greenPixels = getGreenPixels(width, height, colorModel,pixels);
		return createImage(greenPixels, width, height);
	}

	/**
	 * creates an image in blue RGB channel
	 * 
	 * @param width
	 * @param height
	 * @param colorModel
	 * @return
	 */
	public static BufferedImage createBlueImage(final int width, final int height,
			final ColorModel colorModel,final int[] pixels) {
		int[] bluePixels = getBluePixels(width, height, colorModel,pixels);
		return createImage(bluePixels, width, height);
	}
	
	public static int[] grabPixels(BufferedImage image, boolean forceRGB){
		PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), forceRGB);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		return pixels;
	}
	
	public static int[] grabPixels(BufferedImage image){
		return grabPixels(image, true);
	}
	
	public static int[][] loadImageRBGArray(String path) throws IOException{
		BufferedImage img = loadImage(path);
		int[][] pixelsRGB = imageTORGBArray(img);
		return pixelsRGB;
	}

	public static int[][] imageTORGBArray(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		PixelGrabber pixelGrabber = new PixelGrabber(img, 0, 0, width, height, true);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		ColorModel colorModel = pixelGrabber.getColorModel();
		int[][] pixelsRGB = new int[3][pixels.length];
		for(int i=0;i<pixels.length;i++){
			pixelsRGB[0][i] = colorModel.getRed(pixels[i]);
			pixelsRGB[1][i] = colorModel.getGreen(pixels[i]);
			pixelsRGB[2][i] = colorModel.getBlue(pixels[i]);
		}
		return pixelsRGB;
	}
	
	public static int[][] imageTORGBRawArray(BufferedImage img){
		PixelGrabber pixelGrabber = new PixelGrabber(img, 0, 0, img.getWidth(), img.getHeight(), true);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		final ColorModel colorModel = pixelGrabber.getColorModel();
		int[][] pointsRGB = new int[3][];//create array for the RGB points for clusterization
		int numberOfPixels = pixels.length;
		pointsRGB[0] = new int[numberOfPixels];
		pointsRGB[1] = new int[numberOfPixels];
		pointsRGB[2] = new int[numberOfPixels];
		for (int i=0;i<numberOfPixels;i++) {
			pointsRGB[0][i]=ImageUtil.getRedRaw(pixels[i], colorModel);
			pointsRGB[1][i]=ImageUtil.getGreenRaw(pixels[i], colorModel);
			pointsRGB[2][i]=ImageUtil.getBlue(pixels[i], colorModel);
		}
		return pointsRGB; 
	}
	
	public static BufferedImage createRGBImageFrom3Channels(
			final int width,final int height,
			final BufferedImage red,
			final BufferedImage green,
			final BufferedImage blue){
		int[] redPixels = grabPixels(red);
		int[] greenPixels = grabPixels(green);
		int[] bluePixels = grabPixels(blue);
		return createRGBImageFrom3Channels(width, height, redPixels, greenPixels, bluePixels);
	}
	
	public static BufferedImage createRGBImageFrom3Channels(final int width,final int height,final int[] redPixels,final int[] greenPixels,final int[] bluePixels){
		int[] sumPixels = rgbToOneDImensionalArray(redPixels, greenPixels, bluePixels);
		return createImage(sumPixels, width, height);
	}
	
	public static int[] rgbToOneDImensionalArray(final int[] redPixels,final int[] greenPixels,final int[] bluePixels){
		int[] sumPixels = new int[redPixels.length];
		for(int i=0;i<redPixels.length;i++){
			int red = redPixels[i]<<16;
			int green = greenPixels[i]<<8;
			int blue = bluePixels[i];
			sumPixels[i]=red+green+blue;
		}
		return sumPixels;
	}
	
	
	
	public static BufferedImage loadImage(String path) throws IOException{
		return loadImage(new File(path));
	}
	
	public static BufferedImage loadImage(File file) throws IOException{
		BufferedImage read = null;
		try {
			if(!file.exists()){
				throw new FileNotFoundException(file.getAbsolutePath()+" file was not found");
			}
			read = ImageIO.read(file);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return read;
	}
	
	public static void savePNGImage(BufferedImage image,String path) throws IOException{
		savePNGImage(image, new File(path));
	}
	
	public static void savePNGImage(BufferedImage image,File file) throws IOException{
		saveImage(image, file, "png");
	}
	
	public static void saveImage(BufferedImage image,String path,String format) throws IOException{
		saveImage(image, new File(path), format);
	}
	
	public static void saveImage(BufferedImage image,File file,String format) throws IOException{
		try {
			ImageIO.write(image, format, file);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}
	
	public static BufferedImage createImageFromPanel(JPanel panel,int w,int h){
		JFrame f = new JFrame();
		f.setSize(480, 640);
		f.setContentPane(panel);
		f.setVisible(true);
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    //g.dispose();
	    panel.paint(g);
	    g.dispose();
		try {
			TestUtil.saveImageWithNewName(bi, "\\.bmp", "_chart.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return bi;
	}
	
	public static int[] imageToPixels(BufferedImage source){
		PixelGrabber pixelGrabber = new PixelGrabber(source, 0, 0, source.getWidth(), source.getHeight(), true);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		return pixels;
	}
	
	public static short getPixel(short[] fullArray, int channels, int index, int channelIndex){
		int realIndex = index * channels + channelIndex;
		return fullArray[realIndex];
	}
}
