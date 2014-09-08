package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;

import model.Disziplin;
import model.Start;
import controller.Controller;


@SuppressWarnings("serial")
public class Einzel extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private List<Start> ergebnisse;
	private boolean okKlick;
	
	DefaultComboBoxModel<Disziplin> modelD;
	JComboBox<Disziplin> disziplin;
	DefaultComboBoxModel<Start> modelS;
	JComboBox<Start> start;

	public Einzel(Frame parent, Controller controller) {
		super(parent, "Ergebnisauswahl");
		ergebnisse = controller.getModel().getErgebnisse();

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		modelD = new DefaultComboBoxModel<Disziplin>();
		disziplin = new JComboBox<Disziplin>(modelD);
		disziplin.setBounds(10, 11, 422, 22);
		disziplin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setSchutzeItems();
			}
		});
		contentPanel.add(disziplin);

		modelS = new DefaultComboBoxModel<Start>();
		start = new JComboBox<Start>(modelS);
		start.setBounds(10, 59, 422, 22);
		contentPanel.add(start);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				okKlick = true;
				setVisible(false);				
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);				
			}
		});
		buttonPane.add(cancelButton);
	}

	public Start showDialog() {
		okKlick = false;
		disziplin.removeAllItems();
		for (Start s : ergebnisse) {
			if (modelD.getIndexOf(s.getDisziplin()) == -1) {
				disziplin.addItem(s.getDisziplin());
			}
		}
		setSchutzeItems();
		setVisible(true);
		if (okKlick) {
			return (Start) start.getSelectedItem();
		}
		return null;
	}

	private void setSchutzeItems() {
		start.removeAllItems();
		for (Start s : ergebnisse) {
			if (s.getDisziplin() == disziplin.getSelectedItem()) {
				if (modelS.getIndexOf(s) == -1) {
					start.addItem(s);
				}
			}
		}
	}

}