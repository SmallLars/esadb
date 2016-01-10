package view;


import java.util.List;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.Rule;
import model.TargetModel;
import model.Weapon;


public class RuleTableModel implements TableModel {
	List<Rule> rules;
	List<TableModelListener> tml;

	public RuleTableModel(List<Rule> rules) {
		tml = new Vector<TableModelListener>();
		this.rules = rules;
	}

	public void removeRule(int index) {
		rules.remove(index);
		for (TableModelListener ml : tml) {
			ml.tableChanged(new TableModelEvent(this));
		}
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
				return rules.get(row);
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex > 1) return true;
		return false;
	}

	@Override
	public void addTableModelListener(TableModelListener tml) {
		this.tml.add(tml);
	}

	@Override
	public void removeTableModelListener(TableModelListener tml) {
		this.tml.remove(tml);
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
