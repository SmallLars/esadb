package view.member;


import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;

import controller.Controller;
import controller.KampfDB;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.Member;
import model.Club;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableRowSorter;

import view.IconButton;


@SuppressWarnings("serial")
public class Members extends JDialog implements ComponentListener, ActionListener {

	private Controller controller;
	private Member[] schuetzen;

	private JScrollPane scrollPane;
	private MemberTableModel tmodel;
	private TableRowSorter<MemberTableModel> sorter;
	private JTable table;

	private JButton button;

	private JScrollPane scrollPane_1;
	private MemberTableModel tmodel_1;
	private TableRowSorter<MemberTableModel> sorter_1;
	private JTable table_1;

	private JLabel lblVerein;
	private JComboBox<Club> comboBox;

	private JButton cancelButton;

	public Members(Frame parent) {
		super(parent, "Schützen");

		this.controller = Controller.get();
		schuetzen = KampfDB.getAllSchuetzen();

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		Dimension d = new Dimension(780, 500);
		setMinimumSize(d);
		setSize(d);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 340, 418);
		getContentPane().add(scrollPane);

		tmodel = new MemberTableModel(schuetzen);
		sorter = new TableRowSorter<MemberTableModel>(tmodel);
		sorter.setSortsOnUpdates(true);
		table = new JTable(tmodel);
		table.setRowSorter(sorter);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);

		button = new IconButton(IconButton.LEFT, "ADD", this);
		button.setBounds(360, 209, 50, 23);
		getContentPane().add(button);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(420, 11, 340, 418);
		getContentPane().add(scrollPane_1);

		tmodel_1 = new MemberTableModel(schuetzen);
		sorter_1 = new TableRowSorter<MemberTableModel>(tmodel_1);
		sorter_1.setSortsOnUpdates(true);
		table_1 = new JTable(tmodel_1);
		table_1.setRowSorter(sorter_1);
		table_1.getTableHeader().setReorderingAllowed(false);
		scrollPane_1.setViewportView(table_1);
		
		lblVerein = new JLabel("Filter:");
		lblVerein.setBounds(20, 443, 46, 14);
		getContentPane().add(lblVerein);

		comboBox = new JComboBox<Club>(KampfDB.getVereine());
		comboBox.insertItemAt(new Club(0,  "Alle Vereine"), 0);
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(69, 439, 251, 22);
		comboBox.setActionCommand("FILTER");
		comboBox.addActionListener(this);
		getContentPane().add(comboBox);
		sorter.setRowFilter(new MemberRowFilter(true, comboBox));
		sorter_1.setRowFilter(new MemberRowFilter(false, comboBox));

		cancelButton = new JButton("Schließen");
		cancelButton.setBounds(660, 439, 100, 23);
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		getContentPane().add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);

		addComponentListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "ADD":
				Vector<Object> changed = new Vector<Object>();
				for (int i : table_1.getSelectedRows()) {
					Object o = table_1.getValueAt(i, -1);
					if (!controller.contains(o)) {
						controller.add(o);
						changed.add(o);
					}
				}
				for (Object o : changed) {
					int listIndex = Arrays.asList(schuetzen).indexOf(o);
					table.tableChanged(new TableModelEvent(tmodel, listIndex));
					table_1.tableChanged(new TableModelEvent(tmodel_1, listIndex));
				}
				table.revalidate();
				table_1.revalidate();
				scrollPane.repaint();
				scrollPane_1.repaint();
				break;
			case "FILTER":
				tmodel.fireTableDataChanged();
				tmodel_1.fireTableDataChanged();
				break;
			case "CANCEL":
				setVisible(false);
				break;
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Dimension d = getContentPane().getSize();
		scrollPane.setBounds(10, 11, (d.width - 92) / 2, d.height - 55);
		button.setLocation((d.width / 2) - 26, scrollPane.getHeight() / 2);
		scrollPane_1.setLocation((d.width / 2) + 34, 11);
		scrollPane_1.setSize((d.width - 92) / 2, d.height - 55);
		lblVerein.setLocation(20, d.height - 30);
		comboBox.setLocation(69, d.height - 34);
		cancelButton.setLocation(d.width - 112, d.height - 34);
	}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}
}
