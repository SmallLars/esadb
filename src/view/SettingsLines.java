package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.SettingsModel;


@SuppressWarnings("serial")
public class SettingsLines extends JPanel {

	public SettingsLines(SettingsModel config) {
		this.setSize(735,  420);
		this.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 21, 100, 118);
		this.add(scrollPane);

		DefaultListModel<Integer> listModel = new DefaultListModel<Integer>();
		for (int i : config.getLines()) listModel.addElement(i);
		JList<Integer> list = new JList<Integer>(listModel);
		scrollPane.setViewportView(list);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JSpinner spinner = new JSpinner(new SpinnerModel() {
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
		spinner.setBounds(133, 21, 91, 23);
		this.add(spinner);
		spinner.setEditor(new JSpinner.DefaultEditor(spinner));
		
		JButton btnEinfgen = new JButton("Einfügen");
		btnEinfgen.setBounds(133, 55, 91, 20);
		this.add(btnEinfgen);
		btnEinfgen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int toAdd = (int) spinner.getValue();
				config.addLine(toAdd);
				spinner.setValue(spinner.getNextValue());
				for (int i = 0; i < listModel.getSize(); i++) {
					if (listModel.get(i) > toAdd) {
						listModel.add(i, toAdd);
						return;
					}
				}
				listModel.addElement(toAdd);
			}
		});

		JButton btnLschen = new JButton("Löschen");
		btnLschen.setBounds(133, 118, 91, 20);
		this.add(btnLschen);

		btnLschen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Integer l = list.getSelectedValue();
				if (l == null) return;
				config.removeLinie(l);
				listModel.removeElement(l);
			}
		});
	}

}
