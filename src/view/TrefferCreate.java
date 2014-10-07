package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
public class TrefferCreate extends JPanel implements MouseWheelListener, KeyListener, DocumentListener, ItemListener, FocusListener {
	
	private boolean doUpdate;
	private NumberFormat format;
	private Scheibe scheibe;
	private ScheibeTyp typ;

	private JFormattedTextField component_V;
	private JComboBox<String>   component_L;
	private JFormattedTextField component_X;
	private JFormattedTextField component_Y;
	private JFormattedTextField component_R;
	private JFormattedTextField component_W;
	private JFormattedTextField component_Z;

	public TrefferCreate(Scheibe scheibe, ScheibeTyp typ) {
		setBorder(new TitledBorder(new LineBorder(new Color(122, 138, 153)), "Trefferdetails", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		this.doUpdate = true;
		this.format = NumberFormat.getInstance(Locale.GERMAN);
		this.scheibe = scheibe;
		this.typ = typ;

		setLayout(null);
		this.setSize(300, 225);

		JLabel lblWert = new JLabel("Wert");
		lblWert.setBounds(20, 26, 40, 16);
		add(lblWert);

		component_V = new JFormattedTextField(getFormat(2, 1));
		component_V.setText("10,9");
		component_V.setBounds(60, 24, 64, 20);
		component_V.addMouseWheelListener(this);
		component_V.addKeyListener(this);
		component_V.getDocument().addDocumentListener(this);
		component_V.getDocument().putProperty("FIELD", component_V);
		component_V.addFocusListener(this);
		add(component_V);
		component_V.setColumns(10);
		
		JLabel lblLage = new JLabel("Lage");
		lblLage.setBounds(160, 26, 40, 16);
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
		component_L.setBounds(200, 24, 64, 20);
		component_L.setActionCommand("LAGE");
		component_L.addItemListener(this);
		add(component_L);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(122, 138, 153)), "Kartesische Koordinaten", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 55, 280, 46);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblX = new JLabel("X");
		lblX.setBounds(10, 21, 16, 16);
		panel.add(lblX);
		
		component_X = new JFormattedTextField(getFormat(2, 3));
		component_X.setText("0,000");
		component_X.setBounds(26, 19, 64, 20);
		component_X.addMouseWheelListener(this);
		component_X.addKeyListener(this);
		component_X.getDocument().addDocumentListener(this);
		component_X.getDocument().putProperty("FIELD", component_X);
		panel.add(component_X);
		
		JLabel lblMm = new JLabel("mm");
		lblMm.setBounds(94, 21, 26, 16);
		panel.add(lblMm);
		
		JLabel lblY = new JLabel("Y");
		lblY.setBounds(150, 21, 16, 16);
		panel.add(lblY);
		
		component_Y = new JFormattedTextField(getFormat(2, 3));
		component_Y.setText("0,000");
		component_Y.setBounds(166, 19, 64, 20);
		component_Y.addMouseWheelListener(this);
		component_Y.addKeyListener(this);
		component_Y.getDocument().addDocumentListener(this);
		component_Y.getDocument().putProperty("FIELD", component_Y);
		panel.add(component_Y);
		
		JLabel label = new JLabel("mm");
		label.setBounds(234, 21, 26, 16);
		panel.add(label);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(122, 138, 153)), "Polarkoordinaten", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_1.setBounds(10, 112, 280, 46);
		add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblR = new JLabel("R");
		lblR.setBounds(10, 21, 16, 16);
		panel_1.add(lblR);
		
		component_R = new JFormattedTextField(getFormat(2, 4));
		component_R.setText("0,0000");
		component_R.setBounds(26, 19, 64, 20);
		component_R.addMouseWheelListener(this);
		component_R.addKeyListener(this);
		component_R.getDocument().addDocumentListener(this);
		component_R.getDocument().putProperty("FIELD", component_R);
		component_R.addFocusListener(this);
		panel_1.add(component_R);
		
		JLabel label_1 = new JLabel("mm");
		label_1.setBounds(94, 21, 26, 16);
		panel_1.add(label_1);
		
		JLabel lblW = new JLabel("W");
		lblW.setBounds(150, 21, 16, 16);
		panel_1.add(lblW);
		
