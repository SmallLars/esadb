package model;


import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;


public class SettingsModel implements Serializable {
	private static final long serialVersionUID = 2L;
	private static final File file = new File("settings.esc");
	private static final double mmToDots = 72 / 2.54;

	private Rectangle mainWindow;

	private String path;
	private String filename;

	private Set<Integer> lines;

	private double pageWidht;
	private double pageHeight;
	private double pageImageableX;
	private double pageImageableY;
	private double pageImageableWidth;
	private double pageImageableHeight;
	private int pageOrientation;

	private int year;
	private List<Group> groups;

	private Set<TargetModel> targets;
	private Set<Weapon> weapons;
	private Map<String, Rule> rules;
	private Rule standardRule;	

	private SettingsModel() {
		mainWindow = new Rectangle(1, 1, 1022, 580);

		path = (new File("")).getAbsolutePath();
		filename = "yyyy-MM-dd";

		lines = new TreeSet<Integer>();
		
		pageWidht =           21.0 * mmToDots;
		pageHeight =          29.7 * mmToDots;
		pageImageableX =       2.5 * mmToDots;
		pageImageableY =       1.0 * mmToDots;
		pageImageableWidth =  17.5 * mmToDots;
		pageImageableHeight = 27.7 * mmToDots;
		pageOrientation = PageFormat.PORTRAIT;

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		year = cal.get(Calendar.YEAR);

		groups = new ArrayList<Group>();
		groups.add(new Group("Schüler B",          year - 12, year,      true));
		groups.add(new Group("Schüler B",          year - 12, year,      false));
		groups.add(new Group("Schüler A",          year - 14, year - 13, true));
		groups.add(new Group("Schüler A",          year - 14, year - 13, false));
		groups.add(new Group("Jugend",             year - 16, year - 15, true));
		groups.add(new Group("Jugend",             year - 16, year - 15, false));
		groups.add(new Group("Junioren B",         year - 18, year - 17, true));
		groups.add(new Group("Junioren B",         year - 18, year - 17, false));
		groups.add(new Group("Junioren A",         year - 20, year - 19, true));
		groups.add(new Group("Junioren A",         year - 20, year - 19, false));
		groups.add(new Group("Herren",             year - 45, year - 21, true));
		groups.add(new Group("Damen",              year - 45, year - 21, false));
		groups.add(new Group("Altersklasse",       year - 55, year - 46, true));
		groups.add(new Group("Damen Altersklasse", year - 55, year - 46, false));
		groups.add(new Group("Senioren",           year - 99, year - 56, true));
		groups.add(new Group("Seniorinnen",        year - 99, year - 56, false));

		targets = new TreeSet<TargetModel>();
		//                         |                      |           |    Karton-    |Band-   |      Durchmesser       |Ring- | Ring  |  Nummer  |   |     |  Vorhalte-   |
		//                         |Bezeichnung           | Kennummer | breite | höhe |vorschub|Spiegel|Aussen|Innenzehn|breite|Min|Max|Max|Wimkel|Art|Style|Durchm|Abstand|
		targets.add(new TargetModel("Gewehr 10m",           "0.4.3.01",  17000,  17000,       2,   3050,  4550,        0,   250,  1, 10, 8,      0,  0,    2));
		targets.add(new TargetModel("Gewehr 15m",           "0.4.3.02",  17000,  17000,       3,   4050,  8550,        0,   450,  1, 10, 9));
		targets.add(new TargetModel("Gewehr 50m",           "0.4.3.03",  55000,  55000,       5,  11240, 15440,      500,   800,  1, 10, 8));
		targets.add(new TargetModel("Gewehr 100m",          "0.4.3.04",  55000,  55000,       5,  20000, 50000,     2500,  2500,  1, 10, 9));
		targets.add(new TargetModel("Gewehr 300m",          "0.4.3.05", 130000, 130000,       0,  60000, 100000,       0,  5000,  1, 10, 9,      1,  0,    0));
		targets.add(new TargetModel("Muskete",              "0.4.3.06",  55000,  55000,       5,  40000, 80000,     4000,  4000,  1, 10, 8));
		targets.add(new TargetModel("Pistole 10m",          "0.4.3.20",  17000,  17000,       3,   5950, 15550,      500,   800,  1, 10, 8));
		targets.add(new TargetModel("Pistole - Duell",      "0.4.3.22",  55000,  55000,       5,  50000, 50000,     5000,  4000,  5, 10, 9,      0,  4,    0));
		targets.add(new TargetModel("Pistole - Präzision",  "0.4.3.24",  55000,  55000,       5,  20000, 50000,     2500,  2500,  1, 10, 9,      0,  4,    0));
		targets.add(new TargetModel("Laufende Scheibe 10m", "0.4.3.40",  17000,  17000,       2,   3050,  5050,       50,   250,  1, 10, 9,      1,  0,    1,  3100,   7000));
		targets.add(new TargetModel("Laufende Scheibe 50m", "0.4.3.41",  70000,  70000,       0,      2, 36600,     3000,  1700,  1, 10, 9,      1,  5,    0));
		targets.add(new TargetModel("Wildscheibe Bock",    "0.4.3.90",  "Bock.bmp"));
		targets.add(new TargetModel("Wildscheibe Fuchs",   "0.4.3.91",  "Fuchs.bmp"));
		targets.add(new TargetModel("Wildscheibe Keiler",  "0.4.3.92",  "Keiler.bmp"));

		weapons = new TreeSet<Weapon>();
		weapons.add(new Weapon("Luftdruck",                 "01",  4500, Unit.MM,   1));
		weapons.add(new Weapon("Zimmerstutzen",             "02",  4650, Unit.MM,   1));
		weapons.add(new Weapon("Kleinkaliber",              "03",  5600, Unit.MM,   2));
		weapons.add(new Weapon("Großkalibergewehr (<=8mm)", "04",  8000, Unit.MM,   5));
		weapons.add(new Weapon("Großkalibergewehr (>8mm)",  "05", 10000, Unit.MM,   6));
		weapons.add(new Weapon("Kaliber .30",               "06",   300, Unit.INCH, 6));
		weapons.add(new Weapon("Kaliber .32",               "07",   320, Unit.INCH, 6));
		weapons.add(new Weapon("Kaliber .357",              "08",   357, Unit.INCH, 6));
		weapons.add(new Weapon("Kaliber .38",               "09",   380, Unit.INCH, 6));
		weapons.add(new Weapon("Kaliber 10mm",              "10", 10000, Unit.MM,   6));
		weapons.add(new Weapon("Kaliber .44",               "11",   440, Unit.INCH, 6));
		weapons.add(new Weapon("Kaliber .45",               "12",   450, Unit.INCH, 6));
		weapons.add(new Weapon("Vorderlader",               "13", 14000, Unit.MM,   6));

		rules = new TreeMap<String, Rule>();
		rules.put("1.10", new Rule("Luftgewehr",        "1.10", targets.toArray(new TargetModel[0])[0], weapons.toArray(new Weapon[0])[0]));
		rules.put("1.35", new Rule("Kleinkaliber 100m", "1.35", targets.toArray(new TargetModel[0])[3], weapons.toArray(new Weapon[0])[2]));
		rules.put("1.40", new Rule("Kleinkaliber 50m",  "1.40", targets.toArray(new TargetModel[0])[2], weapons.toArray(new Weapon[0])[2]));
		rules.put("2.10", new Rule("Luftpistole",       "2.10", targets.toArray(new TargetModel[0])[6], weapons.toArray(new Weapon[0])[0]));
		standardRule = rules.get("1.40");

		save();
	}

