package view.member;


import javax.swing.RowFilter;

import controller.Controller;
import model.Member;
import model.Club;


public class MemberRowFilter extends RowFilter<MemberTableModel, Integer> {

	private Controller controller;
	private Club verein;
	private boolean active;

	public MemberRowFilter(boolean active, Club verein) {
		this.controller = Controller.get();
		this.active = active;
		this.verein = verein;
	}

	@Override
	public boolean include(RowFilter.Entry<? extends MemberTableModel, ? extends Integer> entry) {
		MemberTableModel model = entry.getModel();
		Member schuetze = (Member) model.getValueAt(entry.getIdentifier(), -1);
		if (verein == null || schuetze.vereinsnummer == verein.getId()) {
			return active ? controller.contains(schuetze) : !controller.contains(schuetze);
		}
		return false;
	}

}