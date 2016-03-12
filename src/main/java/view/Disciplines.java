package view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Discipline;

import javax.swing.JComboBox;
import javax.swing.JLabel;


@SuppressWarnings("serial")
public class Disciplines extends JDialog implements ActionListener {

	private JComboBox<Discipline> comboBox;
	private JLabel lblName;
	private JLabel lblWertung;
	private JLabel lblZeit;
	private JLabel lblSchusszahl;
	private JLabel lblSerienlaenge;
	private JLabel lblWaffengattung;

	private final JPanel contentPanel = new JPanel();

	public Disciplines(Frame parent, Discipline[] disziplinen) {
		super(parent, "Disziplinen");
		setResizable(false);

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(450, 300);
		setLocationRelativeTo(parent);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		lblName = new JLabel("Name");
		lblName.setBounds(10, 11, 422, 14);
		contentPanel.add(lblName);
		
		lblWertung = new JLabel("Wertung");
		lblWertung.setBounds(10, 36, 422, 14);
		contentPanel.add(lblWertung);
		
		lblZeit = new JLabel("Zeit");
		lblZeit.setBounds(10, 61, 422, 14);
		contentPanel.add(lblZeit);
		
		lblSchusszahl = new JLabel("Schusszahl");
		lblSchusszahl.setBounds(10, 86, 422, 14);
		contentPanel.add(lblSchusszahl);
		
		lblSerienlaenge = new JLabel("Serienlänge");
		lblSerienlaenge.setBounds(10, 111, 422, 14);
		contentPanel.add(lblSerienlaenge);
		
		lblWaffengattung = new JLabel("Waffengattung");
		lblWaffengattung.setBounds(10, 136, 422, 14);
		contentPanel.add(lblWaffengattung);

		JPanel comboPane = new JPanel();
		comboPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		getContentPane().add(comboPane, BorderLayout.NORTH);

		comboBox = new JComboBox<Discipline>(disziplinen);
		comboBox.setPreferredSize(new Dimension(257, 22));
		comboBox.setActionCommand("DISZIPLIN");
		comboBox.addActionListener(this);
		comboPane.add(comboBox);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton cancelButton = new JButton("Schließen");
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);

		actionPerformed(new ActionEvent(comboBox, 0, "DISZIPLIN"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "DISZIPLIN":
				Discipline d = (Discipline) comboBox.getSelectedItem();
				lblName.setText(d.toString());
				lblWertung.setText(d.getWertung() == 0 ? "Ringwertung" : "Zehntelwertung");
				if (d.getProbezeit() > 0) {
					lblZeit.setText(String.format("Zeit für Probe: %d Minuten, Zeit für Wertung: %d Minuten.", d.getProbezeit(), d.getSchiesszeit()));
				} else {
					lblZeit.setText(String.format("Zeit für den Wettkampf: %d Minuten.", d.getSchiesszeit()));
				}
				String anzahl = "";
				if (d.getProbezeit() >= 0) {
					if (d.getProbeschuesse() == 0) anzahl = "Probe: beliebig, ";
					else anzahl = String.format("Probe: %d Schuß, ", d.getProbeschuesse());
				}
				if (d.getStellungsanzahl() == 1) {
					anzahl = anzahl.concat(String.format("Wertung: %d Schuß.", d.getSchusszahl()));
				} else {
					anzahl = anzahl.concat(String.format("Wertung: %d Mal %d Schuß.", d.getStellungsanzahl(), d.getSchusszahl() / d.getStellungsanzahl()));
				}
				lblSchusszahl.setText(anzahl);
				lblSerienlaenge.setText(String.format("Serienlänge: %d.", d.getSerienlaenge()));
				lblWaffengattung.setText("Regel: " + d.getRuleNumber() + " (" + Controller.get().getRule(d.getRuleNumber()) + ")");
				break;
			case "CANCEL":
				setVisible(false);
				break;
		}
	}
}
