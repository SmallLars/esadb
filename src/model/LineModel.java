package model;

import javax.swing.ComboBoxModel;


public interface LineModel {

	public int getNummer();
	
	public void configure(Single e);
	
	public Single configure(Member schuetze, Discipline disziplin);

	public Single getResult();

	public void setStatus(Status status);

	public boolean isBusy();

	public boolean isError();
	
	public boolean isFrei();

	public boolean isGesperrt();

	public boolean isGestartet();

	public boolean inMatch();

	public boolean canSwitchPM();

	@Override
	public String toString();

	public ComboBoxModel<Discipline> getDisziplinenModel();

	public ComboBoxModel<Member> getSchuetzenModel();
	
	public void addLineListener(LineListener l);
	
	public void removeLineListener(LineListener l);
}