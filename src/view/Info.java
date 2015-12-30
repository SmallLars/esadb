package view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
		setSize(305, 175);
		setLocationRelativeTo(parent);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblEsadb = new JLabel("ESADB (2.0.0)");
		lblEsadb.setBounds(10, 10, 280, 20);
		contentPanel.add(lblEsadb);
		
		JLabel lblSource = new JLabel("<html><body><a href=\"https://github.com/SmallLars/esadb\">https://github.com/SmallLars/esadb</a></body></html>");
		lblSource.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblSource.setBounds(10, 30, 280, 20);
		lblSource.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 0) {
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
						try {
							Desktop.getDesktop().browse(new URI("https://github.com/SmallLars/esadb"));
						} catch (IOException | URISyntaxException e1) {
							// Do nothing
							// e1.printStackTrace();
						}
					}
				}
			}
		});
		contentPanel.add(lblSource);
		
		JLabel lblAuthor = new JLabel("Copyright © 2015-2016 Lars Schmertmann.");
		lblAuthor.setBounds(10, 50, 280, 20);
		contentPanel.add(lblAuthor);

		JLabel lblEmail = new JLabel("SmallLars@t-online.de");
		lblEmail.setBounds(10, 70, 300, 20);
		contentPanel.add(lblEmail);

		JLabel lblRights = new JLabel("Alle Rechte vorbehalten.");
		lblRights.setBounds(10, 90, 280, 20);
		contentPanel.add(lblRights);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton cancelButton = new JButton("Schließen");
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
