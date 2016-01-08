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
import model.SettingsModel;
import model.Single;
import model.Status;
import controller.Controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.ScrollPaneConstants;

import printPreview.PrintPreview;


@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener, ComponentListener {
	private Controller controller;

	private JPanel contentPane;
	private JTextPane konsole;
	private Target scheiben[];
	private Line linien[];
	private JFileChooser fc;

	private Box scheibenBox;
	private JScrollPane scrollScheiben;
	private JScrollPane scrollKonsole;

	public GUI() {
		super("ESADB - Datenbank für ESA 2002 - " + Controller.get().getFileName());
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/esadb.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {close();}
		});
		setMinimumSize(new Dimension(1022, 580));

		controller = Controller.get();
		scheiben = new Target[controller.getConfig().getLineCount()];
		linien = new Line[controller.getConfig().getLineCount()];
		SettingsModel config = controller.getConfig();

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(config.getMainWindowBounds());

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);

			JMenuItem mntmNeu = new JMenuItem("Neu...");
			mntmNeu.setActionCommand("NEW");
			mntmNeu.addActionListener(this);
			mnDatei.add(mntmNeu);

			JMenuItem mntmLaden = new JMenuItem("Öffnen...");
			mntmLaden.setActionCommand("OPEN");
			mntmLaden.addActionListener(this);
			mnDatei.add(mntmLaden);

			JMenuItem mntmSpeichern = new JMenuItem("Speichern unter...");
			mntmSpeichern.setActionCommand("SAVEAS");
			mntmSpeichern.addActionListener(this);
			mnDatei.add(mntmSpeichern);

			mnDatei.addSeparator();

			JMenuItem mntmEinstellungen = new JMenuItem("Einstellungen...");
			mntmEinstellungen.setActionCommand("PREFERENCES");
			mntmEinstellungen.addActionListener(this);
			mnDatei.add(mntmEinstellungen);

			mnDatei.addSeparator();

			JMenuItem mntmBeenden = new JMenuItem("Beenden");
			mntmBeenden.setActionCommand("CLOSE");
			mntmBeenden.addActionListener(this);
			mnDatei.add(mntmBeenden);

		JMenu mnErgebnisse = new JMenu("Ergebnisse");
		menuBar.add(mnErgebnisse);

			JMenuItem mntmResultOptions = new JMenuItem("Ergebnisliste...");
			mnErgebnisse.add(mntmResultOptions);
			mntmResultOptions.setActionCommand("RESULTLIST");
			mntmResultOptions.addActionListener(this);

			JMenu mnEinzelergebnisse = new JMenu("Einzelergebnisse");
			mnErgebnisse.add(mnEinzelergebnisse);

				JMenuItem mntmEinzel = new JMenuItem("Anzeigen...");
				mnEinzelergebnisse.add(mntmEinzel);
				mntmEinzel.setActionCommand("SINGLEPREVIEW");
				mntmEinzel.addActionListener(this);

				JMenuItem mntmEinzel_1 = new JMenuItem("Bearbeiten...");
				mnEinzelergebnisse.add(mntmEinzel_1);
				mntmEinzel_1.setActionCommand("SINGLEEDIT");
				mntmEinzel_1.addActionListener(this);

			mnErgebnisse.addSeparator();

			JMenuItem mntmTreffer = new JMenuItem("Treffer eingeben...");
			mntmTreffer.setActionCommand("TREFFERADD");
			mntmTreffer.addActionListener(this);
			mnErgebnisse.add(mntmTreffer);

		JMenu mnStammdaten = new JMenu("Stammdaten");
		menuBar.add(mnStammdaten);

			JMenuItem mntmSchtzen = new JMenuItem("Schützen...");
			mntmSchtzen.setActionCommand("SCHUETZEN");
			mntmSchtzen.addActionListener(this);
			mnStammdaten.add(mntmSchtzen);

			JMenuItem mntmDisziplinen = new JMenuItem("Disziplinen...");
			mntmDisziplinen.setActionCommand("DISZIPLINEN");
			mntmDisziplinen.addActionListener(this);
			mnStammdaten.add(mntmDisziplinen);

		JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);

			JMenuItem mntmInfo = new JMenuItem("Info");
			mntmInfo.setActionCommand("INFO");
			mntmInfo.addActionListener(this);
			mnHilfe.add(mntmInfo);

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
		lblSperre.setBounds(47, 8, 50, 14);
		contentPane.add(lblSperre);
		
		JLabel lblStart = new JLabel("Start");
		lblStart.setBounds(97, 8, 39, 14);
		contentPane.add(lblStart);
		
		JLabel lblPm = new JLabel("Wertung");
		lblPm.setBounds(136, 8, 61, 14);
		contentPane.add(lblPm);

		JLabel lblSchtze = new JLabel("Schütze");
		lblSchtze.setBounds(197, 8, 60, 14);
		contentPane.add(lblSchtze);
		
		JLabel lblDisziplin = new JLabel("Disziplin");
		lblDisziplin.setBounds(420, 8, 56, 14);
		contentPane.add(lblDisziplin);

		JLabel lblScheiben = new JLabel("Scheiben");
		lblScheiben.setBounds(749, 8, 60, 14);
		contentPane.add(lblScheiben);

		JScrollPane scrollLinien = new JScrollPane();
		scrollLinien.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollLinien.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollLinien.setBounds(0, 32, 746, 249);
		contentPane.add(scrollLinien);
		
		Box linienBox = Box.createVerticalBox();
		scrollLinien.setViewportView(linienBox);

		scrollScheiben = new JScrollPane();
		scrollScheiben.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollScheiben.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollScheiben.setBounds(746, 32, 268, 498);
		contentPane.add(scrollScheiben);
		
		scheibenBox = Box.createVerticalBox();
		scrollScheiben.setViewportView(scheibenBox);

		int i = 0;
		for (int l : config.getLines()) {
			LineModel linie = new DefaultLineModel(l, controller);
			linie.setStatus(Status.INIT);
			controller.add(linie);

			linien[i] = new Line(linie);
			linienBox.add(linien[i]);

			scheiben[i] = new Target(config.getStandardRule(), l);
			scheibenBox.add(scheiben[i]);
			linie.addLineListener(scheiben[i]);

			i++;
		}
		scheibenBox.setPreferredSize(new Dimension(250, config.getLineCount() * 250));

		scrollKonsole = new JScrollPane();
		scrollKonsole.setBounds(0, 281, 746, 249);
		contentPane.add(scrollKonsole);

		konsole = new JTextPane();
		konsole.setEditable(false);
		konsole.setFont(new Font("Consolas", Font.PLAIN, 12));
		scrollKonsole.setViewportView(konsole);

		addComponentListener(this);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Color green = Color.decode("0x00A050");

		int returnVal;
		PrintPreview dv;
		switch (e.getActionCommand()) {
			case "NEW":
				for (Line l : linien) if (!l.isFrei() || (l.isBusy() && !l.isError())) {
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
					setTitle("ESADB - Datenbank für ESA 2002 - " + controller.getFileName());
					println("Neu: " + file.getPath() + ".", green);
				}
				break;
			case "OPEN":
				for (Line l : linien) if (!l.isFrei() || (l.isBusy() && !l.isError())) {
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
						if (controller.load(file)) {
							setTitle("ESADB - Datenbank für ESA 2002 - " + controller.getFileName());
							println("Öffnen: " + file.getPath() + ".", green);
						} else {
							JOptionPane.showMessageDialog(
								this,
								"Die gewählte Datei stammt von einer alten\nVersion und kann nicht geöffnet werden.",
								"Fehler",
								JOptionPane.WARNING_MESSAGE
							);
						}
					} else {
						JOptionPane.showMessageDialog(
							this,
							"Die gewählte Datei existiert nicht.",
							"Fehler",
							JOptionPane.WARNING_MESSAGE
						);
					}
				}
				break;
			case "SAVEAS":
				returnVal = fc.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = checkPath(fc.getSelectedFile());
					if (file == null) return;
					controller.save(file);
					setTitle("ESADB - Datenbank für ESA 2002 - " + controller.getFileName());
					println("Speichern: " + file.getPath() + ".", green);
				}
				break;
			case "RESULTLIST":
				ResultListOptions rlo = new ResultListOptions(this);
				String action = rlo.showDialog();
				if (action.equals("PRINT")) {
					PrinterJob pjob = PrinterJob.getPrinterJob();
				    if (pjob.printDialog() == false) return;
				    pjob.setPrintable(controller.getModel().getPrintable(), controller.getConfig().getPageFormat());
				    try {
						pjob.print();
					} catch (PrinterException e1) {
						JOptionPane.showMessageDialog(
							this,
							"Druckfehler: " + e1.getMessage(),
							"Fehler",
							JOptionPane.WARNING_MESSAGE
						);
					}
				}
				if (action.equals("SHOW")) {
					dv = new PrintPreview(this, controller.getModel().getPrintable(), controller.getConfig().getPageFormat());
					controller.getConfig().setPageFormat(dv.showDialog());
				}
				break;
			case "SINGLEPREVIEW":
				SingleSelection einzel = new SingleSelection(this);
				Single ez = einzel.showDialog();
				if (ez == null) return;
				dv = new PrintPreview(this, ez, controller.getConfig().getPageFormat());
				controller.getConfig().setPageFormat(dv.showDialog());
				break;
			case "SINGLEEDIT":
				SingleEdit ee = new SingleEdit(this);
				ee.setVisible(true);
				break;
			case "TREFFERADD":
				HitAdd ta = new HitAdd(this);
				ta.setVisible(true);
				break;
			case "PREFERENCES":
				Settings einstellungen = new Settings(this, controller.getConfig());
				einstellungen.setVisible(true);
				break;
			case "CLOSE":
				close();
				break;
			case "DISZIPLINEN":
				Disciplines disziplin = new Disciplines(this, controller.getDisziplinen());
				disziplin.setVisible(true);
				break;
			case "SCHUETZEN":
				Members schuetze = new Members(this);
				schuetze.setVisible(true);
				break;
			case "INFO":
				Info info = new Info(this);
				info.setVisible(true);
				break;
		}
	}

	public void print(String string, Color color) {
		SimpleAttributeSet style = new SimpleAttributeSet();
		StyleConstants.setForeground(style, color);
		StyleConstants.setBold(style, true);

		StyledDocument doc = konsole.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), string, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		konsole.setCaretPosition(konsole.getStyledDocument().getLength());
	}
	
	public void println(String string, Color color) {
		print(string + "\n", color);
	}

	private void close() {
		for (Line l : linien) if (!l.isError() && (!l.isFrei() || l.isBusy())) {
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

	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0) {
			SettingsModel config = Controller.get().getConfig();
			config.setMainWindowBounds(getBounds());
		}
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		SettingsModel config = Controller.get().getConfig();
		if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0) {
			config.setMainWindowBounds(getBounds());
		}
		scheibenBox.setPreferredSize(new Dimension(contentPane.getWidth() - 764, config.getLineCount() * (contentPane.getWidth() - 764)));
		scrollScheiben.setSize(contentPane.getWidth() - 746, contentPane.getHeight() - 32);
		scrollScheiben.revalidate();
		scrollKonsole.setSize(746, contentPane.getHeight() - 281);
		scrollKonsole.revalidate();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {}
}
