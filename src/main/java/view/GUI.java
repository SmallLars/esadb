package view;


import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import controller.Controller;
import model.LineModel;
import model.SettingsModel;
import model.Single;
import printPreview.PrintPreview;
import view.hitadd.HitAdd;
import view.member.Members;
import view.settings.Settings;
import view.singleedit.SingleEdit;
import view.teamedit.TeamEdit;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.SpinnerNumberModel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.ScrollPaneConstants;

import java.awt.GridLayout;

import javax.swing.JSpinner;


@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener, ComponentListener, ChangeListener {
	private Controller controller;

	private Container contentPane;
	private JSplitPane splitPane;
	private JTextPane konsole;
	private Target scheiben[];
	private Line linien[];
	private JFileChooser fc;

	private JLabel lblColumns;
	private JSpinner targetColumns;
	private JPanel scheibenBox;
	private JScrollPane scrollScheiben;
	private JScrollPane scrollKonsole;

	public GUI() {
		super("ESADB - Datenbank für ESA 2002 - " + Controller.get().getFileName());
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/esadb.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {close();}
		});
		setMinimumSize(new Dimension(1022, 570));

		controller = Controller.get();
		scheiben = new Target[controller.getConfig().getLineCount()];
		linien = new Line[controller.getConfig().getLineCount()];
		SettingsModel config = controller.getConfig();

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(config.getValue("MainWindowBounds", new Rectangle(1, 1, 1022, 570)));

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

			JMenuItem mntmEinzel = new JMenuItem("Einzelergebnisse...");
			mnErgebnisse.add(mntmEinzel);
			mntmEinzel.setActionCommand("SINGLEPREVIEW");
			mntmEinzel.addActionListener(this);

			mnErgebnisse.addSeparator();

			JMenuItem mntmTreffer = new JMenuItem("Treffer eingeben...");
			mntmTreffer.setActionCommand("TREFFERADD");
			mntmTreffer.addActionListener(this);
			mnErgebnisse.add(mntmTreffer);

			JMenuItem mntmEinzel_1 = new JMenuItem("Ergebnisse bearbeiten...");
			mnErgebnisse.add(mntmEinzel_1);
			mntmEinzel_1.setActionCommand("SINGLEEDIT");
			mntmEinzel_1.addActionListener(this);

			JMenuItem mntmTeam = new JMenuItem("Mannschaften bearbeiten...");
			mntmTeam.setActionCommand("TEAMEDIT");
			mntmTeam.addActionListener(this);
			mnErgebnisse.add(mntmTeam);

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

		JMenu mnLinien = new JMenu("Linien");
		menuBar.add(mnLinien);

			JMenuItem mntmShutdown = new JMenuItem("Herunterfahren");
			mntmShutdown.setActionCommand("SHUTDOWN");
			mntmShutdown.addActionListener(this);
			mnLinien.add(mntmShutdown);

		JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);

			JMenuItem mntmInfo = new JMenuItem("Info");
			mntmInfo.setActionCommand("INFO");
			mntmInfo.addActionListener(this);
			mnHilfe.add(mntmInfo);

		fc = new JFileChooser();
	    fc.setCurrentDirectory(new File(Controller.getPath(".")));
	    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    fc.setAcceptAllFileFilterUsed(false);
	    fc.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File file) {return file.isDirectory() || file.getName().endsWith(".esa");}

			@Override
			public String getDescription() {return "*.esa";}
		});

		contentPane = this.getContentPane();
		contentPane.setLayout(null);

		JLabel lblSperre = new JLabel("Sperre");
		lblSperre.setBounds(47, 6, 50, 14);
		contentPane.add(lblSperre);
		
		JLabel lblStart = new JLabel("Start");
		lblStart.setBounds(97, 6, 39, 14);
		contentPane.add(lblStart);
		
		JLabel lblPm = new JLabel("Wertung");
		lblPm.setBounds(136, 6, 61, 14);
		contentPane.add(lblPm);

		JLabel lblSchtze = new JLabel("Schütze");
		lblSchtze.setBounds(197, 6, 60, 14);
		contentPane.add(lblSchtze);
		
		JLabel lblDisziplin = new JLabel("Disziplin");
		lblDisziplin.setBounds(420, 6, 56, 14);
		contentPane.add(lblDisziplin);

		JLabel lblScheiben = new JLabel("Scheiben");
		lblScheiben.setBounds(749, 6, 60, 14);
		contentPane.add(lblScheiben);

		lblColumns = new JLabel("Spalten:");
		lblColumns.setBounds(875, 6, 60, 14);
		contentPane.add(lblColumns);

		targetColumns = new JSpinner(new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(1), new Integer(1)));
		updateTargetColumns();
		targetColumns.setBounds(938, 4, 60, 18);
		targetColumns.addChangeListener(this);
		getContentPane().add(targetColumns);

		JScrollPane scrollLinien = new JScrollPane();
		scrollLinien.setMinimumSize(new Dimension(728, 86));
		scrollLinien.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollLinien.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		Box linienBox = Box.createVerticalBox();
		scrollLinien.setViewportView(linienBox);
		Dimension d = new Dimension(728, 41 * config.getLines().size());
		linienBox.setSize(d);
		linienBox.setMinimumSize(d);
		linienBox.setPreferredSize(d);

		scrollKonsole = new JScrollPane();
		scrollKonsole.setMinimumSize(new Dimension(728, 90));
		scrollKonsole.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollKonsole.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		konsole = new JTextPane();
		konsole.setEditable(false);
		scrollKonsole.setViewportView(konsole);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollLinien, scrollKonsole);
		splitPane.setBorder(null);
		splitPane.setBounds(0, 22, 746, 500);
		splitPane.setDividerLocation(config.getValue("MainWindowDividerLocation", 250));
		splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				SettingsModel config = Controller.get().getConfig();
				config.setValue("MainWindowDividerLocation", splitPane.getDividerLocation());
				config.save();
			}
		});
		contentPane.add(splitPane);

		scrollScheiben = new JScrollPane();
		scrollScheiben.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollScheiben.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollScheiben.setBounds(746, 22, 268, 500);
		contentPane.add(scrollScheiben);

		scheibenBox = new JPanel();
		scheibenBox.setLayout(new GridLayout(0, config.getValue("TargetColumns", 2), 0, 0));
		scrollScheiben.setViewportView(scheibenBox);

		int i = 0;
		for (int l : config.getLines()) {
			LineModel linie = controller.newLine(l);
			if (linie != null) {
				linien[i] = new Line(linie);
				linienBox.add(linien[i]);
	
				scheiben[i] = new Target(config.getStandardRule(), l);
				scheibenBox.add(scheiben[i]);
				linie.addLineListener(scheiben[i]);
			}
			i++;
		}

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
					JOptionPane.showMessageDialog(
						this,
						"Ein neuer Wettkampf kann erst angelegt werden, wenn alle Linien frei sind.",
						"Fehler",
						JOptionPane.WARNING_MESSAGE
					);
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
					JOptionPane.showMessageDialog(
						this,
						"Ein Wettkampf kann erst geladen werden, wenn alle Linien frei sind.",
						"Fehler",
						JOptionPane.WARNING_MESSAGE
					);
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
				    pjob.setPrintable(controller.getModel().getPrintable(), getPageFormat());
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
					dv = new PrintPreview(this, controller.getModel().getPrintable(), getPageFormat());
					setPageFormat(dv.showDialog());
				}
				break;
			case "SINGLEPREVIEW":
				SingleSelection einzel = new SingleSelection(this);
				Single ez = einzel.showDialog();
				if (ez == null) return;
				dv = new PrintPreview(this, ez, getPageFormat());
				setPageFormat(dv.showDialog());
				break;
			case "SINGLEEDIT":
				SingleEdit ee = new SingleEdit(this);
				ee.setVisible(true);
				break;
			case "TEAMEDIT":
				(new TeamEdit(this)).setVisible(true);
				break;
			case "TREFFERADD":
				HitAdd ta = new HitAdd(this);
				ta.setVisible(true);
				break;
			case "PREFERENCES":
				Settings einstellungen = new Settings(this, controller.getConfig());
				einstellungen.setVisible(true);
				updateTargetColumns();
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
			case "SHUTDOWN":
				for (Line l : linien) if (!l.isError() && (!l.isFrei() || l.isBusy())) {
					JOptionPane.showMessageDialog(
						this,
						"Die Linien können erst heruntergefahren werden, wenn alle Linien frei sind.",
						"Fehler",
						JOptionPane.WARNING_MESSAGE
					);
					return;
				}
				int v = JOptionPane.showConfirmDialog(
					this,
					"Sollen die Linien wirklich heruntergefahren werden?",
					"Warnung",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE
				);
				if (v == JOptionPane.OK_OPTION) {
					for (Line l : linien) l.shutdown();
				}
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

	private PageFormat getPageFormat() {
		final double mmToDots = 72 / 2.54;

		SettingsModel settings = Controller.get().getConfig();
		Paper p = new Paper();
		p.setSize(
			settings.getValue("PageWidht", 21.0 * mmToDots),
			settings.getValue("PageHeight", 29.7 * mmToDots)
		);
		p.setImageableArea(
			settings.getValue("PageImageableX", 2.5 * mmToDots),
			settings.getValue("PageImageableY", 1.0 * mmToDots),
			settings.getValue("PageImageableWidth", 17.5 * mmToDots),
			settings.getValue("PageImageableHeight", 27.7 * mmToDots)
		);
		PageFormat pf = new PageFormat();
		pf.setPaper(p);
		pf.setOrientation(settings.getValue("PageOrientation", PageFormat.PORTRAIT));
		return pf;
	}

	private void setPageFormat(PageFormat pf) {
		SettingsModel settings = Controller.get().getConfig();
		settings.setValue("PageWidht", pf.getPaper().getWidth());
		settings.setValue("PageHeight", pf.getPaper().getHeight());
		settings.setValue("PageImageableX", pf.getPaper().getImageableX());
		settings.setValue("PageImageableY", pf.getPaper().getImageableY());
		settings.setValue("PageImageableWidth", pf.getPaper().getImageableWidth());
		settings.setValue("PageImageableHeight", pf.getPaper().getImageableHeight());
		settings.setValue("PageOrientation", pf.getOrientation());
		settings.save();
	}

	private void close() {
		for (Line l : linien) if (!l.isError() && (!l.isFrei() || l.isBusy())) {
			JOptionPane.showMessageDialog(
				this,
				"Das Programm kann erst beendet werden, wenn alle Linien frei sind.",
				"Fehler",
				JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		int v = JOptionPane.showConfirmDialog(
			this,
			"Soll das Programm wirklich beendet werden?",
			"Warnung",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE
		);
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
			int v = JOptionPane.showConfirmDialog(
				this,
				"Soll die gewählte Datei wirklich überschrieben werden?",
				"Warnung",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE
			);
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
			config.setValue("MainWindowBounds", getBounds());
			config.save();
		}
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		SettingsModel config = Controller.get().getConfig();
		if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0) {
			config.setValue("MainWindowBounds", getBounds());
			config.save();
		}
		lblColumns.setLocation(contentPane.getWidth() - 139, 6);
		targetColumns.setLocation(contentPane.getWidth() - 76, 4);
		int columns = config.getValue("TargetColumns", 2);
		int rows = (int) Math.ceil(linien.length / (double) columns);
		((GridLayout) scheibenBox.getLayout()).setColumns(columns);
		scheibenBox.setPreferredSize(new Dimension((contentPane.getWidth() - 764), rows * (contentPane.getWidth() - 764) / columns));
		scrollScheiben.setSize(contentPane.getWidth() - 746, contentPane.getHeight() - 22);
		scrollScheiben.revalidate();
		splitPane.setSize(746, contentPane.getHeight() - 22);
		splitPane.revalidate();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {}

	@Override
	public void stateChanged(ChangeEvent e) {
		int columns = (int) targetColumns.getValue();

		SettingsModel config = Controller.get().getConfig();
		config.setValue("TargetColumns", columns);
		config.save();

		componentResized(null);
		scheibenBox.revalidate();
	}

	private void updateTargetColumns() {
		SettingsModel settings = controller.getConfig();
		int max = settings.getLineCount() > 0 ? settings.getLineCount() : 1;
		int value = settings.getValue("TargetColumns", 2);
		if (value > max) {
			value = max;
			settings.setValue("TargetColumns", value);
			settings.save();
		}
		targetColumns.setValue(value);
		((SpinnerNumberModel) targetColumns.getModel()).setMaximum(max);
	}
}
