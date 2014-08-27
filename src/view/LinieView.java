package view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import model.Disziplin;
import model.Schuetze;
import model.Treffer;
import controller.Linie;
import controller.Status;

@SuppressWarnings("serial")
public class LinieView extends JPanel {
	private Linie linie;
	private GUI gui;

	private JComboBox<Schuetze> schuetze;
	private JComboBox<Disziplin> disziplin;
	private JCheckBox sperre;
	private JCheckBox start;
	private JCheckBox wertung;
	private JButton frei;

	private boolean match = false;

	public LinieView(Linie linie, GUI gui) {
		this.linie = linie;
		this.gui = gui;
		linie.setView(this);

		this.setLayout(null);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.setSize(new Dimension(718, 31));

		JLabel label = new JLabel("" + linie.getNummer());
		label.setBounds(14, 8, 14, 14);
		this.add(label);
		
		sperre = new JCheckBox("");
		sperre.setBounds(39, 4, 23, 23);
		sperre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (sperre.isSelected()) {
					Schuetze s = (Schuetze) schuetze.getSelectedItem();
					Disziplin d = (Disziplin) disziplin.getSelectedItem();
					if (s == null || d == null) {
						// TODO Warnung
						sperre.setSelected(false);
					} else {
						linie.configure(s, d);
						linie.setStatus(Status.SPERREN);
					}
				} else {
					wertung.setSelected(false);
					match = false;
					linie.setStatus(Status.ENTSPERREN);
				}
			}
		});
		this.add(sperre);
		
		start = new JCheckBox("");
		start.setBounds(85, 4, 23, 23);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (start.isSelected()) {
					linie.setStatus(Status.START);
				} else {
					linie.setStatus(Status.STOP);
				}
			}
		});
		this.add(start);
		
		wertung = new JCheckBox("");
		wertung.setBounds(131, 4, 23, 23);
		wertung.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (wertung.isSelected()) {
					linie.setStatus(Status.WERTUNG);
				} else {
					linie.setStatus(Status.PROBE);
				}
			}
		});
		this.add(wertung);

		schuetze = new JComboBox<Schuetze>();
		schuetze.setBounds(177, 5, 200, 22);
		this.add(schuetze);
		
		disziplin = new JComboBox<Disziplin>();
		disziplin.setBounds(400, 5, 200, 22);
		this.add(disziplin);

		refresh();

		frei = new JButton("Frei");
		frei.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				schuetze.setSelectedItem(null);
				disziplin.setSelectedItem(null);
				linie.setStatus(Status.FREI);
			}
		});
		frei.setBounds(623, 5, 91, 22);
		this.add(frei);
		
		setEnabled(true);
	}

	@Override
	public void setEnabled(boolean enabled) {
		Color c = enabled ? null : Color.ORANGE;
		super.setEnabled(enabled);
		setBackground(c);
		sperre.setBackground(c);
		start.setBackground(c);
		wertung.setBackground(c);
		schuetze.setEnabled(enabled && !sperre.isSelected());
		disziplin.setEnabled(enabled && !sperre.isSelected());
		sperre.setEnabled(enabled && !start.isSelected());
		start.setEnabled(enabled && sperre.isSelected());
		wertung.setEnabled(enabled && sperre.isSelected() && !match);
		frei.setEnabled(enabled && !sperre.isSelected());
	}

	public void refresh() {
		schuetze.removeAllItems();
		Vector<Schuetze> vs = linie.getSchuetzen();
		vs.sort(null);
		schuetze.addItem(null);
		for (Schuetze s : vs) schuetze.addItem(s);

		disziplin.removeAllItems();
		Vector<Disziplin> vd = linie.getDisziplinen();
		vd.sort(null);
		disziplin.addItem(null);
		for (Disziplin s : vd) disziplin.addItem(s);
	}

	public void setMatch() {
		match = true;
		wertung.setSelected(true);
		if (this.isEnabled()) {
			wertung.setEnabled(false);
		}
	}

	public void showResult(String resultString) {
		gui.println(linie + ": " + resultString);
	}

	public void showTreffer(Treffer t) {
		gui.showTreffer(linie.getNummer(), t);		
	}
}