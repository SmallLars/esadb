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

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.BoxLayout;


@SuppressWarnings("serial")
public class Info extends JDialog implements ActionListener {

	public Info(Frame parent) {
		super(parent, "Info");
		setResizable(false);

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.CENTER);
			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			{
				JLabel label = new JLabel("ESADB 2.0.10 vom 21.05.2018");
				panel.add(label);
				panel.add(Box.createVerticalStrut(8));
			}
			{
				JLabel label = new JLabel("<html><body><a href=\"https://github.com/SmallLars/esadb\">https://github.com/SmallLars/esadb</a></body></html>");
				label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				label.addMouseListener(new MouseAdapter() {
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
				panel.add(label);
				panel.add(Box.createVerticalStrut(8));
			}
			{
				JLabel label = new JLabel("Copyright © 2015-2018 Lars Schmertmann.");
				panel.add(label);
				panel.add(Box.createVerticalStrut(8));
			}
			{
				JLabel label = new JLabel("SmallLars@t-online.de");
				panel.add(label);
				panel.add(Box.createVerticalStrut(8));
			}
			{
				JLabel label = new JLabel("Alle Rechte vorbehalten.");
				panel.add(label);
			}
		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.SOUTH);
			panel.setBorder(null);
			panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton button = new JButton("Schließen");
				button.setActionCommand("CANCEL");
				button.addActionListener(this);
				panel.add(button);
				getRootPane().setDefaultButton(button);
			}
		}

		pack();
		setLocationRelativeTo(parent);
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