		component_W = new JFormattedTextField(getFormat(3, 3));
		component_W.setText("0,000");
		component_W.setBounds(166, 19, 64, 20);
		component_W.addMouseWheelListener(this);
		component_W.addKeyListener(this);
		component_W.getDocument().addDocumentListener(this);
		component_W.getDocument().putProperty("FIELD", component_W);
		component_W.addFocusListener(this);
		panel_1.add(component_W);
		
		JLabel label_2 = new JLabel("\u00B0");
		label_2.setBounds(234, 21, 26, 16);
		panel_1.add(label_2);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(122, 138, 153)), "Zeit", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_2.setBounds(10, 169, 280, 46);
		add(panel_2);
		panel_2.setLayout(null);
		
		component_Z = new JFormattedTextField(getFormat(4, 0));
		component_Z.setBounds(10, 19, 64, 20);
		panel_2.add(component_Z);
		component_Z.setText("0");
		component_Z.getDocument().addDocumentListener(this);
		component_Z.getDocument().putProperty("FIELD", component_Z);
		
		JLabel lblZeit = new JLabel("Sekunden seit Start");
		lblZeit.setBounds(84, 21, 186, 16);
		panel_2.add(lblZeit);

		scheibe.setTreffer(getTreffer());
	}

	public void setTyp(ScheibeTyp typ) {
		this.typ = typ;
		doUpdate = false;
		updateVwithR();
		updateLwithRandW();
		doUpdate = true;
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

	// MouseWheelListener
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		scrollField((JFormattedTextField) e.getComponent(), -e.getWheelRotation());
	}

	// KeyListener
	@Override
	public void keyPressed(KeyEvent e) {
		JFormattedTextField textField = (JFormattedTextField) e.getComponent();
		if (e.getKeyCode() == 38) scrollField(textField, 1);
		if (e.getKeyCode() == 40) scrollField(textField, -1);
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

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
				textField.setText("0,0000");
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

	private void scrollField(JFormattedTextField textField, int amount) {
		Double d = null;
		try {
			d = format.parse(textField.getText()).doubleValue();
		} catch (ParseException e1) {
			return;
		}
		
		if (textField == component_V) {
			d += 0.1 * amount;
			if (d < 1) d = amount < 0 ? 0.0 : 1.0;
			if (d > 10.9) d = 10.9;
			textField.setText(String.format("%.1f", d));
			return;
		}

		d += amount;

		if (textField == component_R) {
			if (d < 0) d = 0.0;
			textField.setText(String.format("%.4f", d));
			return;
		}

		if (textField == component_W) {
			if (d < 0) d += 360;
			if (d >= 360) d -= 360;
		}
		textField.setText(String.format("%.3f", d));
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
		float f;
		try {
			f = format.parse(component_V.getText()).floatValue();
		} catch (ParseException e) {
			return;
		}
		
		if (f == 0 || (f >= 1 && f <= 10.9)) {
			component_R.setText(String.format("%.4f", typ.getRadiusByValue(f) / 100));
		}
	}

	private void updateWorRwithL() {
		double d;
		try {
			d = format.parse(component_R.getText()).doubleValue();
		} catch (ParseException e) {
			return;
		}
		boolean isInnenZehn = typ.isInnenZehn(d * 100);

		String lage = (String) component_L.getSelectedItem();
		if (lage.equals("R")) {
			if (!isInnenZehn) {
				component_R.setText(String.format("%.4f", (typ.getInnenZehnRadius() + typ.getSchussRadius()) / 100f));
			}
		} else {
			if (isInnenZehn) {
				component_R.setText(String.format("%.4f", (typ.getRingRadius(10) + typ.getSchussRadius()) / 100f));
			}
			switch (lage) {
				case "f": component_W.setText("180,000"); break;
				case "g": component_W.setText("0,000"); break;
				case "h": component_W.setText("90,000"); break;
				case "i": component_W.setText("270,000"); break;
				case "j": component_W.setText("135,000"); break;
				case "k": component_W.setText("45,000"); break;
				case "l": component_W.setText("225,000"); break;
				case "m": component_W.setText("315,000"); break;
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

		component_V.setText(String.format("%.1f", typ.getValuebyRadius(d * 100)));
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

		if (typ.isInnenZehn(r * 100)) {
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