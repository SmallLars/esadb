package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.Treffer;

@SuppressWarnings("serial")
public class TrefferCreate extends JPanel implements DocumentListener, ItemListener, FocusListener {
	
	private boolean doUpdate;
	private NumberFormat format;
	private Scheibe scheibe;

	private JFormattedTextField component_V;
	private JComboBox<String>   component_L;
	private JFormattedTextField component_X;
	private JFormattedTextField component_Y;
	private JFormattedTextField component_R;
	private JFormattedTextField component_W;
	private JFormattedTextField component_Z;

	public TrefferCreate(Scheibe scheibe) {
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Trefferdetails", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.doUpdate = true;
		this.format = NumberFormat.getInstance(Locale.GERMAN);
		this.scheibe = scheibe;

		setLayout(null);
		this.setSize(300, 200);

		JLabel lblWert = new JLabel("Wert");
		lblWert.setBounds(10, 25, 46, 14);
		add(lblWert);

		component_V = new JFormattedTextField(getFormat(2, 1));
		component_V.setText("10,9");
		component_V.setBounds(37, 21, 86, 23);
		component_V.getDocument().addDocumentListener(this);
		component_V.getDocument().putProperty("FIELD", component_V);
		component_V.addFocusListener(this);
		add(component_V);
		component_V.setColumns(10);
		
		JLabel lblLage = new JLabel("Lage");
		lblLage.setBounds(151, 25, 46, 14);
		add(lblLage);
		
		component_L = new JComboBox<String>();
		component_L.setModel(new DefaultComboBoxModel<String>(new String[] {"R", "f", "g", "h", "i", "j", "k", "l", "m"}));
		component_L.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		    	URL url = getClass().getResource("/" + (String) value + ".png");
				setIcon(url == null ? null : new ImageIcon(url));
				setHorizontalAlignment(CENTER);
				setText("");
				return this;
			}
			
		});
		component_L.setBounds(178, 21, 62, 22);
		component_L.setActionCommand("LAGE");
		component_L.addItemListener(this);
		add(component_L);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(null), "Kartesische Koordinaten", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 55, 232, 46);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblX = new JLabel("X");
		lblX.setBounds(10, 21, 14, 14);
		panel.add(lblX);
		
		component_X = new JFormattedTextField(getFormat(2, 3));
		component_X.setText("0,000");
		component_X.setBounds(20, 18, 86, 20);
		component_X.getDocument().addDocumentListener(this);
		component_X.getDocument().putProperty("FIELD", component_X);
		panel.add(component_X);
		component_X.setColumns(10);
		
		JLabel lblY = new JLabel("Y");
		lblY.setBounds(116, 21, 14, 14);
		panel.add(lblY);
		
		component_Y = new JFormattedTextField(getFormat(2, 3));
		component_Y.setText("0,000");
		component_Y.setBounds(126, 18, 86, 20);
		component_Y.getDocument().addDocumentListener(this);
		component_Y.getDocument().putProperty("FIELD", component_Y);
		panel.add(component_Y);
		component_Y.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Polarkoordinaten", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 112, 232, 46);
		add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblR = new JLabel("R");
		lblR.setBounds(10, 21, 14, 14);
		panel_1.add(lblR);
		
		component_R = new JFormattedTextField(getFormat(2, 4));
		component_R.setText("0,0000");
		component_R.setBounds(20, 18, 86, 20);
		component_R.getDocument().addDocumentListener(this);
		component_R.getDocument().putProperty("FIELD", component_R);
		component_R.addFocusListener(this);
		panel_1.add(component_R);
		component_R.setColumns(10);
		
		JLabel lblW = new JLabel("W");
		lblW.setBounds(116, 21, 14, 14);
		panel_1.add(lblW);
		
		component_W = new JFormattedTextField(getFormat(3, 3));
		component_W.setText("0,000");
		component_W.setBounds(126, 18, 86, 20);
		component_W.getDocument().addDocumentListener(this);
		component_W.getDocument().putProperty("FIELD", component_W);
		component_W.addFocusListener(this);
		panel_1.add(component_W);
		component_W.setColumns(10);
		
		JLabel lblZeit = new JLabel("Zeit");
		lblZeit.setBounds(10, 169, 46, 14);
		add(lblZeit);
		
		component_Z = new JFormattedTextField(getFormat(4, 0));
		component_Z.setText("0");
		component_Z.setBounds(37, 166, 86, 20);
		component_Z.getDocument().addDocumentListener(this);
		component_Z.getDocument().putProperty("FIELD", component_Z);
		add(component_Z);
		component_Z.setColumns(10);

		scheibe.setTreffer(getTreffer());
	}

	public void setValues(Treffer t) {
		doUpdate = false;
		component_V.setText(String.format("%.1f", t.getWert()));
		component_L.setSelectedItem(t.getLage());
		component_X.setText(String.format("%.3f", t.getX() / 100));
		component_Y.setText(String.format("%.3f", t.getY() / 100));
		component_R.setText(String.format("%.4f", t.getR() / 100));
		component_W.setText(String.format("%.3f", t.getPhi()));
		component_Z.setText(String.format("%d", t.getZeit()));
		doUpdate = true;
		scheibe.setTreffer(getTreffer());
	}

	public Treffer getTreffer() {
		try {
			float wert = format.parse(component_V.getText()).floatValue();
			String lage = (String) component_L.getSelectedItem();
			double x = format.parse(component_X.getText()).doubleValue() * 100;
			double y = format.parse(component_Y.getText()).doubleValue() * 100;
			double r = format.parse(component_R.getText()).doubleValue() * 100;
			double phi = format.parse(component_W.getText()).doubleValue();
			int zeit = format.parse(component_Z.getText()).intValue();
			return new Treffer(wert, lage, x, y, r, phi, zeit);
		} catch (ParseException e) {
			return null;
		}	
	}

	// DocumentListener
	@Override
	public void changedUpdate(DocumentEvent e) {
		update(e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		update(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		update(e);
	}
	
	// ItemListener
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED && doUpdate) {
			doUpdate = false;
			updateWorRwithL();
			updateXandYwithRandW();
			updateVwithR();
			doUpdate = true;
			scheibe.setTreffer(getTreffer());
		}
	}
	
	// FocusListener
	@Override
	public void focusGained(FocusEvent e) {}

	@Override
	public void focusLost(FocusEvent e) {
		JFormattedTextField textField = (JFormattedTextField) e.getComponent();
		Double d = null;
		try {
			d = format.parse(textField.getText()).doubleValue();
		} catch (ParseException e1) {
			textField.setText("0");
			return;
		}

		if (textField == component_V) {
			if (d != 0) {
				if (d < 1) {
					textField.setText("1");
					return;
				}
				if (d > 10.9) {
					textField.setText("10,9");
					return;
				}
			}
			return;
		}

		if (textField == component_R) {
			if (d < 0) {
				textField.setText("0");
			}
			return;
		}

		if (textField == component_W) {
			while (d < 0 || d >= 360) {
				d += (d < 0 ? 360 : -360);
				textField.setText(String.format("%.4f", d));
			}
			return;
		}
	}
	
	// Private Hilfsmethoden

	private NumberFormat getFormat(int integer, int fraction) {
		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(false);
		format.setMaximumIntegerDigits(integer);
		format.setMinimumFractionDigits(fraction);
		format.setMaximumFractionDigits(fraction);
		return format;
	}

	private void update(DocumentEvent e) {
		if (doUpdate) {
			JFormattedTextField textField = (JFormattedTextField) e.getDocument().getProperty("FIELD");
			try {
				format.parse(textField.getText());
			} catch (ParseException e1) {
				return;
			}

			doUpdate = false;
			if (textField == component_V) {
				updateRwithV();
				updateXandYwithRandW();
			}
			if (textField == component_X || textField == component_Y) {
				updateRandWwithXandY();
				updateVwithR();
				updateLwithRandW();
			}
			if (textField == component_R || textField == component_W) {
				updateXandYwithRandW();
				updateVwithR();
				updateLwithRandW();
			}
			doUpdate = true;
			scheibe.setTreffer(getTreffer());
		}
	}

	private void updateRwithV() {
		double d;
		try {
			d = format.parse(component_V.getText()).floatValue();
		} catch (ParseException e) {
			return;
		}
		
		if (d == 0 || (d >= 1 && d <= 10.9)) {
			component_R.setText(String.format("%.1f", Math.round(880 - d * 80) / 10f));
		}
	}

	private void updateWorRwithL() {
		double d;
		try {
			d = format.parse(component_R.getText()).doubleValue();
		} catch (ParseException e) {
			return;
		}

		String lage = (String) component_L.getSelectedItem();
		if (lage.equals("R")) {
			if (d > 5.3) {
				component_R.setText("5,3");
			}
		} else {
			if (d <= 5.3) {
				component_R.setText("7");
			}
			switch (lage) {
				case "f": component_W.setText("180"); break;
				case "g": component_W.setText("0"); break;
				case "h": component_W.setText("90"); break;
				case "i": component_W.setText("270"); break;
				case "j": component_W.setText("135"); break;
				case "k": component_W.setText("45"); break;
				case "l": component_W.setText("225"); break;
				case "m": component_W.setText("315"); break;
			}
		}
	}

	private void updateVwithR() {
		double d;
		try {
			d = format.parse(component_R.getText()).doubleValue();
		} catch (ParseException e) {
			return;
		}

		d = (int) (110 - d / 0.8) / 10f;
		if (d < 1) d = 0;
		if (d > 10.9) d = 10.9;
		component_V.setText(String.format("%.1f", d));
	}

	private void updateLwithRandW() {
		double r;
		double w;
		try {
			r = format.parse(component_R.getText()).doubleValue();
			w = format.parse(component_W.getText()).doubleValue();
		} catch (ParseException e) {
			return;
		}

		if (r <= 5.3) {
			component_L.setSelectedItem("R");
		} else {
			String[] values = {"g", "k", "h", "j", "f", "l", "i", "m", "g"};
			for (int i = 0; i <= 8; i++) {
				if (w < 22.5 + i * 45) {
					component_L.setSelectedItem(values[i]);
					break;
				}
			}
		}
		
	}

	private void updateXandYwithRandW() {
		double r;
		double w;
		try {
			r = format.parse(component_R.getText()).doubleValue();
			w = format.parse(component_W.getText()).doubleValue();
		} catch (ParseException e) {
			return;
		}

		double rad = w / 180 * Math.PI;
		component_X.setText(String.format("%.3f",  Math.cos(rad) * r));
		component_Y.setText(String.format("%.3f",  Math.sin(rad) * r));
	}

	private void updateRandWwithXandY() {
		double x;
		double y;
		try {
			x = format.parse(component_X.getText()).doubleValue();
			y = format.parse(component_Y.getText()).doubleValue();
		} catch (ParseException e) {
			return;
		}

		double w = Math.atan2(y , x) / Math.PI * 180;
		component_R.setText(String.format("%.4f", Math.sqrt(x * x + y * y)));
		component_W.setText(String.format("%.3f", w < 0 ? 360 + w : w));	
	}

}