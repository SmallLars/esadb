package model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.Timer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.comboBoxModel.DisziplinenModel;
import model.comboBoxModel.SchuetzenModel;
import controller.Controller;


public class DefaultLineModel implements LineModel, LineReader, ActionListener {
	public static final int STATE_CHANGED = 1;
	public static final int RESULT_CHANGED = 2;

	private int nummer;
	private Controller controller;
	private Single einzel;

	private boolean busy = false;
	private Timer busyTimer;
	private boolean error = false;
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
	
	public void configure(Single e) {
		if (e != null) {
			einzel = e;
			controller.add(einzel);
			modelChanged(RESULT_CHANGED);
		}
	}
	
	public Single configure(Member schuetze, Discipline disziplin) {
		if (einzel == null || einzel.getSchuetze() != schuetze || einzel.getDisziplin() != disziplin) {
			disziplin.getRegel().toFile();

			Single incomplete = controller.findIncomplete(schuetze, disziplin);
			Single new_event = new Single(nummer, disziplin, schuetze);
			if (incomplete != null) {
				einzel = incomplete;
				return new_event;
			}
			configure(new_event);
		} else {
			if (einzel.isEmpty()) controller.add(einzel);
		}
		return null;
	}

	public Single getResult() {
		return einzel;
	}

	public void setStatus(Status status) {
		if (busy == false) {
			busy = true;
			busyTimer = new Timer(30000, this);
			busyTimer.start();
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
	
	public boolean isError() {
		return error;
	}
	
	public boolean isFrei() {
		return einzel == null;
	}

	public boolean isGesperrt() {
		return state > 0;
	}

	public boolean isGestartet() {
		return state > 1;
	}

	public boolean inMatch() {
		return state > 2 || (einzel != null && einzel.inMatch());
	}

	public boolean canSwitchPM() {
		return isGestartet() && einzel.getTreffer(false, 1) == null;
	}

	public void reenable() {
		busy = false;
		busyTimer.stop();
		error = false;
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

	public boolean addTreffer(Hit t) {
		SimpleAttributeSet style = new SimpleAttributeSet();
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.decode("0x0050A0"));

		if (einzel == null) {
			controller.println(this + ": Schuß von freier Linie erhalten. Zuordnung nicht möglich.", style);
			return false;
		}

		int oldNum = einzel.addTreffer(t);
		if (oldNum > 0) {
			einzel.toFile("HTErg" + nummer  + ".dat", t.isProbe());
			setStatus(Status.UPDATE);

			SimpleAttributeSet red = new SimpleAttributeSet();
			StyleConstants.setBold(red, true);
			StyleConstants.setForeground(red, Color.decode("0xC80000"));
			controller.println(String.format("Linie %d: Treffer %s(%d) wurde empfangen und als %s(%d) gespeichert. Linie wurde aktualisiert.", nummer, t.isProbe() ? "P" : "M", oldNum, t.isProbe() ? "P" : "M", t.getNummer()), red);
		}
		String s = String.format("Linie %d: %s: %s: %s(%d) %s", nummer, einzel.getSchuetze(), einzel.getDisziplin(), t.isProbe() ? "P" : "M", t.getNummer(), t);
		controller.println(s, style);
		controller.save();
		modelChanged(RESULT_CHANGED);
		if (!t.isProbe() && t.getNummer() == 1) modelChanged(STATE_CHANGED);

		return true;
	}
	
	@Override
	public String toString() {
		return "Linie " + nummer;
	}

	public ComboBoxModel<Discipline> getDisziplinenModel() {
		return new DisziplinenModel(controller);
	}

	public ComboBoxModel<Member> getSchuetzenModel() {
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (busy) {
			error = true;
			busyTimer.stop();
			modelChanged(STATE_CHANGED);
		}
	}
}