package view;

import java.util.Arrays;
import java.util.Vector;

import javax.swing.JPanel;

import model.SettingsModel;
import model.Unit;
import model.Weapon;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;


@SuppressWarnings("serial")
public class SettingsWeapons extends JPanel {
	private JTable table;

	/**
	 * Create the panel.
	 */
	public SettingsWeapons(SettingsModel config) {
		this.setSize(735, 420);
		this.setLayout(null);
		
		table = new JTable(new WeaponTableModel(new Vector<Weapon>(Arrays.asList(config.getWaffen()))));
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

		table.getColumnModel().getColumn(2).setCellEditor(new WeaponTableEditor(new JSpinner()));
		table.getColumnModel().getColumn(3).setCellEditor(new WeaponTableEditor(new JComboBox<Unit>(Unit.values())));
		JComboBox<Integer> comboBox = new JComboBox<Integer>(new Integer[] {1, 2, 3, 4, 5, 6});
		((JLabel) comboBox.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
		table.getColumnModel().getColumn(4).setCellEditor(new WeaponTableEditor(comboBox));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(15, 15, 705, 355);
		scrollPane.setViewportView(table);
		add(scrollPane);		

		JButton button_minus = new JButton("-");
		button_minus.setBounds(15, 385, 45, 20);
		add(button_minus);

		JButton button_plus = new JButton("+");
		button_plus.setBounds(675, 385, 45, 20);
		add(button_plus);
	}
}