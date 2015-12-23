package view;


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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import controller.Controller;
import model.Rule;
import model.SettingsModel;
import model.TargetModel;
import model.Weapon;


@SuppressWarnings("serial")
public class SettingsRules extends JPanel implements ActionListener, TableModelListener {
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

		table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			{setHorizontalAlignment(SwingConstants.RIGHT);}
		});

		JComboBox<TargetModel> targets = new JComboBox<TargetModel>(config.getTargets());
		table.getColumnModel().getColumn(2).setCellEditor(new TableEditor(targets));
		JComboBox<Weapon> weapons = new JComboBox<Weapon>(config.getWeapons());
		table.getColumnModel().getColumn(3).setCellEditor(new TableEditor(weapons));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(15, 15, 705, 355);
		scrollPane.setViewportView(table);
		add(scrollPane);		

		JButton button_minus = new JButton("-");
		button_minus.setBounds(15, 385, 45, 20);
		button_minus.addActionListener(this);
		add(button_minus);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index = table.getSelectedRow(); 
		if (index < 0) return;
		
		Controller.get().getConfig().removeRule((String) table.getValueAt(index, 0));
		rtm.removeRule(index);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		JComboBox<TargetModel> targets = new JComboBox<TargetModel>(Controller.get().getConfig().getTargets());
		table.getColumnModel().getColumn(2).setCellEditor(new TableEditor(targets));
		JComboBox<Weapon> weapons = new JComboBox<Weapon>(Controller.get().getConfig().getWeapons());
		table.getColumnModel().getColumn(3).setCellEditor(new TableEditor(weapons));
	}
}