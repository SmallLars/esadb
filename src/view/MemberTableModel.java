package view;

import java.util.Date;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.Member;


public class MemberTableModel implements TableModel {
	List<Member> schuetzen;

	public MemberTableModel(List<Member> schuetzen) {
		this.schuetzen = schuetzen;
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
		return schuetzen.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return schuetzen.get(row).nachname;
			case 1:
				return schuetzen.get(row).vorname;
			case 2:
				return schuetzen.get(row).geburtsdatum;
			default:
				return schuetzen.get(row);
		}
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
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
