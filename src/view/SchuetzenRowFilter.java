package view;

import javax.swing.RowFilter;

import controller.Controller;
import model.Schuetze;
import model.Verein;

public class SchuetzenRowFilter extends RowFilter<SchuetzenTableModel, Integer> {

	Controller controller;
	Verein verein;
	boolean active;

	public SchuetzenRowFilter(Controller controller, boolean active, Verein verein) {
		this.controller = controller;
		this.active = active;
		this.verein = verein;
	}

	@Override
	public boolean include(RowFilter.Entry<? extends SchuetzenTableModel, ? extends Integer> entry) {
		 SchuetzenTableModel model = entry.getModel();
	     Schuetze schuetze = (Schuetze) model.getValueAt(entry.getIdentifier(), -1);
	     if (verein == null || schuetze.vereinsnummer == verein.getId()) {
	    	 return active ? controller.contains(schuetze) : !controller.contains(schuetze);
	     }
	     return false;
	}

}
