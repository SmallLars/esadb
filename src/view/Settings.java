package view;


import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.SettingsModel;

import javax.swing.JTabbedPane;

import controller.Controller;


@SuppressWarnings("serial")
public class Settings extends JDialog {

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
			public void windowClosing(WindowEvent arg0) {
				close(false);
			}
		});

		getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 10, 740, 445);
		SettingsGeneral general = new SettingsGeneral(config);
		SettingsRules rules = new SettingsRules(config);
		SettingsTargets targets = new SettingsTargets(config, rules);
		SettingsWeapons weapons = new SettingsWeapons(config, rules);
		tabbedPane.addTab("Allgemein", general);
		tabbedPane.addTab("Regeln", rules);
		tabbedPane.addTab("Scheiben", targets);
		tabbedPane.addTab("Waffen", weapons);
		getContentPane().add(tabbedPane);

		JButton abortButton = new JButton("Abbrechen");
		abortButton.setBounds(10, 467, 200, 23);
		abortButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				close(false);
			}
		});
		getContentPane().add(abortButton);

		JButton closeButton = new JButton("Speichern & Schließen");
		closeButton.setBounds(550, 467, 200, 23);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				close(true);
			}
		});
		getContentPane().add(closeButton);

		getRootPane().setDefaultButton(abortButton);
	}

	private void close(boolean save) {
		if (save) {
			Controller.get().saveConfig();
			JOptionPane.showMessageDialog(
				this,
				"Um die Änderung zu übernehmen ist ein Neustart des Programms erforderlich.",
				"Neustart erforderlich",
				JOptionPane.INFORMATION_MESSAGE
			);
		} else {
			Controller.get().reloadConfig();
		}

		dispose();
	}
}
