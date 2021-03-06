package controller;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import model.DefaultLineModel;
import model.Discipline;
import model.Hit;
import model.LineModel;
import model.Member;
import model.Model;
import model.ModelChangeListener;
import model.Rule;
import model.SettingsModel;
import model.Single;
import model.Status;
import view.GUI;


public class Controller {
	private static Controller controller = null;
	private static GUI gui = null;

	private OutputStream errorLog = null;

	private List<ModelChangeListener> modelChangeListener;

	private SettingsModel config;
	private FileChecker fileChecker;
	private File file;
	private Model model;

	public static Controller get() {
		if (controller == null) {
			controller = new Controller();
			gui = new GUI();
		}
		return controller;
	}

	public static String getPath(String file) {
		CodeSource codeSource = Controller.class.getProtectionDomain().getCodeSource();
		try {
			URI jarFile = codeSource.getLocation().toURI();
			String appendix = file.equals(".") ? "" : File.separator + file;
			return (new File(jarFile)).getParentFile().getPath() + appendix;
		} catch (URISyntaxException e) {
			return "";
		}
	}

	public String getFileName() {
		return file.getName();
	}

	public Rule getStandardRule() {
		return config.getStandardRule();
	}

	public Rule[] getRules() {
		return config.getRules();
	}

	public Rule getRule(String ruleNumber) {
		return config.getRule(ruleNumber);
	}

	public SettingsModel getConfig() {
		return config;
	}

	public void reloadConfig() {
		config = SettingsModel.load();
	}

	public void saveConfig() {
		config.save();
	}

	public File getFile() {
		return file;
	}
	
	public Model getModel() {
		return model;
	}

	public void neu(File file) {
		this.file = file;
		model = new Model(config);
		save(file);
		modelChanged();
	}

	public boolean load(File file) {
		Model model = Model.load(file);
		if (model == null) return false;

		this.file = file;
		this.model = model;
		modelChanged();
		return true;
	}

	public void save() {
		if (model.save(file)) return;
		System.out.println("Datei konnte nicht gespeichert werden. Änderungen gehen bei Verlassen des Programms verloren.");
	}

	public void save(File file) {
		this.file = file;
		model.save(file);
	}

	public void exit() {
		fileChecker.exit();
	}

