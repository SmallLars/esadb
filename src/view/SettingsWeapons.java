package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JPanel;

import model.SettingsChangeListener;
import model.SettingsModel;
import model.Unit;
import model.Weapon;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import controller.Controller;


@SuppressWarnings("serial")
public class SettingsWeapons extends JPanel implements ActionListener, TableModelListener {

	private SettingsChangeListener scl;

	private JTable table;
	private WeaponTableModel wtm;

	public SettingsWeapons(SettingsModel config, SettingsChangeListener scl) {
		this.scl = scl;

		this.setSize(735, 420);
		this.setLayout(null);
		
		wtm = new WeaponTableModel(Arrays.asList(config.getWeapons()));
		wtm.addTableModelListener(this);
		table = new JTable(wtm);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(20);

		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(200);
		table.getColumnModel().getColumn(2).setMinWidth(80);
		table.getColumnModel().getColumn(3).setMinWidth(70);
		table.getColumnModel().getColumn(4).setMinWidth(80);

		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(500);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(70);
		table.getColumnModel().getColumn(4).setPreferredWidth(80);

		table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			{setHorizontalAlignment(SwingConstants.RIGHT);}
		});
		table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			{setHorizontalAlignment(SwingConstants.RIGHT);}
	
			@Override
			protected void setValue(Object o) {
				super.setValue(String.format("%.3f", (Double) o));
			}
		});

		table.getColumnModel().getColumn(2).setCellEditor(new TableEditor(new JSpinner(), 1));
		table.getColumnModel().getColumn(3).setCellEditor(new TableEditor(new JComboBox<Unit>(Unit.values()), 0));
		JComboBox<Integer> comboBox = new JComboBox<Integer>(new Integer[] {1, 2, 3, 4, 5, 6});
		((JLabel) comboBox.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
		table.getColumnModel().getColumn(4).setCellEditor(new TableEditor(comboBox, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(15, 15, 705, 355);
		scrollPane.setViewportView(table);
		add(scrollPane);		

		JButton button_minus = new JButton("-");
		button_minus.setBounds(15, 385, 45, 20);
		button_minus.setActionCommand("-");
		button_minus.addActionListener(this);
		add(button_minus);

		JButton button_plus = new JButton("+");
		button_plus.setBounds(675, 385, 45, 20);
		button_plus.setActionCommand("+");
		button_plus.addActionListener(this);
		add(button_plus);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
			case "+":
				Controller.get().getConfig().newWeapon();
				break;
			case "-":
				if (table.getSelectedRow() < 0) break;
				Weapon w = (Weapon) wtm.getValueAt(table.getSelectedRow(), 1);
				if (!Controller.get().getConfig().removeWeapon(w)) {
					JOptionPane.showMessageDialog(
						this,
						"Die Waffe kann nicht gelöscht werden, da sie\nnoch mindestens einer Regel zugeordnet ist.",
						"Löschen nicht möglich",
						JOptionPane.INFORMATION_MESSAGE
					);
				}
				break;
		}
		wtm.setWeapons(Arrays.asList(Controller.get().getConfig().getWeapons()));
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		scl.settingsChanged();
	}
}