package view;


import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;

import model.Rule;
import model.Hit;
import controller.Controller;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JComboBox;


@SuppressWarnings("serial")
public class HitAdd extends JDialog implements ComponentListener, ListSelectionListener, ActionListener {

	private Controller controller;
	
	private JComboBox<Rule> comboBox;
	private Target scheibe;
	private HitCreate treffer;
	
	private JButton button;

	private JLabel lblZwischenablage;

	private JScrollPane scrollPane_1;
	private JTable table_1;
	
	private JButton cancelButton;
	private JButton button_1;

	public HitAdd(Frame parent) {
		super(parent, "Treffer eingeben");

		controller = Controller.get();

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		Dimension d = new Dimension(719, 500);
		setMinimumSize(d);
		setSize(d);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		comboBox = new JComboBox<Rule>();//controller.getRules());
		comboBox.setSelectedItem(controller.getStandardRule());
		comboBox.setBounds(411, 15, 200, 25);
		comboBox.setActionCommand("TYP");
		comboBox.addActionListener(this);
		getContentPane().add(comboBox);

		scheibe = new Target(controller.getStandardRule());
		scheibe.setBounds(324, 52, 375, 375);
		scheibe.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				treffer.setPosition(scheibe.getPointForPixel(e.getX(), e.getY()));
			}			
		});
		getContentPane().add(scheibe);

		treffer = new HitCreate(scheibe, controller.getStandardRule());
		treffer.setSize(300, 200);
		treffer.setLocation(12, 10);
		getContentPane().add(treffer);

		button_1 = new JButton("-");
		button_1.setBounds(14, 222, 44, 20);
		button_1.setActionCommand("REMOVE");
		button_1.addActionListener(this);
		getContentPane().add(button_1);

		button = new JButton("+");
		button.setBounds(266, 222, 44, 20);
		button.setActionCommand("ADD");
		button.addActionListener(this);
		getContentPane().add(button);

		lblZwischenablage = new JLabel("Zwischenablage");
		lblZwischenablage.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblZwischenablage.setBounds(12, 257, 300, 22);
		getContentPane().add(lblZwischenablage);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 290, 300, 172);
		getContentPane().add(scrollPane_1);
		
		table_1 = new JTable(new HitTableModel(controller.getTreffer()));
		table_1.setDefaultRenderer(Hit.class, new HitTableCellRenderer());
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.getSelectionModel().addListSelectionListener(this);
		table_1.getTableHeader().setReorderingAllowed(false);
		scrollPane_1.setViewportView(table_1);

		cancelButton = new JButton("Schließen");
		cancelButton.setBounds(599, 439, 100, 23);
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
		scrollPane_1.setSize(300, d.height - 301);
		cancelButton.setLocation(d.width - 112, d.height - 34);
		scheibe.setSize(d.width - 336, d.height - 98);
		comboBox.setLocation(224 + scheibe.getWidth() / 2, 15);
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
		
		Hit t = (Hit) table_1.getValueAt(row, -1);
		treffer.setValues(t);
	}
	
	// ActionListener
	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
			case "CANCEL":
				setVisible(false);
				break;
			case "TYP":
				Rule typ = (Rule) comboBox.getSelectedItem();
				scheibe.setRule(typ);
				treffer.setTyp(typ);
				break;
			case "ADD":
				controller.add(treffer.getTreffer());
				table_1.setModel(new HitTableModel(controller.getTreffer()));
				break;
			case "REMOVE":
				int index = table_1.getSelectedRow();
				if (index < 0) break;

				Hit h = (Hit) table_1.getValueAt(index, 2);
				if (h.getLinie() > 0) {
					JOptionPane.showMessageDialog(
						this,
						"Der Treffer kann nicht gelöscht werden, da er von einer Linie stammt.\nNur Treffer der Linie 0 (von esadb erzeugt) können gelöscht werden.",
						"Löschen nicht möglich",
						JOptionPane.INFORMATION_MESSAGE
					);
					break;
				}

				controller.remove(h);
				table_1.setModel(new HitTableModel(controller.getTreffer()));
				break;
		}
	}
}