package controller;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;

import model.Disziplin;
import model.Schuetze;
import model.Start;
import model.Treffer;
import view.LinieView;


public class Linie {
	private int nummer;
	private Controller controller;
	private Status letzterStatus;
	private Start start;

	private boolean busy = false;
	private LinieView view = null;

	public Linie(int nummer, Controller controller) {
		this.nummer = nummer;
		this.controller = controller;
		this.letzterStatus = Status.FREI;
	}

	public int getNummer() {
		return nummer;
	}
	
	public void configure(Schuetze schuetze, Disziplin disziplin) {
		start = new Start(nummer, disziplin, schuetze);
		controller.getModel().add(start);
	}

	public void setView(LinieView view) {
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
				if (start.isEmpty()) controller.getModel().remove(start);
			}
			if (cmd != null) {
				writeFile(".ctl", cmd);
				writeFile(".nrt", cmd);
			}
		}
	}
	
	public boolean isFrei() {
		return !busy && letzterStatus == Status.FREI;
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

	public void addTreffer(String resultString) {
		Treffer t = new Treffer(resultString);
		String info = start.addTreffer(t);
		controller.save();
		if (view != null) {
			if (!t.isProbe() && t.getNummer() == 1) view.setMatch();
			view.showTreffer(t);
			view.showResult(info);
		}
	}
	
	@Override
	public String toString() {
		return "Linie " + nummer;
	}

	public void refresh() {
		if (view != null) view.refresh();
	}

	public Vector<Schuetze> getSchuetzen() {
		return controller.getModel().getSchuetzen();
	}

	public Vector<Disziplin> getDisziplinen() {
		return controller.getModel().getDisziplinen();
	}

}