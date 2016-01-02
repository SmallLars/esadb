package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import model.SettingsChangeListener;
import model.SettingsModel;
import model.TargetModel;
import model.TargetType;
import model.TargetValue;

import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;

import controller.Controller;


@SuppressWarnings("serial")
public class SettingsDeerTargets extends JPanel implements ActionListener, ChangeListener, FocusListener {

	private boolean doUpdate = false;
	private SettingsChangeListener scl;

	private JComboBox<TargetModel> comboBox;
	private Target scheibe;
	private JTextField text_name;
	private JTextField text_number;
	private JSpinner spinner_size_x;
	private JSpinner spinner_size_y;
	private JSpinner spinner_zoom_x;
	private JSpinner spinner_zoom_y;
	private JComboBox<TargetType> comboBox_typ;
	private JSpinner spinner_zoom_lvl;
	private JSpinner spinner_offset_x;
	private JSpinner spinner_offset_y;

	public SettingsDeerTargets(SettingsModel config, SettingsChangeListener scl) {
		this.scl = scl;

		this.setSize(735,  420);
		this.setLayout(null);

		scheibe = new Target(config.getStandardRule().getScheibe());
		scheibe.setBorder(new LineBorder(new Color(0, 0, 0)));
		scheibe.setBounds(15, 15, 390, 390);
		this.add(scheibe);

		JButton button_minus = new JButton("-");
		button_minus.setBounds(420, 15, 45, 20);
		button_minus.setActionCommand("-");
		button_minus.addActionListener(this);
		add(button_minus);

		comboBox = new JComboBox<TargetModel>();
		for (TargetModel tm : config.getTargets()) {
			if (tm.isDeerTarget()) comboBox.addItem(tm);
		}
		comboBox.setSelectedItem(config.getStandardRule().getScheibe());
		comboBox.setBounds(480, 15, 180, 20);
		comboBox.setActionCommand("TYP");
		comboBox.addActionListener(this);
		this.add(comboBox);
		
		JButton button_plus = new JButton("+");
		button_plus.setBounds(675, 15, 45, 20);
		button_plus.setActionCommand("+");
		button_plus.addActionListener(this);
		add(button_plus);
		
		final int X[] = {420, 160};
		final int Y[] = {70, 45};
		
		text_name = addJTextField(this, X[0], Y[0] + 0 * Y[1], 160, "Name");
		text_name.setActionCommand("Name");
		text_name.addActionListener(this);
		text_name.addFocusListener(this);
		
		text_number = addJTextField(this, X[0] + 180, Y[0] + 0 * Y[1], 90, "Kennnummer");
		text_number.setActionCommand("Kennnummer");
		text_number.addActionListener(this);
		text_number.addFocusListener(this);
		text_number.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent arg0) {
				if (!text_number.getText().matches("^[0-9]+(\\.[0-9]+)*$")) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(
						null,
						"Die Kennnummer darf nur aus Punkten und Zahlen bestehen\nund muss mit einer mit einer Zahl beginnen und enden.",
						"Ungültige Eingabe",
						JOptionPane.WARNING_MESSAGE
					);
					return false;
				}

				for (int i = 0; i < comboBox.getItemCount(); i++) {
					if (comboBox.getSelectedItem() == comboBox.getItemAt(i)) continue;
					if (comboBox.getItemAt(i).getNumber().equals(text_number.getText())) {
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(
							null,
							"Zwei Scheiben mit der gleichen Kennnummer sind nicht möglich.",
							"Ungültige Eingabe",
							JOptionPane.WARNING_MESSAGE
						);
						return false;
					}
				}

