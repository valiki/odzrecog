package by.sunnycore.recognition.image.filter.impl;

import by.sunnycore.recognition.image.filter.RawImageFilter;


public class EqualRGBImageFilter implements RawImageFilter{

	@Override
	public int[][] filter(int[][] rgbArray) {
		for(int i=0;i<rgbArray[0].length;i++){
			if(rgbArray[0][i]!=rgbArray[1][i] || 
			   rgbArray[1][i]!=rgbArray[2][i] || 
			   rgbArray[0][i]!=rgbArray[2][i]){
				rgbArray[0][i]=255;
				rgbArray[1][i]=255;
				rgbArray[2][i]=255;
			}else{
				rgbArray[0][i]=0;
				rgbArray[1][i]=0;
				rgbArray[2][i]=0;
			}
		}
		return rgbArray;
	}

}
