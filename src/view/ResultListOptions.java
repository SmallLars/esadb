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
import javax.swing.SwingConstants;


@SuppressWarnings("serial")
public class ResultListOptions extends JDialog implements ActionListener {

	private String action;

	private JCheckBox chckbxGender;
	private JCheckBox chckbxGroup;

	private JCheckBox chckbxDiscipline;
	private DefaultComboBoxModel<Discipline> modelD;
	private JComboBox<Discipline> cbDisziplin;
	private JCheckBox chckbxNewPage;

	public ResultListOptions(Frame parent) {
		super(parent, "Ergebnisliste");
		setResizable(false);

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(536, 188);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		Container contentPanel = getContentPane();
		contentPanel.setLayout(null);

		JLabel lblGruppen = new JLabel("Gruppen");
		lblGruppen.setBounds(10, 11, 200, 14);
		contentPanel.add(lblGruppen);

		chckbxGender = new JCheckBox("Nach Geschlecht trennen");
		chckbxGender.setSelected(true);
		chckbxGender.setBounds(10, 29, 200, 23);
		chckbxGender.addActionListener(this);
		contentPanel.add(chckbxGender);

		chckbxGroup = new JCheckBox("Nach Altersgruppen trennen");
		chckbxGroup.setSelected(true);
		chckbxGroup.setBounds(10, 58, 200, 23);
		chckbxGroup.addActionListener(this);
		contentPanel.add(chckbxGroup);

		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(235, 11, 2, 95);
		contentPanel.add(separator_1);

		JLabel lblDisziplinen = new JLabel("Disziplinen");
		lblDisziplinen.setBounds(252, 11, 168, 14);
		contentPanel.add(lblDisziplinen);

		chckbxDiscipline = new JCheckBox("Nur folgende Disziplin auswerten:");
		chckbxDiscipline.setSelected(false);
		chckbxDiscipline.setBounds(252, 29, 268, 23);
		chckbxDiscipline.addActionListener(this);
		contentPanel.add(chckbxDiscipline);

		modelD = new DefaultComboBoxModel<Discipline>();
		cbDisziplin = new JComboBox<Discipline>(modelD);
		cbDisziplin.setBounds(274, 59, 246, 22);
		for (Start s : Controller.get().getModel().getErgebnisse()) {
			if (modelD.getIndexOf(s.getDisziplin()) == -1) {
				cbDisziplin.addItem(s.getDisziplin());
			}
		}
		cbDisziplin.setEnabled(false);
		contentPanel.add(cbDisziplin);

		chckbxNewPage = new JCheckBox("Neue Seite für jede Disziplin");
		chckbxNewPage.setBounds(252, 87, 268, 23);
		contentPanel.add(chckbxNewPage);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 116, 510, 2);
		contentPanel.add(separator);

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.setBounds(10, 129, 120, 23);
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		contentPanel.add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);

		JButton printButton = new JButton("Drucken");
		printButton.setBounds(270, 129, 120, 23);
		printButton.setActionCommand("PRINT");
		printButton.addActionListener(this);
		contentPanel.add(printButton);

		JButton showButton = new JButton("Anzeigen");
		showButton.setBounds(400, 129, 120, 23);
		showButton.setActionCommand("SHOW");
		showButton.addActionListener(this);
		contentPanel.add(showButton);
	}

	public String showDialog() {
		setVisible(true);
		return action;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		action = e.getActionCommand();
		switch (action) {
			case "CANCEL":
			case "PRINT":
			case "SHOW":
				setVisible(false);
				break;
			default:
				cbDisziplin.setEnabled(chckbxDiscipline.isSelected());
				chckbxNewPage.setEnabled(!chckbxDiscipline.isSelected());
		}
	}
}