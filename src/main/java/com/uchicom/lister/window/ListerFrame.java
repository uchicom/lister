// (c) 2016 uchicom
package com.uchicom.lister.window;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.uchicom.lister.FileItem;
import com.uchicom.lister.ListerTableModel;

/**
 * 起動したらドラッグアンドドロップでパスを追加する。
 *
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class ListerFrame extends JFrame {
	private File file;

	private Properties properties = new Properties();

	private List<FileItem> itemList = new ArrayList<>();

	JTable table;
	ListerTableModel tableModel;
	TableColumnModel tableColumnModel;

	public ListerFrame(File file) throws FileNotFoundException, IOException {
		super("lister");
		this.file = file;
		initProperties();
		initComponents();
	}

	/**
	 * target.0.path=ファイルやディレクトリのパス
	 * target.0.status=文字列
	 * target.0.visible=true or false
	 */
	private void initComponents() {
		for (int i = 0; i < 100; i++) {
			String key = "target." + i;
			if (properties.containsKey(key)) {
				File file = new File(properties.getProperty(key));
				FileItem fileItem = new FileItem(file, properties.getProperty(key + ".status"));
				itemList.add(fileItem);
			} else {
				break;
			}
		}
		tableModel = new ListerTableModel(itemList, 2);
		tableColumnModel = new DefaultTableColumnModel();
		TableColumn tableColumn = new TableColumn(0);
		tableColumn.setHeaderValue("作業対象");
		if (properties.containsKey("column.0.width")) {
			tableColumn.setPreferredWidth(Integer.parseInt(properties.getProperty("column.0.width")));
		}
		tableColumnModel.addColumn(tableColumn);
		tableColumn.setCellEditor(new TableCellEditor() {

			@Override
			public boolean stopCellEditing() {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}

			@Override
			public boolean shouldSelectCell(EventObject arg0) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}

			@Override
			public void removeCellEditorListener(CellEditorListener arg0) {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public boolean isCellEditable(EventObject arg0) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}

			@Override
			public Object getCellEditorValue() {
				// TODO 自動生成されたメソッド・スタブ
				return null;
			}

			@Override
			public void cancelCellEditing() {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public void addCellEditorListener(CellEditorListener arg0) {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
				// TODO 自動生成されたメソッド・スタブ
				return null;
			}
		});
		tableColumn = new TableColumn(1);
		tableColumn.setHeaderValue("状態");
		if (properties.containsKey("column.1.width")) {
			tableColumn.setPreferredWidth(Integer.parseInt(properties.getProperty("column.1.width")));
		}
		tableColumnModel.addColumn(tableColumn);
		table = new JTable(tableModel, tableColumnModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getContentPane().add(new JScrollPane(table));

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				try {
					saveProperties();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(ListerFrame.this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				System.exit(0);
			}
		});
		setTransferHandler(new TransferHandler() {
			@Override
			public boolean canImport(TransferHandler.TransferSupport support) {
				if (support.isDrop() && support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					return true;
				}
				return false;
			}

			@Override
			public boolean importData(TransferSupport support) {
				// 受け取っていいものか確認する
				if (!canImport(support)) {
			        return false;
			    }

				// ドロップ処理
				Transferable t = support.getTransferable();
				try {
					// ファイルを受け取る
					@SuppressWarnings("unchecked")
					List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
					//ファイルを追加する
					files.forEach((file)->{
						itemList.add(new FileItem(file, null));
					});
					tableModel.fireTableDataChanged();

				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
		});
		pack();
		//画面サイズ初期化
		if (properties.containsKey("bounds")) {
			String[] bounds = properties.getProperty("bounds").split(",");
			if (bounds.length == 4) {
				setBounds(Integer.parseInt(bounds[0]),
						Integer.parseInt(bounds[1]),
						Integer.parseInt(bounds[2]),
						Integer.parseInt(bounds[3]));
			}
		}
	}

	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 *
	 */
	private void initProperties() throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			properties.load(fis);
		}
	}

	private void saveProperties() throws FileNotFoundException, IOException {
		// 一覧を保存する
		for (int i = 0; i < itemList.size(); i++) {
			FileItem item = itemList.get(i);
			String key = "target." + i;
			properties.setProperty(key, item.getFile().getCanonicalPath());
			if (item.getStatus() != null) {
				properties.setProperty(key + ".status", item.getStatus());
			} else {
				properties.remove(key + ".status");
			}
		}
		//Table状態を保存する
		properties.setProperty("column.0.width", String.valueOf(tableColumnModel.getColumn(0).getWidth()));
		properties.setProperty("column.1.width", String.valueOf(tableColumnModel.getColumn(1).getWidth()));

		//Window状態を保存する
		Rectangle bounds = getBounds();
		properties.setProperty("bounds", bounds.x + "," + bounds.y + "," + bounds.width + "," + bounds.height);

		try (FileOutputStream fos = new FileOutputStream(file)) {
			properties.store(fos, "lister config.");
		}
	}
}
