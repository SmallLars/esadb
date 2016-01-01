package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.SettingsModel;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import controller.Controller;

import javax.swing.JTextField;


@SuppressWarnings("serial")
public class SettingsGeneral extends JPanel implements ActionListener, DocumentListener {
	private JSpinner spinner;
	private DefaultListModel<Integer> listModel;
	private JList<Integer> list;
	private JTextField pathField;
	private JTextField nameField;

	public SettingsGeneral(SettingsModel config) {
		this.setSize(735,  420);
		this.setLayout(null);

		JLabel lblLinien = new JLabel("Linien");
		lblLinien.setFont(lblLinien.getFont().deriveFont(18f));
		lblLinien.setBounds(15, 11, 78, 20);
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
		spinner.setBounds(15, 42, 91, 23);
		this.add(spinner);
		spinner.setEditor(new JSpinner.DefaultEditor(spinner));
		
		JButton btnEinfgen = new JButton("Einfügen");
		btnEinfgen.setBounds(15, 81, 91, 20);
		btnEinfgen.setActionCommand("ADD");
		btnEinfgen.addActionListener(this);
		this.add(btnEinfgen);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 117, 91, 251);
		this.add(scrollPane);

		listModel = new DefaultListModel<Integer>();
		for (int i : config.getLines()) listModel.addElement(i);
		list = new JList<Integer>(listModel);
		scrollPane.setViewportView(list);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JButton btnLschen = new JButton("Löschen");
		btnLschen.setBounds(15, 384, 91, 20);
		btnLschen.setActionCommand("REMOVE");
		btnLschen.addActionListener(this);
		this.add(btnLschen);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(121, 11, 2, 398);
		add(separator);

		JLabel lblDatei = new JLabel("Standarddatei");
		lblDatei.setFont(lblDatei.getFont().deriveFont(18f));
		lblDatei.setBounds(138, 11, 200, 20);
		add(lblDatei);

		JLabel lblDateipfad = new JLabel("Dateipfad");
		lblDateipfad.setBounds(138, 42, 100, 14);
		add(lblDateipfad);

		pathField = new JTextField(config.getPath());
		pathField.setBounds(138, 60, 250, 20);
		pathField.setColumns(10);
		pathField.setEditable(false);
		add(pathField);
		
		JButton button = new JButton("...");
		button.setBounds(398, 59, 32, 21);
		button.setActionCommand("PATH");
		button.addActionListener(this);
		add(button);

		JLabel lblDateiname = new JLabel("Dateiname");
		lblDateiname.setBounds(138, 95, 100, 14);
		add(lblDateiname);

		nameField = new JTextField(config.getFilename());
		nameField.setBounds(138, 111, 250, 20);
		nameField.setColumns(10);
		nameField.getDocument().addDocumentListener(this);
		add(nameField);

		JLabel lblesa = new JLabel(".esa");
		lblesa.setBounds(398, 114, 32, 14);
		add(lblesa);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "ADD":
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
			case "REMOVE":
				int index = list.getSelectedIndex();

				Integer l = list.getSelectedValue();
				if (l == null) return;
				Controller.get().getConfig().removeLinie(l);
				listModel.removeElement(l);

				if (index < listModel.getSize()) {
					list.setSelectedIndex(index);
				} else if (listModel.getSize() > 0) {
					list.setSelectedIndex(listModel.getSize() - 1);
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