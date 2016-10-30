package view.member;


import java.util.Date;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import model.Member;


@SuppressWarnings("serial")
public class MemberTableModel extends DefaultTableModel {
	Member[] schuetzen;

	public MemberTableModel(Set<Member> schuetzen) {
		this.schuetzen = schuetzen.toArray(new Member[0]);
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		if (arg0 == 2) return Date.class;
		return Object.class;
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int arg0) {
		String[] columnNames = {"Nachname", "Vorname", "Geburtsdatum"};
		return columnNames[arg0];
	}

	@Override
	public int getRowCount() {
		return schuetzen == null ? 0 : schuetzen.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return schuetzen[row].nachname;
			case 1:
				return schuetzen[row].vorname;
			case 2:
				return schuetzen[row].geburtsdatum;
			default:
				return schuetzen[row];
		}
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}
}