	public Single findIncomplete(Member schuetze, Discipline disziplin) {
		for (Single e : model.getIncomplete()) {
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

	public List<Member> getSchuetzen() {
		return model.getSchuetzen();
	}

	public Discipline[] getDisziplinen() {
		return model.getDisziplinen();
	}

	public List<Hit> getTreffer() {
		return  model.getTreffer();
	}

	public LineModel newLine(int number) {
		DefaultLineModel line = new DefaultLineModel(number, this);
		if (fileChecker.addLineReader(line)) {
			line.setStatus(Status.INIT);
			return line;
		}
		return null;
	}

	public boolean add(Object o) {
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

	public void println(String string, Color color) {
		gui.println(string, color);
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

	private Controller() {
		initConsole();
		initFiles();
		initFont();

		modelChangeListener = new Vector<ModelChangeListener>();

		config = SettingsModel.load();

		fileChecker = new FileChecker(config.getLineCount());

		SimpleDateFormat sdf = new SimpleDateFormat(config.getValue("Filename", "yyyy-MM-dd"));
		file = new File(config.getValue("Filepath", getPath("")) + sdf.format(new Date()) + ".esa");
		if (file.exists()) {
			model = Model.load(file);
		} else {
			model = new Model(config);
		}
	}

	private void initConsole() {
		try {
			errorLog = new FileOutputStream(getPath("logfile.txt"), true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			PrintStream ps = new PrintStream(new OutputStream() {
				@Override
				public void write(int b) throws IOException {				
					if (gui != null) gui.print(String.valueOf((char) b), Color.decode("0xC80000"));
					if (errorLog != null) errorLog.write(b);
				}
			}, false, "UTF-8");
			System.setOut(ps);
			System.setErr(ps);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.print("   Start: ");
		System.out.println((new SimpleDateFormat("dd.MM.yyyy - HH:mm")).format(new Date()));
		System.out.println();
	}

	private void initFiles() {
		final String[] files = {"esadb.ico", "data.mdb", "Stammdaten.mdb", "HZ_7775", "Bock.bmp", "HZ_Bock.bmp", "Fuchs.bmp", "HZ_Fuchs.bmp", "Keiler.bmp", "HZ_Keiler.bmp"};
		for (String s : files) {
			File file = new File(getPath(s));
			if (!file.exists() || s.equals("Stammdaten.mdb")) {
				URL inputUrl = getClass().getResource("/" + s);
				if (inputUrl == null) continue;

				try {
					FileUtils.copyURLToFile(inputUrl, file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initFont() {
		// http://all-fonts.com/about-fonts/download-arial-font/
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		if (classloader == null) return;

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String fontFiles[] = {"Vera.ttf", "Vera-Bold.ttf", "Vera-Bold-Italic.ttf", "Vera-Italic.ttf"};
		for (String font : fontFiles) {
			InputStream is = null;
			try {
				is = classloader.getResourceAsStream(font);
				Font f = Font.createFont(Font.TRUETYPE_FONT, is);
				ge.registerFont(f);
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(is);
			}
		}

		String fontString = "Bitstream Vera Sans";
		Font b12 = new Font(fontString, Font.BOLD, 11);
		Font p10 = new Font(fontString, Font.PLAIN, 9);
		Font p12 = new Font(fontString, Font.PLAIN, 11);

		UIManager.put("Label.font", b12);
		UIManager.put("ComboBox.font", p12);
		UIManager.put("Table.font", p12);
		UIManager.put("InternalFrame.titleFont", b12);
		UIManager.put("Button.font", b12);
		UIManager.put("MenuItem.acceleratorFont", p10);
		UIManager.put("Spinner.font", b12);
		UIManager.put("TableHeader.font", b12);
		UIManager.put("TextPane.font", p12);
		UIManager.put("PasswordField.font", p12);
		UIManager.put("ColorChooser.font", p12);
		UIManager.put("ScrollPane.font", p12);
		UIManager.put("Menu.acceleratorFont", p10);
		UIManager.put("RadioButton.font", b12);
		UIManager.put("Menu.font", b12);
		UIManager.put("Viewport.font", p12);
		UIManager.put("CheckBoxMenuItem.font", b12);
		UIManager.put("DesktopIcon.font", b12);
		UIManager.put("TextArea.font", p12);
		UIManager.put("ToolBar.font", b12);
		UIManager.put("Tree.font", p12);
		UIManager.put("ToggleButton.font", b12);
		UIManager.put("EditorPane.font", p12);
		UIManager.put("List.font", p12);
		UIManager.put("CheckBox.font", p12);
		UIManager.put("MenuBar.font", b12);
		UIManager.put("OptionPane.font", p12);
		UIManager.put("Panel.font", p12);
		UIManager.put("ProgressBar.font", b12);
		UIManager.put("TabbedPane.font", b12);
		UIManager.put("Slider.font", b12);
		UIManager.put("TextField.font", p12);
		UIManager.put("ToolTip.font", p12);
		UIManager.put("FormattedTextField.font", p12);
		UIManager.put("TitledBorder.font", b12);
		UIManager.put("MenuItem.font", b12);
		UIManager.put("RadioButtonMenuItem.acceleratorFont", p10);
		UIManager.put("RadioButtonMenuItem.font", b12);
		UIManager.put("PopupMenu.font", b12);
		UIManager.put("CheckBoxMenuItem.acceleratorFont", p10);
	}

	public static void main(String[] args) throws IOException {
		File lockfile = new File(getPath("esadb.lock"));
		FileOutputStream out = new FileOutputStream(lockfile);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					out.close();
					if (!lockfile.delete()) {
						System.out.println("esadb.lock konnte nicht gelöscht werden. Diese Datei verhindert einen ");
						System.out.println("weiteren Programmstart falls sie nicht manuell gelöscht wird.");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));

		if (out.getChannel().tryLock() == null) {
			JOptionPane.showMessageDialog(
				null,
				"Das Programm kann nur einmal zur Zeit gestartet werden.",
				"Fehler",
				JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		get();
	}
}