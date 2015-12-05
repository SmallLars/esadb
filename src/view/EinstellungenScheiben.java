package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import model.Config;
import model.RegelTyp;

import javax.swing.border.LineBorder;

import java.awt.Color;

@SuppressWarnings("serial")
public class EinstellungenScheiben extends JPanel implements ActionListener {
	
	private JComboBox<RegelTyp> comboBox;
	private Scheibe scheibe;

	/**
	 * Create the panel.
	 */
	public EinstellungenScheiben(Config config) {
		this.setSize(620,  405);
		this.setLayout(null);

		comboBox = new JComboBox<RegelTyp>(config.getRegeln());
		//comboBox = new JComboBox<RegelTyp>();
		comboBox.setSelectedItem(config.getStandardRegel());
		comboBox.setBounds(15, 15, 200, 25);
		comboBox.setActionCommand("TYP");
		comboBox.addActionListener(this);
		this.add(comboBox);

		scheibe = new Scheibe((RegelTyp) comboBox.getSelectedItem());
		scheibe.setBorder(new LineBorder(new Color(0, 0, 0)));
		scheibe.setBounds(230, 15, 375, 375);
		this.add(scheibe);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
			case "TYP":
				RegelTyp typ = (RegelTyp) comboBox.getSelectedItem();
				scheibe.setTyp(typ);
				break;
		}
	}
}
