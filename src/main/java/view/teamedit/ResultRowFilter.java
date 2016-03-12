package view.teamedit;


import javax.swing.RowFilter;

import model.Result;
import model.Single;
import model.Team;
import view.FilterBox;


public class ResultRowFilter extends RowFilter<ResultTableModel, Integer> {

	private boolean teams;
	private FilterBox cd;
	private FilterBox cg;
	private ResultTable team;
	private boolean member;

	public ResultRowFilter(boolean teams, FilterBox cd, FilterBox cg, ResultTable team, boolean member) {
		this.teams = teams;
		this.cd = cd;
		this.cg = cg;
		this.team = team;
		this.member = member;
	}

	@Override
	public boolean include(RowFilter.Entry<? extends ResultTableModel, ? extends Integer> entry) {
		ResultTableModel model = entry.getModel();
		Result result = (Result) model.getValueAt(entry.getIdentifier(), -1);
		
		if (teams != result instanceof Team) return false;
		
		if (cd != null && cd.doFilter() && !cd.getSelectedItem().equals(result.getDisziplin())) {
			return false;
		}

		if (cg != null && cg.doFilter() && !cg.getSelectedItem().equals(result.getGroup(false))) {
			return false;
		}

		if (!teams && team != null) {
			int row = team.getSelectedRow();
			if (row >= 0) {
				Team t = (Team) team.getValueAt(row, -1);
				return t.contains((Single) result) == member;
			}
		}

		return true;
	}

}