package view;


import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import model.Rule;
import model.SettingsModel;
import model.TargetModel;
import model.Weapon;


@SuppressWarnings("serial")
public class SettingsRules extends JPanel {
	private JTable table;

	public SettingsRules(SettingsModel config) {
		this.setSize(735,  420);
		this.setLayout(null);

		table = new JTable(new RuleTableModel(new Vector<Rule>(Arrays.asList(config.getRules()))));
		table.setRowHeight(20);

		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setMinWidth(180);
		table.getColumnModel().getColumn(3).setMinWidth(220);

		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(500);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(3).setPreferredWidth(220);

		table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			{setHorizontalAlignment(SwingConstants.RIGHT);}
		});

		table.getColumnModel().getColumn(2).setCellEditor(new TableEditor(new JComboBox<TargetModel>(config.getTargets())));
		table.getColumnModel().getColumn(3).setCellEditor(new TableEditor(new JComboBox<Weapon>(config.getWeapons())));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(15, 15, 705, 355);
		scrollPane.setViewportView(table);
		add(scrollPane);		

		JButton button_minus = new JButton("-");
		button_minus.setBounds(15, 385, 45, 20);
		add(button_minus);
	}

}