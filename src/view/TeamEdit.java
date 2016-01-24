package view;


import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JComboBox;

import model.Discipline;
import model.Gender;
import model.Group;
import model.Single;
import model.Result;
import controller.Controller;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JSeparator;


@SuppressWarnings("serial")
public class TeamEdit extends JDialog implements ComponentListener, ActionListener, ListSelectionListener {

	private Controller controller;
	private List<Single> ergebnisse;
	
	private JComboBox<Discipline> disziplin;
	private JComboBox<Group> group;

	private JButton button;
	private JButton button_1;

	private JLabel lblEinzelergebnisse;

	private JScrollPane scrollPane_1;
	private JTable table_1;
	
	private JButton cancelButton;
	private JSeparator separator;

	public TeamEdit(Frame parent) {
		super(parent, "Mannschaften bearbeiten");

		this.controller = Controller.get();
		ergebnisse = new Vector<Single>();
		for (Result s : controller.getModel().getErgebnisse()) {
			if (s instanceof Single) ergebnisse.add((Single) s);
		}

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		Dimension d = new Dimension(902, 500);
		setMinimumSize(d);
		setSize(d);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblFilter = new JLabel("Filter:");
		lblFilter.setBounds(14, 15, 64, 14);
		getContentPane().add(lblFilter);

		disziplin = new JComboBox<Discipline>(new DefaultComboBoxModel<Discipline>(Controller.get().getDisziplinen()));
		disziplin.insertItemAt(new Discipline("Alle Disziplinen"), 0);
		disziplin.setSelectedIndex(0);
		disziplin.setBounds(84, 11, 200, 22);
		disziplin.setActionCommand("DISZIPLIN");
		disziplin.addActionListener(this);
		getContentPane().add(disziplin);

		group = new JComboBox<Group>(new DefaultComboBoxModel<Group>(Controller.get().getConfig().getGroups()));
		group.insertItemAt(new Group("Alle Gruppen", 0, 0, Gender.ANY), 0);
		group.setSelectedIndex(0);
		group.setBounds(298, 11, 200, 22);
		group.setActionCommand("START");
		group.addActionListener(this);
		getContentPane().add(group);

		button = new JButton("->");
		button.setBounds(320, 173, 50, 23);
		button.setActionCommand("REMOVE");
		button.addActionListener(this);
		getContentPane().add(button);

		button_1 = new JButton("<-");
		button_1.setBounds(320, 308, 50, 23);
		button_1.setActionCommand("ADD");
		button_1.addActionListener(this);
		getContentPane().add(button_1);

		lblEinzelergebnisse = new JLabel("Einzelergebnisse");
		lblEinzelergebnisse.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblEinzelergebnisse.setBounds(382, 57, 300, 22);
		getContentPane().add(lblEinzelergebnisse);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(382, 90, 500, 338);
		getContentPane().add(scrollPane_1);
		
		table_1 = new JTable(new ResultTableModel(controller.getModel().getErgebnisse()));
		table_1.setDefaultRenderer(Result.class, new ResultTableCellRenderer());
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.getSelectionModel().addListSelectionListener(this);
		table_1.getTableHeader().setReorderingAllowed(false);
		
		table_1.getColumnModel().getColumn(3).setMinWidth(70);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(70);
		table_1.getColumnModel().getColumn(3).setMaxWidth(70);

		scrollPane_1.setViewportView(table_1);

		cancelButton = new JButton("Schließen");
		cancelButton.setBounds(784, 439, 100, 23);
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		getContentPane().add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);
		
		separator = new JSeparator();
		separator.setBounds(14, 44, 868, 2);
		getContentPane().add(separator);

		addComponentListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
			case "CANCEL":
				setVisible(false);
				break;
			case "DISZIPLIN":
				break;
			case "START":
				break;
			case "REMOVE":
				break;
			case "ADD":
				break;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int row = table_1.getSelectedRow();
		if (row == -1) return;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		/*
		Dimension d = getContentPane().getSize();
		disziplin.setSize(				(d.width - 92) / 2, 22);
		start.setSize(					(d.width - 92) / 2, 22);
		scrollPane.setSize(				(d.width - 92) / 2, d.height - 122);
		button.setLocation(				(d.width / 2) - 26, -2 + scrollPane.getHeight() / 2);
		rdbtnProbe.setLocation(			(d.width / 2) - 32, 55 + scrollPane.getHeight() / 2);
		rdbtnMatch.setLocation(			(d.width / 2) - 32, 74 + scrollPane.getHeight() / 2);
		spinner.setLocation(			(d.width / 2) - 26, 104 + scrollPane.getHeight() / 2);
		button_1.setLocation(			(d.width / 2) - 26, 133 + scrollPane.getHeight() / 2);
		lblZwischenablage.setLocation(	(d.width / 2) + 34, 44);
		lblZwischenablage.setSize(		(d.width - 92) / 2, 22);
		scrollPane_1.setLocation(		(d.width / 2) + 34, 77);
		scrollPane_1.setSize(			(d.width - 92) / 2, d.height - 122);
		cancelButton.setLocation(		d.width - 112, d.height - 34);
		*/
	}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}
}