package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import by.bsu.nummethods.IFunctionXY;
import by.bsu.nummethods.gauss.GaussFunction;
import by.sunnycore.recognition.image.IImageFilter;
import by.sunnycore.recognition.image.util.ImageUtil;

public class GaussFilter implements IImageFilter{

	@Override
	public BufferedImage filter(BufferedImage image) {
		int coreSize = 7;
		double sigma = 1.00;
		double[][] GC = gaussFilterCore(coreSize, sigma);
		int width = image.getWidth();
		int height = image.getHeight();
		PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, width, height, true);
		pixelGrabber.startGrabbing();
		int[] pixels = (int[]) pixelGrabber.getPixels();
		int shift = (int) Math.floor(coreSize/2);
		//the number of rows of pixels minus shift
		int[][] matrix = toMatrixForm(pixels, height, width);
		int[][] result = new int[height][width];
		for(int i=shift;i<matrix.length-shift;i++){
			for(int j=shift;j<matrix[i].length-shift;j++){
				int[][] a = subMatrix(matrix, i-shift, i+shift, j-shift, j+shift, width);
				double sum=0;
				for(int x=0;x<=1;x++){
					for(int y=0;y<=1;y++){
						sum+=GC[x][y]*(double)a[x][y];
					}
				}
				result[i][j]=(int)sum;
			}
		}
		int[] linearResult = fromMatrixForm(result, height, width); 
		return ImageUtil.createImage(linearResult, width, height,image.getType());
	}
	
	private int[] fromMatrixForm(int[][] matrix,int height,int width){
		int[] linear = new int[height*width];
		int index = 0;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				linear[index]=matrix[i][j];
				index++;
			}
		}
		return linear;
	}
	
	private int[][] toMatrixForm(int[] src,int height,int width){
		int[][] matrix = new int[height][width];
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				matrix[i][j]=src[i*width+j];
			}
		}
		return matrix;
	}
	
	private int[][] subMatrix(int[][] source,int xfrom,int xto,int yfrom,int yto,int width){
		int[][] result = new int[xto-xfrom][yto-yfrom];
		int w=0;
		int h=0;
		for(int i=xfrom;i<xto;i++){
			w=0;
			for(int j=yfrom;j<yto;j++){
				result[h][w]=source[i][j];
				w++;
			}
			h++;
		}
		return result;
	}
	
	private double[][] gaussFilterCore(int coreSize,double sigma){
		IFunctionXY gaussFunction = new GaussFunction(sigma);
		int shift = (int) Math.floor(coreSize/2);
		double[][] res = new double[coreSize][coreSize];
		for(int i=0;i<coreSize;i++){
			for(int j=0;j<coreSize;j++){
				res[i][j]=gaussFunction.getFuncValue(i-shift, j-shift);
			}
		}
		return res;
	}

}
