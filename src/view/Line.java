package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import model.DefaultLineModel;
import model.Discipline;
import model.Single;
import model.LineListener;
import model.LineModel;
import model.Member;
import model.Status;


@SuppressWarnings("serial")
public class Line extends JPanel implements LineListener {
	private LineModel linie;

	private JComboBox<Member> schuetze;
	private JComboBox<Discipline> disziplin;
	private JCheckBox sperre;
	private JCheckBox start;
	private JCheckBox wertung;
	private JButton frei;

	public Line(LineModel linie) {
		this.linie = linie;
		linie.addLineListener(this);

		this.setLayout(null);
		Border out = BorderFactory.createEmptyBorder(5,  5,  5,  5);
		Border in = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		this.setBorder(BorderFactory.createCompoundBorder(out, in));
		Dimension d = new Dimension(728, 41);
		this.setMinimumSize(d);
		this.setSize(d);
		this.setMaximumSize(d);

		JLabel label = new JLabel("" + linie.getNummer());
		label.setBounds(10, 13, 25, 14);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(label);
		
		sperre = new JCheckBox("");
		sperre.setBounds(55, 9, 23, 23);
		sperre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (sperre.isSelected()) {
					Member s = (Member) schuetze.getSelectedItem();
					Discipline d = (Discipline) disziplin.getSelectedItem();
					if (s == null || d == null) {
						sperre.setSelected(false);
						JOptionPane.showMessageDialog(	null,
								"Ein Sperren der Linie ist erst nach Auswahl eines Sch�tzen und einer Disziplin möglich.",
								"Fehler",
								JOptionPane.WARNING_MESSAGE);
					} else {
						Single e = linie.configure(s, d);
						if (e != null) {
							int answer = JOptionPane.showConfirmDialog(	null,
									"Der Schütze ist in dieser Disziplin bereits einmal gestartet ohne den Wettkampf abzuschlie�en. Soll der Wettkampf fortgesetzt werden?",
									"Information",
									JOptionPane.YES_NO_OPTION);
							if (answer == JOptionPane.NO_OPTION) {
								linie.configure(e);
							}
						}
						linie.setStatus(Status.SPERREN);
					}
				} else {
					linie.setStatus(Status.ENTSPERREN);
				}
			}
		});
		this.add(sperre);
		
		start = new JCheckBox("");
		start.setBounds(101, 9, 23, 23);
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
		wertung.setBounds(147, 9, 23, 23);
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

		schuetze = new JComboBox<Member>(linie.getSchuetzenModel());
		schuetze.setBounds(193, 10, 200, 22);
		this.add(schuetze);
		
		disziplin = new JComboBox<Discipline>(linie.getDisziplinenModel());
		disziplin.setBounds(416, 10, 200, 22);
		this.add(disziplin);

		frei = new JButton("Frei");
		frei.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				schuetze.setSelectedItem(null);
				disziplin.setSelectedItem(null);
				linie.setStatus(Status.FREI);
			}
		});
		frei.setBounds(639, 10, 80, 22);
		this.add(frei);
		
		setEnabled(false);
	}

	public boolean isBusy() {
		return linie.isBusy();
	}

	public boolean isError() {
		return linie.isError();
	}

	public boolean isFrei() {
		return linie.isFrei();
	}

	@Override
	public void lineChanged(LineModel lm, int type) {
		if (type == DefaultLineModel.STATE_CHANGED) {
			setEnabled(!linie.isBusy());
			sperre.setSelected(linie.isGesperrt());
			start.setSelected(linie.isGestartet());
			wertung.setSelected(linie.inMatch());
			repaint();
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		Color c = enabled ? null : (linie.isError() ? Color.red : Color.ORANGE);
		sperre.setBackground(c);
		start.setBackground(c);
		wertung.setBackground(c);
		schuetze.setEnabled(enabled && !linie.isGesperrt());
		disziplin.setEnabled(enabled && !linie.isGesperrt());
		sperre.setEnabled(enabled && !linie.isGestartet());
		start.setEnabled(enabled && linie.isGesperrt());
		wertung.setEnabled(enabled && linie.canSwitchPM());
		frei.setEnabled(enabled && !linie.isGesperrt() && !linie.isFrei());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (linie.isError()) {
			g.setColor(Color.RED);
			g.fillRect(5, 5, 718, 31);
			return;
		}
		if (!this.isEnabled()) {
			g.setColor(Color.ORANGE);
			g.fillRect(5, 5, 718, 31);
		}
	}
}