// (c) 2016 uchicom
package com.uchicom.lister;

import java.util.List;

import javax.swing.table.DefaultTableModel;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class ListerTableModel extends DefaultTableModel {
    /** データ格納リスト */
	private List<FileItem> rowList;

	/** 列最大数 */
	private int columnCount;

	/**
	 *
	 * @param rowList
	 * @param columnCount
	 */
	public ListerTableModel(List<FileItem> rowList, int columnCount) {
		this.rowList = rowList;
		this.columnCount = columnCount;
	}

	@Override
	public Object getValueAt(int row, int col) {
		FileItem bean = rowList.get(row);
		String value = null;
		switch (col) {
		case 0:
			value = bean.getFile().getName();
			break;
		case 1:
			value = bean.getStatus();
			break;
		default:
			value = "";
				break;
		}
		return value;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
	    System.out.println(value);
		if (row < rowList.size()) {
			FileItem bean = rowList.get(row);
			String viewString = (String)value;
			switch (col) {
			case 0:
				break;
			case 1:
				bean.setStatus(viewString);
				break;
			default:
				System.out.println(col);
			}
		} else {
			//削除時の選択解除時にcancelcelleditingを実施したから問題ないはずだけど。。一応残しておくか。
			System.out.println("row:" + row + ",col:" + col);
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
	    Class<?> returnClass = null;
	    switch (columnIndex) {
	    case 0:
	        returnClass = String.class;
	        break;
	    case 1:
	        returnClass = String.class;
	        break;
	    }
		return returnClass;
	}

	@Override
	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public int getRowCount() {
		if (rowList != null) {
			return rowList.size();
		} else {
			return 0;
		}
	}

	public void setRowList(List<FileItem> rowList) {
		this.rowList = rowList;
	}
    public List<FileItem> getRowList() {
        return rowList;
    }
}
