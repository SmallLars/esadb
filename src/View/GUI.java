package View;
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

import Controller.Controller;
import Model.Treffer;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.Toolkit;


@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener {
	Controller controller;

	JMenuItem mntmLaden;
	JMenuItem mntmSpeichern;
	JMenuItem mntmBeenden;
	JMenuItem mntmDrucken;
	JMenuItem mntmVorschau;
	JMenuItem mntmDebug;
	JPanel contentPane;
	JTextPane konsole;
	Scheibe scheibe;
    JFileChooser fc;

	public GUI(Controller controller) {
		super("ESADB - Datenbank für ESA 2002 - " + controller.getFile().getName());
		setIconImage(Toolkit.getDefaultToolkit().getImage("esadb.png"));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {close();}
		});
		setMinimumSize(new Dimension(1196, 500));

		this.controller = controller;

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(controller.getConfig().getMainWindowBouds());
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
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

		mnDatei.addSeparator();
		
		mntmDebug = new JMenuItem("Debug");
		mntmDebug.addActionListener(this);
		mnDatei.add(mntmDebug);
		
		mnDatei.addSeparator();
		
		mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(this);
		mnDatei.add(mntmBeenden);
		
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
			LinieView panel = new LinieView(controller.getLinie(i), this);
			panel.setLocation(10, 32 + (i * (panel.getHeight() + 10)));
			contentPane.add(panel);
		}

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 278, 738, 503);
		contentPane.add(scrollPane);

		konsole = new JTextPane();
		konsole.setEditable(false);
		konsole.setFont(new Font("Consolas", Font.PLAIN, 12));
		scrollPane.setViewportView(konsole);
		
		scheibe = new Scheibe();
		scheibe.setBounds(738, 0, 450, 450);
		contentPane.add(scheibe);

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
				scheibe.setSize(arg0.getComponent().getWidth() - 738,
								arg0.getComponent().getHeight()
				);
			}

			@Override
			public void componentShown(ComponentEvent arg0) {}
			
		});
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mntmLaden) {
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
				String name = fc.getSelectedFile().getPath();
				if (!name.endsWith(".esa")) {
					name = name + ".esa";
				}
				
				File file = new File(name);
				if (file.exists()) {
					int v = JOptionPane.showConfirmDialog(	this,
															"Soll die gewählte Datei wirklich überschrieben werden?",
															"Warnung",
															JOptionPane.YES_NO_OPTION,
															JOptionPane.WARNING_MESSAGE);
					if (v != JOptionPane.OK_OPTION) return;
				}

				controller.save(file);
				setTitle("ESADB - Datenbank für ESA 2002 - " + controller.getFile().getName());
				println("Speichern: " + name + ".");
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
		} else if (e.getSource() == mntmDebug) {
			controller.printModel();
		} else if (e.getSource() == mntmBeenden) {
			close();
		}
	}

	public void print(String string) {
		konsole.setText(konsole.getText() + string);
		konsole.setCaretPosition(konsole.getDocument().getLength());
	}
	
	public void println(String string) {
		print(string + "\n");
	}

	public void showTreffer(Treffer t) {
		scheibe.showTreffer(t);
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
}
