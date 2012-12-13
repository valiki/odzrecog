package by.bsu.nummethods.methods;

import javax.swing.JTable;
/**
 * 
 * @author Valiantsin Shukaila
 *
 */
public class TableMassivConverter {
public static double[] TableRowToMassiv(JTable table,int rowNumber){
	double[] res = new double[table.getColumnCount()];
	for(int i=0;i<table.getColumnCount();i++){
		String str = (String) table.getValueAt(rowNumber, i);
		res[i]= Double.valueOf(str);
	}
	return res;
}
}
