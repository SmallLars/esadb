package view;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import controller.KampfDB;
import model.Member;

import javax.swing.JLabel;


@SuppressWarnings("serial")
public class Update extends JDialog implements ActionListener {

	private JLabel lblName;
	private JLabel lblWertung;
	private JLabel lblZeit;
	private JLabel lblSchusszahl;
	private JLabel lblSerienlaenge;
	private JLabel lblWaffengattung;

	private final JPanel contentPanel = new JPanel();

	public Update(Frame parent) {
		super(parent, "Stammdaten aktualisieren");
		setResizable(false);

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(450, 300);
		if (getGraphicsConfiguration() != null) setLocationRelativeTo(parent);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		lblName = new JLabel("Name");
		lblName.setBounds(10, 11, 422, 14);
		contentPanel.add(lblName);
		
		lblWertung = new JLabel("Wertung");
		lblWertung.setBounds(10, 36, 422, 14);
		contentPanel.add(lblWertung);
		
		lblZeit = new JLabel("Zeit");
		lblZeit.setBounds(10, 61, 422, 14);
		contentPanel.add(lblZeit);
		
		lblSchusszahl = new JLabel("Schusszahl");
		lblSchusszahl.setBounds(10, 86, 422, 14);
		contentPanel.add(lblSchusszahl);
		
		lblSerienlaenge = new JLabel("Serienlänge");
		lblSerienlaenge.setBounds(10, 111, 422, 14);
		contentPanel.add(lblSerienlaenge);
		
		lblWaffengattung = new JLabel("Waffengattung");
		lblWaffengattung.setBounds(10, 136, 422, 14);
		contentPanel.add(lblWaffengattung);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton updateButton = new JButton("Aktualisieren");
		updateButton.setActionCommand("UPDATE");
		updateButton.addActionListener(this);
		buttonPane.add(updateButton);

		JButton cancelButton = new JButton("Schließen");
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "UPDATE":
				update();
				break;
			case "CANCEL":
				setVisible(false);
				break;
		}
	}

	private void update() {
		Set<Member> allMember = KampfDB.getAllMembers();
		
		for (Member m : Controller.get().getSchuetzen()) {
			for (Member newMember : allMember) {
				if (m.equals(newMember)) {
					m.update(newMember);
				}
			}
		}

		Controller.get().getModel().updateDB();
		Controller.get().save();
	}
}
