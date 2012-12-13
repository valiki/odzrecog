package by.bsu.nummethods.methods;

import javax.swing.JTable;

import org.apache.log4j.Logger;

/**
 * 
 * @author Valiantsin Shukaila
 * XXX: this is something awful=))
 */
public class NewtonMethod {
	private static Logger logger = Logger.getLogger(NewtonMethod.class);
	/**
	 * 
	 */
	private NewtonMethod() {
	};

	/**
	 * 
	 * @param pogreshn
	 * @param xlev
	 * @param xprav
	 * @param results
	 * @param canvas
	 */
	public static void executeNewTon(double pogreshnost, double Xlev,
			double Xprav, JTable results) {
		try {
			double result;

			result = 0.7;
			do {
				result = result - myEquation(result) / proizvodnaya(result);
			} while (Math.abs(myEquation(result)) > pogreshnost);
			results.setValueAt(result, 0, 0);

			result = 1.4;
			do {
				result = result - myEquation(result) / proizvodnaya(result);
			} while (Math.abs(myEquation(result)) > pogreshnost);
			results.setValueAt(result, 0, 1);
			result = 2.1;
			do {
				result = result - myEquation(result) / proizvodnaya(result);
			} while (Math.abs(myEquation(result)) > pogreshnost);
			results.setValueAt(result, 0, 2);
		} catch (NumberFormatException e) {
			logger.error("entered data should be a nuber", e);
		} catch (Exception e) {
			logger.error("Oooh", e);
		}
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public static double myEquation(double x) {
		return (10 / x * Math.sin(4 * x) - 5 * Math.log(x) + 2);
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	public static double proizvodnaya(double x) {
		return (-10 / Math.pow(x, 2) * Math.sin(4 * x) + 40 / x
				* Math.cos(4 * x) - 5 / x);
	}

	/**
	 * 
	 * @param Xprav
	 * @param Xlev
	 * @return
	 */
	public static double nacalnoePriblijenie(double Xprav, double Xlev) {
		double shag = 0.1;
		double pogreshn = 0.1;
		double result = Xlev;
		do {
			result += shag;
		} while (Math.abs(myEquation(result)) > pogreshn);
		return result;
	}

}
