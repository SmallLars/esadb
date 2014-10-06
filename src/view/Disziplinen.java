package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;

import model.Disziplin;

import javax.swing.JLabel;


@SuppressWarnings("serial")
public class Disziplinen extends JDialog implements ComponentListener, ActionListener {

	private JComboBox<Disziplin> comboBox;
	private JLabel lblName;
	private JLabel lblWertung;
	private JLabel lblZeit;
	private JLabel lblSchusszahl;
	private JLabel lblSerienlaenge;
	private JLabel lblWaffengattung;

	private final JPanel contentPanel = new JPanel();

	public Disziplinen(Frame parent, List<Disziplin> disziplinen) {
		super(parent, "Disziplinen");
		setResizable(false);

		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(450, 300);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
		
		lblSerienlaenge = new JLabel("Serienl�nge");
		lblSerienlaenge.setBounds(10, 111, 422, 14);
		contentPanel.add(lblSerienlaenge);
		
		lblWaffengattung = new JLabel("Waffengattung");
		lblWaffengattung.setBounds(10, 136, 422, 14);
		contentPanel.add(lblWaffengattung);

		JPanel comboPane = new JPanel();
		comboPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		getContentPane().add(comboPane, BorderLayout.NORTH);

		comboBox = new JComboBox<Disziplin>(disziplinen.toArray(new Disziplin[0]));
		comboBox.setPreferredSize(new Dimension(257, 22));
		comboBox.setActionCommand("DISZIPLIN");
		comboBox.addActionListener(this);
		comboPane.add(comboBox);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton cancelButton = new JButton("Schlie�en");
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);

		addComponentListener(this);
		
		actionPerformed(new ActionEvent(comboBox, 0, "DISZIPLIN"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "DISZIPLIN":
				Disziplin d = (Disziplin) comboBox.getSelectedItem();
				lblName.setText(d.toString());
				lblWertung.setText(d.getWertung() == 0 ? "Ringwertung" : "Zehntelwertung");
				if (d.getProbezeit() > 0) {
					lblZeit.setText(String.format("Zeit f�r Probe: %d Minuten, Zeit f�r Wertung: %d Minuten.", d.getProbezeit(), d.getSchiesszeit()));
				} else {
					lblZeit.setText(String.format("Zeit f�r den Wettkampf: %d Minuten.", d.getSchiesszeit()));
				}
				String anzahl = "";
				if (d.getProbezeit() >= 0) {
					if (d.getProbeschuesse() == 0) anzahl = "Probe: beliebig, ";
					else anzahl = String.format("Probe: %d Schu�, ", d.getProbeschuesse());
				}
				if (d.getStellungsanzahl() == 1) {
					anzahl = anzahl.concat(String.format("Wertung: %d Schu�.", d.getSchusszahl()));
				} else {
					anzahl = anzahl.concat(String.format("Wertung: %d Mal %d Schu�.", d.getStellungsanzahl(), d.getSchusszahl() / d.getStellungsanzahl()));
				}
				lblSchusszahl.setText(anzahl);
				lblSerienlaenge.setText(String.format("Serienl�nge: %d.", d.getSerienlaenge()));
				lblWaffengattung.setText("Waffengattung: " + d.getWaffengattung());
				break;
			case "CANCEL":
				setVisible(false);
				break;
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}
}
