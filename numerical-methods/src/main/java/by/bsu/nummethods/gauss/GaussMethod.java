package by.bsu.nummethods.gauss;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JOptionPane;
import javax.swing.JTable;
/**
 * 
 * @author Valiantsin Shukaila
 *
 */
public class GaussMethod {
/*	private static DecimalFormat f;*/
	private static NumberFormat format;
	/**
	 * 
	 * @param input
	 * @param output
	 * @param roots
	 */
	public static void executeGaussTables4x5(JTable input,JTable output,JTable roots){
		double[][] a = new double[4][5];
		fromTableToMassiv(input, a);
		executeForwardTrace(a);
		fromMassivToTable(a, output);
		double[] x = new double[4];
		executeReverseTrace(a, x);
		fromMassivToTable(x, roots);
	}
	/**
	 * 
	 * @param input
	 * @param roots
	 */
	public static void executeGauss(double[][] input,double[] roots){
		executeForwardTrace(input);
		executeReverseTrace(input, roots);
	}
	/**
	 * 
	 * @param a
	 */
	public static void executeForwardTrace(double[][] a){
		double c;
		for(int i=0;i<4;i++){
			for(int k=i+1;k<3+1;k++){
				for(int m=k;m<3;m++){
					if(a[i][m]>a[i][k]&&m!=k){
						changePlaceOfEquations(a, k, m);
					}
				}
				c=a[k][i]/a[i][i];
				a[k][i]=0;
				for(int j=i+1;j<3+1;j++){
					a[k][j]=a[k][j]-c*a[i][j];
				}
				a[k][4]=a[k][4]-c*a[i][4];
				}
			}
	}
	/**
	 * 
	 * @param a
	 * @param x
	 */
	public static void executeReverseTrace(double[][] a,double[] x){
		x[x.length-1]=a[x.length-1][x.length]/a[x.length-1][x.length-1];
		double s=0;
		for(int k=x.length-2;k>-1;k--){
			s=0;
			for(int i=k+1;i<x.length;i++){
				s=s+x[i]*a[k][i];
			}
			x[k]=(a[k][4]-s)/a[k][k];
		}
	}
	/**
	 * 
	 * @param eqations
	 * @param eq1
	 * @param eq2
	 */
	public static void changePlaceOfEquations(double[][] eqations,int eq1,int eq2){
		double[] promej = eqations[eq2].clone();
		for(int i=0;i<eqations[eq2].length;i++){
			eqations[eq2][i]=eqations[eq1][i];
			eqations[eq1][i]=promej[i];
		}
	}
	/**
	 * 
	 * @return
	 */
	public static NumberFormat getFormatter(){
		if(format==null){
			format = new DecimalFormat("#.####");
			}
		return format;
	}
	/**
	 * 
	 * @param a
	 * @param output
	 */
	public static void fromMassivToTable(double[][] a , JTable output){
		for(int i =0;i<output.getColumnCount();i++){
			for(int j=0;j<output.getRowCount();j++){
					String str = getFormatter().format(a[j][i]);
					str = str.replaceAll("^-0$", "0");
					str = str.replaceAll(",", ".");
					output.setValueAt(str, j, i);
				}
			}
		}
	/**
	 * 
	 * @param a
	 * @param output
	 */
	public  static void fromMassivToTable(double[] a , JTable output){
			for(int j=0;j<output.getColumnCount();j++){
				String str = getFormatter().format(a[j]);
				str = str.replaceAll("^-0$", "0");
				str = str.replaceAll(",", ".");
				output.setValueAt(str, 0, j);
			}
	}
	/**
	 * 
	 * @param input
	 * @param a
	 */
	public static void fromTableToMassiv(JTable input, double[][] a){
		try {
			for(int i =0;i<input.getColumnCount();i++){
				for(int j=0;j<input.getRowCount();j++){
						String str = (String) input.getValueAt(j, i);
						str = str.replaceAll(",", ".");
						a[j][i]=Double.valueOf(str);
				}
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Entered data should be a number");
			e.printStackTrace();
		}
	}

}
