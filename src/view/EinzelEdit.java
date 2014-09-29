package view;

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
import javax.swing.JComboBox;

import model.Disziplin;
import model.Einzel;
import model.Start;
import model.Treffer;
import controller.Controller;

import javax.swing.JScrollPane;
import javax.swing.JTable;


@SuppressWarnings("serial")
public class EinzelEdit extends JDialog implements ComponentListener, ActionListener {

	private Controller controller;
	private List<Einzel> ergebnisse;
	
	private DefaultComboBoxModel<Disziplin> modelD;
	private JComboBox<Disziplin> disziplin;

	private DefaultComboBoxModel<Einzel> modelS;
	private JComboBox<Einzel> start;

	private JScrollPane scrollPane;
	private JTable table;

	private JButton button;
	private JButton button_1;

	private JScrollPane scrollPane_1;
	private JTable table_1;
	
	private JButton cancelButton;

	public EinzelEdit(Frame parent, Controller controller) {
		super(parent, "Ergebnisauswahl");

		this.controller = controller;
		ergebnisse = new Vector<Einzel>();
		for (Start s : controller.getModel().getErgebnisse()) {
			if (s instanceof Einzel) ergebnisse.add((Einzel) s);
		}

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		Dimension d = new Dimension(700, 500);
		setMinimumSize(d);
		setSize(d);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		modelD = new DefaultComboBoxModel<Disziplin>();
		disziplin = new JComboBox<Disziplin>(modelD);
		disziplin.setBounds(10, 11, 300, 22);
		disziplin.setActionCommand("DISZIPLIN");
		disziplin.addActionListener(this);
		getContentPane().add(disziplin);

		modelS = new DefaultComboBoxModel<Einzel>();
		start = new JComboBox<Einzel>(modelS);
		start.setBounds(10, 44, 300, 22);
		start.setActionCommand("START");
		start.addActionListener(this);
		getContentPane().add(start);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 77, 300, 351);
		getContentPane().add(scrollPane);
		
		table = new JTable(new TrefferTableModel(new Vector<Treffer>()));
		scrollPane.setViewportView(table);

		button = new JButton("<-");
		button.setBounds(320, 223, 50, 23);
		getContentPane().add(button);

		button_1 = new JButton("->");
		button_1.setBounds(320, 258, 50, 23);
		getContentPane().add(button_1);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(380, 77, 300, 351);
		getContentPane().add(scrollPane_1);
		
		table_1 = new JTable(new TrefferTableModel(controller.getTreffer()));
		scrollPane_1.setViewportView(table_1);

		cancelButton = new JButton("Schlieﬂen");
		cancelButton.setBounds(582, 439, 100, 23);
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		getContentPane().add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);

		addComponentListener(this);

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
				table.setDefaultRenderer(Treffer.class, new TrefferTableCellRenderer());
				break;
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Dimension d = getContentPane().getSize();
		cancelButton.setLocation(d.width - 112, d.height - 34);
		disziplin.setSize((d.width - 92) / 2, 22);
		start.setSize((d.width - 92) / 2, 22);
		scrollPane.setSize((d.width - 92) / 2, d.height - 122);
		button.setLocation((d.width / 2) - 26, 48 + scrollPane.getHeight() / 2);
		button_1.setLocation((d.width / 2) - 26, 83 + scrollPane.getHeight() / 2);
		scrollPane_1.setLocation((d.width / 2) + 34, 77);
		scrollPane_1.setSize((d.width - 92) / 2, d.height - 122);
	}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}

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