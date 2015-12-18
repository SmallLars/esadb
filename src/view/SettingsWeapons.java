package view;

import java.util.Arrays;
import java.util.Vector;

import javax.swing.JPanel;

import model.SettingsModel;
import model.Unit;
import model.Weapon;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;


@SuppressWarnings("serial")
public class SettingsWeapons extends JPanel {
	private JTable table;

	/**
	 * Create the panel.
	 */
	public SettingsWeapons(SettingsModel config) {
		this.setSize(735,  420);
		this.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(88, 59, 525, 289);
		add(scrollPane);



		table = new JTable(new WeaponTableModel(new Vector<Weapon>(Arrays.asList(config.getWaffen()))));
		table.getColumnModel().getColumn(2).setCellEditor(new WeaponTableEditor(new JSpinner()));
		table.getColumnModel().getColumn(3).setCellEditor(new WeaponTableEditor(new JComboBox<Unit>(Unit.values())));
		scrollPane.setViewportView(table);
		

	}
}