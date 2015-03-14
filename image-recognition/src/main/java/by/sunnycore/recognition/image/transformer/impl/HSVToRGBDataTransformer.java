package by.sunnycore.recognition.image.transformer.impl;

import by.sunnycore.recognition.image.transformer.DataTransformer;

public class HSVToRGBDataTransformer implements DataTransformer{
	
	private static final int MULTIPLIER = 100;

	@Override
	public short[][] transform(short[][] data) {
		short[][] result = new short[data.length][data[0].length];
		for(int i=0;i<data[0].length;i++){
			short h = (short) data[0][i];
			short s = (short) data[1][i];
			short v = (short) (data[2][i]);
			short hi = (short)(h/(60*MULTIPLIER));
			double vmin = (double) ((100*MULTIPLIER-s)*v/(100*MULTIPLIER));
			double a = (double) ((v-vmin)*(h%60*MULTIPLIER)/(60*MULTIPLIER));
			double vinc = (vmin+a);
			double vdec = (v-a);
			double r = 0;
			double g = 0;
			double b = 0;
			if(hi==0){
				r = v;
				g = vinc;
				b = vmin;
			}else
			if(hi==1){
				r = vdec;
				g = v;
				b = vmin;
			}else
			if(hi==2){
				r = vmin;
				g = v;
				b = vinc;
			}else
			if(hi==3){
				r = vmin;
				g = vdec;
				b = v;
			}else
			if(hi==4){
				r = vinc;
				g = vmin;
				b = v;
			}else
			if(hi==5){
				r = v;
				g = vmin;
				b = vdec;
			}
			if(r<0 || g<0 || b<0){
				System.out.println("aaa");
			}
			result[0][i]=(short) Math.round((r/(100*MULTIPLIER)*255));
			result[1][i]=(short) Math.round(g/(100*MULTIPLIER)*255);
			result[2][i]=(short) Math.round(b/(100*MULTIPLIER)*255);
		}
		return result;
	}
	
}
