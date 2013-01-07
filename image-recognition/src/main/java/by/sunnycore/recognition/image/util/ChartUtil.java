package by.sunnycore.recognition.image.util;

import java.awt.image.BufferedImage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

public class ChartUtil {
	public static BufferedImage buildChart(double[][] data,String title,String xl,String yl,int width,int height,final String type){
		JFreeChart chart = buildChart(data, title, xl, yl,type);
		return chart.createBufferedImage(width, height);
	}

	private static JFreeChart buildChart(double[][] data,final String title,
			String xl, String yl,String type) {
		DefaultXYDataset dataset = new DefaultXYDataset();
		@SuppressWarnings("rawtypes")
		Comparable key = new Comparable() {
			@Override
			public int compareTo(Object o) {
				return 0;
			}
		};
		dataset.addSeries(key, data);
		JFreeChart chart;
		switch (type) {
		case "line":
			chart = ChartFactory.createXYLineChart(
					title, 
					xl, 
					yl, 
					dataset, 
					PlotOrientation.VERTICAL, 
					false, 
					true, 
					false);
			break;
		case "scatter":
			chart = ChartFactory.createScatterPlot(title, xl, yl, dataset, PlotOrientation.VERTICAL, true, true, false);
			break;
		default:
			chart = ChartFactory.createXYLineChart(
					title, 
					xl, 
					yl, 
					dataset, 
					PlotOrientation.VERTICAL, 
					false, 
					true, 
					false);
			break;
		}
		
		return chart;
	}
}
