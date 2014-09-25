package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Config;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;


@SuppressWarnings("serial")
public class Einstellungen extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public Einstellungen(Frame parent, Config config) {
		super(parent, "Einstellungen");
		setResizable(false);

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(225, 200);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 100, 118);
		contentPanel.add(scrollPane);

		DefaultListModel<Integer> listModel = new DefaultListModel<Integer>();
		for (int i : config.getLinien()) listModel.addElement(i);
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
				for (int i = value + 1; true; i++) if (!config.getLinien().contains(i)) return i;
			}

			@Override
			public Object getPreviousValue() {
				for (int i = value - 1; i > 0; i--) if (!config.getLinien().contains(i)) return i;
				return value;
			}

			@Override
			public Object getValue() {
				if (value == -1) {
					for (int i = 1; true; i++) {
						if (!config.getLinien().contains(i)) {
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
		spinner.setEditor(new JSpinner.DefaultEditor(spinner));
		spinner.setBounds(120, 12, 91, 23);
		contentPanel.add(spinner);
		
		JButton btnEinfgen = new JButton("Einfügen");
		btnEinfgen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int toAdd = (int) spinner.getValue();
				config.addLinie(toAdd);
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
		btnEinfgen.setBounds(120, 46, 91, 23);
		contentPanel.add(btnEinfgen);
		
		JButton btnLschen = new JButton("Löschen");
		btnLschen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Integer l = list.getSelectedValue();
				if (l == null) return;
				config.removeLinie(l);
				listModel.removeElement(l);
			}
		});
		btnLschen.setBounds(120, 80, 91, 23);
		contentPanel.add(btnLschen);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton closeButton = new JButton("Schließen");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);				
			}
		});
		buttonPane.add(closeButton);
		getRootPane().setDefaultButton(closeButton);
	}
}
