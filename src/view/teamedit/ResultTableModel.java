package view.teamedit;


import java.util.List;

import javax.swing.table.DefaultTableModel;

import model.Discipline;
import model.Group;
import model.Result;
import model.Team;


@SuppressWarnings("serial")
public class ResultTableModel extends DefaultTableModel {
	List<Result> results;

	public ResultTableModel(List<Result> starts) {
		this.results = starts;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
			case 0: return String.class;
			case 1: return Discipline.class;
			case 2: return Group.class;
			case 3: return Float.class;
			default: return Object.class;
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int col) {
		String[] columnNames = {"Name", "Disziplin", "Altersgruppe", "Ergebnis"};
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		return results == null ? 0 : results.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return results.get(row).getName();
			case 1:
				return results.get(row).getDisziplin();
			case 2:
				return results.get(row).getGroup(false);
			case 3:
				return results.get(row).getResult(false);
			default:
				return results.get(row);
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col <= 2;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if (results.get(row) instanceof Team) {
			Team t = (Team) results.get(row);
			switch (col) {
				case 0:
					t.setName((String) value); break;
				case 1:
					t.setDisziplin((Discipline) value); break;
				case 2:
					t.setGroup((Group) value); break;
			}
			fireTableDataChanged();
		}
	}
}
