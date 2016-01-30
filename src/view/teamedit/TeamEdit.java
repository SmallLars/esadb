package view.teamedit;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import model.Discipline;
import model.Gender;
import model.Group;
import model.Team;
import controller.Controller;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import view.TableEditor;


@SuppressWarnings("serial")
public class TeamEdit extends JDialog implements ActionListener {

	private Controller controller;

	private JComboBox<Discipline> discipline;
	private JComboBox<Group> group;

	private ResultTableModel tmodel;
	private JTable table;

	public TeamEdit(Frame parent) {
		super(parent, "Mannschaften bearbeiten");

		this.controller = Controller.get();

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setMinimumSize(new Dimension(480, 231));
		setSize(630, 500);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.NORTH);
			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			panel.setLayout(new BorderLayout(5, 5));
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.CENTER);
				panel_1.setBorder(null);
				panel_1.setLayout(new FlowLayout(FlowLayout.LEFT));
				{
					JLabel label = new JLabel("Filter:");
					panel_1.add(label);
				}
				{
					discipline = new JComboBox<Discipline>(new DefaultComboBoxModel<Discipline>(Controller.get().getDisziplinen()));
					discipline.insertItemAt(new Discipline("Alle Disziplinen"), 0);
					discipline.setSelectedIndex(0);
					discipline.setActionCommand("DISCIPLINE");
					discipline.addActionListener(this);
					panel_1.add(discipline);
				}
				{
					group = new JComboBox<Group>(new DefaultComboBoxModel<Group>(Controller.get().getConfig().getGroups()));
					group.insertItemAt(new Group("Alle Gruppen", 0, 0, Gender.ANY), 0);
					group.setSelectedIndex(0);
					group.setActionCommand("GROUP");
					group.addActionListener(this);
					panel_1.add(group);
				}
			}
			{
				JSeparator separator = new JSeparator();
				panel.add(separator, BorderLayout.SOUTH);
			}
		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.CENTER);
			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			panel.setLayout(new BorderLayout(5, 5));
			{
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				panel.add(scrollPane, BorderLayout.CENTER);
				{
					tmodel = new ResultTableModel(controller.getModel().getErgebnisse());
					TableRowSorter<ResultTableModel> sorter = new TableRowSorter<ResultTableModel>(tmodel);
					sorter.setSortsOnUpdates(true);
					sorter.setRowFilter(new ResultRowFilter(discipline, group, true));
					
					table = new JTable(tmodel);
					table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
					table.setRowSorter(sorter);
					table.setDefaultRenderer(Float.class, new DefaultTableCellRenderer() {
						{setHorizontalAlignment(SwingConstants.RIGHT);}
						
						@Override
						protected void setValue(Object o) {
							super.setValue(String.format("%.1f", (Float) o));
						}
					});
					table.getColumnModel().getColumn(1).setCellEditor(new TableEditor(new JComboBox<Discipline>(new DefaultComboBoxModel<Discipline>(Controller.get().getDisziplinen())), 0));
					table.getColumnModel().getColumn(2).setCellEditor(new TableEditor(new JComboBox<Group>(new DefaultComboBoxModel<Group>(Controller.get().getConfig().getGroups())), 0));
					table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					table.getTableHeader().setReorderingAllowed(false);
					
					table.getColumnModel().getColumn(3).setMinWidth(70);
					table.getColumnModel().getColumn(3).setPreferredWidth(70);
					table.getColumnModel().getColumn(3).setMaxWidth(70);

					scrollPane.setViewportView(table);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.SOUTH);
				panel_1.setBorder(null);
				panel_1.setLayout(new BorderLayout(5, 10));
				{
					JButton btnNewButton = new JButton("-");
					btnNewButton.setActionCommand("REMOVE");
					btnNewButton.addActionListener(this);
					panel_1.add(btnNewButton, BorderLayout.WEST);
				}
				{
					JPanel panel_1_1 = new JPanel();
					panel_1.add(panel_1_1, BorderLayout.CENTER);
					panel_1_1.setBorder(null);
					panel_1_1.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
					{
						JButton btnNewButton = new JButton("Bearbeiten");
						btnNewButton.setActionCommand("EDIT");
						btnNewButton.addActionListener(this);
						panel_1_1.add(btnNewButton);
					}
				}
				{
					JButton btnNewButton = new JButton("+");
					btnNewButton.setActionCommand("ADD");
					btnNewButton.addActionListener(this);
					panel_1.add(btnNewButton, BorderLayout.EAST);
				}
				{
					JSeparator separator = new JSeparator();
					panel_1.add(separator, BorderLayout.SOUTH);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.SOUTH);
			panel.setBorder(null);
			panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton cancelButton = new JButton("Schließen");
				cancelButton.setActionCommand("CANCEL");
				cancelButton.addActionListener(this);
				panel.add(cancelButton);
				getRootPane().setDefaultButton(cancelButton);

			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "CANCEL":
				setVisible(false);
				break;
			case "DISCIPLINE":
			case "GROUP":
				tmodel.fireTableDataChanged();
				break;
			case "REMOVE":
				int row = table.getSelectedRow();
				if (row >= 0) {
					controller.remove(table.getValueAt(row, -1));
					tmodel.fireTableDataChanged();
				}
				break;
			case "EDIT":
				break;
			case "ADD":
				controller.add(new Team(null, null));
				tmodel.fireTableDataChanged();
				break;
		}
	}
}