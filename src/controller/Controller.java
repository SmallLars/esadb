package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Config;
import model.Model;

import org.apache.commons.io.FileUtils;

import view.GUI;


public class Controller {
	private Config config;
	
	private File file;
	private Model model;
	private Linie linien[];
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

		linien = new Linie[6];
		for (int i = 0; i < 6; i++) {
			linien[i] = new Linie(i + 1, this);
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

	public Linie getLinie(int nummer) {
		return linien[nummer];
	}

	public void load(File file) {
		this.file = file;
		model = Model.load(file);
		for (int i = 0; i < 6; i++) {
			linien[i].refresh();
		}
	}

	public void save() {
		model.save(file);
	}

	public void save(File file) {
		this.file = file;
		save();
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

	public static void main(String[] args) {
		new Controller();
	}
}