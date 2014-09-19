package model;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.comboBoxModel.DisziplinenModel;
import model.comboBoxModel.SchuetzenModel;
import controller.Controller;
import controller.Status;


public class DefaultLineModel implements LineModel, LineReader {
	public static final int STATE_CHANGED = 1;
	public static final int RESULT_CHANGED = 2;

	private int nummer;
	private Controller controller;
	private Einzel einzel;

	private boolean busy = false;
	private Vector<LineListener> listener;

	private int state;

	public DefaultLineModel(int nummer, Controller controller) {
		this.nummer = nummer;
		this.controller = controller;
		this.einzel = null;
		this.listener = new Vector<LineListener>();

		state = 0;
	}

	public int getNummer() {
		return nummer;
	}
	
	public void configure(Schuetze schuetze, Disziplin disziplin) {
		if (einzel == null || einzel.getSchuetze() != schuetze || einzel.getDisziplin() != disziplin) {
			einzel = new Einzel(nummer, disziplin, schuetze);
			controller.add(einzel);
			modelChanged(RESULT_CHANGED);
		}
	}

	public Einzel getResult() {
		return einzel;
	}

	public void setStatus(Status status) {
		if (busy == false) {
			busy = true;
			String cmd = null;
			if (status == Status.SPERREN) {
				if (einzel != null) {
					cmd = "\"" + status.getCode() + " $" + einzel.getSchuetze().wettkampfID + "$" + einzel.getSchuetze().passnummer + "$" + einzel.getDisziplin().getId() + "$0$0\"";
				}
			} else {
				cmd = status.getCode();
			}
			switch (status) {
				case SPERREN:
					state = 1;
					break;
				case ENTSPERREN:
					if (einzel.isEmpty()) controller.remove(einzel);
					state = 0;
					break;
				case START:
					state = 2;
					break;
				case STOP:
					state = 1;
					break;
				case WERTUNG:
					state = 3;
					break;
				case PROBE:
					state = 2;
					break;
				case FREI:
					einzel = null;
					modelChanged(RESULT_CHANGED);
				default:
			}
			if (cmd != null) {
				writeFile(".ctl", cmd + "\n");
				writeFile(".nrt", cmd + "\n");
			}

			modelChanged(STATE_CHANGED);
		}
	}

	public boolean isBusy() {
		return busy;
	}
	
	public boolean isFrei() {
		return !busy && einzel == null;
	}

	public boolean isGesperrt() {
		return state > 0;
	}

	public boolean isGestartet() {
		return state > 1;
	}

	public boolean inMatch() {
		return state > 2 || (einzel != null && einzel.getTreffer(false, 1) != null);
	}

	public boolean canSwitchPM() {
		return isGestartet() && einzel.getTreffer(false, 1) == null;
	}

	public void reenable() {
		busy = false;
		modelChanged(STATE_CHANGED);
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
			controller.println(this + ": Schuß von freier Linie erhalten. Zuordnung nicht möglich.", style);
			return false;
		}

		controller.println(this + ": " + einzel.addTreffer(t), style);
		modelChanged(STATE_CHANGED);
		modelChanged(RESULT_CHANGED);

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

	@Override
	public void addLineListener(LineListener l) {
		listener.add(l);		
	}

	@Override
	public void removeLineListener(LineListener l) {
		listener.remove(l);
	}

	private void modelChanged(int type) {
		for (LineListener l : listener) l.lineChanged(this, type);
	}
}