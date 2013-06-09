package by.sunnycore.recognition.image.transformer.impl;

import by.sunnycore.recognition.image.transformer.DataTransformer;

/**
 * transforms array of pixels represented by RGB model into HSV model
 * 
 * @author Valiantsin Shukaila
 * 
 */
public class RGBToHSVDataTransformer implements DataTransformer {
	
	private static final int MULTIPLIER = 100;
	
	@Override
	public short[][] transform(short[][] data) {
		short[][] result = new short[data.length][data[0].length];
		for(int i=0;i<data[0].length;i++){
			double r = (double) data[0][i]/255;
			double g = (double) data[1][i]/255;
			double b = (double) data[2][i]/255;
			double[] minMax = countMinMax(r, g, b);
			double min = minMax[0];
			double max = minMax[1];
			double h = rgbToH(r, g, b, min, max)*MULTIPLIER;
			double s = rgbToS(r, g, b, min, max)*MULTIPLIER;
			double v = rgbToV(r, g, b, min, max)*MULTIPLIER;
			result[0][i]=(short) Math.round(h);
			result[1][i]=(short) Math.round(s);
			result[2][i]=(short) Math.round(v);
		}
		return result;
	}
	
	private double rgbToH(double r,double g,double b, double min,double max){
		double h = 0;
		if(min==max){
			return 0;
		} else {
			double maxMinusMin = (double) (max-min);
			if(max==r && g>=b){
				double gb = (double) (g-b);
				h = (double)((60)*gb)/(double)maxMinusMin;
			}else
			if(max==r && g<b){
				double gb = (double) (g-b);
				h = (double)((60)*gb)/(double)maxMinusMin+360;
			}else
			if(max==g){
				h = (double)((60)*(b-r))/(double)maxMinusMin+120;
			}else
			if(max==b){
				h = (double)((60)*(r-g))/(double)maxMinusMin+240;
			}
		}
		return (double) (h%(360));
	}

	private double[] countMinMax(double r, double g, double b) {
		double max = r;
		double min = r;
		double[] bins = new double[]{r,g,b};
		for(int i=0;i<bins.length;i++){
			if(bins[i]>max){
				max = bins[i];
			}else
			if(bins[i]<min){
				min = bins[i];
			}
		}
		double[] minMax = new double[]{min,max};
		return minMax;
	}
	
	private double rgbToS(double r,double g,double b, double min, double max){
		if(max==0){
			return 0;
		}else{
			return ((1f-(double)min/(double)max)*100);
		}
	}
	
	private double rgbToV(double r,double g,double b,double min,double max){
		return ((double)max*100);
	}

}
