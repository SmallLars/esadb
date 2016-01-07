package view;


import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

import model.Gender;
import model.Group;
import model.SettingsModel;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import controller.Controller;

import javax.swing.JTextField;
import javax.swing.JTable;


@SuppressWarnings("serial")
public class SettingsGeneral extends JPanel implements ActionListener, DocumentListener {

	private JSpinner spinner;
	private DefaultListModel<Integer> listModel;
	private JList<Integer> list;

	private JTextField pathField;
	private JTextField nameField;

	private JTable table;
	private GroupTableModel gtm;

	public SettingsGeneral(SettingsModel config) {
		this.setSize(735,  420);
		this.setLayout(null);

		JLabel lblLinien = new JLabel("Linien");
		lblLinien.setFont(lblLinien.getFont().deriveFont(18f));
		lblLinien.setBounds(15, 11, 62, 20);
		add(lblLinien);

		spinner = new JSpinner(new SpinnerModel() {
			Vector<ChangeListener> listener = new Vector<ChangeListener>();
			int value = -1;

			@Override
			public void setValue(Object value) {
				this.value = (int) value;
				for (ChangeListener l : listener) l.stateChanged(new ChangeEvent(this));
			}

			@Override
			public Object getNextValue() {
				for (int i = value + 1; true; i++) if (!config.getLines().contains(i)) return i;
			}

			@Override
			public Object getPreviousValue() {
				for (int i = value - 1; i > 0; i--) if (!config.getLines().contains(i)) return i;
				return value;
			}

			@Override
			public Object getValue() {
				if (value == -1) {
					for (int i = 1; true; i++) {
						if (!config.getLines().contains(i)) {
							value = i;
							break;
						}
					}
				}
				return value;
			}

			@Override
			public void addChangeListener(ChangeListener l) {
				listener.add(l);
			}

			@Override
			public void removeChangeListener(ChangeListener l) {
				listener.remove(l);
			}
		});
		spinner.setBounds(15, 42, 62, 23);
		this.add(spinner);
		spinner.setEditor(new JSpinner.DefaultEditor(spinner){
			{getTextField().setHorizontalAlignment(SwingConstants.RIGHT);}
		});

		JButton btnEinfgen = new JButton("+");
		btnEinfgen.setBounds(15, 81, 62, 20);
		btnEinfgen.setActionCommand("ADD_LINE");
		btnEinfgen.addActionListener(this);
		this.add(btnEinfgen);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 117, 62, 251);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane);

		listModel = new DefaultListModel<Integer>();
		for (int i : config.getLines()) listModel.addElement(i);
		list = new JList<Integer>(listModel);
		list.setBorder(new EmptyBorder(0, 10, 0, 10));
		scrollPane.setViewportView(list);
		list.setLayoutOrientation(JList.VERTICAL);
		((DefaultListCellRenderer) list.getCellRenderer()).setHorizontalAlignment(SwingConstants.RIGHT);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JButton btnLschen = new JButton("-");
		btnLschen.setBounds(15, 384, 62, 20);
		btnLschen.setActionCommand("REMOVE_LINE");
		btnLschen.addActionListener(this);
		this.add(btnLschen);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(93, 11, 2, 393);
		add(separator);

		JLabel lblDatei = new JLabel("Standarddatei");
		lblDatei.setFont(lblDatei.getFont().deriveFont(18f));
		lblDatei.setBounds(110, 11, 200, 20);
		add(lblDatei);

		JLabel lblDateipfad = new JLabel("Dateipfad");
		lblDateipfad.setBounds(110, 42, 100, 14);
		add(lblDateipfad);

		pathField = new JTextField(config.getPath());
		pathField.setBounds(110, 60, 328, 20);
		pathField.setColumns(10);
		pathField.setEditable(false);
		add(pathField);
		
		JButton button = new JButton("...");
		button.setBounds(448, 60, 32, 20);
		button.setActionCommand("PATH");
		button.addActionListener(this);
		add(button);

		JLabel lblDateiname = new JLabel("Dateiname");
		lblDateiname.setBounds(500, 42, 100, 14);
		add(lblDateiname);

		nameField = new JTextField(config.getFilename());
		nameField.setBounds(500, 60, 178, 20);
		nameField.setColumns(10);
		nameField.getDocument().addDocumentListener(this);
		add(nameField);

		JLabel lblesa = new JLabel(".esa");
		lblesa.setBounds(688, 63, 32, 14);
		add(lblesa);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(110, 95, 610, 2);
		add(separator_1);

		JLabel lblAltersgruppen = new JLabel("Altersgruppen");
		lblAltersgruppen.setFont(lblAltersgruppen.getFont().deriveFont(18f));
		lblAltersgruppen.setBounds(110, 112, 200, 20);
		add(lblAltersgruppen);

		gtm = new GroupTableModel(new Vector<Group>(Arrays.asList(config.getGroups())));
		table = new JTable(gtm);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table.setRowHeight(20);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) {
				int i = gtm.getLastChanged();
				if (i < 0) return;
				table.setRowSelectionInterval(i, i);
			}
		});
		table.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				gtm.getLastChanged();
			}

			@Override
			public void focusLost(FocusEvent arg0) {}
		});

		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(80);
		table.getColumnModel().getColumn(2).setMinWidth(80);
		table.getColumnModel().getColumn(3).setMinWidth(100);

		table.getColumnModel().getColumn(0).setPreferredWidth(500);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);

		table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			{setHorizontalAlignment(SwingConstants.RIGHT);}

			@Override
			protected void setValue(Object o) {
				int year = (int) o;
				super.setValue("(" + (Controller.get().getConfig().getYear() - year) + ") " + year);
			}
		});
		table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			{setHorizontalAlignment(SwingConstants.RIGHT);}

			@Override
			protected void setValue(Object o) {
				int year = (int) o;
				super.setValue("(" + (Controller.get().getConfig().getYear() - year) + ") " + year);
			}
		});

		table.getColumnModel().getColumn(1).setCellEditor(new TableEditor(new JSpinner(), 0));
		table.getColumnModel().getColumn(2).setCellEditor(new TableEditor(new JSpinner(), 0));
		table.getColumnModel().getColumn(3).setCellEditor(new TableEditor(new JComboBox<Gender>(Gender.values()), 0));

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane1.setBounds(110, 143, 510, 225);
		scrollPane1.setViewportView(table);
		add(scrollPane1);

		JButton button_1 = new JButton("-");
		button_1.setBounds(110, 384, 45, 20);
		button_1.setActionCommand("REMOVE_GROUP");
		button_1.addActionListener(this);
		add(button_1);

		JButton button_2 = new JButton("+");
		button_2.setBounds(575, 384, 45, 20);
		button_2.setActionCommand("ADD_GROUP");
		button_2.addActionListener(this);
		add(button_2);

		JLabel lblSportjahr = new JLabel("Sportjahr");
		lblSportjahr.setBounds(640, 143, 80, 14);
		add(lblSportjahr);

		JSpinner spinner_1 = new JSpinner(new SpinnerNumberModel(config.getYear(), 0, null, new Integer(1)));
		spinner_1.setBounds(640, 161, 80, 18);
		spinner_1.setEditor(new NumberEditor(spinner_1, "0000"));
		spinner_1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				Controller.get().getConfig().setYear((Integer) spinner_1.getValue());
				table.repaint();
			}
		});
		add(spinner_1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index;
		switch (e.getActionCommand()) {
			case "ADD_LINE":
				int toAdd = (int) spinner.getValue();
				Controller.get().getConfig().addLine(toAdd);
				spinner.setValue(spinner.getNextValue());
				for (int i = 0; i < listModel.getSize(); i++) {
					if (listModel.get(i) > toAdd) {
						listModel.add(i, toAdd);
						return;
					}
				}
				listModel.addElement(toAdd);
				break;
			case "REMOVE_LINE":
				index = list.getSelectedIndex();
				if (index < 0) break;

				Integer l = list.getSelectedValue();
				if (Controller.get().getConfig().removeLinie(l)) {
					listModel.removeElement(l);
					if (index >= listModel.getSize()) index = listModel.getSize() - 1;
					if (listModel.getSize() > 0) list.setSelectedIndex(index);
				}
				break;
			case "PATH":
				JFileChooser fc = new JFileChooser(Controller.get().getConfig().getPath());
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					String path = fc.getSelectedFile().getPath();
					Controller.get().getConfig().setPath(path);
					pathField.setText(path);
				}
				break;
			case "ADD_GROUP":
				gtm.addGroup(Controller.get().getConfig().newGroup());
				int i = table.getRowCount() - 1;
				table.setRowSelectionInterval(i, i);
				table.scrollRectToVisible(new Rectangle(table.getCellRect(i, 0, true)));
				break;
			case "REMOVE_GROUP":
				index = table.getSelectedRow();
				if (index < 0) break;

				Group g = (Group) gtm.getValueAt(table.getSelectedRow(), 0);
				if (Controller.get().getConfig().removeGroup(g)) {
					gtm.removeGroup(g);
					if (index >= table.getRowCount()) index = table.getRowCount() - 1;
					if (table.getRowCount() > 0) {
						table.setRowSelectionInterval(index, index);
						table.scrollRectToVisible(new Rectangle(table.getCellRect(index, 0, true)));
					}
				}
				break;
		}
	}

	private void saveFilename() {
		Controller.get().getConfig().setFilename(nameField.getText());
	}

	@Override
	public void changedUpdate(DocumentEvent e) { saveFilename(); }

	@Override
	public void insertUpdate(DocumentEvent e) { saveFilename(); }

	@Override
	public void removeUpdate(DocumentEvent e) { saveFilename(); }
}
