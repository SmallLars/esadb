package view;


import java.util.List;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.Gender;
import model.Group;


public class GroupTableModel implements TableModel {
	List<Group> groups;
	List<TableModelListener> tml;

	private Group lastChanged = null;

	public GroupTableModel(List<Group> groups) {
		tml = new Vector<TableModelListener>();
		this.groups = groups;
	}

	public int getLastChanged() {
		if (lastChanged == null) return -1;
		int i = groups.indexOf(lastChanged);
		lastChanged = null;
		return i;
	}

	public void addGroup(Group g) {
		groups.add(g);
		for (TableModelListener ml : tml) {
			ml.tableChanged(new TableModelEvent(this));
		}
	}

	public void removeGroup(Group g) {
		groups.remove(g);
		for (TableModelListener ml : tml) {
			ml.tableChanged(new TableModelEvent(this));
		}
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
			case 1:
			case 2:
				return Integer.class;
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
		String[] columnNames = {"Name", "Von", "Bis", "Geschlecht"};
		return columnNames[arg0];
	}

	@Override
	public int getRowCount() {
		return groups.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 1:
				return groups.get(row).getTo();
			case 2:
				return groups.get(row).getFrom();
			case 3:
				return groups.get(row).getGender();
			default:
				return groups.get(row);
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
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
		lastChanged = groups.get(row);
		switch (col) {
			case 0:
				groups.get(row).setName((String) value); break;
			case 1:
				groups.get(row).setTo((int) value); break;
			case 2:
				groups.get(row).setFrom((int) value); break;
			case 3:
				groups.get(row).setGender((Gender) value); break;
		}
		groups.sort(null);

		for (TableModelListener ml : tml) {
			ml.tableChanged(new TableModelEvent(this));
		}
	}
}
