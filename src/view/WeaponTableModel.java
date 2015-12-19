package view;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.Unit;
import model.Weapon;


public class WeaponTableModel implements TableModel {
	List<Weapon> weapons;

	public WeaponTableModel(List<Weapon> weapons) {
		this.weapons = weapons;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
			case 2:
				return Double.class;
			case 4:
				return Integer.class;
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
		String[] columnNames = {"Kennummer", "Bezeichnung", "Ø", "Einheit", "Mikrofon"};
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
			case 2:
				return weapons.get(row).getDiameter() / 1000.;
			case 3:
				return weapons.get(row).getUnit();
			case 4:
				return weapons.get(row).getMikro();
			default:
				return weapons.get(row).toString();
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
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
	public void setValueAt(Object value, int row, int col) {
		switch (col) {
			case 0:
				weapons.get(row).setNumber((String) value); break;
			case 2:
				weapons.get(row).setDiameter((int) (((Double) value) * 1000)); break;
			case 3:
				weapons.get(row).setUnit((Unit) value); break;
			case 4:
				weapons.get(row).setMikro((int) value); break;
			default:
				weapons.get(row).setName((String) value); break;
		}
	}
}
