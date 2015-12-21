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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;


public class SettingsModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final File file = new File("settings.esc");
	private static final double mmToDots = 72 / 2.54;

	private Rectangle mainWindow;

	private Set<Integer> lines;

	private double pageWidht;
	private double pageHeight;
	private double pageImageableX;
	private double pageImageableY;
	private double pageImageableWidth;
	private double pageImageableHeight;
	private int pageOrientation;

	private List<TargetModel> targets;
	private List<Weapon> weapons;
	private Map<String, Rule> rules;
	private Rule standardRule;	

	private SettingsModel() {
		mainWindow = new Rectangle(1, 1, 1022, 580);

		lines = new TreeSet<Integer>();
		
		pageWidht =           21.0 * mmToDots;
		pageHeight =          29.7 * mmToDots;
		pageImageableX =       2.5 * mmToDots;
		pageImageableY =       1.0 * mmToDots;
		pageImageableWidth =  17.5 * mmToDots;
		pageImageableHeight = 27.7 * mmToDots;
		pageOrientation = PageFormat.PORTRAIT;

		targets = new Vector<TargetModel>();
		//                         |                      |           |       |Band-   |      Durchmesser       |Ring- | Ring  |  Nummer  |   |     |  Vorhalte-   |
		//                         |Bezeichnung           | Kennummer | Karton|vorschub|Spiegel|Aussen|Innenzehn|breite|Min|Max|Max|Wimkel|Art|Style|Durchm|Abstand|
		targets.add(new TargetModel("Gewehr 10m",           "0.4.3.01",  17000,       2,   3050,  4550,        0,   250,   1, 10, 8,      0,  0,    2));
		targets.add(new TargetModel("Gewehr 15m",           "0.4.3.02",  17000,       3,   4050,  8550,        0,   450,   1, 10, 9));
		targets.add(new TargetModel("Gewehr 50m",           "0.4.3.03",  55000,       5,  11240, 15440,      500,   800,   1, 10, 8));
		targets.add(new TargetModel("Gewehr 100m",          "0.4.3.04",  55000,       5,  20000, 50000,     2500,  2500,   1, 10, 9));
		targets.add(new TargetModel("Gewehr 300m",          "0.4.3.05", 130000,       0,  60000, 100000,       0,  5000,   1, 10, 9,      1,  0,    0));
		targets.add(new TargetModel("Muskete",              "0.4.3.06",  55000,       5,  40000, 80000,     4000,  4000,   1, 10, 8));
		targets.add(new TargetModel("Pistole - Präzision",  "0.4.3.04",  55000,       5,  20000, 50000,     5000,  2500,   1, 10, 9,      0,  4,    0));
		targets.add(new TargetModel("Pistole 10m",          "0.4.3.20",  17000,       3,   5950, 15550,      500,   800,   1, 10, 8));
		targets.add(new TargetModel("Pistole - Duell",      "0.4.3.22",  55000,       5,  50000, 50000,     5000,  4000,   5, 10, 9,      0,  4,    0));
		targets.add(new TargetModel("Laufende Scheibe 10m", "0.4.3.40",  17000,       2,   3050,  5050,       50,   250,   1, 10, 9,      1,  0,    1,  3100,   7000));
		targets.add(new TargetModel("Laufende Scheibe 50m", "0.4.3.41",  70000,       0,      2, 36600,     3000,  1700,   1, 10, 9,      1,  5,    0));

		weapons = new Vector<Weapon>();
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
		rules.put("1.10", new Rule("Luftgewehr",        "1.10", targets.get(0), weapons.get(0)));
		rules.put("1.35", new Rule("Kleinkaliber 100m", "1.35", targets.get(3), weapons.get(2)));
		rules.put("1.40", new Rule("Kleinkaliber 50m",  "1.40", targets.get(2), weapons.get(2)));
		rules.put("2.10", new Rule("Luftpistole",       "2.10", targets.get(7), weapons.get(0)));
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

	public Vector<Integer> getLinien() {
		return new Vector<Integer>(lines);
	}

	public int getLinienCount() {
		return lines.size();
	}

	public boolean addLinie(Integer l) {
		boolean b = lines.add(l);
		save();
		return b;
	}

	public boolean removeLinie(Integer l) {
		boolean b = lines.remove(l);
		save();
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

	public Weapon[] getWaffen() {
		return weapons.toArray(new Weapon[0]);
	}

	public TargetModel[] getScheiben() {
		return targets.toArray(new TargetModel[0]);
	}

	public Rule[] getRules() {
		return rules.values().toArray(new Rule[0]);
	}

	public Rule getRule(String ruleNumber) {
		if (!rules.containsKey(ruleNumber)) {
			rules.put(ruleNumber, new Rule("Standardregel",  ruleNumber, standardRule.getScheibe(), standardRule.getWaffe()));
		}

		return rules.get(ruleNumber);
	}

	public Rule getStandardRule() {
		return standardRule;
	}

	private boolean save() {
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