package model;

import javax.swing.ComboBoxModel;


public interface LineModel {

	public int getNummer();
	
	public void configure(Schuetze schuetze, Disziplin disziplin);

	public Einzel getResult();

	public void setStatus(Status status);

	public boolean isBusy();
	
	public boolean isFrei();

	public boolean isGesperrt();

	public boolean isGestartet();

	public boolean inMatch();

	public boolean canSwitchPM();

	@Override
	public String toString();

	public ComboBoxModel<Disziplin> getDisziplinenModel();

	public ComboBoxModel<Schuetze> getSchuetzenModel();
	
	public void addLineListener(LineListener l);
	
	public void removeLineListener(LineListener l);
}