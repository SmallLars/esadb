package model;

import javax.swing.ComboBoxModel;

import controller.Status;
import view.Linie;
import view.Scheibe;


public interface LineModel {

	public int getNummer();
	
	public void configure(Schuetze schuetze, Disziplin disziplin);

	public void setView(Linie view);

	public void setScheibe(Scheibe scheibe);
	
	public void setStatus(Status status);
	
	public boolean isFrei();

	@Override
	public String toString();

	public ComboBoxModel<Disziplin> getDisziplinenModel();

	public ComboBoxModel<Schuetze> getSchuetzenModel();
}