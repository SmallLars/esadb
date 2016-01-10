package view;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.Hit;


public class HitTableModel implements TableModel {
	List<Hit> treffer;

	public HitTableModel(List<Hit> treffer) {
		this.treffer = treffer;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return Hit.class;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int col) {
		String[] columnNames = {"Nummer", "Art", "Wert", "Lage", "Linie"};
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		return treffer.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return treffer.get(row).getNummer();
			case 1:
				return treffer.get(row).isProbe();
			case 2:
				return treffer.get(row);
			case 3:
				return treffer.get(row).getLage();
			case 4:
				return treffer.get(row).getLinie();
			default:
				return treffer.get(row);
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
