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
import model.Status;
import controller.Controller;
import druckvorschau.Druckvorschau;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.ScrollPaneConstants;


@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener {
	private Controller controller;

	private JMenuItem mntmNeu;
	private JMenuItem mntmLaden;
	private JMenuItem mntmSpeichern;
	private JMenuItem mntmBeenden;
	private JMenuItem mntmDrucken;
	private JMenuItem mntmVorschau;
	private JMenuItem mntmEinzel;
	private JMenuItem mntmEinzel_1;
	private JMenuItem mntmTreffer;
	private JMenuItem mntmEinstellungen;

	private JMenuItem mntmSchtzen;
	private JMenuItem mntmDisziplinen;

	private JPanel contentPane;
	private JTextPane konsole;
	private Scheibe scheiben[];
	private Linie linien[];
	private JFileChooser fc;

	public GUI(Controller controller, int linienCount) {
		super("ESADB - Datenbank für ESA 2002 - " + controller.getFile().getName());
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/esadb.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {close();}
		});
		setMinimumSize(new Dimension(1022, 580));

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
	
			mntmEinstellungen = new JMenuItem("Einstellungen...");
			mntmEinstellungen.setActionCommand("PREFERENCES");
			mntmEinstellungen.addActionListener(this);
			mnDatei.add(mntmEinstellungen);
			
			mnDatei.addSeparator();
			
			mntmBeenden = new JMenuItem("Beenden");
			mntmBeenden.setActionCommand("CLOSE");
			mntmBeenden.addActionListener(this);
			mnDatei.add(mntmBeenden);

		JMenu mnErgebnisse = new JMenu("Ergebnisse");
		menuBar.add(mnErgebnisse);
		
			JMenu mnErgebnisliste = new JMenu("Ergebnisliste");
			mnErgebnisse.add(mnErgebnisliste);
			
				mntmDrucken = new JMenuItem("Drucken...");
				mnErgebnisliste.add(mntmDrucken);
				mntmDrucken.setActionCommand("PRINT");
				mntmDrucken.addActionListener(this);
						
				mntmVorschau = new JMenuItem("Druckvorschau...");
				mnErgebnisliste.add(mntmVorschau);
				mntmVorschau.setActionCommand("PRINTPREVIEW");
				mntmVorschau.addActionListener(this);
		
			JMenu mnEinzelergebnisse = new JMenu("Einzelergebnisse");
			mnErgebnisse.add(mnEinzelergebnisse);
			
				mntmEinzel = new JMenuItem("Druckvorschau...");
				mnEinzelergebnisse.add(mntmEinzel);
				mntmEinzel.setActionCommand("SINGLEPREVIEW");
				mntmEinzel.addActionListener(this);
				
				mntmEinzel_1 = new JMenuItem("Bearbeiten...");
				mnEinzelergebnisse.add(mntmEinzel_1);
				mntmEinzel_1.setActionCommand("SINGLEEDIT");
				mntmEinzel_1.addActionListener(this);

			mnErgebnisse.addSeparator();
	
			mntmTreffer = new JMenuItem("Treffer eingeben...");
			mntmTreffer.setActionCommand("TREFFERADD");
			mntmTreffer.addActionListener(this);
			mnErgebnisse.add(mntmTreffer);
		
		JMenu mnStammdaten = new JMenu("Stammdaten");
		menuBar.add(mnStammdaten);
		
			mntmSchtzen = new JMenuItem("Schützen...");
			mntmSchtzen.setActionCommand("SCHUETZEN");
			mntmSchtzen.addActionListener(this);
			mnStammdaten.add(mntmSchtzen);
			
			mntmDisziplinen = new JMenuItem("Disziplinen...");
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
		lblSperre.setBounds(47, 8, 40, 14);
		contentPane.add(lblSperre);
		
		JLabel lblStart = new JLabel("Start");
		lblStart.setBounds(97, 8, 32, 14);
		contentPane.add(lblStart);
		
		JLabel lblPm = new JLabel("Wertung");
		lblPm.setBounds(136, 8, 56, 14);
		contentPane.add(lblPm);

		JLabel lblSchtze = new JLabel("Schütze");
		lblSchtze.setBounds(197, 8, 46, 14);
		contentPane.add(lblSchtze);
		
		JLabel lblDisziplin = new JLabel("Disziplin");
		lblDisziplin.setBounds(420, 8, 56, 14);
		contentPane.add(lblDisziplin);

		JLabel lblScheiben = new JLabel("Scheiben");
		lblScheiben.setBounds(749, 8, 56, 14);
		contentPane.add(lblScheiben);

		JScrollPane scrollLinien = new JScrollPane();
		scrollLinien.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollLinien.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollLinien.setBounds(0, 32, 746, 249);
		contentPane.add(scrollLinien);
		
		Box linienBox = Box.createVerticalBox();
		scrollLinien.setViewportView(linienBox);

		JScrollPane scrollScheiben = new JScrollPane();
		scrollScheiben.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollScheiben.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollScheiben.setBounds(746, 32, 268, 498);
		contentPane.add(scrollScheiben);
		
		Box scheibenBox = Box.createVerticalBox();
		scrollScheiben.setViewportView(scheibenBox);

		int i = 0;
		for (int l : controller.getConfig().getLinien()) {
			LineModel linie = new DefaultLineModel(l, controller);
			linie.setStatus(Status.INIT);
			controller.add(linie);

			linien[i] = new Linie(linie);
			linienBox.add(linien[i]);

			scheiben[i] = new Scheibe(l);
			scheibenBox.add(scheiben[i]);
			linie.addLineListener(scheiben[i]);

			i++;
		}
		scheibenBox.setPreferredSize(new Dimension(250, controller.getConfig().getLinienCount() * 250));

		JScrollPane scrollKonsole = new JScrollPane();
		scrollKonsole.setBounds(0, 281, 746, 249);
		contentPane.add(scrollKonsole);

		konsole = new JTextPane();
		konsole.setEditable(false);
		konsole.setFont(new Font("Consolas", Font.PLAIN, 12));
		scrollKonsole.setViewportView(konsole);

		addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0) {
					controller.getConfig().setMainWindowBounds(getBounds());
				}
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0) {
					controller.getConfig().setMainWindowBounds(getBounds());
				}
				scheibenBox.setPreferredSize(new Dimension(contentPane.getWidth() - 764, controller.getConfig().getLinienCount() * (contentPane.getWidth() - 764)));
				scrollScheiben.setSize(contentPane.getWidth() - 746, contentPane.getHeight() - 32);
				scrollScheiben.revalidate();
				scrollKonsole.setSize(746, contentPane.getHeight() - 281);
				scrollKonsole.revalidate();
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
				for (Linie l : linien) if (!l.isFrei()) {
					JOptionPane.showMessageDialog(	this,
													"Ein neuer Wettkampf kann erst angelegt werden, wenn alle Linien frei sind.",
													"Fehler",
													JOptionPane.WARNING_MESSAGE);
					return;
				}
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
				for (Linie l : linien) if (!l.isFrei()) {
					JOptionPane.showMessageDialog(	this,
													"Ein Wettkampf kann erst geladen werden, wenn alle Linien frei sind.",
													"Fehler",
													JOptionPane.WARNING_MESSAGE);
					return;
				}
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
					JOptionPane.showMessageDialog(	this,
													"Druckfehler: " + e1.getMessage(),
													"Fehler",
													JOptionPane.WARNING_MESSAGE);
					//e1.printStackTrace();
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
			case "SINGLEEDIT":
				EinzelEdit ee = new EinzelEdit(this, controller);
				ee.setVisible(true);
				break;
			case "TREFFERADD":
				TrefferAdd ta = new TrefferAdd(this, controller);
				ta.setVisible(true);
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

	private void close() {
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
