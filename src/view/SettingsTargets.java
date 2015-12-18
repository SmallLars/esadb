package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import model.SettingsModel;
import model.TargetAngle;
import model.TargetFill;
import model.TargetModel;
import model.TargetType;
import model.TargetValue;

import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;


@SuppressWarnings("serial")
public class SettingsTargets extends JPanel implements ActionListener, ChangeListener {

	private boolean doUpdate = false;

	private JComboBox<TargetModel> comboBox;
	private Target scheibe;
	private JTextField text_name;
	private JTextField text_number;
	private JSpinner spinner_size;
	private JSpinner spinner_feed;
	private JSpinner spinner_dia_outside;
	private JSpinner spinner_ring_width;
	private JSpinner spinner_dia_black;
	private JSpinner spinner_dia_inner_ten;
	private JSpinner spinner_ring_min;
	private JSpinner spinner_ring_max;
	private JSpinner spinner_num_max;
	private JComboBox<Object> comboBox_ring_angle;
	private JComboBox<Object> comboBox_typ;
	private JComboBox<Object> comboBox_style;
	private JSpinner spinner_vorhaltediameter;
	private JSpinner spinner_vorhalteabstand;

	/**
	 * Create the panel.
	 */
	public SettingsTargets(SettingsModel config) {
		this.setSize(735,  420);
		this.setLayout(null);

		scheibe = new Target(config.getStandardRegel().getScheibe());
		scheibe.setBorder(new LineBorder(new Color(0, 0, 0)));
		scheibe.setBounds(15, 15, 390, 390);
		this.add(scheibe);

		JButton button_minus = new JButton("-");
		button_minus.setBounds(420, 15, 45, 20);
		add(button_minus);

		comboBox = new JComboBox<TargetModel>(config.getScheiben());
		//comboBox = new JComboBox<TargetModel>();
		comboBox.setSelectedItem(config.getStandardRegel().getScheibe());
		comboBox.setBounds(480, 15, 180, 20);
		comboBox.setActionCommand("TYP");
		comboBox.addActionListener(this);
		this.add(comboBox);
		
		JButton button_plus = new JButton("+");
		button_plus.setBounds(675, 15, 45, 20);
		add(button_plus);
		
		final int X[] = {420, 160};
		final int Y[] = {70, 45};
		
		text_name = addJTextField(this, X[0], Y[0] + 0 * Y[1], 160, "Name");
		text_number = addJTextField(this, X[0] + 180, Y[0] + 0 * Y[1], 90, "Kennnummer");

		spinner_size = addJSpinner(this, X[0], Y[0] + 1 * Y[1], "Kartongröße", "mm");
		spinner_feed = addJSpinner(this, X[0] + X[1], Y[0] + 1 * Y[1], "Bandvorschub", "");

		spinner_dia_outside = addJSpinner(this, X[0], Y[0] + 2 * Y[1], "Ø Aussen", "mm");
		spinner_ring_width = addJSpinner(this, X[0] + X[1], Y[0] + 2 * Y[1], "Ringbreite", "mm");

		spinner_dia_black = addJSpinner(this, X[0], Y[0] + 3 * Y[1], "Ø Spiegel", "mm");
		spinner_dia_inner_ten = addJSpinner(this, X[0] + X[1], Y[0] + 3 * Y[1], "Ø Innenzehn", "mm");
		
		spinner_ring_min = addJSpinner(this, X[0], Y[0] + 4 * Y[1], "Kleinster Ring", "");
		spinner_ring_max = addJSpinner(this, X[0] + X[1], Y[0] + 4 * Y[1], "Größter Ring", "");

		spinner_num_max = addJSpinner(this, X[0], Y[0] + 5 * Y[1], "Größte Ringzahl", "");
		comboBox_ring_angle = addJComboBox(this, X[0] + X[1], Y[0] + 5 * Y[1], "Winkel Ringzahlen", "°", TargetAngle.values());
		((JLabel) comboBox_ring_angle.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);

		comboBox_typ = addJComboBox(this, X[0], Y[0] + 6 * Y[1], "Scheibenart", "", TargetType.values());
		comboBox_style = addJComboBox(this, X[0] + X[1], Y[0] + 6 * Y[1], "Ausgefüllter Ring", "", TargetFill.values());
	
		spinner_vorhaltediameter = addJSpinner(this, X[0], Y[0] + 7 * Y[1], "Ø Vorhaltespiegel", "mm");
		spinner_vorhalteabstand =  addJSpinner(this, X[0] + X[1], Y[0] + 7 * Y[1], "Vorhalteabstand", "mm");
		
		updateDisplay();
		doUpdate = true;
	}

