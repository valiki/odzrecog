package by.sunnycore.recognition.image.transformer;

/**
 * Transforms data array to some other representation of this data.
 * 
 * @author Val
 * 
 */
public interface DataTransformer {
	/**
	 * 
	 * @param data
	 *            multidimensional array of pixels where first dimension defines
	 *            bin number and second pixel number
	 * @return
	 */
	short[][] transform(short[][] data);
}
