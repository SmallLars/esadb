package model;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.ComboBoxModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.comboBoxModel.DisziplinenModel;
import model.comboBoxModel.SchuetzenModel;
import controller.Controller;
import controller.Status;
import view.Linie;
import view.Scheibe;


public class DefaultLineModel implements LineModel, LineReader {
	private int nummer;
	private Controller controller;
	private Einzel einzel;

	private boolean busy = false;
	private Linie view = null;
	private Scheibe scheibe = null;

	public DefaultLineModel(int nummer, Controller controller) {
		this.nummer = nummer;
		this.controller = controller;
		this.einzel = null;
	}

	public int getNummer() {
		return nummer;
	}
	
	public void configure(Schuetze schuetze, Disziplin disziplin) {
		einzel = new Einzel(nummer, disziplin, schuetze);
		if (scheibe != null) scheibe.setStart(einzel);
		controller.add(einzel);
	}

	public void setView(Linie view) {
		this.view = view;
	}

	public void setScheibe(Scheibe scheibe) {
		this.scheibe = scheibe;
	}
	
	public void setStatus(Status status) {
		if (busy == false) {
			busy = true;
			if (view != null) view.setEnabled(false);
			String cmd = null;
			if (status == Status.SPERREN) {
				if (einzel != null) {
					cmd = "\"" + status.getCode() + " $" + einzel.getSchuetze().wettkampfID + "$" + einzel.getSchuetze().passnummer + "$" + einzel.getDisziplin().getId() + "$0$0\"";
				}
			} else {
				cmd = status.getCode();
			}
			switch (status) {
				case ENTSPERREN:
					if (einzel.isEmpty()) controller.remove(einzel);
					break;
				case FREI:
					einzel = null;
					if (scheibe != null) scheibe.setStart(einzel);
				default:
			}
			if (cmd != null) {
				writeFile(".ctl", cmd + "\n");
				writeFile(".nrt", cmd + "\n");
			}
		}
	}
	
	public boolean isFrei() {
		return !busy && einzel == null;
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

	public boolean addTreffer(Treffer t) {
		SimpleAttributeSet style = new SimpleAttributeSet();
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.decode("0x0050A0"));

		if (einzel == null) {
			controller.println(this + ": Schu� von freier Linie erhalten. Zuordnung nicht m�glich.", style);
			return false;
		}

		controller.println(this + ": " + einzel.addTreffer(t), style);
		if (view != null) {
			if (!t.isProbe() && t.getNummer() == 1) view.setMatch();
		}
		if (scheibe != null) scheibe.newTreffer();
		return true;
	}
	
	@Override
	public String toString() {
		return "Linie " + nummer;
	}

	public ComboBoxModel<Disziplin> getDisziplinenModel() {
		return new DisziplinenModel(controller);
	}

	public ComboBoxModel<Schuetze> getSchuetzenModel() {
		return new SchuetzenModel(controller);
	}
}