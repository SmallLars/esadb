package view;

import java.awt.Color;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import model.DefaultLineModel;
import model.LineModel;
import model.Einzel;
import controller.Controller;
import controller.Status;
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
	JMenuItem mntmEinstellungen;

	JMenuItem mntmSchtzen;
	JMenuItem mntmDisziplinen;

	JPanel contentPane;
	JTextPane konsole;
	Scheibe scheiben[];
	Linie linien[];
    JFileChooser fc;

	public GUI(Controller controller, int linienCount) {
		super("ESADB - Datenbank für ESA 2002 - " + controller.getFile().getName());
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/esadb.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {close();}
		});
		setMinimumSize(new Dimension(1196, 726));

		this.controller = controller;
		scheiben = new Scheibe[linienCount];
		linien = new Linie[linienCount];

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(controller.getConfig().getMainWindowBounds());
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);

		mntmNeu = new JMenuItem("Neu...");
		mntmNeu.setActionCommand("NEW");
		mntmNeu.addActionListener(this);
		mnDatei.add(mntmNeu);

		mntmLaden = new JMenuItem("Öffnen...");
		mntmLaden.setActionCommand("OPEN");
		mntmLaden.addActionListener(this);
		mnDatei.add(mntmLaden);
		
		mntmSpeichern = new JMenuItem("Speichern unter...");
		mntmSpeichern.setActionCommand("SAVEAS");
		mntmSpeichern.addActionListener(this);
		mnDatei.add(mntmSpeichern);
		
		mnDatei.addSeparator();
		
		mntmDrucken = new JMenuItem("Drucken...");
		mntmDrucken.setActionCommand("PRINT");
		mntmDrucken.addActionListener(this);
		mnDatei.add(mntmDrucken);

		mntmVorschau = new JMenuItem("Druckvorschau...");
		mntmVorschau.setActionCommand("PRINTPREVIEW");
		mntmVorschau.addActionListener(this);
		mnDatei.add(mntmVorschau);
		
		mntmEinzel = new JMenuItem("Einzelergebnisse...");
		mntmEinzel.setActionCommand("SINGLEPREVIEW");
		mntmEinzel.addActionListener(this);
		mnDatei.add(mntmEinzel);

		mnDatei.addSeparator();
		
		mntmEinstellungen = new JMenuItem("Einstellungen");
		mntmEinstellungen.setActionCommand("PREFERENCES");
		mntmEinstellungen.addActionListener(this);
		mnDatei.add(mntmEinstellungen);
		
		mnDatei.addSeparator();
		
		mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.setActionCommand("CLOSE");
		mntmBeenden.addActionListener(this);
		mnDatei.add(mntmBeenden);
		
		JMenu mnStammdaten = new JMenu("Stammdaten");
		menuBar.add(mnStammdaten);
		
		mntmSchtzen = new JMenuItem("Schützen");
		mntmSchtzen.setActionCommand("SCHUETZEN");
		mntmSchtzen.addActionListener(this);
		mnStammdaten.add(mntmSchtzen);
		
		mntmDisziplinen = new JMenuItem("Disziplinen");
		mntmDisziplinen.setActionCommand("DISZIPLINEN");
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

		int i = 0;
		for (int l : controller.getConfig().getLinien()) {
			LineModel linie = new DefaultLineModel(l, controller);
			linie.setStatus(Status.INIT);
			controller.add(linie);

			linien[i] = new Linie(linie);
			linien[i].setLocation(10, 32 + (i * (linien[i].getHeight() + 10)));
			contentPane.add(linien[i]);

			scheiben[i] = new Scheibe(null);
			scheiben[i].setBounds(738 + (i % 2 * 225), i / 2 * 225, 225, 225);
			contentPane.add(scheiben[i]);
			linie.setScheibe(scheiben[i]);

			i++;
		}

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 278, 738, 398);
		contentPane.add(scrollPane);

		konsole = new JTextPane();
		konsole.setEditable(false);
		konsole.setFont(new Font("Consolas", Font.PLAIN, 12));
		scrollPane.setViewportView(konsole);

		addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				controller.getConfig().setMainWindowBounds(getBounds());
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				controller.getConfig().setMainWindowBounds(getBounds());
				scrollPane.setSize(738, contentPane.getHeight() - 278);
				for (int i = 0; i < linienCount; i++) {
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
		SimpleAttributeSet style = new SimpleAttributeSet();
		StyleConstants.setForeground(style, Color.decode("0x00A050"));
		StyleConstants.setBold(style, true);

		int returnVal;
		Druckvorschau dv;
		switch (e.getActionCommand()) {
			case "NEW":
				returnVal = fc.showDialog(this, "Neu");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = checkPath(fc.getSelectedFile());
					if (file == null) return;
					controller.neu(file);
					setTitle("ESADB - Datenbank für ESA 2002 - " + controller.getFile().getName());
					println("Neu: " + file.getPath() + ".", style);
				}
				break;
			case "OPEN":
				returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					if (file.exists()) {
						controller.load(file);
						setTitle("ESADB - Datenbank für ESA 2002 - " + controller.getFile().getName());
						println("Öffnen: " + file.getPath() + ".", style);
					} else {
						JOptionPane.showMessageDialog(	this,
														"Die gewählte Datei existiert nicht.",
														"Fehler",
														JOptionPane.WARNING_MESSAGE);
					}
				}
				break;
			case "SAVEAS":
				returnVal = fc.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = checkPath(fc.getSelectedFile());
					if (file == null) return;
					controller.save(file);
					setTitle("ESADB - Datenbank für ESA 2002 - " + controller.getFile().getName());
					println("Speichern: " + file.getPath() + ".", style);
				}
				break;
			case "PRINT":
				PrinterJob pjob = PrinterJob.getPrinterJob();
			    if (pjob.printDialog() == false) return;
			    pjob.setPrintable(controller.getModel(), controller.getConfig().getPageFormat());
			    try {
					pjob.print();
				} catch (PrinterException e1) {
					e1.printStackTrace();
				}
				break;
			case "PRINTPREVIEW":
				dv = new Druckvorschau(this, controller.getModel(), controller.getConfig().getPageFormat());
				controller.getConfig().setPageFormat(dv.showDialog());
				break;
			case "SINGLEPREVIEW":
				EinzelAuswahl einzel = new EinzelAuswahl(this, controller);
				Einzel ez = einzel.showDialog();
				if (ez == null) return;
				dv = new Druckvorschau(this, ez, controller.getConfig().getPageFormat());
				controller.getConfig().setPageFormat(dv.showDialog());
				break;
			case "PREFERENCES":
				Einstellungen einstellungen = new Einstellungen(this, controller.getConfig());
				einstellungen.setVisible(true);
				break;
			case "CLOSE":
				close();
				break;
			case "DISZIPLINEN":
				Disziplinen disziplin = new Disziplinen(this, controller.getDisziplinen());
				disziplin.setVisible(true);
				break;
			case "SCHUETZEN":
				Schuetzen schuetze = new Schuetzen(this, controller);
				schuetze.setVisible(true);
				break;
		}
	}

	/*
	SimpleAttributeSet style = new SimpleAttributeSet();
	StyleConstants.setForeground(style, Color.RED);
	StyleConstants.setBackground(style, Color.YELLOW);
	StyleConstants.setBold(style, true);
	*/
	public void print(String string, SimpleAttributeSet style) {
		StyledDocument doc = konsole.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), string, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		konsole.setCaretPosition(konsole.getStyledDocument().getLength());
	}
	
	public void println(String string, SimpleAttributeSet style) {
		print(string + "\n", style);
	}

	public void close() {
		for (Linie l : linien) if (!l.isFrei()) {
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
