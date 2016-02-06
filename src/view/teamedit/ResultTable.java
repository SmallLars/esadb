package view.teamedit;


import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import view.TableEditor;
import controller.Controller;
import model.Discipline;
import model.Group;
import model.Result;


@SuppressWarnings("serial")
public class ResultTable extends JTable {

	public ResultTable(boolean team, List<Result> starts, JComboBox<Discipline> cd, JComboBox<Group> cg, ResultTable t, boolean m) {
		super(new ResultTableModel(starts));
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setReorderingAllowed(false);

		TableRowSorter<ResultTableModel> sorter = new TableRowSorter<ResultTableModel>(getModel());
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

	@Override
	public ResultTableModel getModel() {
		return (ResultTableModel) super.getModel();
	}
}