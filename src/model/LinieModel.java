package model;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.ComboBoxModel;

import controller.Controller;
import controller.Status;
import view.Linie;


public class LinieModel {
	private int nummer;
	private Controller controller;
	private Status letzterStatus;
	private Start start;

	private SchuetzenModel sModel;
	private DisziplinenModel dModel;

	private boolean busy = false;
	private Linie view = null;

	public LinieModel(int nummer, Controller controller) {
		this.nummer = nummer;
		this.controller = controller;
		this.dModel = new DisziplinenModel(controller);
		this.sModel = new SchuetzenModel(controller);
		this.letzterStatus = Status.FREI;
	}

	public void modelChanged() {
		sModel.modelChanged();
		dModel.modelChanged();
	}

	public int getNummer() {
		return nummer;
	}
	
	public void configure(Schuetze schuetze, Disziplin disziplin) {
		start = new Start(nummer, disziplin, schuetze);
		controller.add(start);
	}

	public void setView(Linie view) {
		this.view = view;
	}
	
	public void setStatus(Status status) {
		if (busy == false) {
			letzterStatus = status;
			busy = true;
			if (view != null) view.setEnabled(false);
			String cmd = null;
			if (status == Status.SPERREN) {
				if (start != null) {
					cmd = "\"" + status.getCode() + " $" + start.getSchuetze().wettkampfID + "$" + start.getSchuetze().passnummer + "$" + start.getDisziplin().getId() + "$0$0\"";
				}
			} else {
				cmd = status.getCode();
			}
			if (status == Status.ENTSPERREN) {
				if (start.isEmpty()) controller.remove(start);
			}
			if (cmd != null) {
				writeFile(".ctl", cmd + "\n");
				writeFile(".nrt", cmd + "\n");
			}
		}
	}
	
	public boolean isFrei() {
		return !busy && (letzterStatus == Status.FREI || letzterStatus == Status.INIT);
	}

	public void reenable() {
		busy = false;
		if (view != null) view.setEnabled(true);
	}
	
	private void writeFile(String type, String cmd) {
		try {
			PrintWriter writer = new PrintWriter("HServ" + nummer + type);
			writer.println(cmd);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String addTreffer(Treffer t) {
		String info = start.addTreffer(t);
		if (view != null) {
			if (!t.isProbe() && t.getNummer() == 1) view.setMatch();
		}
		return info;
	}
	
	@Override
	public String toString() {
		return "Linie " + nummer;
	}

	public ComboBoxModel<Disziplin> getDisziplinenModel() {
		return dModel;
	}

	public ComboBoxModel<Schuetze> getSchuetzenModel() {
		return sModel;
	}

}