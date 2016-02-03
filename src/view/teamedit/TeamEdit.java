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
import java.awt.CardLayout;


@SuppressWarnings("serial")
public class TeamEdit extends JDialog implements ActionListener {

	private Controller controller;

	private JComboBox<Discipline> discipline;
	private JComboBox<Group> group;

	private ResultTableModel tmodel;
	private JTable table;

	private JPanel card_panel;
	private CardLayout cards;
	private JButton btnNewButton;

	public TeamEdit(Frame parent) {
		super(parent, "Mannschaften bearbeiten");

		this.controller = Controller.get();

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setMinimumSize(new Dimension(480, 231));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		BorderLayout borderLayout = new BorderLayout();
		getContentPane().setLayout(borderLayout);
		{
			card_panel = new JPanel();
			getContentPane().add(card_panel, BorderLayout.CENTER);
			card_panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			cards = new CardLayout(0, 0);
			card_panel.setLayout(cards);
			{
				JPanel panel = new JPanel();
				card_panel.add(panel, "TEAM_LIST");
				panel.setBorder(null);
				panel.setLayout(new BorderLayout(5, 5));
				{
					JPanel panel_2_1 = new JPanel();
					panel.add(panel_2_1, BorderLayout.NORTH);
					panel_2_1.setBorder(null);
					panel_2_1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
					{
						JLabel label = new JLabel("Filter:");
						panel_2_1.add(label);
					}
					{
						discipline = new JComboBox<Discipline>(new DefaultComboBoxModel<Discipline>(Controller.get().getDisziplinen()));
						panel_2_1.add(discipline);
						discipline.insertItemAt(new Discipline("Alle Disziplinen"), 0);
						discipline.setSelectedIndex(0);
						discipline.setActionCommand("DISCIPLINE");
						discipline.addActionListener(this);
					}
					{
						group = new JComboBox<Group>(new DefaultComboBoxModel<Group>(Controller.get().getConfig().getGroups()));
						panel_2_1.add(group);
						group.insertItemAt(new Group("Alle Gruppen", 0, 0, Gender.ANY), 0);
						group.setSelectedIndex(0);
						group.setActionCommand("GROUP");
						group.addActionListener(this);
					}
				}
				{
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setPreferredSize(new Dimension(600, 400));
					panel.add(scrollPane, BorderLayout.CENTER);
		
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

					for (int i = 0; i < 4; i++) {
						table.getColumnModel().getColumn(i).setMinWidth(80);
						table.getColumnModel().getColumn(i).setPreferredWidth(i < 3 ? 160: 80);
					}

					scrollPane.setViewportView(table);
				}
				{
					JPanel panel_2_1 = new JPanel();
					panel.add(panel_2_1, BorderLayout.SOUTH);
					panel_2_1.setBorder(null);
					panel_2_1.setLayout(new BorderLayout(5, 5));
					{
						JButton btnNewButton = new JButton("-");
						btnNewButton.setActionCommand("REMOVE");
						btnNewButton.addActionListener(this);
						panel_2_1.add(btnNewButton, BorderLayout.WEST);
					}
					{
						JPanel panel_1_1 = new JPanel();
						panel_2_1.add(panel_1_1, BorderLayout.CENTER);
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
						panel_2_1.add(btnNewButton, BorderLayout.EAST);
					}
				}
			}
			{
				JPanel panel = new JPanel();
				panel.setBorder(null);
				card_panel.add(panel, "TEAM_MEMBER");
				{
					btnNewButton = new JButton("Mannschaften");
					btnNewButton.setActionCommand("LIST");
					btnNewButton.addActionListener(this);
					panel.add(btnNewButton);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.SOUTH);
			panel.setBorder(new EmptyBorder(0, 5, 5, 5));
			panel.setLayout(new BorderLayout(5, 5));
			{
				JSeparator separator = new JSeparator();
				panel.add(separator, BorderLayout.NORTH);
			}
			{
				JButton cancelButton = new JButton("Schließen");
				panel.add(cancelButton, BorderLayout.EAST);
				cancelButton.setActionCommand("CANCEL");
				cancelButton.addActionListener(this);
				getRootPane().setDefaultButton(cancelButton);
			}
		}

		pack();
		setLocationRelativeTo(parent);
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
				cards.show(card_panel, "TEAM_MEMBER");
				break;
			case "ADD":
				Team t = new Team(null, null);
				controller.add(t);
				tmodel.fireTableDataChanged();
				break;
			case "LIST":
				cards.show(card_panel, "TEAM_LIST");
				break;
		}
	}
}