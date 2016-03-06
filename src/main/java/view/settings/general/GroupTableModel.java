package main.java.view.settings.general;


import java.util.Arrays;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import main.java.controller.Controller;
import main.java.model.Gender;
import main.java.model.Group;


@SuppressWarnings("serial")
public class GroupTableModel extends DefaultTableModel {
	Vector<Group> groups;

	public GroupTableModel(Group[] groups) {
		this.groups = new Vector<Group>(Arrays.asList(groups));
		this.groups.sort(null);
	}

	public void addGroup(Gender gender, Group group) {
		Group g = new Group("Neue Gruppe", 0, 0, gender);

		// Versuch die älteste Gruppe mit gefordertem Geschlecht zu finden
		int i = groups.size() - 1;
		if (group == null) {
			while (i >= 0 && groups.get(i).getGender() != gender) --i;
		} else {
			i = groups.indexOf(group);
		}

		if (i == -1) {
			// Gruppe wurde nicht gefunden
			int year = Controller.get().getConfig().getYear();
			g.setFrom(year - 12);
			g.setTo(year);
		} else {
			// Es wurde eine Gruppe gefunden
			
			// Gefundene Gruppe wird nur genutzt, falls keine ausgewählt wurde
			if (group == null) group = groups.get(i);

			// Gefundene oder gewählte Gruppe wird genutzt
			int year = group.getFrom() - 1;
			g.setFrom(year);
			g.setTo(year);

			//Nachfolgende Gruppen werden angepasst, um Überschneidungen zu vermeiden
			updateYearForward(i, year);
		}

		groups.add(g);
		groups.sort(null);
		fireTableDataChanged();
		Controller.get().getConfig().addGroup(g);
	}

	public void removeGroup(Group group) {
		for (int i = groups.indexOf(group) + 1; i < groups.size(); i++) {
			Group g = groups.get(i);
			if (g.getGender() == group.getGender()) {
				g.setTo(group.getTo());
				break;
			}
		}

		groups.remove(group);
		groups.sort(null);
		fireTableDataChanged();
		Controller.get().getConfig().removeGroup(group);
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
		return columnIndex == 0 || columnIndex == 2;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		Group group = groups.get(row);
		switch (col) {
			case 0:
				group.setName((String) value);
				break;
			case 2:
				int year = (int) value;
				if (year < group.getFrom()) {
					group.setFrom(year);
					updateYearForward(row, year);
				} else {
					year = updateYearBackward(row, year, group.getGender());
					for (int i = row + 1; i < groups.size(); i++) {
						if (groups.get(i).getGender() == group.getGender()) {
							groups.get(i).setTo(year);
							break;
						}
					}
				}
				break;
		}
		groups.sort(null);
		fireTableDataChanged();
	}

	private void updateYearForward(int i, int year) {
		Gender gender = groups.get(i).getGender();

		for (++i; i < groups.size(); i++) {
			Group group = groups.get(i);
			if (group.getGender() != gender) continue;

			if (year <= group.getTo()) group.setTo(year - 1);
			if (group.getFrom() <= group.getTo()) break;

			group.setFrom(--year);
		}
	}

	private int updateYearBackward(int i, int year, Gender gender) {
		for (; i >= 0; i--) {
			Group group = groups.get(i);
			if (group.getGender() != gender) continue;

			if (group.getTo() >= year) {
				group.setFrom(year);
				return year - 1;
			}

			year = updateYearBackward(i - 1, year + 1, gender);
			group.setFrom(year);
			group.setTo(year);
			return year - 1;
		}

		return Controller.get().getConfig().getYear();
	}
}
