package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import model.SettingsModel;
import model.TargetModel;

import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;


@SuppressWarnings("serial")
public class SettingsTargets extends JPanel implements ActionListener {
	
	private JComboBox<TargetModel> comboBox;
	private Target scheibe;
	private JTextField text_name;
	private JTextField text_number;
	private JSpinner spinner_feed;
	private JSpinner spinner_size;
	private JSpinner spinner_dia_black;
	private JSpinner spinner_dia_outside;
	private JSpinner spinner_dia_inner_ten;
	private JSpinner spinner_ring_min;
	private JSpinner spinner_ring_max;
	private JSpinner spinner_num_max;
	private JComboBox comboBox_ring_angle;
	private JComboBox comboBox_typ;
	private JComboBox comboBox_style;
	private JSpinner spinner_vorhaltediameter;
	private JSpinner spinner_vorhalteabstand;

	/**
	 * Create the panel.
	 */
	public SettingsTargets(SettingsModel config) {
		this.setSize(735,  420);
		this.setLayout(null);

		comboBox = new JComboBox<TargetModel>(config.getScheiben());
		//comboBox = new JComboBox<TargetModel>();
		comboBox.setSelectedItem(config.getStandardRegel().getScheibe());
		comboBox.setBounds(15, 15, 250, 25);
		comboBox.setActionCommand("TYP");
		comboBox.addActionListener(this);
		this.add(comboBox);

		scheibe = new Target((TargetModel) comboBox.getSelectedItem());
		scheibe.setBorder(new LineBorder(new Color(0, 0, 0)));
		scheibe.setBounds(330, 15, 390, 390);
		this.add(scheibe);
		
		JButton button = new JButton("+");
		button.setBounds(275, 16, 45, 23);
		add(button);
		
		final int X[] = {15, 160};
		final int Y[] = {70, 45};
		
		text_name = addJTextField(this, X[0], Y[0] + 0 * Y[1], 270, "Name");
		
		text_number = addJTextField(this, X[0], Y[0] + 1 * Y[1], 110, "Kennnummer");
		spinner_feed = addJSpinner(this, X[0] + X[1], Y[0] + 1 * Y[1], "Bandvorschub", "");

		spinner_size = addJSpinner(this, X[0], Y[0] + 2 * Y[1], "Kartongröße", "mm");		
		spinner_dia_black = addJSpinner(this, X[0] + X[1], Y[0] + 2 * Y[1], "Ø Spiegel", "mm");

		spinner_dia_outside = addJSpinner(this, X[0], Y[0] + 3 * Y[1], "Ø Aussen", "mm");
		spinner_dia_inner_ten = addJSpinner(this, X[0] + X[1], Y[0] + 3 * Y[1], "Ø Innenzehn", "mm");
		
		spinner_ring_min = addJSpinner(this, X[0], Y[0] + 4 * Y[1], "Kleinster Ring", "");
		spinner_ring_max = addJSpinner(this, X[0] + X[1], Y[0] + 4 * Y[1], "Größter Ring", "");

		spinner_num_max = addJSpinner(this, X[0], Y[0] + 5 * Y[1], "Größte Ringzahl", "mm");
		comboBox_ring_angle = addJComboBox(this, X[0] + X[1], Y[0] + 5 * Y[1], "Winkel Ringzahlen", "°", "0", "45");

		comboBox_typ = addJComboBox(this, X[0], Y[0] + 6 * Y[1], "Scheibenart", "", "1", "2", "3", "4", "5", "6");
		comboBox_style = addJComboBox(this, X[0] + X[1], Y[0] + 6 * Y[1], "Ausgefüllt", "", "Keiner", "Innenzehn", "Zehn");
	
		spinner_vorhaltediameter = addJSpinner(this, X[0], Y[0] + 7 * Y[1], "Ø Vorhaltespiegel", "mm");
		spinner_vorhalteabstand =  addJSpinner(this, X[0] + X[1], Y[0] + 7 * Y[1], "Vorhalteabstand", "mm");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
			case "TYP":
				scheibe.setTarget((TargetModel) comboBox.getSelectedItem());
				break;
		}
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
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), new Integer(9999999), new Integer(1)));
		spinner.setBounds(x, y, 110, 20);
		parent.add(spinner);
		addCaption(spinner, parent, caption, unit);
		return spinner;
	}

	private JComboBox<String> addJComboBox(JPanel parent, int x, int y, String caption, String unit, String... values) {
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(values));
		((JLabel) comboBox.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
		comboBox.setBounds(x, y, 110, 20);
		parent.add(comboBox);
		addCaption(comboBox, parent, caption, unit);
		return comboBox;
	}

	private void addCaption(JComponent component, JPanel parent, String caption, String unit) {
		JLabel label;
		
		label = new JLabel(caption);
		label.setBounds(component.getX(), component.getY() - 20, component.getWidth() + 30, 20);
		parent.add(label);

		if (unit.isEmpty()) return;

		label = new JLabel(unit);
		label.setBounds(component.getX() + component.getWidth() + 6, component.getY(), 24, component.getHeight());
		parent.add(label);
	}
}
