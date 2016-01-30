package view.teamedit;


import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import model.Discipline;
import model.Gender;
import model.Group;
import model.Single;
import model.Result;
import model.Team;
import controller.Controller;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import view.TableEditor;


@SuppressWarnings("serial")
public class TeamEdit extends JDialog implements ActionListener, ListSelectionListener {

	private Controller controller;
	private List<Single> ergebnisse;
	
	private JComboBox<Discipline> disziplin;
	private JComboBox<Group> group;

	private JButton button;
	private JButton button_1;

	private JScrollPane scrollPane_1;
	private ResultTableModel tmodel;
	private TableRowSorter<ResultTableModel> sorter;
	private JTable table_1;
	
	private JButton cancelButton;
	private JSeparator separator;

	public TeamEdit(Frame parent) {
		super(parent, "Mannschaften bearbeiten");
		setResizable(false);

		this.controller = Controller.get();
		ergebnisse = new Vector<Single>();
		for (Result s : controller.getModel().getErgebnisse()) {
			if (s instanceof Single) ergebnisse.add((Single) s);
		}

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(632, 500);
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
		group.setActionCommand("GROUP");
		group.addActionListener(this);
		getContentPane().add(group);

		button = new JButton("-");
		button.setBounds(14, 391, 50, 23);
		button.setActionCommand("REMOVE");
		button.addActionListener(this);
		getContentPane().add(button);

		button_1 = new JButton("+");
		button_1.setBounds(564, 391, 50, 23);
		button_1.setActionCommand("ADD");
		button_1.addActionListener(this);
		getContentPane().add(button_1);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 57, 600, 320);
		getContentPane().add(scrollPane_1);

		tmodel = new ResultTableModel(controller.getModel().getErgebnisse());
		sorter = new TableRowSorter<ResultTableModel>(tmodel);
		sorter.setSortsOnUpdates(true);
		sorter.setRowFilter(new ResultRowFilter(disziplin, group, true));
		table_1 = new JTable(tmodel);
		table_1.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table_1.setRowSorter(sorter);
		table_1.setDefaultRenderer(Float.class, new DefaultTableCellRenderer() {
			{setHorizontalAlignment(SwingConstants.RIGHT);}
			
			@Override
			protected void setValue(Object o) {
				super.setValue(String.format("%.1f", (Float) o));
			}
		});
		table_1.getColumnModel().getColumn(1).setCellEditor(new TableEditor(new JComboBox<Discipline>(new DefaultComboBoxModel<Discipline>(Controller.get().getDisziplinen())), 0));
		table_1.getColumnModel().getColumn(2).setCellEditor(new TableEditor(new JComboBox<Group>(new DefaultComboBoxModel<Group>(Controller.get().getConfig().getGroups())), 0));
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.getSelectionModel().addListSelectionListener(this);
		table_1.getTableHeader().setReorderingAllowed(false);
		
		table_1.getColumnModel().getColumn(3).setMinWidth(70);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(70);
		table_1.getColumnModel().getColumn(3).setMaxWidth(70);

		scrollPane_1.setViewportView(table_1);

		cancelButton = new JButton("Schließen");
		cancelButton.setBounds(514, 438, 100, 23);
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		getContentPane().add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);
		
		separator = new JSeparator();
		separator.setBounds(14, 44, 600, 2);
		getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(14, 425, 600, 2);
		getContentPane().add(separator_1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "CANCEL":
				setVisible(false);
				break;
			case "DISZIPLIN":
			case "GROUP":
				tmodel.fireTableDataChanged();
				break;
			case "REMOVE":
				int row = table_1.getSelectedRow();
				if (row >= 0) {
					controller.remove(table_1.getValueAt(row, 0));
					tmodel.fireTableDataChanged();
				}
				break;
			case "ADD":
				controller.add(new Team(null, null));
				tmodel.fireTableDataChanged();
				break;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int row = table_1.getSelectedRow();
		if (row == -1) return;
	}
}