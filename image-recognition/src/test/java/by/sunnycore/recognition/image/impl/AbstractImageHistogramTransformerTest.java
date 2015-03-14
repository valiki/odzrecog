package by.sunnycore.recognition.image.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import by.sunnycore.recognition.image.histogram.impl.JAIHistogramBuilder;
import by.sunnycore.recognition.image.util.TestUtil;

public abstract class AbstractImageHistogramTransformerTest {
	
	public void buildHistograms(BufferedImage res,String bmpReplaceName) throws IOException {
		JAIHistogramBuilder histogramBuilder = new JAIHistogramBuilder();
		int[][] buildHistogram = histogramBuilder.buildHistogram(res);
		BufferedImage[] result = TestUtil.buildHistogramCharts(buildHistogram);
		String[] colors;
		if (result.length==3) {
			String[] c={"Red","Green","Blue"};
			colors = c;
		}else{
			String[] c={"Black","White"};
			colors = c;
		}
		for(int i=0;i<result.length;i++){
			TestUtil.saveImageWithNewName(result[i], "\\.bmp", bmpReplaceName+"_histogram"+colors[i]+".png");
		}
	}
}
