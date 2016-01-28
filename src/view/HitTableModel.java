package view;


import java.util.List;

import javax.swing.table.DefaultTableModel;

import model.Hit;


@SuppressWarnings("serial")
public class HitTableModel extends DefaultTableModel {
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
		return treffer == null ? 0 : treffer.size();
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
}