				return true;
			}
		});

		spinner_size_x = addJSpinner(this, X[0], Y[0] + 2 * Y[1], "Kartonbreite", "mm");
		spinner_size_y = addJSpinner(this, X[0] + X[1], Y[0] + 2 * Y[1], "Kartonhöhe", "mm");

		spinner_zoom_x = addJSpinner(this, X[0], Y[0] + 3 * Y[1], "Zoomzentrum X", "mm");
		spinner_zoom_y = addJSpinner(this, X[0] + X[1], Y[0] + 3 * Y[1], "Zoomzentrum Y", "mm");

		comboBox_typ = addJComboBox(this, X[0], Y[0] + 4 * Y[1], "Scheibenart", "", new TargetType[] {
				TargetType.KLAPP,
				TargetType.JAGD,
				TargetType.DOPPELSAU
			});
		spinner_zoom_lvl = addJSpinner(this, X[0] + X[1], Y[0] + 4 * Y[1], "Zoomlevels", "");
	
		spinner_offset_x = addJSpinner(this, X[0], Y[0] + 5 * Y[1], "Offset X", "mm");
		spinner_offset_y =  addJSpinner(this, X[0] + X[1], Y[0] + 5 * Y[1], "Offset Y", "mm");
		
		updateDisplay();
		scheibe.setTarget(getSelectedTargetModel());
		doUpdate = true;
	}

	private void updateDisplay() {
		doUpdate = false;

		TargetModel target = getSelectedTargetModel();
		text_name.setText(target.toString());
		text_number.setText(target.getNumber());			
		spinner_size_x.setValue(target.getValue(TargetValue.SIZE_HEIGHT) / 100.);
		spinner_size_y.setValue(target.getValue(TargetValue.SIZE_WIDTH) / 100.);
		spinner_zoom_x.setValue(target.getValue(TargetValue.ZOOM_CENTER_X) / 100.);
		spinner_zoom_y.setValue(target.getValue(TargetValue.ZOOM_CENTER_Y) / 100.);
		comboBox_typ.setSelectedItem(target.getValue(TargetValue.TYPE));
		spinner_zoom_lvl.setValue(target.getValue(TargetValue.ZOOM_LEVELS));
		spinner_offset_x.setValue(target.getValue(TargetValue.OFFSET_X) / 100.);
		spinner_offset_y.setValue(target.getValue(TargetValue.OFFSET_Y) / 100.);

		doUpdate = true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TargetModel t = (TargetModel) comboBox.getSelectedItem();
		switch (e.getActionCommand()) {
			case "-":
				if (t == null) return;
				if (!Controller.get().getConfig().removeTarget(t)) {
					JOptionPane.showMessageDialog(
						this,
						"Die Scheibe kann nicht gelöscht werden, da sie\nnoch mindestens einer Regel zugeordnet ist.",
						"Löschen nicht möglich",
						JOptionPane.INFORMATION_MESSAGE
					);
				} else {
					comboBox.removeItem(comboBox.getSelectedItem());
					if (comboBox.getItemCount() == 0) comboBox.setSelectedItem(null);
					scl.settingsChanged();
				}
				return;
			case "+":
				createTarget();
				return;
		}

		if (!doUpdate) return;
		
		if (e.getActionCommand().equals("TYP")) {
			if (t != null) {
				updateDisplay();
				scheibe.setTarget(t);
			}
			return;
		}

		int value;
		t = getSelectedTargetModel();
		switch (e.getActionCommand()) {
			case "Name":
				t.setName(text_name.getText());
				comboBox.repaint();
				break;
			case "Kennnummer":
				if (text_number.getInputVerifier().verify(text_number)) {
					t.setNumber(text_number.getText());
				}
				break;
			case "Scheibenart":
				value = ((TargetType) comboBox_typ.getSelectedItem()).getValue();
				t.setValue(TargetValue.TYPE, value);
				break;
		}
		updateDisplay();
		scheibe.setTarget(t);
	}

	@Override
	public void focusGained(FocusEvent e) {}

	@Override
	public void focusLost(FocusEvent e) {
		if (!doUpdate) return;

		TargetModel target = getSelectedTargetModel();
		
		target.setName(text_name.getText());
		comboBox.repaint();

		if (text_number.getInputVerifier().verify(text_number)) {
			target.setNumber(text_number.getText());
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (!doUpdate) return;

		TargetModel target = getSelectedTargetModel();
		switch (((JSpinner) e.getSource()).getName()) {
			case "Kartonbreite":  target.setValue(TargetValue.SIZE_WIDTH,    (int) ((double) spinner_size_x.getValue()   * 100)); break;
			case "Kartonhöhe":    target.setValue(TargetValue.SIZE_HEIGHT,   (int) ((double) spinner_size_y.getValue()   * 100)); break;
			case "Zoomzentrum X": target.setValue(TargetValue.ZOOM_CENTER_X, (int) ((double) spinner_zoom_x.getValue()   * 100)); break;
			case "Zoomzentrum Y": target.setValue(TargetValue.ZOOM_CENTER_Y, (int) ((double) spinner_zoom_y.getValue()   * 100)); break;
			case "Zoomlevels":    target.setValue(TargetValue.ZOOM_LEVELS,   (int)           spinner_zoom_lvl.getValue()       ); break;
			case "Offset X":      target.setValue(TargetValue.OFFSET_X,      (int) ((double) spinner_offset_x.getValue() * 100)); break;
			case "Offset Y":      target.setValue(TargetValue.OFFSET_Y,      (int) ((double) spinner_offset_y.getValue() * 100)); break;
		}

		updateDisplay();
		scheibe.setTarget(target);
	}

	private TargetModel getSelectedTargetModel() {
		TargetModel tm = (TargetModel) comboBox.getSelectedItem();
		if (tm == null) tm = createTarget();
		return tm;
	}

	private TargetModel createTarget() {
		TargetModel tm = Controller.get().getConfig().newTarget();
		comboBox.addItem(tm);
		comboBox.setSelectedItem(tm);
		scl.settingsChanged();
		return tm;
	}

	private JTextField addJTextField(JPanel parent, int x, int y, int width, String caption) {
		JTextField textField = new JTextField();
		textField.setBounds(x, y, width, 20);
		textField.setColumns(10);
		parent.add(textField);
		addCaption(textField, parent, caption, "");
		return textField;
	}

	private JSpinner addJSpinner(JPanel parent, int x, int y, String caption, String unit) {
		JSpinner spinner = new JSpinner();
		if (unit.equals("mm")) {
			spinner.setModel(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(0.1)));
			JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "0.00");
			spinner.setEditor(editor);
		} else {
			spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), new Integer(9999999), new Integer(1)));
		}
		spinner.setBounds(x, y, 110, 20);
		spinner.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() == 0) return;
				JSpinner s = (JSpinner) e.getComponent();
				Object o = null;
				if (e.getWheelRotation() < 0) {
					o = s.getModel().getNextValue();
				} else {
					o = s.getModel().getPreviousValue();
				}
				if (o != null) s.setValue(o);
			}
		});
		spinner.setName(caption);
		spinner.addChangeListener(this);
		parent.add(spinner);
		addCaption(spinner, parent, caption, unit);
		return spinner;
	}

	private <T> JComboBox<T> addJComboBox(JPanel parent, int x, int y, String caption, String unit, T[] content) {
		JComboBox<T> comboBox = new JComboBox<T>() {
			@Override
			public Dimension getSize() {
				Dimension dim = super.getSize();
				dim.width = Math.max(dim.width, getPreferredSize().width - 20);
				return dim;
			}
		};
		comboBox.setModel(new DefaultComboBoxModel<T>(content));
		comboBox.setBounds(x, y, 110, 20);
		comboBox.setActionCommand(caption);
		comboBox.addActionListener(this);
		parent.add(comboBox);
		addCaption(comboBox, parent, caption, unit);
		return comboBox;
	}

	private void addCaption(JComponent component, JPanel parent, String caption, String unit) {
		JLabel label;
		
		label = new JLabel(caption);
		label.setBounds(component.getX(), component.getY() - 18, component.getWidth() + 30, 20);
		parent.add(label);

		if (unit.isEmpty()) return;

		label = new JLabel(unit);
		label.setBounds(component.getX() + component.getWidth() + 6, component.getY(), 24, component.getHeight());
		parent.add(label);
	}
}