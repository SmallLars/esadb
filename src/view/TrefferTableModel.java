package view;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.Treffer;


public class TrefferTableModel implements TableModel {
	List<Treffer> treffer;

	public TrefferTableModel(List<Treffer> treffer) {
		this.treffer = treffer;
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		return Treffer.class;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int arg0) {
		String[] columnNames = {"Nummer", "Art", "Wert", "Lage"};
		return columnNames[arg0];
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
				return treffer.get(row).isProbe() ? "Probe" : "Match";
			case 2:
				return treffer.get(row).getWert();
			case 3:
				return treffer.get(row).getLage();
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