	public Rectangle getMainWindowBounds() {
		return mainWindow;
	}

	public void setMainWindowBounds(Rectangle mainWindow) {
		this.mainWindow = mainWindow;
		save();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Vector<Integer> getLines() {
		return new Vector<Integer>(lines);
	}

	public int getLineCount() {
		return lines.size();
	}

	public boolean addLine(Integer l) {
		boolean b = lines.add(l);
		return b;
	}

	public boolean removeLinie(Integer l) {
		boolean b = lines.remove(l);
		return b;
	}

	public PageFormat getPageFormat() {
		Paper p = new Paper();
		p.setSize(pageWidht, pageHeight);
		p.setImageableArea(pageImageableX, pageImageableY, pageImageableWidth, pageImageableHeight);
		PageFormat pf = new PageFormat();
		pf.setPaper(p);
		pf.setOrientation(pageOrientation);
		return pf;
	}

	public void setPageFormat(PageFormat pf) {
		pageWidht = pf.getPaper().getWidth();
		pageHeight = pf.getPaper().getHeight();
		pageImageableX = pf.getPaper().getImageableX();
		pageImageableY = pf.getPaper().getImageableY();
		pageImageableWidth = pf.getPaper().getImageableWidth();
		pageImageableHeight = pf.getPaper().getImageableHeight();
		pageOrientation = pf.getOrientation();
		save();
	}

	public int getYear() {
		return year;
	}

	public void setYear(int value) {
		int diff = value - year;
		year = value;
		for (Group g : groups) g.change(diff);
	}

	public Group[] getGroups() {
		return groups.toArray(new Group[0]);
	}

	public Weapon newWeapon() {
		Weapon w = new Weapon(	"Neue Waffe", "01",
								standardRule.getWaffe().getDiameter(),
								standardRule.getWaffe().getUnit(),
								standardRule.getWaffe().getMikro()
		);
		for (int n = 1; true; n++) {
			w.setNumber(String.format("%02d", n));
			if (!weapons.contains(w)) {
				weapons.add(w);
				return w;
			}
		}

	}

	public Weapon[] getWeapons() {
		return weapons.toArray(new Weapon[0]);
	}

	public boolean removeWeapon(Weapon w) {
		for (Rule r : rules.values()) {
			if (w.compareTo(r.getWaffe()) == 0) return false;
		}
		return weapons.remove(w);
	}

	public TargetModel newTarget(int type) {
		TargetModel tm = new TargetModel();
		tm.setValue(TargetValue.TYPE, type);
		
		String number = tm.getNumber().substring(0, tm.getNumber().lastIndexOf(".") + 1);
		for (int n = 1; true; n++) {
			tm.setNumber(number + String.format("%02d", n));
			if (!targets.contains(tm)) {
				targets.add(tm);
				return tm;
			}
		}
	}

	public boolean validTargetNumber(TargetModel tm, String number) {
		for (TargetModel t : targets) {
			if (tm == t) continue;

			if (number.equals(t.getNumber())) {
				return false;
			}
		}
		return true;
	}

	public boolean changeTargetNumber(TargetModel tm, String number) {
		if (!validTargetNumber(tm, number)) return false;
			
		targets.remove(tm);
		tm.setNumber(number);
		targets.add(tm);
		return true;
	}

	public TargetModel[] getTargets() {
		return targets.toArray(new TargetModel[0]);
	}

	public boolean removeTarget(TargetModel t) {
		for (Rule r : rules.values()) {
			if (t.compareTo(r.getScheibe()) == 0) return false;
		}
		return targets.remove(t);
	}

	public Rule[] getRules() {
		return rules.values().toArray(new Rule[0]);
	}

	public Rule getRule(String ruleNumber) {
		if (!rules.containsKey(ruleNumber)) {
			TargetModel tm = standardRule.getScheibe();
			if (targets.contains(tm)) {
				for (TargetModel t : targets) if (t.equals(tm)) {
					tm = t;
					break;
				}
			} else {
				targets.add(tm);
			}

			Weapon we = standardRule.getWaffe();
			if (weapons.contains(we)) {
				for (Weapon w : weapons) if (w.equals(we)) {
					we = w;
					break;
				}
			} else {
				weapons.add(we);
			}

			rules.put(ruleNumber, new Rule("Standardregel",  ruleNumber, tm, we));
			if (rules.size() == 1) standardRule = rules.get(ruleNumber);
		}

		return rules.get(ruleNumber);
	}

	public Rule getStandardRule() {
		return standardRule;
	}

	public void setStandardRule(Rule rule) {
		standardRule = rule;
	}

	public void removeRule(String ruleNumber) {
		rules.remove(ruleNumber);
		if (ruleNumber.equals(standardRule.getRegelnummer())) {
			if (rules.size() > 0) {
				standardRule = rules.values().toArray(new Rule[0])[0];
			}
		}
	}

	public boolean save() {
		groups.sort(null);

		boolean succeed = false;
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			succeed = true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (oos != null) try { oos.close(); } catch (IOException e) {}
			if (fos != null) try { fos.close(); } catch (IOException e) {}
		}
		return succeed;
	}

	public static SettingsModel load() {
		if (!file.exists()) return new SettingsModel();

		SettingsModel config = null;
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			if (obj instanceof SettingsModel) {
				config = (SettingsModel) obj;
			}
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (ois != null) try { ois.close(); } catch (IOException e) {}
			if (fis != null) try { fis.close(); } catch (IOException e) {}
		}

		return config;
	}
}