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
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.Rule;
import model.SettingsModel;
import model.Discipline;
import model.Single;
import model.LineReader;
import model.Model;
import model.ModelChangeListener;
import model.Member;
import model.Hit;

import org.apache.commons.io.FileUtils;

import view.GUI;


public class Controller {
	private OutputStream errorLog = null;

	static private SettingsModel config;
	private List<ModelChangeListener> modelChangeListener;
	
	static private File file;
	private Model model;
	private FileChecker fileChecker;
	private GUI gui = null;

	private SimpleAttributeSet redStyle;
	
	public static String getFileName() {
		return file.getName();
	}

	public static Rule getStandardRule() {
		return config.getStandardRule();
	}

	public static Rule getRule(String ruleNumber) {
		return config.getRule(ruleNumber);
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

		config = SettingsModel.load();
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
			model = new Model(config);
		}

		// http://all-fonts.com/about-fonts/download-arial-font/
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("Vera-Bold.ttf");
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, is));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		String fontString = "Bitstream Vera Sans";
		Font b12 = new Font(fontString, Font.BOLD, 11);
		Font p10 = new Font(fontString, Font.PLAIN, 9);
		Font p12 = new Font(fontString, Font.PLAIN, 11);

		UIManager.put("Label.font", b12);
		UIManager.put("ComboBox.font", b12);
		UIManager.put("Table.font", p12);
		UIManager.put("InternalFrame.titleFont", b12);
		UIManager.put("Button.font", b12);
		UIManager.put("MenuItem.acceleratorFont", p10);
		UIManager.put("Spinner.font", b12);
		UIManager.put("TableHeader.font", p12);
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
		UIManager.put("List.font", b12);
		UIManager.put("CheckBox.font", b12);
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

		fileChecker = new FileChecker(config.getLinienCount());
		gui = new GUI(this, config.getLinienCount());
	}

	public SettingsModel getConfig() {
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
		model = new Model(config);
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

	public List<Discipline> getDisziplinen() {
		return  model.getDisziplinen();
	}

	public List<Hit> getTreffer() {
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