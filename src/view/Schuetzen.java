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
import javax.swing.RowFilter;

import controller.Controller;
import controller.KampfDB;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.Schuetze;
import model.Verein;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.table.TableRowSorter;

@SuppressWarnings("serial")
public class Schuetzen extends JDialog {

	public Schuetzen(Frame parent, Controller controller) {
		super(parent, "Schützen");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		Dimension d = new Dimension(780, 582);
		setMinimumSize(d);
		setSize(d);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 340, 500);
		getContentPane().add(scrollPane);

		SchuetzenTableModel tmodel = new SchuetzenTableModel(new Vector<Schuetze>(controller.getSchuetzen()));
		TableRowSorter<SchuetzenTableModel> sorter = new TableRowSorter<SchuetzenTableModel>(tmodel);
		JTable table = new JTable(tmodel);
		table.setRowSorter(sorter);
		scrollPane.setViewportView(table);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(420, 11, 340, 500);
		getContentPane().add(scrollPane_1);
		
		SchuetzenTableModel tmodel_1 = new SchuetzenTableModel(KampfDB.getNewSchuetzen());
		TableRowSorter<SchuetzenTableModel> sorter_1 = new TableRowSorter<SchuetzenTableModel>(tmodel_1);
		JTable table_1 = new JTable(tmodel_1);
		table_1.setRowSorter(sorter_1);
		scrollPane_1.setViewportView(table_1);
		
		JButton cancelButton = new JButton("Schließen");
		cancelButton.setBounds(660, 521, 100, 23);
		//cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		getContentPane().add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);
		
		JButton button = new JButton("<-");
		button.setBounds(360, 250, 50, 23);
		getContentPane().add(button);
		
		JLabel lblVerein = new JLabel("Verein");
		lblVerein.setBounds(20, 525, 46, 14);
		getContentPane().add(lblVerein);

		Vector<Verein> vereine = KampfDB.getVereine();
		vereine.add(0, null);
		JComboBox<Verein> comboBox = new JComboBox<Verein>(vereine);
		comboBox.setBounds(69, 521, 251, 22);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (comboBox.getSelectedItem() == null) {
					sorter.setRowFilter(null);
					sorter_1.setRowFilter(null);
				} else {
					RowFilter<SchuetzenTableModel, Integer> filter = new RowFilter<SchuetzenTableModel, Integer>() {
						@Override
						public boolean include(javax.swing.RowFilter.Entry<? extends SchuetzenTableModel, ? extends Integer> entry) {
							 SchuetzenTableModel model = entry.getModel();
						     Schuetze schuetze = (Schuetze) model.getValueAt(entry.getIdentifier(), -1);
						     if (schuetze.vereinsnummer == ((Verein) comboBox.getSelectedItem()).getId()) {
						       return true;
						     }
						     return false;
						}
				    };
					sorter.setRowFilter(filter);
					sorter_1.setRowFilter(filter);
				}
			}
		});
		getContentPane().add(comboBox);

		addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {}

			@Override
			public void componentMoved(ComponentEvent arg0) {}

			@Override
			public void componentResized(ComponentEvent arg0) {
				Dimension d = getContentPane().getSize();
				scrollPane.setBounds(10, 11, (d.width - 92) / 2, d.height - 55);
				button.setLocation((d.width / 2) - 26, scrollPane.getHeight() / 2);
				scrollPane_1.setBounds((d.width / 2) + 34, 11, (d.width - 92) / 2, d.height - 55);
				lblVerein.setLocation(20, d.height - 30);
				comboBox.setLocation(69, d.height - 34);
				cancelButton.setLocation(d.width - 112, d.height - 34);
			}

			@Override
			public void componentShown(ComponentEvent arg0) {}
			
		});
	}
}
