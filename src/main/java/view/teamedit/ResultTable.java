package main.java.view.teamedit;


import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import main.java.controller.Controller;
import main.java.model.Discipline;
import main.java.model.Group;
import main.java.model.Result;
import main.java.view.FilterBox;
import main.java.view.TableEditor;


@SuppressWarnings("serial")
public class ResultTable extends JTable {

	private int selectedRow = -1;

	public ResultTable(boolean team, List<Result> starts, FilterBox cd, FilterBox cg, ResultTable t, boolean m) {
		super(new ResultTableModel(starts));
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setReorderingAllowed(false);

		TableRowSorter<ResultTableModel> sorter = new TableRowSorter<ResultTableModel>((ResultTableModel) getModel());
		sorter.setSortsOnUpdates(true);
		sorter.setRowFilter(new ResultRowFilter(team, cd, cg, t, m));
		setRowSorter(sorter);

		setDefaultRenderer(Float.class, new DefaultTableCellRenderer() {
			{setHorizontalAlignment(SwingConstants.RIGHT);}
			
			@Override
			protected void setValue(Object o) {
				super.setValue(String.format("%.1f", (Float) o));
			}
		});

		getColumnModel().getColumn(1).setCellEditor(new TableEditor(new JComboBox<Discipline>(new DefaultComboBoxModel<Discipline>(Controller.get().getDisziplinen())), 0));
		getColumnModel().getColumn(2).setCellEditor(new TableEditor(new JComboBox<Group>(new DefaultComboBoxModel<Group>(Controller.get().getConfig().getGroups())), 0));

		for (int i = 0; i < 4; i++) {
			getColumnModel().getColumn(i).setMinWidth(80);
			getColumnModel().getColumn(i).setPreferredWidth(i < 3 ? 160: 80);
		}
	}

	public void fireTableDataChanged(Result newResult) {
		selectedRow = getSelectedRow();
		((ResultTableModel) getModel()).fireTableDataChanged();

		if (newResult == null) {
			if (selectedRow >= 0) {
				if (selectedRow >= getRowCount()) selectedRow = getRowCount() - 1;
				if (selectedRow >= 0) setRowSelectionInterval(selectedRow, selectedRow);
			}
		} else {
			for (int i = 0; i < getRowCount(); i++) {
				if (getValueAt(i, -1) == newResult) {
					setRowSelectionInterval(i, i);
					return;
				}
			}
		}
	}
}