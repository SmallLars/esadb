package controller;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.Config;
import model.Disziplin;
import model.Einzel;
import model.LineReader;
import model.Model;
import model.ModelChangeListener;
import model.Schuetze;
import model.Treffer;

import org.apache.commons.io.FileUtils;

import view.GUI;


public class Controller {
	private OutputStream errorLog = null;

	private Config config;
	private List<ModelChangeListener> modelChangeListener;
	
	static private File file;
	private Model model;
	private FileChecker fileChecker;
	private GUI gui = null;

	private SimpleAttributeSet redStyle;
	
	public static String getFileName() {
		return file.getName();
	}

	public Controller() {
		redStyle = new SimpleAttributeSet();
		StyleConstants.setBold(redStyle, true);
		StyleConstants.setForeground(redStyle, Color.decode("0xC80000"));

		try {
			errorLog = new FileOutputStream("error.log", true);
		} catch (FileNotFoundException e) {}
		PrintStream ps = new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				if (gui != null) gui.print(String.valueOf((char) b), redStyle);
				if (errorLog != null) errorLog.write(b);
			}
		}, false);
		System.setOut(ps);
		System.setErr(ps);
		System.out.println();
		System.out.print("   Start: ");
		System.out.println((new SimpleDateFormat("dd.MM.yyyy - HH:mm")).format(new Date()));
		System.out.println();

		// --------------------------------------------------------------------

		config = Config.load();
		modelChangeListener = new Vector<ModelChangeListener>();
		
		final String[] files = {"esadb.ico", "data.mdb", "Stammdaten.mdb"};
		for (String s : files) {
			File file = new File(s);
			if (!file.exists() || s.equals("Stammdaten.mdb")) {
				URL inputUrl = getClass().getResource("/" + s);
				try {
					FileUtils.copyURLToFile(inputUrl, file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		file = new File(sdf.format(new Date()) + ".esa");
		if (file.exists()) {
			model = Model.load(file);
		} else {
			model = new Model();
		}

		fileChecker = new FileChecker(config.getLinienCount());
		gui = new GUI(this, config.getLinienCount());
	}

	public Config getConfig() {
		return config;
	}

	public File getFile() {
		return file;
	}
	
	public Model getModel() {
		return model;
	}

	public void neu(File file) {
		Controller.file = file;
		model = new Model();
		save(file);
		modelChanged();
	}

	public void load(File file) {
		Controller.file = file;
		model = Model.load(file);
		modelChanged();
	}

	public void save() {
		model.save(file);
	}

	public void save(File file) {
		Controller.file = file;
		model.save(file);
	}

	public void exit() {
		fileChecker.exit();
	}

	public Einzel findIncomplete(Schuetze schuetze, Disziplin disziplin) {
		for (Einzel e : model.getIncomplete()) {
			if (e.getSchuetze().compareTo(schuetze) == 0) {
				if (e.getDisziplin().compareTo(disziplin) == 0) {
					return e;
				}
			}
		}
		return null;
	}

	public boolean contains(Object item) {
		return model.contains(item);
	}

	public List<Schuetze> getSchuetzen() {
		return model.getSchuetzen();
	}

	public List<Disziplin> getDisziplinen() {
		return  model.getDisziplinen();
	}

	public List<Treffer> getTreffer() {
		return  model.getTreffer();
	}

	public boolean add(Object o) {
		if (o instanceof LineReader) {
			return fileChecker.addLineReader((LineReader) o);
		}

		if (model.add(o)) {
			modelChanged();
			save();
			return true;
		}

		return false;
	}

	public boolean remove(Object o) {
		if (model.remove(o)) {
			save();
			return true;
		}
		return false;
	}

	public void println(String string, SimpleAttributeSet style) {
		gui.println(string, style);
	}

	public void addModelChangeListener(ModelChangeListener l) {
		modelChangeListener.add(l);
	}

	public void removeModelChangeListener(ModelChangeListener l) {
		modelChangeListener.remove(l);
	}

	private void modelChanged() {
		for (ModelChangeListener l : modelChangeListener) {
			l.modelChanged();
		}
	}

	public static void main(String[] args) throws IOException {
		File lockfile = new File("esadb.lock");
		FileOutputStream out = new FileOutputStream(lockfile);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					out.close();
					lockfile.delete();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}));

		if (out.getChannel().tryLock() == null) {
			JOptionPane.showMessageDialog(	null,
					"Das Programm kann nur einmal zur Zeit gestartet werden.",
					"Fehler",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		new Controller();
	}
}