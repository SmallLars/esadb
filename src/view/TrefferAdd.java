package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import model.Treffer;
import controller.Controller;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.border.TitledBorder;


@SuppressWarnings("serial")
public class TrefferAdd extends JDialog implements ComponentListener, ListSelectionListener, ActionListener, DocumentListener, ItemListener, FocusListener {

	private Controller controller;
	private boolean doUpdate;
	private NumberFormat format;
	
	private JButton button;

	private JLabel lblZwischenablage;

	private JScrollPane scrollPane_1;
	private JTable table_1;
	
	private JButton cancelButton;
	private JFormattedTextField textField_V;
	private JComboBox<String> comboBox;
	private JFormattedTextField textField_X;
	private JFormattedTextField textField_Y;
	private JFormattedTextField textField_R;
	private JFormattedTextField textField_W;
	private JFormattedTextField textField_Z;

	public TrefferAdd(Frame parent, Controller controller) {
		super(parent, "Treffer eingeben");

		this.controller = controller;
		this.doUpdate = true;
		this.format = NumberFormat.getInstance(Locale.GERMAN);

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		Dimension d = new Dimension(700, 500);
		setMinimumSize(d);
		setSize(d);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		// float wert, String lage, double x, double y, double r, double phi, int zeit
		// Trefferdetails start

		JLabel lblWert = new JLabel("Wert");
		lblWert.setBounds(10, 109, 46, 14);
		getContentPane().add(lblWert);
		
		JLabel lblLage = new JLabel("Lage");
		lblLage.setBounds(153, 109, 46, 14);
		getContentPane().add(lblLage);
		
		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"f", "g", "h", "i", "j", "k", "l", "m", "R"}));
		comboBox.setRenderer(new DefaultListCellRenderer() {
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
		comboBox.setBounds(180, 105, 62, 22);
		comboBox.setActionCommand("LAGE");
		comboBox.addItemListener(this);
		getContentPane().add(comboBox);
		
		textField_V = new JFormattedTextField(getFormat(2, 1));
		textField_V.setBounds(37, 105, 86, 23);
		textField_V.getDocument().addDocumentListener(this);
		textField_V.getDocument().putProperty("FIELD", textField_V);
		textField_V.addFocusListener(this);
		getContentPane().add(textField_V);
		textField_V.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(null), "Kartesische Koordinaten", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 139, 286, 46);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblX = new JLabel("X");
		lblX.setBounds(10, 21, 14, 14);
		panel.add(lblX);
		
		textField_X = new JFormattedTextField(getFormat(2, 3));
		textField_X.setBounds(20, 18, 86, 20);
		textField_X.getDocument().addDocumentListener(this);
		textField_X.getDocument().putProperty("FIELD", textField_X);
		panel.add(textField_X);
		textField_X.setColumns(10);
		
		JLabel lblY = new JLabel("Y");
		lblY.setBounds(116, 21, 14, 14);
		panel.add(lblY);
		
		textField_Y = new JFormattedTextField(getFormat(2, 3));
		textField_Y.setBounds(126, 18, 86, 20);
		textField_Y.getDocument().addDocumentListener(this);
		textField_Y.getDocument().putProperty("FIELD", textField_Y);
		panel.add(textField_Y);
		textField_Y.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Polarkoordinaten", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 196, 286, 46);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblR = new JLabel("R");
		lblR.setBounds(10, 21, 14, 14);
		panel_1.add(lblR);
		
		textField_R = new JFormattedTextField(getFormat(2, 4));
		textField_R.setBounds(20, 18, 86, 20);
		textField_R.getDocument().addDocumentListener(this);
		textField_R.getDocument().putProperty("FIELD", textField_R);
		textField_R.addFocusListener(this);
		panel_1.add(textField_R);
		textField_R.setColumns(10);
		
		JLabel lblW = new JLabel("W");
		lblW.setBounds(116, 21, 14, 14);
		panel_1.add(lblW);
		
		textField_W = new JFormattedTextField(getFormat(3, 3));
		textField_W.setBounds(126, 18, 86, 20);
		textField_W.getDocument().addDocumentListener(this);
		textField_W.getDocument().putProperty("FIELD", textField_W);
		textField_W.addFocusListener(this);
		panel_1.add(textField_W);
		textField_W.setColumns(10);
		
		JLabel lblZeit = new JLabel("Zeit");
		lblZeit.setBounds(10, 253, 46, 14);
		getContentPane().add(lblZeit);
		
		textField_Z = new JFormattedTextField(getFormat(4, 0));
		textField_Z.setBounds(37, 250, 86, 20);
		textField_Z.getDocument().addDocumentListener(this);
		textField_Z.getDocument().putProperty("FIELD", textField_Z);
		getContentPane().add(textField_Z);
		textField_Z.setColumns(10);

		// Trefferdetails ende
		
		button = new JButton("->");
		button.setBounds(320, 173, 50, 23);
		button.setActionCommand("ADD");
		button.addActionListener(this);
		getContentPane().add(button);

		lblZwischenablage = new JLabel("Zwischenablage");
		lblZwischenablage.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblZwischenablage.setBounds(380, 44, 300, 22);
		getContentPane().add(lblZwischenablage);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(380, 77, 300, 351);
		getContentPane().add(scrollPane_1);
		
		table_1 = new JTable(new TrefferTableModel(controller.getTreffer()));
		table_1.setDefaultRenderer(Treffer.class, new TrefferTableCellRenderer());
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.getSelectionModel().addListSelectionListener(this);
		scrollPane_1.setViewportView(table_1);

		cancelButton = new JButton("Schlieﬂen");
		cancelButton.setBounds(582, 439, 100, 23);
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		getContentPane().add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);
		
		addComponentListener(this);
	}

	// ComponentListener
	@Override
	public void componentResized(ComponentEvent e) {
		Dimension d = getContentPane().getSize();
		button.setLocation(				(d.width / 2) - 26, -2 + scrollPane_1.getHeight() / 2);
		lblZwischenablage.setLocation(	(d.width / 2) + 34, 44);
		lblZwischenablage.setSize(		(d.width - 92) / 2, 22);
		scrollPane_1.setLocation(		(d.width / 2) + 34, 77);
		scrollPane_1.setSize(			(d.width - 92) / 2, d.height - 122);
		cancelButton.setLocation(		d.width - 112, d.height - 34);
	}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}

	// ListSelectionListener
	@Override
	public void valueChanged(ListSelectionEvent e) {
		int row = table_1.getSelectedRow();
		if (row == -1) return;
		
		Treffer t = (Treffer) table_1.getValueAt(row, -1);
		
		doUpdate = false;
		textField_V.setText(String.format("%.1f", t.getWert()));
		comboBox.setSelectedItem(t.getLage());
		textField_X.setText(String.format("%.3f", t.getX() / 100));
		textField_Y.setText(String.format("%.3f", t.getY() / 100));
		textField_R.setText(String.format("%.4f", t.getR() / 100));
		textField_W.setText(String.format("%.3f", t.getPhi()));
		textField_Z.setText(String.format("%d", t.getZeit()));
		doUpdate = true;
	}
	
	// ActionListener
	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
			case "CANCEL":
				setVisible(false);
				break;
			case "ADD":
				/*
				Treffer t = (Treffer) table.getValueAt(row, -1);
				((Einzel) start.getSelectedItem()).removeTreffer(t);
				controller.add(t);
				*/
				table_1.setModel(new TrefferTableModel(controller.getTreffer()));
				break;
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

		if (textField == textField_V) {
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

		if (textField == textField_R) {
			if (d < 0) {
				textField.setText("0");
			}
			return;
		}

		if (textField == textField_W) {
			if (d < 0 || d >= 360) {
				textField.setText("0");
			}
			return;
		}
	}

	// Private Hilfsmethoden

	private NumberFormat getFormat(int integer, int fraction) {
		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(false);
		format.setMaximumIntegerDigits(integer);
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
			if (textField == textField_V) {
				updateRwithV();
				updateXandYwithRandW();
			}
			if (textField == textField_X || textField == textField_Y) {
				updateRandWwithXandY();
				updateVwithR();
				updateLwithRandW();
			}
			if (textField == textField_R || textField == textField_W) {
				updateXandYwithRandW();
				updateVwithR();
				updateLwithRandW();
			}
			doUpdate = true;
			return;
		}
	}

	private void updateRwithV() {
		double d;
		try {
			d = format.parse(textField_V.getText()).floatValue();
		} catch (ParseException e) {
			return;
		}
		
		if (d == 0 || (d >= 1 && d <= 10.9)) {
			textField_R.setText(String.format("%.1f", Math.round(880 - d * 80) / 10f));
		}
	}

	private void updateWorRwithL() {
		double d;
		try {
			d = format.parse(textField_R.getText()).doubleValue();
		} catch (ParseException e) {
			return;
		}

		String lage = (String) comboBox.getSelectedItem();
		if (lage.equals("R")) {
			if (d > 5.3) {
				textField_R.setText("5,3");
			}
		} else {
			if (d <= 5.3) {
				textField_R.setText("7");
			}
			switch (lage) {
				case "f": textField_W.setText("180"); break;
				case "g": textField_W.setText("0"); break;
				case "h": textField_W.setText("90"); break;
				case "i": textField_W.setText("270"); break;
				case "j": textField_W.setText("135"); break;
				case "k": textField_W.setText("45"); break;
				case "l": textField_W.setText("225"); break;
				case "m": textField_W.setText("315"); break;
			}
		}
	}

	private void updateVwithR() {
		double d;
		try {
			d = format.parse(textField_R.getText()).doubleValue();
		} catch (ParseException e) {
			return;
		}

		d = (int) (110 - d / 0.8) / 10f;
		if (d < 1) d = 0;
		if (d > 10.9) d = 10.9;
		textField_V.setText(String.format("%.1f", d));
	}

	private void updateLwithRandW() {
		double r;
		double w;
		try {
			r = format.parse(textField_R.getText()).doubleValue();
			w = format.parse(textField_W.getText()).doubleValue();
		} catch (ParseException e) {
			return;
		}

		if (r <= 5.3) {
			comboBox.setSelectedItem("R");
		} else {
			String[] values = {"g", "k", "h", "j", "f", "l", "i", "m", "g"};
			for (int i = 0; i <= 8; i++) {
				if (w < 22.5 + i * 45) {
					comboBox.setSelectedItem(values[i]);
					break;
				}
			}
		}
		
	}

	private void updateXandYwithRandW() {
		
	}

	private void updateRandWwithXandY() {
		
	}
}