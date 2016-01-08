package view;


import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JComboBox;

import model.Discipline;
import model.Start;
import controller.Controller;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;


@SuppressWarnings("serial")
public class ResultListOptions extends JDialog implements ActionListener {

	private DefaultComboBoxModel<Discipline> modelD;
	private JCheckBox chckbxDiscipline;
	private JComboBox<Discipline> cbDisziplin;
	private JCheckBox chckbxNewPage;
	private JCheckBox chckbxGender;
	private JCheckBox chckbxGroup;
	private JLabel lblDisziplinen;
	private JSeparator separator;
	private JLabel lblGruppen;
	private JSeparator separator_1;
	public ResultListOptions(Frame parent) {
		super(parent, "Ergebnisliste");
		setResizable(false);

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 348, 278);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		Container contentPanel = getContentPane();
		contentPanel.setLayout(null);

		lblDisziplinen = new JLabel("Disziplinen");
		lblDisziplinen.setBounds(10, 11, 300, 14);
		getContentPane().add(lblDisziplinen);

		chckbxDiscipline = new JCheckBox("Nur folgende Disziplin auswerten:");
		chckbxDiscipline.setBounds(10, 29, 300, 23);
		chckbxDiscipline.addActionListener(this);
		getContentPane().add(chckbxDiscipline);

		modelD = new DefaultComboBoxModel<Discipline>();
		cbDisziplin = new JComboBox<Discipline>(modelD);
		cbDisziplin.setBounds(32, 59, 300, 22);
		for (Start s : Controller.get().getModel().getErgebnisse()) {
			if (modelD.getIndexOf(s.getDisziplin()) == -1) {
				cbDisziplin.addItem(s.getDisziplin());
			}
		}
		cbDisziplin.setEnabled(false);
		contentPanel.add(cbDisziplin);

		chckbxNewPage = new JCheckBox("Neue Seite für jede Disziplin");
		chckbxNewPage.setBounds(10, 88, 300, 23);
		getContentPane().add(chckbxNewPage);

		separator = new JSeparator();
		separator.setBounds(10, 118, 322, 2);
		getContentPane().add(separator);

		lblGruppen = new JLabel("Gruppen");
		lblGruppen.setBounds(10, 132, 200, 14);
		getContentPane().add(lblGruppen);

		chckbxGender = new JCheckBox("Nach Geschlecht trennen");
		chckbxGender.setSelected(true);
		chckbxGender.setBounds(10, 150, 300, 23);
		chckbxGender.addActionListener(this);
		contentPanel.add(chckbxGender);
		
		chckbxGroup = new JCheckBox("Nach Altersgruppen trennen");
		chckbxGroup.setSelected(true);
		chckbxGroup.setBounds(10, 176, 300, 23);
		chckbxGroup.addActionListener(this);
		contentPanel.add(chckbxGroup);

		separator_1 = new JSeparator();
		separator_1.setBounds(10, 206, 322, 2);
		getContentPane().add(separator_1);

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.setBounds(10, 219, 120, 23);
		contentPanel.add(cancelButton);
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);

		JButton okButton = new JButton("Speichern");
		okButton.setBounds(212, 219, 120, 23);
		contentPanel.add(okButton);
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		getRootPane().setDefaultButton(okButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "OK":
				// TODO speichern
				setVisible(false);
				break;
			case "CANCEL":
				setVisible(false);
				break;
			default:
				cbDisziplin.setEnabled(chckbxDiscipline.isSelected());
				chckbxNewPage.setEnabled(!chckbxDiscipline.isSelected());
		}
	}
}