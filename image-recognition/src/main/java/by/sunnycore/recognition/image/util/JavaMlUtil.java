package by.sunnycore.recognition.image.util;

import java.util.Collection;
import java.util.Iterator;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class JavaMlUtil {
	/**
	 * converts two dimensional array of vectors to the dataset
	 * @param points
	 * @return
	 */
	public static Dataset arrayToDataSet(int[][] points){
		Dataset dataset = new DefaultDataset();
		for(int i=0;i<points[0].length;i++){
			double[] values = new double[points.length];
			for(int j=0;j<points.length;j++){
				values[j]=points[j][i];
			}
			dataset.add(new DenseInstance(values));
		}
		return dataset;
	}
	
	public static int[][] dataSetToArray(Dataset dataset){
		int[][] result = new int[dataset.size()][];
		for(int i=0;i<dataset.size();i++){
			Instance instance = dataset.get(i);
			result[i] = instanceToArray(instance);
		}
		return result;
	}

	public static int[] instanceToArray(Instance instance) {
		Collection<Double> values = instance.values();
		Iterator<Double> iterator = values.iterator();
		int j = 0;
		int[] result = new int[values.size()];
		while(iterator.hasNext()){
			Double next = iterator.next();
			result[j] = (int) next.doubleValue();
			j++;
		}
		return result;
	}
}
