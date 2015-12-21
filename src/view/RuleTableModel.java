package view;


import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.Rule;
import model.TargetModel;
import model.Weapon;


public class RuleTableModel implements TableModel {
	List<Rule> rules;

	public RuleTableModel(List<Rule> rules) {
		this.rules = rules;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
			case 2:
				return TargetModel.class;
			case 3:
				return Weapon.class;
			default:
				return String.class;
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int arg0) {
		String[] columnNames = {"Kennummer", "Bezeichnung", "Scheibe", "Waffe"};
		return columnNames[arg0];
	}

	@Override
	public int getRowCount() {
		return rules.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return rules.get(row).getRegelnummer();
			case 2:
				return rules.get(row).getScheibe();
			case 3:
				return rules.get(row).getWaffe();
			default:
				return rules.get(row).toString();
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex > 1) return true;
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
	public void setValueAt(Object value, int row, int col) {
		switch (col) {
			case 2:
				rules.get(row).setScheibe((TargetModel) value); break;
			case 3:
				rules.get(row).setWaffe((Weapon) value); break;
		}
	}
}
