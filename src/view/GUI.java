package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import model.Start;
import model.Treffer;
import controller.Controller;
import druckvorschau.Druckvorschau;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.Toolkit;


@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener {
	Controller controller;

	JMenuItem mntmNeu;
	JMenuItem mntmLaden;
	JMenuItem mntmSpeichern;
	JMenuItem mntmBeenden;
	JMenuItem mntmDrucken;
	JMenuItem mntmVorschau;
	JMenuItem mntmEinzel;

	JMenuItem mntmSchtzen;
	JMenuItem mntmDisziplinen;

	JPanel contentPane;
	JTextPane konsole;
	Scheibe scheiben[];
	Linie linien[];
    JFileChooser fc;

	public GUI(Controller controller) {
		super("ESADB - Datenbank für ESA 2002 - " + controller.getFile().getName());
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/esadb.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {close();}
		});
		setMinimumSize(new Dimension(1196, 726));

		this.controller = controller;
		scheiben = new Scheibe[6];
		linien = new Linie[6];

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(controller.getConfig().getMainWindowBouds());
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);

		mntmNeu = new JMenuItem("Neu...");
		mntmNeu.addActionListener(this);
		mnDatei.add(mntmNeu);

		mntmLaden = new JMenuItem("Öffnen...");
		mntmLaden.addActionListener(this);
		mnDatei.add(mntmLaden);
		
		mntmSpeichern = new JMenuItem("Speichern unter...");
		mntmSpeichern.addActionListener(this);
		mnDatei.add(mntmSpeichern);
		
		mnDatei.addSeparator();
		
		mntmDrucken = new JMenuItem("Drucken...");
		mntmDrucken.addActionListener(this);
		mnDatei.add(mntmDrucken);

		mntmVorschau = new JMenuItem("Druckvorschau...");
		mntmVorschau.addActionListener(this);
		mnDatei.add(mntmVorschau);
		
		mntmEinzel = new JMenuItem("Einzelergebnisse...");
		mntmEinzel.addActionListener(this);
		mnDatei.add(mntmEinzel);
		
		mnDatei.addSeparator();
		
		mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(this);
		mnDatei.add(mntmBeenden);
		
		JMenu mnStammdaten = new JMenu("Stammdaten");
		menuBar.add(mnStammdaten);
		
		mntmSchtzen = new JMenuItem("Schützen");
		mnStammdaten.add(mntmSchtzen);
		
		mntmDisziplinen = new JMenuItem("Disziplinen");
		mntmDisziplinen.addActionListener(this);
		mnStammdaten.add(mntmDisziplinen);
		
		fc = new JFileChooser();
	    fc.setCurrentDirectory(new File("."));
	    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    fc.setAcceptAllFileFilterUsed(false);
	    fc.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File file) {return file.isDirectory() || file.getName().endsWith(".esa");}

			@Override
			public String getDescription() {return "*.esa";}
		});


		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblSperre = new JLabel("Sperre");
		lblSperre.setBounds(40, 8, 40, 14);
		contentPane.add(lblSperre);
		
		JLabel lblStart = new JLabel("Start");
		lblStart.setBounds(90, 8, 32, 14);
		contentPane.add(lblStart);
		
		JLabel lblPm = new JLabel("Wertung");
		lblPm.setBounds(129, 8, 56, 14);
		contentPane.add(lblPm);

		JLabel lblSchtze = new JLabel("Schütze");
		lblSchtze.setBounds(190, 8, 46, 14);
		contentPane.add(lblSchtze);
		
		JLabel lblDisziplin = new JLabel("Disziplin");
		lblDisziplin.setBounds(413, 8, 56, 14);
		contentPane.add(lblDisziplin);

		for (int i = 0; i < controller.getLinienAnzahl(); i++) {
			linien[i] = new Linie(controller.getLinie(i));
			linien[i].setLocation(10, 32 + (i * (linien[i].getHeight() + 10)));
			contentPane.add(linien[i]);
		}

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 278, 738, 398);
		contentPane.add(scrollPane);

		konsole = new JTextPane();
		konsole.setEditable(false);
		konsole.setFont(new Font("Consolas", Font.PLAIN, 12));
		scrollPane.setViewportView(konsole);
		
		for (int i = 0; i < 6; i++) {
			scheiben[i] = new Scheibe();
			scheiben[i].setBounds(738 + (i % 2 * 225), i / 2 * 225, 225, 225);
			contentPane.add(scheiben[i]);
		}

		addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				controller.getConfig().setMainWindowBouds(getBounds());
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				controller.getConfig().setMainWindowBouds(getBounds());
				scrollPane.setSize(738, contentPane.getHeight() - 278);
				for (int i = 0; i < 6; i++) {
					scheiben[i].setSize(	(contentPane.getWidth() - 738) / 2,
											contentPane.getHeight() / 3
					);
					scheiben[i].setLocation(738 + (i % 2 * scheiben[i].getWidth()), i / 2 * scheiben[i].getHeight());
				}
			}

			@Override
			public void componentShown(ComponentEvent arg0) {}
			
		});
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mntmNeu) {
			int returnVal = fc.showDialog(this, "Neu");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = checkPath(fc.getSelectedFile());
				if (file == null) return;
				controller.neu(file);
				setTitle("ESADB - Datenbank für ESA 2002 - " + controller.getFile().getName());
				println("Neu: " + file.getPath() + ".");
			}
		} else if (e.getSource() == mntmLaden) {
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if (file.exists()) {
					controller.load(file);
					setTitle("ESADB - Datenbank für ESA 2002 - " + controller.getFile().getName());
					println("Öffnen: " + file.getPath() + ".");
				} else {
					JOptionPane.showMessageDialog(	this,
													"Die gewählte Datei existiert nicht.",
													"Fehler",
													JOptionPane.WARNING_MESSAGE);
				}
			}
		} else if (e.getSource() == mntmSpeichern) {
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = checkPath(fc.getSelectedFile());
				if (file == null) return;
				controller.save(file);
				setTitle("ESADB - Datenbank für ESA 2002 - " + controller.getFile().getName());
				println("Speichern: " + file.getPath() + ".");
			}
		} else if (e.getSource() == mntmDrucken) {
			PrinterJob pjob = PrinterJob.getPrinterJob();
		    if (pjob.printDialog() == false) return;
		    pjob.setPrintable(controller.getModel(), controller.getConfig().getPageFormat());
		    try {
				pjob.print();
			} catch (PrinterException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == mntmVorschau) {
			Druckvorschau dv = new Druckvorschau(this, controller.getModel(), controller.getConfig().getPageFormat());
			controller.getConfig().setPageFormat(dv.showDialog());
		} else if (e.getSource() == mntmEinzel) {
			Einzel einzel = new Einzel(this, controller);
			Start s = einzel.showDialog();
			if (s == null) return;
			Druckvorschau dv = new Druckvorschau(this, s, controller.getConfig().getPageFormat());
			controller.getConfig().setPageFormat(dv.showDialog());
		} else if (e.getSource() == mntmBeenden) {
			close();
		} else if (e.getSource() == mntmDisziplinen) {
			Disziplinen disziplin = new Disziplinen(this, controller.getDisziplinen());
			disziplin.setVisible(true);
		}
	}

	public void print(String string) {
		konsole.setText(konsole.getText() + string);
		konsole.setCaretPosition(konsole.getDocument().getLength());
	}
	
	public void println(String string) {
		print(string + "\n");
	}

	public void showTreffer(int linie, Treffer t) {
		scheiben[linie - 1].showTreffer(t);
	}

	public void close() {
		if (!controller.canExit()) {
			JOptionPane.showMessageDialog(	this,
											"Das Programm kann erst beendet werden, wenn alle Linien frei sind.",
											"Fehler",
											JOptionPane.WARNING_MESSAGE);
			return;
		}

		int v = JOptionPane.showConfirmDialog(	this,
												"Soll das Programm wirklich beendet werden?",
												"Warnung",
												JOptionPane.YES_NO_OPTION,
												JOptionPane.WARNING_MESSAGE);
		if (v != JOptionPane.OK_OPTION) return;

		controller.exit();
		dispose();
	}

	private File checkPath(File file) {
		String name = file.getPath();
		if (!name.endsWith(".esa")) {
			name = name + ".esa";
		}
		file = new File(name);
		if (file.exists()) {
			int v = JOptionPane.showConfirmDialog(	this,
													"Soll die gewählte Datei wirklich überschrieben werden?",
													"Warnung",
													JOptionPane.YES_NO_OPTION,
													JOptionPane.WARNING_MESSAGE);
			if (v != JOptionPane.OK_OPTION) return null;
		}
		return file;
	}
}
