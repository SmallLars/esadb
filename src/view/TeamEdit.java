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
import model.Single;
import model.Start;
import controller.Controller;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import java.awt.Font;


@SuppressWarnings("serial")
public class TeamEdit extends JDialog implements ComponentListener, ActionListener, ListSelectionListener {

	private Controller controller;
	private List<Single> ergebnisse;
	
	private DefaultComboBoxModel<Discipline> modelD;
	private JComboBox<Discipline> disziplin;

	private DefaultComboBoxModel<Single> modelS;
	private JComboBox<Single> start;

	private JButton button;

	private JButton button_1;

	private JLabel lblZwischenablage;

	private JScrollPane scrollPane_1;
	private JTable table_1;
	
	private JButton cancelButton;

	public TeamEdit(Frame parent) {
		super(parent, "Mannschaften bearbeiten");

		this.controller = Controller.get();
		ergebnisse = new Vector<Single>();
		for (Start s : controller.getModel().getErgebnisse()) {
			if (s instanceof Single) ergebnisse.add((Single) s);
		}

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		Dimension d = new Dimension(702, 500);
		setMinimumSize(d);
		setSize(d);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		modelD = new DefaultComboBoxModel<Discipline>();
		disziplin = new JComboBox<Discipline>(modelD);
		disziplin.setBounds(382, 44, 300, 22);
		disziplin.setActionCommand("DISZIPLIN");
		disziplin.addActionListener(this);
		getContentPane().add(disziplin);

		modelS = new DefaultComboBoxModel<Single>();
		start = new JComboBox<Single>(modelS);
		start.setBounds(382, 77, 300, 22);
		start.setActionCommand("START");
		start.addActionListener(this);
		getContentPane().add(start);

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

		lblZwischenablage = new JLabel("Einzelergebnisse");
		lblZwischenablage.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblZwischenablage.setBounds(382, 11, 300, 22);
		getContentPane().add(lblZwischenablage);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(382, 110, 300, 318);
		getContentPane().add(scrollPane_1);
		
		table_1 = new JTable(new StartTableModel(controller.getModel().getErgebnisse()));
		table_1.setDefaultRenderer(Start.class, new StartTableCellRenderer());
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.getSelectionModel().addListSelectionListener(this);
		table_1.getTableHeader().setReorderingAllowed(false);
		scrollPane_1.setViewportView(table_1);

		cancelButton = new JButton("Schließen");
		cancelButton.setBounds(584, 439, 100, 23);
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		getContentPane().add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);

		addComponentListener(this);

		for (Single e : ergebnisse) {
			if (modelD.getIndexOf(e.getDisziplin()) == -1) {
				disziplin.addItem(e.getDisziplin());
			}
		}
		setSchutzeItems();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
			case "CANCEL":
				setVisible(false);
				break;
			case "DISZIPLIN":
				setSchutzeItems();
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
}