package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;

import model.Disziplin;
import model.Einzel;
import model.Start;
import model.Treffer;
import controller.Controller;

import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;


@SuppressWarnings("serial")
public class EinzelEdit extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private List<Einzel> ergebnisse;
	
	DefaultComboBoxModel<Disziplin> modelD;
	JComboBox<Disziplin> disziplin;
	DefaultComboBoxModel<Einzel> modelS;
	JComboBox<Einzel> start;
	JCheckBox chckbxProbe;
	JCheckBox chckbxMatch;
	private JTable table;

	public EinzelEdit(Frame parent, Controller controller) {
		super(parent, "Ergebnisauswahl");
		setTitle("Ergebnis bearbeiten");
		ergebnisse = new Vector<Einzel>();
		for (Start s : controller.getModel().getErgebnisse()) {
			if (s instanceof Einzel) ergebnisse.add((Einzel) s);
		}

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 700, 500);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		modelD = new DefaultComboBoxModel<Disziplin>();
		disziplin = new JComboBox<Disziplin>(modelD);
		disziplin.setBounds(10, 11, 300, 22);
		disziplin.setActionCommand("DISZIPLIN");
		disziplin.addActionListener(this);
		contentPanel.add(disziplin);

		modelS = new DefaultComboBoxModel<Einzel>();
		start = new JComboBox<Einzel>(modelS);
		start.setBounds(10, 59, 300, 22);
		start.setActionCommand("START");
		start.addActionListener(this);
		contentPanel.add(start);

		JButton cancelButton = new JButton("Schlieﬂen");
		cancelButton.setBounds(582, 439, 100, 23);
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		contentPanel.add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 92, 300, 370);
		contentPanel.add(scrollPane);
		
		table = new JTable(new TrefferTableModel(new Vector<Treffer>()));
		scrollPane.setViewportView(table);

		addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {}

			@Override
			public void componentMoved(ComponentEvent arg0) {}

			@Override
			public void componentResized(ComponentEvent arg0) {
				Dimension d = getContentPane().getSize();
				cancelButton.setLocation(d.width - 112, d.height - 34);
			}

			@Override
			public void componentShown(ComponentEvent arg0) {}
			
		});

		for (Einzel e : ergebnisse) {
			if (modelD.getIndexOf(e.getDisziplin()) == -1) {
				disziplin.addItem(e.getDisziplin());
			}
		}
		setSchutzeItems();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
			case "CANCEL":
				setVisible(false);
				break;
			case "DISZIPLIN":
				setSchutzeItems();
				break;
			case "START":
				if (start.getSelectedItem() == null) break;
				table.setModel(new TrefferTableModel(((Einzel) start.getSelectedItem()).getTreffer()));
				table.setDefaultRenderer(Object.class, new TrefferTableCellRenderer());
				break;
		}
	}

	private void setSchutzeItems() {
		start.removeAllItems();
		for (Einzel e : ergebnisse) {
			if (e.getDisziplin() == disziplin.getSelectedItem()) {
				if (modelS.getIndexOf(e) == -1) {
					start.addItem((Einzel) e);
				}
			}
		}
	}
}