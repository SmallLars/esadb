package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;


@SuppressWarnings("serial")
public class Info extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();


	public Info(Frame parent) {
		super(parent, "Info");
		setResizable(false);

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(300, 155);
		setLocationRelativeTo(parent);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblEsadb = new JLabel("ESADB (1.0.1)");
		lblEsadb.setBounds(10, 10, 280, 20);
		contentPanel.add(lblEsadb);

		JLabel lblAuthor = new JLabel("Copyright © 2015-2016 Lars Schmertmann.");
		lblAuthor.setBounds(10, 30, 280, 20);
		contentPanel.add(lblAuthor);

		JLabel lblEmail = new JLabel("SmallLars@t-online.de");
		lblEmail.setBounds(10, 50, 300, 20);
		contentPanel.add(lblEmail);

		JLabel lblRights = new JLabel("Alle Rechte vorbehalten.");
		lblRights.setBounds(10, 70, 280, 20);
		contentPanel.add(lblRights);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton cancelButton = new JButton("Schlieﬂen");
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "CANCEL":
				setVisible(false);
				break;
		}
	}
}
