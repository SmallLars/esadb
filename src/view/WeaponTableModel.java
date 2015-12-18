package view;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.Weapon;


public class WeaponTableModel implements TableModel {
	List<Weapon> weapons;

	public WeaponTableModel(List<Weapon> weapons) {
		this.weapons = weapons;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
			case 0:
				return Integer.class;
			case 2:
				return Double.class;
			default:
				return String.class;
		}
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int arg0) {
		String[] columnNames = {"Kennummer", "Bezeichnung", "Durchmesser", "Einheit", "Mikrofoneinstellung"};
		return columnNames[arg0];
	}

	@Override
	public int getRowCount() {
		return weapons.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return weapons.get(row).getNumber();
			case 1:
				return weapons.get(row).toString();
			case 2:
				return weapons.get(row).getRadius() * 2 / 1000.;
			case 3:
				return weapons.get(row).getUnit();
			default:
				return weapons.get(row).toString();
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 2 || columnIndex == 3) return true;
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
