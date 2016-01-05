package view;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import controller.Controller;
import model.Rule;
import model.SettingsChangeListener;
import model.SettingsModel;
import model.TargetModel;
import model.Weapon;


@SuppressWarnings("serial")
public class SettingsRules extends JPanel implements ActionListener, SettingsChangeListener {
	private JTable table;
	private RuleTableModel rtm;

	public SettingsRules(SettingsModel config) {
		this.setSize(735,  420);
		this.setLayout(null);

		rtm = new RuleTableModel(new Vector<Rule>(Arrays.asList(config.getRules())));
		table = new JTable(rtm);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table.setRowHeight(20);

		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setMinWidth(180);
		table.getColumnModel().getColumn(3).setMinWidth(220);

		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(500);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(3).setPreferredWidth(220);

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        	setHorizontalAlignment(column == 0 ? SwingConstants.RIGHT : SwingConstants.LEFT);
	        	
	        	boolean isStandard = Controller.get().getStandardRule() == table.getValueAt(row, 1);
	        	setBackground(isStandard ? Color.decode("0x9ffff9") : null);

		        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		    }
		});

		JComboBox<TargetModel> targets = new JComboBox<TargetModel>(config.getTargets());
		table.getColumnModel().getColumn(2).setCellEditor(new TableEditor(targets, 0));
		JComboBox<Weapon> weapons = new JComboBox<Weapon>(config.getWeapons());
		table.getColumnModel().getColumn(3).setCellEditor(new TableEditor(weapons, 0));

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

		JButton button_plus = new JButton("Standardregel");
		button_plus.setBounds(570, 385, 150, 20);
		button_plus.setActionCommand("DEFAULT");
		button_plus.addActionListener(this);
		add(button_plus);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index = table.getSelectedRow();
		if (index < 0) return;

		SettingsModel config = Controller.get().getConfig();

		switch (e.getActionCommand()) {
			case "-":
				config.removeRule((String) table.getValueAt(index, 0));
				rtm.removeRule(index);
				break;
			case "DEFAULT":
				config.setStandardRule((Rule) table.getValueAt(index, 1));
				table.repaint();
				break;
		}
	}

	@Override
	public void settingsChanged() {
		JComboBox<TargetModel> targets = new JComboBox<TargetModel>(Controller.get().getConfig().getTargets());
		table.getColumnModel().getColumn(2).setCellEditor(new TableEditor(targets, 0));
		JComboBox<Weapon> weapons = new JComboBox<Weapon>(Controller.get().getConfig().getWeapons());
		table.getColumnModel().getColumn(3).setCellEditor(new TableEditor(weapons, 0));
	}
}