package view.settings.general;


import java.util.List;

import javax.swing.table.DefaultTableModel;

import model.Gender;
import model.Group;


@SuppressWarnings("serial")
public class GroupTableModel extends DefaultTableModel {
	List<Group> groups;

	private Group lastChanged = null;

	public GroupTableModel(List<Group> groups) {
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
		fireTableDataChanged();
	}

	public void removeGroup(Group g) {
		groups.remove(g);
		fireTableDataChanged();
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
		return groups == null ? 0 : groups.size();
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

		fireTableDataChanged();
	}
}
