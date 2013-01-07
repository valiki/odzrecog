package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.junit.Test;

import by.sunnycore.recognition.image.filter.EqualRGBImageFilter;
import by.sunnycore.recognition.image.filter.IRawImageFilter;
import by.sunnycore.recognition.image.util.ImageUtil;

public class EqualRGBImageFilterTest {
	
	@Test
	public void test() throws IOException{
		File parent = new File("H:/CAPTCHAS");
		File[] children = parent.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		final IRawImageFilter imageFilter = new EqualRGBImageFilter();
		int counter = 0;
		for(File child:children){
			File[] images = child.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".png");
				}
			});
			for(File image:images){
				BufferedImage img = ImageUtil.loadImage(image);
				int[][] imagePixels = ImageUtil.imageTORGBArray(img);

				int[][] filteredPixels = imageFilter.filter(imagePixels);
				int[] pixels = ImageUtil.rgbToOneDImensionalArray(
								filteredPixels[0], 
								filteredPixels[1], 
								filteredPixels[2]);
				BufferedImage filteredImage = ImageUtil.createImage(pixels, img.getWidth(), img.getHeight());
				ImageUtil.savePNGImage(filteredImage, image);
				counter++;
				System.out.println(counter+" images processed. ");
			}
		}
	}
}
