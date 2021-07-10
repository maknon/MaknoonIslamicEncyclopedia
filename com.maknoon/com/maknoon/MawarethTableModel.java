package com.maknoon;

import javax.swing.table.*;
import java.util.Vector;

class MawarethTableModel extends AbstractTableModel
{
	// Columns Number.
	private static final int INHERITORS_COL = 0;
	private static final int INHERITORS_SHARE_COL = 1;
	private static final int INHERITORS_COUNT_COL = 2;
	private static final int INHERITORS_AMOUNT_COL = 3;
	private static final int INHERITORS_MOWQOF_COL = 4;

	// Names of the columns
	private final String[] colNames = new String[5];

	// Types of the columns.
	private final Class[] colTypes = {String.class, String.class, String.class, String.class, String.class};

	// Store the data
	private final Vector<ResultsData> dataVector;

	// Constructor
	MawarethTableModel(final Vector<ResultsData> providedDataVector, final String headerTitle_0, final String headerTitle_1, final String headerTitle_2, final String headerTitle_3, final String headerTitle_4)
	{
		//Names of the columns
		colNames[0] = headerTitle_0;
		colNames[1] = headerTitle_1;
		colNames[2] = headerTitle_2;
		colNames[3] = headerTitle_3;
		colNames[4] = headerTitle_4;

		//store the data
		dataVector = providedDataVector;
	}

	/**
	 * getColumnCount
	 * Number columns same as the column array length
	 */
	public int getColumnCount()
	{
		return colNames.length;
	}

	/**
	 * getRowCount
	 * Row count same as the size of data vector
	 */
	public int getRowCount()
	{
		return dataVector.size();
	}

	/**
	 * setValueAt
	 * This function updates the data in the TableModel
	 * depending upon the change in the JTable
	 * <p>
	 * public void setValueAt(Object value, int row, int col)
	 * {
	 * ResultsData rd = (ResultsData)(dataVector.elementAt(row));
	 * <p>
	 * switch(col)
	 * {
	 * case INHERITORS_COL : rd.setInheritors((String) value);break;
	 * case INHERITORS_SHARE_COL : rd.setInheritorsShare((String) value);break;
	 * case INHERITORS_COUNT_COL : rd.setInheritorsCount((String) value); break;
	 * case INHERITORS_AMOUNT_COL : rd.setInheritorsAmount((String) value); break;
	 * case INHERITORS_MOWQOF_COL : rd.setInheritorsMowqof((String) value);break;
	 * }
	 * }
	 */

	public String getColumnName(int col)
	{
		return colNames[col];
	}

	public Class getColumnClass(int col)
	{
		return colTypes[col];
	}

	/**
	 * getValueAt
	 * This function updates the JTable depending upon the
	 * data in the TableModel
	 */
	public String getValueAt(int row, int col)
	{
		final ResultsData rd = dataVector.elementAt(row);

		switch (col)
		{
			case INHERITORS_COL:
				return rd.getInheritors();
			case INHERITORS_SHARE_COL:
				return rd.getInheritorsShare();
			case INHERITORS_COUNT_COL:
				return rd.getInheritorsCount();
			case INHERITORS_AMOUNT_COL:
				return rd.getInheritorsAmount();
			case INHERITORS_MOWQOF_COL:
				return rd.getInheritorsMowqof();
		}
		return null;
	}
}