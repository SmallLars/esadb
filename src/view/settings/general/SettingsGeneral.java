package view.settings.general;


import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
import javax.swing.table.TableRowSorter;

import model.Gender;
import model.Group;
import model.SettingsModel;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import controller.Controller;

import javax.swing.JTextField;
import javax.swing.JTable;

import view.FilterBox;
import view.IconButton;
import view.TableEditor;


@SuppressWarnings("serial")
public class SettingsGeneral extends JPanel implements ActionListener, DocumentListener {

	private JSpinner spinner;
	private DefaultListModel<Integer> listModel;
	private JList<Integer> list;

	private JTextField pathField;
	private JTextField nameField;

	private FilterBox genderFilter;
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

		JButton btnEinfgen = new IconButton(IconButton.ADD, "ADD_LINE", this);
		btnEinfgen.setBounds(24, 81, 44, 20);
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

		JButton btnLschen = new IconButton(IconButton.REMOVE, "REMOVE_LINE", this);
		btnLschen.setBounds(24, 384, 44, 20);
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

		pathField = new JTextField(config.getValue("Filepath", (new File("")).getAbsolutePath()));
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

		nameField = new JTextField(config.getValue("Filename", "yyyy-MM-dd"));
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

		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setBounds(640, 143, 80, 14);
		add(lblFilter);

		genderFilter = new FilterBox("Alle", Gender.values());
		genderFilter.setBounds(640, 161, 80, 20);
		genderFilter.setActionCommand("FILTER_CHANGE");
		genderFilter.addActionListener(this);
		add(genderFilter);

		JLabel lblSportjahr = new JLabel("Sportjahr");
		lblSportjahr.setBounds(640, 192, 80, 14);
		add(lblSportjahr);

		JSpinner spinner_1 = new JSpinner(new SpinnerNumberModel(config.getYear(), 0, null, new Integer(1)));
		spinner_1.setBounds(640, 210, 80, 18);
		spinner_1.setEditor(new NumberEditor(spinner_1, "0000"));
		spinner_1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				Controller.get().getConfig().setYear((Integer) spinner_1.getValue());
				table.repaint();
			}
		});
		add(spinner_1);

		gtm = new GroupTableModel(config.getGroups());
		table = new JTable(gtm);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(20);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableRowSorter<GroupTableModel> sorter = new TableRowSorter<GroupTableModel>(gtm);
		sorter.setSortsOnUpdates(true);
		sorter.setRowFilter(new GroupRowFilter(genderFilter));
		table.setRowSorter(sorter);

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
		table.getColumnModel().getColumn(2).setCellEditor(new TableEditor(new JSpinner(), 0));

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane1.setBounds(110, 143, 510, 225);
		scrollPane1.setViewportView(table);
		add(scrollPane1);

		JButton button_1 = new IconButton(IconButton.REMOVE, "REMOVE_GROUP", this);
		button_1.setBounds(110, 384, 45, 20);
		add(button_1);

		JButton button_2 = new IconButton(IconButton.ADD, "ADD_GROUP", this);
		button_2.setBounds(575, 384, 45, 20);
		add(button_2);
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
				JFileChooser fc = new JFileChooser(Controller.get().getConfig().getValue("Filepath", (new File("")).getAbsolutePath()));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					String path = fc.getSelectedFile().getPath();
					Controller.get().getConfig().setValue("Filepath", path);
					pathField.setText(path);
				}
				break;
			case "ADD_GROUP":
				Gender gender = null;
				int row = table.getSelectedRow();
				Group group = row < 0 ? null : (Group) table.getValueAt(row, -1);

				// 1. Falls Filter gesetzt: Geschlecht nutzen
				if (genderFilter.doFilter()) {
					gender = (Gender) genderFilter.getSelectedItem();
				}

				// 2. Falls eine Altersgruppe markiert ist: Geschlecht nutzen
				if (gender == null) {
					if (group != null) {
						gender = group.getGender();
					}
				}

				// 3. Benutzer nach Geschlecht fragen
				if (gender == null) {
					gender = (Gender) JOptionPane.showInputDialog(
						this,
						"Bitte wählen Sie ein Geschlecht\nfür die neue Altersgruppe:",
						"Geschlechtsauswahl",
						JOptionPane.QUESTION_MESSAGE,
						null,
						Gender.values(),
						Gender.ANY
					);
				}

				if (gender != null) {
					gtm.addGroup(gender, group);
	
					if (row++ == -1) row = table.getRowCount() - 1;
					table.setRowSelectionInterval(row, row);
					table.scrollRectToVisible(new Rectangle(table.getCellRect(row, 0, true)));
				}

				break;
			case "REMOVE_GROUP":
				index = table.getSelectedRow();
				if (index < 0) break;

				Group g = (Group) table.getValueAt(table.getSelectedRow(), -1);
				gtm.removeGroup(g);

				if (index >= table.getRowCount()) index = table.getRowCount() - 1;
				if (table.getRowCount() > 0) {
					table.setRowSelectionInterval(index, index);
					table.scrollRectToVisible(new Rectangle(table.getCellRect(index, 0, true)));
				}
				break;
			case "FILTER_CHANGE":
				gtm.fireTableDataChanged();
				break;
		}
	}

	private void saveFilename() {
		Controller.get().getConfig().setValue("Filename", nameField.getText());
	}

	@Override
	public void changedUpdate(DocumentEvent e) { saveFilename(); }

	@Override
	public void insertUpdate(DocumentEvent e) { saveFilename(); }

	@Override
	public void removeUpdate(DocumentEvent e) { saveFilename(); }
}