	private void updateDisplay() {
		doUpdate = false;

		TargetModel target = (TargetModel) comboBox.getSelectedItem();
		scheibe.setTarget(target);
		text_name.setText(target.toString());
		text_number.setText(target.getNumber());			
		spinner_size.setValue(target.getValue(TargetValue.SIZE) / 100.);
		spinner_feed.setValue(target.getValue(TargetValue.FEED));
		spinner_dia_outside.setValue(target.getValue(TargetValue.DIA_OUTSIDE) / 100.);
		spinner_ring_width.setValue(target.getValue(TargetValue.RING_WIDTH) / 100.);
		spinner_dia_black.setValue(target.getValue(TargetValue.DIA_BLACK) / 100.);
		spinner_dia_inner_ten.setValue(target.getValue(TargetValue.DIA_INNER_TEN) / 100.);
		spinner_ring_min.setValue(target.getValue(TargetValue.RING_MIN));
		spinner_ring_max.setValue(target.getValue(TargetValue.RING_MAX));
		spinner_num_max.setValue(target.getValue(TargetValue.NUM_MAX));
		comboBox_ring_angle.setSelectedIndex(target.getValue(TargetValue.NUM_ANGLE));
		comboBox_typ.setSelectedIndex(target.getValue(TargetValue.TYPE));
		comboBox_style.setSelectedIndex(target.getValue(TargetValue.STYLE_TEN));
		spinner_vorhaltediameter.setValue(target.getValue(TargetValue.SUSP_DIA) / 100.);
		spinner_vorhalteabstand.setValue(target.getValue(TargetValue.SUSP_DISTANCE) / 100.);

		doUpdate = true;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (!doUpdate) return;

		TargetModel target = (TargetModel) comboBox.getSelectedItem();
		switch (arg0.getActionCommand()) {
			case "TYP":               updateDisplay();                                                                break;
			case "Name":              target.setName(text_name.getText());                                            break;
			case "Kennnummer":        target.setNumber(text_number.getText());                                        break;
			case "Winkel Ringzahlen": target.setValue(TargetValue.NUM_ANGLE, comboBox_ring_angle.getSelectedIndex()); break;
			case "Scheibenart":       target.setValue(TargetValue.TYPE, comboBox_typ.getSelectedIndex());             break;
			case "Ausgefüllter Ring": target.setValue(TargetValue.STYLE_TEN, comboBox_style.getSelectedIndex());      break;
		}
		scheibe.setTarget(target);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		if (!doUpdate) return;

		boolean updateNeeded = false;
		TargetModel target = (TargetModel) comboBox.getSelectedItem();
		switch (((JSpinner) arg0.getSource()).getName()) {
			case "Kartongröße":       updateNeeded = target.setValue(TargetValue.SIZE,          (int) ((double) spinner_size.getValue()             * 100)); break;
			case "Bandvorschub":      updateNeeded = target.setValue(TargetValue.FEED,          (int)           spinner_feed.getValue());                    break;
			case "Ø Aussen":          updateNeeded = target.setValue(TargetValue.DIA_OUTSIDE,   (int) ((double) spinner_dia_outside.getValue()      * 100)); break;
			case "Ringbreite":        updateNeeded = target.setValue(TargetValue.RING_WIDTH,    (int) ((double) spinner_ring_width.getValue()       * 100)); break;
			case "Ø Spiegel":         updateNeeded = target.setValue(TargetValue.DIA_BLACK,     (int) ((double) spinner_dia_black.getValue()        * 100)); break;
			case "Ø Innenzehn":       updateNeeded = target.setValue(TargetValue.DIA_INNER_TEN, (int) ((double) spinner_dia_inner_ten.getValue()    * 100)); break;
			case "Kleinster Ring":    updateNeeded = target.setValue(TargetValue.RING_MIN,      (int)           spinner_ring_min.getValue());                break;
			case "Größter Ring":      updateNeeded = target.setValue(TargetValue.RING_MAX,      (int)           spinner_ring_max.getValue());                break;
			case "Größte Ringzahl":   updateNeeded = target.setValue(TargetValue.NUM_MAX,       (int)           spinner_num_max.getValue());                 break;
			case "Ø Vorhaltespiegel": updateNeeded = target.setValue(TargetValue.SUSP_DIA,      (int) ((double) spinner_vorhaltediameter.getValue() * 100)); break;
			case "Vorhalteabstand":   updateNeeded = target.setValue(TargetValue.SUSP_DISTANCE, (int) ((double) spinner_vorhalteabstand.getValue()  * 100)); break;
		}
		if (updateNeeded) updateDisplay();
		scheibe.setTarget(target);
	}

	private JTextField addJTextField(JPanel parent, int x, int y, int width, String caption) {
		JTextField textField = new JTextField();
		textField.setBounds(x, y, width, 20);
		textField.setColumns(10);
		textField.setActionCommand(caption);
		textField.addActionListener(this);
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

	private JComboBox<Object> addJComboBox(JPanel parent, int x, int y, String caption, String unit, Object content[]) {
		JComboBox<Object> comboBox = new JComboBox<Object>() {
			@Override
			public Dimension getSize() {
				Dimension dim = super.getSize();
				dim.width = Math.max(dim.width, getPreferredSize().width - 20);
				return dim;
			}
		};
		comboBox.setModel(new DefaultComboBoxModel<Object>(content));
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