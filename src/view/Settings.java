package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.SettingsModel;

import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class Settings extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public Settings(Frame parent, SettingsModel config) {
		super(parent, "Einstellungen");
		setResizable(false);

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(765, 525);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {close();}
		});

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);


		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 10, 740, 445);

		JPanel oho1 = new SettingsLines(config);
		tabbedPane.addTab("Linien", oho1);
		
		JPanel oho2 = new SettingsTargets(config);
		tabbedPane.addTab("Scheiben", oho2);
		
		contentPanel.add(tabbedPane);

		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton closeButton = new JButton("Schließen");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {close();}
		});
		buttonPane.add(closeButton);
		getRootPane().setDefaultButton(closeButton);
	}

	private void close() {
		JOptionPane.showMessageDialog(	this,
										"Um die Änderung zu übernehmen ist ein Neustart des Programms erforderlich.",
										"Neustart erforderlich",
										JOptionPane.INFORMATION_MESSAGE);

		dispose();
	}
}
