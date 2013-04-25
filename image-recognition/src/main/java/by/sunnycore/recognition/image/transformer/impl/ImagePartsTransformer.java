package by.sunnycore.recognition.image.transformer.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import by.sunnycore.recognition.image.transformer.ImageTransformer;
import by.sunnycore.recognition.image.util.ImageUtil;
import by.sunnycore.recognition.test.TestUtil;

/**
 * Image Transformer that splits image into parts and transform each part
 * separatelly using constructor argument transformer
 * 
 * @author Val
 * 
 */
public class ImagePartsTransformer implements ImageTransformer {

	private ImageTransformer imageTransformer;
	private int widthDivider;
	private int heightDivider;

	/**
	 * 
	 * @param widthDivider
	 *            defines the width of the part. The width of each part will be
	 *            width/widthDivider
	 * @param heightDivider
	 *            defines the height of the part. The height of each part will
	 *            be height/heightDivider
	 * @param transformer
	 *            the transformer that will be used to transform each part
	 */
	public ImagePartsTransformer(int widthDivider, int heightDivider,
			ImageTransformer transformer) {
		this.widthDivider = widthDivider;
		this.heightDivider = heightDivider;
		this.imageTransformer = transformer;
	}

	@Override
	public BufferedImage transform(BufferedImage source) {
		int[] pixels = ImageUtil.imageToPixels(source);
		int width = source.getWidth();
		int height = source.getHeight();
		int[] widths = initDimensionParts(width, widthDivider);
		int[] heights = initDimensionParts(height, heightDivider);
		int startPixel = 0;
		for(int i=0;i<heightDivider;i++){
			for(int j=0;j<widthDivider;j++){
				int partWidth = widths[j];
				int imageSize = partWidth*heights[i];
				//create array of pixels that will contain only part of the image
				int[] imagePart = new int[imageSize];
				int localStartPixel = startPixel;
				for(int k=0;k<imagePart.length;k++){
					int pixel = localStartPixel+k%partWidth;
					if(k%partWidth==0 && k!=0){
						//if we reached side of the main image go to the next row
						localStartPixel = localStartPixel+(width);
					}
					imagePart[k]=pixels[pixel];
				}
				//create image from part of pixels and transofrm it
				BufferedImage partialImage = ImageUtil.createImage(imagePart, widths[j], heights[i]);
/*				writePartImage(i, j, partialImage);*/
				BufferedImage resultPart = imageTransformer.transform(partialImage);
				int[] resultPixels = ImageUtil.imageToPixels(resultPart);
				//put the pixels back to the image
				localStartPixel = startPixel;
				for(int k=0;k<resultPixels.length;k++){
					int pixel = localStartPixel+k%partWidth;
					if(k%partWidth==0 && k!=0){
						//if we reached side of the main image go to the next row
						localStartPixel = localStartPixel+(width);
					}
					pixels[pixel]=resultPixels[k];
				}
				startPixel = startPixel+widths[j];
			}
			startPixel = startPixel-width+(heights[i]*width);
		}
		return ImageUtil.createImage(pixels, width, width);
	}

	private void writePartImage(int i, int j, BufferedImage partialImage) {
		try {
			TestUtil.saveImageWithNewName(partialImage, "48", "-part-"+j+""+i+".png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the array that contains part's dimension, height or width
	 * This is created to avoid problems with rounding when dividing height or
	 * width on the number of parts
	 * 
	 * @param dimensionSize
	 * @param divider
	 * @return
	 */
	private int[] initDimensionParts(int dimensionSize, int divider) {
		int heightsSum = 0;
		int[] dimensionParts = new int[divider];
		int partHeight = dimensionSize / divider;
		for (int i = 0; i < divider - 1; i++) {
			dimensionParts[i] = partHeight;
			heightsSum += partHeight;
		}
		dimensionParts[divider - 1] = dimensionSize - heightsSum;
		return dimensionParts;
	}

	public ImageTransformer getImageTransformer() {
		return imageTransformer;
	}

	public int getWidthDivider() {
		return widthDivider;
	}

	public int getHeigthDivider() {
		return heightDivider;
	}
	
}
