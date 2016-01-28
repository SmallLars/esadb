package view.teamedit;


import javax.swing.JComboBox;
import javax.swing.RowFilter;

import model.Discipline;
import model.Group;
import model.Result;


public class ResultRowFilter extends RowFilter<ResultTableModel, Integer> {

	private JComboBox<Discipline> cd;
	private JComboBox<Group> cg;

	public ResultRowFilter(JComboBox<Discipline> cd, JComboBox<Group> cg) {
		this.cd = cd;
		this.cg = cg;
	}

	@Override
	public boolean include(RowFilter.Entry<? extends ResultTableModel, ? extends Integer> entry) {
		ResultTableModel model = entry.getModel();
		Result result = (Result) model.getValueAt(entry.getIdentifier(), -1);

		if (cd.getSelectedIndex() > 0 && !cd.getSelectedItem().equals(result.getDisziplin())) {
			return false;
		}

		if (cg.getSelectedIndex() > 0 && !cg.getSelectedItem().equals(result.getGroup(false))) {
			return false;
		}

		return true;
	}

}