package by.sunnycore.recognition.image.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

public class ImageUtilTest {
	private static final int TEST_IMAGE_HEIGHT = 1707;
	private static final int TEST_IMAGE_WIDTH = 1144;
	private static final String IMAGE_PATH = "images/0020.bmp";

	public void testLoadImage() throws IOException, URISyntaxException {
		BufferedImage image = loadImage();
		assertNotNull(image);
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		assertNotNull(width);
		assertTrue(width != 0 && width != -1);
		assertTrue(height != 0 && height != -1);
		assertEquals(TEST_IMAGE_WIDTH, width);
		assertEquals(TEST_IMAGE_HEIGHT, height);
	}

	private BufferedImage loadImage() throws IOException, URISyntaxException {
		URL resource = this.getClass().getClassLoader().getResource(IMAGE_PATH);
		BufferedImage image = ImageUtil.loadImage(new File(resource.toURI()));
		return image;
	}

	public void testSaveImage() throws IOException, URISyntaxException {
		BufferedImage image = loadImage();
		String path = TestUtil.getImagePathString();
		path = path.replaceAll("\\.bmp", "_result.png");
		ImageUtil.saveImage((BufferedImage) image, new File(path), "png");
		Image resultImage = ImageUtil.loadImage(new File(path));
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		assertNotNull(resultImage);
		assertTrue(width != 0 && width != -1);
		assertTrue(height != 0 && height != -1);
		assertEquals(TEST_IMAGE_WIDTH, width);
		assertEquals(TEST_IMAGE_HEIGHT, height);
	}

	@Test
	public void testCreateRedImage() throws IOException, URISyntaxException{
		BufferedImage image = loadImage();
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, width, height, true);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		final ColorModel colorModel = pixelGrabber.getColorModel();
		BufferedImage redImage = ImageUtil.createRedImage(width, height, colorModel,pixels);
		String path = TestUtil.getImagePathString();
		String redPath = path.replaceAll("\\.bmp", "_red.png");
		ImageUtil.saveImage(redImage, new File(redPath), "png");
		BufferedImage greenImage = ImageUtil.createGreenImage(width, height, colorModel,pixels);
		String greenPath = path.replaceAll("\\.bmp", "_green.png");
		ImageUtil.saveImage(greenImage, new File(greenPath), "png");
		BufferedImage blueImage = ImageUtil.createBlueImage(width, height, colorModel,pixels);
		String bluePath = path.replaceAll("\\.bmp", "_blue.png");
		ImageUtil.saveImage(blueImage, new File(bluePath), "png");
		BufferedImage combinedImage = ImageUtil.createRGBImageFrom3Channels(width, height, redImage, greenImage, blueImage);
		String combinedPath = path.replaceAll("\\.bmp", "combined.png");
		ImageUtil.saveImage(combinedImage, new File(combinedPath), "png");
	}
	
}
