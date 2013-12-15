package by.sunnycore.recognition.image.util;

import java.awt.image.BufferedImage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

public class ChartUtil {
	public static BufferedImage buildChart(double[][] data,String title,String xl,String yl,int width,int height,final String type){
		JFreeChart chart = buildChart(title, xl, yl,type,new String[]{"Chart"},data);
		return chart.createBufferedImage(width, height);
	}
	
	public static JFreeChart buildChart(final String title,
			String xl, String yl,String type,String[] titles,double[][]... data) {
		DefaultXYDataset dataset = new DefaultXYDataset();
		for(int i=0;i<data.length;i++){
			double[][] series = data[i];
			dataset.addSeries(titles[i], series);
		}
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
