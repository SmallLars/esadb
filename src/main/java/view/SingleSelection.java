package view;


import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import controller.Controller;
import model.Discipline;
import model.Result;
import model.Single;

import javax.swing.JComboBox;
import javax.swing.JCheckBox;


@SuppressWarnings("serial")
public class SingleSelection extends JDialog implements ActionListener {

	private List<Single> ergebnisse;
	private boolean okKlick;
	
	private DefaultComboBoxModel<Discipline> modelD;
	private JComboBox<Discipline> disziplin;
	private DefaultComboBoxModel<Single> modelS;
	private JComboBox<Single> start;
	private JCheckBox chckbxProbe;
	private JCheckBox chckbxMatch;
	private JCheckBox chckbxFactor;

	public SingleSelection(Frame parent) {
		super(parent, "Ergebnisauswahl");
		setResizable(false);

		ergebnisse = new Vector<Single>();
		for (Result s : Controller.get().getModel().getErgebnisse()) {
			if (s instanceof Single) ergebnisse.add((Single) s);
		}

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(450, 300);
		if (getGraphicsConfiguration() != null) setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		Container contentPanel = getContentPane();
		contentPanel.setLayout(null);

		modelD = new DefaultComboBoxModel<Discipline>();
		disziplin = new JComboBox<Discipline>(modelD);
		disziplin.setBounds(10, 11, 422, 22);
		disziplin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setSchutzeItems();
			}
		});
		contentPanel.add(disziplin);

		modelS = new DefaultComboBoxModel<Single>();
		start = new JComboBox<Single>(modelS);
		start.setBounds(10, 59, 422, 22);
		contentPanel.add(start);

		chckbxProbe = new JCheckBox("Probe");
		chckbxProbe.setSelected(Single.print == Single.PROBE || Single.print == Single.BOTH);
		chckbxProbe.setBounds(10, 106, 200, 23);
		contentPanel.add(chckbxProbe);
		
		chckbxMatch = new JCheckBox("Match");
		chckbxMatch.setSelected(Single.print == Single.MATCH || Single.print == Single.BOTH);
		chckbxMatch.setBounds(10, 132, 200, 23);
		contentPanel.add(chckbxMatch);

		chckbxFactor = new JCheckBox("Beste Teiler anzeigen");
		chckbxFactor.setSelected(Single.factor);
		chckbxFactor.setBounds(10, 158, 200, 23);
		contentPanel.add(chckbxFactor);

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.setBounds(10, 241, 120, 23);
		contentPanel.add(cancelButton);
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);

		JButton okButton = new JButton("Anzeigen");
		okButton.setBounds(312, 241, 120, 23);
		contentPanel.add(okButton);
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		getRootPane().setDefaultButton(okButton);
	}

	public Single showDialog() {
		okKlick = false;
		disziplin.removeAllItems();
		for (Single e : ergebnisse) {
			if (modelD.getIndexOf(e.getDisziplin()) == -1) {
				disziplin.addItem(e.getDisziplin());
			}
		}
		setSchutzeItems();
		setVisible(true);
		if (okKlick) {
			return (Single) start.getSelectedItem();
		}
		return null;
	}

	private void setSchutzeItems() {
		start.removeAllItems();
		for (Single e : ergebnisse) {
			if (e.getDisziplin() == disziplin.getSelectedItem()) {
				if (modelS.getIndexOf(e) == -1) {
					start.addItem((Single) e);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("OK")) {
			if (!chckbxProbe.isSelected() && !chckbxMatch.isSelected()) {
				JOptionPane.showMessageDialog(
					this,
					"Probe und/oder Match muss ausgewählt werden.",
					"Fehler",
					JOptionPane.WARNING_MESSAGE
				);
				return;
			}
			if (chckbxProbe.isSelected() && chckbxMatch.isSelected()) {
				Single.print = Single.BOTH;
			} else {
				if (chckbxProbe.isSelected()) Single.print = Single.PROBE;
				else Single.print = Single.MATCH;
			}
			Single.factor = chckbxFactor.isSelected();
			okKlick = true;
		}
		setVisible(false);
	}
}