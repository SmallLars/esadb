package view.settings.general;


import javax.swing.RowFilter;

import model.Gender;
import model.Group;
import view.FilterBox;


public class GroupRowFilter extends RowFilter<GroupTableModel, Integer> {

	private FilterBox filterBox;

	public GroupRowFilter(FilterBox filterBox) {
		this.filterBox = filterBox;
	}

	@Override
	public boolean include(RowFilter.Entry<? extends GroupTableModel, ? extends Integer> entry) {
		GroupTableModel model = entry.getModel();
		Group group = (Group) model.getValueAt(entry.getIdentifier(), -1);
		
		if (filterBox != null && filterBox.doFilter()) {
			if ((Gender) filterBox.getSelectedItem() != group.getGender()) {
				return false;
			}
		}

		return true;
	}

}