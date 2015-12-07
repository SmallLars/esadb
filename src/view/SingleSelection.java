package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;

import model.Discipline;
import model.Single;
import model.Start;
import controller.Controller;

import javax.swing.JCheckBox;


@SuppressWarnings("serial")
public class SingleSelection extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private List<Single> ergebnisse;
	private boolean okKlick;
	
	private DefaultComboBoxModel<Discipline> modelD;
	private JComboBox<Discipline> disziplin;
	private DefaultComboBoxModel<Single> modelS;
	private JComboBox<Single> start;
	private JCheckBox chckbxProbe;
	private JCheckBox chckbxMatch;

	public SingleSelection(Frame parent, Controller controller) {
		super(parent, "Ergebnisauswahl");
		setResizable(false);

		ergebnisse = new Vector<Single>();
		for (Start s : controller.getModel().getErgebnisse()) {
			if (s instanceof Single) ergebnisse.add((Single) s);
		}

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
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
		chckbxProbe.setBounds(10, 106, 97, 23);
		contentPanel.add(chckbxProbe);
		
		chckbxMatch = new JCheckBox("Match");
		chckbxMatch.setSelected(Single.print == Single.MATCH || Single.print == Single.BOTH);
		chckbxMatch.setBounds(10, 132, 97, 23);
		contentPanel.add(chckbxMatch);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);
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
				JOptionPane.showMessageDialog(	this,
												"Probe und/oder Match muss ausgewählt werden.",
												"Fehler",
												JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (chckbxProbe.isSelected() && chckbxMatch.isSelected()) {
				Single.print = Single.BOTH;
			} else {
				if (chckbxProbe.isSelected()) Single.print = Single.PROBE;
				else Single.print = Single.MATCH;
			}
			okKlick = true;
		}
		setVisible(false);
	}
}