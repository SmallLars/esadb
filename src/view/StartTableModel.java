package view;


import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.Start;


public class StartTableModel implements TableModel {
	List<Start> starts;

	public StartTableModel(List<Start> starts) {
		this.starts = starts;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return Start.class;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int col) {
		String[] columnNames = {"Name", "Disziplin", "Altersgruppe", "Ergebnis"};
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		return starts.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return starts.get(row);
			case 1:
				return starts.get(row).getDisziplin();
			case 2:
				return starts.get(row).getGroup(false);
			case 3:
				return starts.get(row).getResult(false);
			default:
				return starts.get(row);
		}
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
}
