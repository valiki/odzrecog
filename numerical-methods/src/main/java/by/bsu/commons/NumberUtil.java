package by.bsu.commons;

public class NumberUtil {

	public static final int DEFAULT_PRECITION = 6;

	/**
	 * returns true or false whrher two double value differ no more that
	 * 0.1*default_precition which is equal to 6.
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public static boolean equalsDouble(double one, double two) {
		return equalsDouble(one, two, DEFAULT_PRECITION);
	}

	/**
	 * returns true or false whrher two double value differ no more that
	 * 0.1*precition
	 * 
	 * @param one
	 * @param two
	 * @param precition
	 * @return
	 */
	public static boolean equalsDouble(double one, double two, int precition) {
		return Math.abs(one - two) < Math.pow(10, -precition);
	}
	
	/**
	 * defines whether the entered double is equal to zero
	 * @param one
	 * @return
	 */
	public static boolean equalsZero(double one){
		return equalsDouble(one, 0.0);
	}
}
