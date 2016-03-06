package main.java.view.settings.rules;


import java.util.List;

import javax.swing.table.DefaultTableModel;

import main.java.model.Rule;
import main.java.model.TargetModel;
import main.java.model.Weapon;


@SuppressWarnings("serial")
public class RuleTableModel extends DefaultTableModel {
	List<Rule> rules;

	public RuleTableModel(List<Rule> rules) {
		this.rules = rules;
	}

	public void removeRule(int index) {
		rules.remove(index);
		fireTableDataChanged();
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
			case 0:
				return String.class;
			case 2:
				return TargetModel.class;
			case 3:
				return Weapon.class;
			default:
				return Rule.class;
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int col) {
		String[] columnNames = {"Kennummer", "Bezeichnung", "Scheibe", "Waffe"};
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		return rules == null ? 0 : rules.size();
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
				return rules.get(row);
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex > 1) return true;
		return false;
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
