package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;

import controller.Controller;
import controller.KampfDB;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.Schuetze;
import model.Verein;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableRowSorter;


@SuppressWarnings("serial")
public class Schuetzen extends JDialog implements ComponentListener, ActionListener {

	private Controller controller;
	private Vector<Schuetze> schuetzen;

	private JScrollPane scrollPane;
	private SchuetzenTableModel tmodel;
	private TableRowSorter<SchuetzenTableModel> sorter;
	private JTable table;

	private JButton button;

	private JScrollPane scrollPane_1;
	private SchuetzenTableModel tmodel_1;
	private TableRowSorter<SchuetzenTableModel> sorter_1;
	private JTable table_1;

	private JLabel lblVerein;
	private JComboBox<Verein> comboBox;

	private JButton cancelButton;

	public Schuetzen(Frame parent, Controller controller) {
		super(parent, "Schützen");

		this.controller = controller;
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

		tmodel = new SchuetzenTableModel(schuetzen);
		sorter = new TableRowSorter<SchuetzenTableModel>(tmodel);
		sorter.setSortsOnUpdates(true);
		sorter.setRowFilter(new SchuetzenRowFilter(controller, true, null));
		table = new JTable(tmodel);
		table.setRowSorter(sorter);
		scrollPane.setViewportView(table);

		button = new JButton("<-");
		button.setBounds(360, 209, 50, 23);
		button.setActionCommand("ADD");
		button.addActionListener(this);
		getContentPane().add(button);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(420, 11, 340, 418);
		getContentPane().add(scrollPane_1);

		tmodel_1 = new SchuetzenTableModel(schuetzen);
		sorter_1 = new TableRowSorter<SchuetzenTableModel>(tmodel_1);
		sorter_1.setSortsOnUpdates(true);
		sorter_1.setRowFilter(new SchuetzenRowFilter(controller, false, null));
		table_1 = new JTable(tmodel_1);
		table_1.setRowSorter(sorter_1);
		scrollPane_1.setViewportView(table_1);
		
		lblVerein = new JLabel("Verein");
		lblVerein.setBounds(20, 443, 46, 14);
		getContentPane().add(lblVerein);

		Vector<Verein> vereine = KampfDB.getVereine();
		vereine.add(0, null);
		comboBox = new JComboBox<Verein>(vereine);
		comboBox.setBounds(69, 439, 251, 22);
		comboBox.setActionCommand("FILTER");
		comboBox.addActionListener(this);
		getContentPane().add(comboBox);

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
					int listIndex = schuetzen.indexOf(o);
					table.tableChanged(new TableModelEvent(tmodel, listIndex));
					table_1.tableChanged(new TableModelEvent(tmodel_1, listIndex));
				}
				table.revalidate();
				table_1.revalidate();
				scrollPane.repaint();
				scrollPane_1.repaint();
				break;
			case "FILTER":
				sorter.setRowFilter(new SchuetzenRowFilter(controller, true, (Verein) comboBox.getSelectedItem()));
				sorter_1.setRowFilter(new SchuetzenRowFilter(controller, false, (Verein) comboBox.getSelectedItem()));
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
