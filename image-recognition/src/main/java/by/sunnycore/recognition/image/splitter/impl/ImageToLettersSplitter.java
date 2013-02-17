package by.sunnycore.recognition.image.splitter.impl;

import java.util.ArrayList;
import java.util.List;

import by.sunnycore.recognition.image.splitter.RawImageSplitter;


public class ImageToLettersSplitter implements RawImageSplitter {

	private static final int COLORED = 255;

	private static class Splitted {
		List<int[][]> images;
	}

	@Override
	public int[][][] split(int[][] image, int width) {
		int[] r = image[0];
		// create the plain XY representation of the red channel
		int[][] rXY = new int[width][r.length / width];
		for (int i = 0; i < r.length; i++) {
			rXY[i % width][(int) Math.ceil(i / width)] = r[i];
		}
		Splitted splitted = splitXYImage(rXY);
		return null;
	}

	private Splitted splitXYImage(int[][] xyImage) {
		Splitted result = new Splitted();
		result.images = new ArrayList<int[][]>();
		int x, y;
		x = 1;
		y = 1;
		int height = xyImage[x].length - 1;
		int width = xyImage.length - 1;
		boolean foundBorder = false;
		List<Integer> borderXs = new ArrayList<Integer>();
		a: for (int i = 0; i < xyImage.length; i++) {
			boolean foundBorderInY = false;
			b: for (int j = 0; j < xyImage[i].length; j++) {
				int pixel = xyImage[i][j];
				if (pixel == COLORED) {
					foundBorder = true;
					foundBorderInY = true;
					continue a;
				}
				if (!foundBorderInY && foundBorder) {
					//then this is vertical line dividing 2 images
					borderXs.add(i);
					foundBorder = false;
					continue a;
				}
			}
		}
		int prevX = 0;
		for(Integer borderX:borderXs){
			int border = borderX+1;
			int[][] image = new int[border][height];
			System.arraycopy(xyImage, prevX, image, 0, border);
			prevX = border;
		}
		return result;
	}

	private int[] findNextPixelXY(int[][] xyImage, int x, int y) {
		int pixelXm1 = xyImage[x - 1][y];
		int pixelXp1 = xyImage[x + 1][y];
		int pixelYp1 = xyImage[x][y + 1];
		int pixelXYp1 = xyImage[x + 1][y + 1];
		int pixelXm1Yp1 = xyImage[x - 1][y + 1];
		if (pixelYp1 == COLORED) {
			return new int[] { x, y + 1 };
		} else if (pixelXm1Yp1 == COLORED) {
			return new int[] { x - 1, y + 1 };
		} else if (pixelXYp1 == COLORED) {
			return new int[] { x + 1, y + 1 };
		} else if (pixelXm1 == COLORED) {
			return new int[] { x - 1, y };
		} else if (pixelXp1 == COLORED) {
			return new int[] { x + 1, y };
		} else {
			return null;
		}
	}
}
