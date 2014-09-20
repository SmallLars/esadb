package controller;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.Config;
import model.Disziplin;
import model.LineReader;
import model.Model;
import model.ModelChangeListener;
import model.Schuetze;
import model.Start;

import org.apache.commons.io.FileUtils;

import view.GUI;


public class Controller {
	private Config config;
	private List<ModelChangeListener> modelChangeListener;
	
	private File file;
	private Model model;
	private FileChecker fileChecker;
	private GUI gui;

	private SimpleAttributeSet redStyle;

	public Controller() {
		redStyle = new SimpleAttributeSet();
		StyleConstants.setBold(redStyle, true);
		StyleConstants.setForeground(redStyle, Color.decode("0xC80000"));

		config = Config.load();
		modelChangeListener = new Vector<ModelChangeListener>();
		
		final String[] files = {"esadb.ico", "Stammdaten.mdb"};
		for (String s : files) {
			File file = new File(s);
			if (!file.exists()) {
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

		PrintStream ps = new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				gui.print(String.valueOf((char) b), redStyle);
			}
			
		}, false);
		System.setOut(ps);
		System.setErr(ps);
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
		this.file = file;
		model = new Model();
		save(file);
		modelChanged();
	}

	public void load(File file) {
		this.file = file;
		model = Model.load(file);
		modelChanged();
	}

	public void save() {
		model.save(file);
	}

	public void save(File file) {
		this.file = file;
		model.save(file);
	}

	public void exit() {
		fileChecker.exit();
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

	public boolean add(Object o) {
		if (o instanceof LineReader) {
			return fileChecker.addLineReader((LineReader) o);
		}

		if (model.add(o)) {
			modelChanged();
			save(file);
			return true;
		}

		return false;
	}

	public void println(String string, SimpleAttributeSet style) {
		gui.println(string, style);
	}

	public boolean remove(Start s) {
		return model.remove(s);
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

	public static void main(String[] args) {
		new Controller();
	}
}