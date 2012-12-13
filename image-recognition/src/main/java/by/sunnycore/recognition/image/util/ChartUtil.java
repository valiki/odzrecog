package by.sunnycore.recognition.image.util;

import java.awt.image.BufferedImage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

public class ChartUtil {
	public static BufferedImage buildChart(double[][] data,String title,String xl,String yl,int width,int height){
		JFreeChart chart = buildChart(data, title, xl, yl);
		return chart.createBufferedImage(width, height);
	}

	@SuppressWarnings("unchecked")
	private static JFreeChart buildChart(double[][] data, String title,
			String xl, String yl) {
		DefaultXYDataset dataset = new DefaultXYDataset();
		Comparable key = new Comparable() {
			@Override
			public int compareTo(Object o) {
				return 0;
			}
		};
		dataset.addSeries(key, data);
		JFreeChart chart = ChartFactory.createXYLineChart(
				title, 
				xl, 
				yl, 
				dataset, 
				PlotOrientation.VERTICAL, 
				false, 
				true, 
				false);
		return chart;
	}
}
