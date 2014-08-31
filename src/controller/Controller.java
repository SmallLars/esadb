package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Config;
import model.Disziplin;
import model.LinieModel;
import model.Model;
import model.Schuetze;
import model.Start;
import model.Treffer;

import org.apache.commons.io.FileUtils;

import view.GUI;


public class Controller {
	private Config config;
	
	private File file;
	private Model model;
	private LinieModel linien[];
	private Schuetze[] schuetzen;
	private Disziplin[] disziplinen;
	private FileChecker fileChecker;
	private GUI gui;

	public Controller() {
		config = Config.load();
		
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
		schuetzen = model.getSchuetzen();
		disziplinen = model.getDisziplinen();

		linien = new LinieModel[6];
		for (int i = 0; i < 6; i++) {
			linien[i] = new LinieModel(i + 1, this);
			linien[i].setStatus(Status.INIT);
		}

		fileChecker = new FileChecker(this);
		gui = new GUI(this);

		PrintStream ps = new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				gui.print(String.valueOf((char) b));
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

	public int getLinienAnzahl() {
		return 6;
	}

	public LinieModel getLinie(int nummer) {
		return linien[nummer];
	}

	public void load(File file) {
		this.file = file;
		model = Model.load(file);
		schuetzen = model.getSchuetzen();
		disziplinen = model.getDisziplinen();
		for (int i = 0; i < 6; i++) {
			linien[i].modelChanged();
		}
	}

	public void save(File file) {
		this.file = file;
		model.save(file);
	}

	public boolean canExit() {
		for (int i = 0; i < 6; i++) {
			if (!linien[i].isFrei()) return false;
		}
		return true;
	}

	public void exit() {
		fileChecker.exit();
	}

	public boolean contains(Object item) {
		return model.contains(item);
	}

	public Schuetze[] getSchuetzen() {
		return schuetzen;
	}

	public Disziplin[] getDisziplinen() {
		return disziplinen;
	}

	public static void main(String[] args) {
		new Controller();
	}

	public void add(Start s) {
		model.add(s);
	}

	public boolean remove(Start s) {
		return model.remove(s);
	}

	public void add(Treffer t) {
		LinieModel l = getLinie(t.getLinie() - 1);
		if (l == null) return;
		String info = l.addTreffer(t);
		save(file);
		gui.println(l + ": " + info);
		gui.showTreffer(l.getNummer(), t);
	}
}