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
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;


public class SettingsModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final File file = new File("settings.esc");
	private static final double mmToDots = 72 / 2.54;

	private Rectangle mainWindow;

	private Set<Integer> linien;

	private double pageWidht;
	private double pageHeight;
	private double pageImageableX;
	private double pageImageableY;
	private double pageImageableWidth;
	private double pageImageableHeight;
	private int pageOrientation;

	private List<TargetModel> scheiben;
	private List<Weapon> waffen;
	private List<Rule> regeln;
	
	private Rule standardRegel;	

	private SettingsModel() {
		mainWindow = new Rectangle(1, 1, 1022, 580);

		linien = new TreeSet<Integer>();
		
		pageWidht =           21.0 * mmToDots;
		pageHeight =          29.7 * mmToDots;
		pageImageableX =       2.5 * mmToDots;
		pageImageableY =       1.0 * mmToDots;
		pageImageableWidth =  17.5 * mmToDots;
		pageImageableHeight = 27.7 * mmToDots;
		pageOrientation = PageFormat.PORTRAIT;

		scheiben = new Vector<TargetModel>();
		//                           |                      |           |       |Band-   |      Durchmesser       |Ring- | Ring  |  Nummer  |   |     |  Vorhalte-   |
		//                           |Bezeichnung           | Kennummer | Karton|vorschub|Spiegel|Aussen|Innenzehn|breite|Min|Max|Max|Wimkel|Art|Style|Durchm|Abstand|
		scheiben.add(new TargetModel("Gewehr 10m",           "0.4.3.01",  17000,       2,   3050,  4550,        0,   250,   1, 10, 8,      0,  0,    2));
		scheiben.add(new TargetModel("Gewehr 15m",           "0.4.3.02",  17000,       3,   4050,  8550,        0,   450,   1, 10, 9));
		scheiben.add(new TargetModel("Gewehr 50m",           "0.4.3.03",  55000,       5,  11240, 15440,      500,   800,   1, 10, 8));
		scheiben.add(new TargetModel("Gewehr 100m",          "0.4.3.04",  55000,       5,  20000, 50000,     2500,  2500,   1, 10, 9));
		scheiben.add(new TargetModel("Gewehr 300m",          "0.4.3.05", 130000,       0,  60000, 100000,       0,  5000,   1, 10, 9,      1,  0,    0));
		scheiben.add(new TargetModel("Muskete",              "0.4.3.06",  55000,       5,  40000, 80000,     4000,  4000,   1, 10, 8));
		scheiben.add(new TargetModel("Pistole - Präzision",  "0.4.3.04",  55000,       5,  20000, 50000,     5000,  2500,   1, 10, 9,      0,  4,    0));
		scheiben.add(new TargetModel("Pistole 10m",          "0.4.3.20",  17000,       3,   5950, 15550,      500,   800,   1, 10, 8));
		scheiben.add(new TargetModel("Pistole - Duell",      "0.4.3.22",  55000,       5,  50000, 50000,     5000,  4000,   5, 10, 9,      0,  4,    0));
		scheiben.add(new TargetModel("Laufende Scheibe 10m", "0.4.3.40",  17000,       2,   3050,  5050,       50,   250,   1, 10, 9,      1,  0,    1,  3100,   7000));
		scheiben.add(new TargetModel("Laufende Scheibe 50m", "0.4.3.41",  70000,       0,      2, 36600,     3000,  1700,   1, 10, 9,      1,  5,    0));

		waffen = new Vector<Weapon>();
		waffen.add(new Weapon( 1, "Luftdruck",                  4500, Unit.MM,   1));
		waffen.add(new Weapon( 2, "Zimmerstutzen",              4650, Unit.MM,   1));
		waffen.add(new Weapon( 3, "Kleinkaliber",               5600, Unit.MM,   2));
		waffen.add(new Weapon( 4, "Großkalibergewehr (<=8mm)",  8000, Unit.MM,   5));
		waffen.add(new Weapon( 5, "Großkalibergewehr (>8mm)",  10000, Unit.MM,   6));
		waffen.add(new Weapon( 6, "Kaliber .30",                 300, Unit.INCH, 6));
		waffen.add(new Weapon( 7, "Kaliber .32",                 320, Unit.INCH, 6));
		waffen.add(new Weapon( 8, "Kaliber .357",                357, Unit.INCH, 6));
		waffen.add(new Weapon( 9, "Kaliber .38",                 380, Unit.INCH, 6));
		waffen.add(new Weapon(10, "Kaliber 10mm",              10000, Unit.MM,   6));
		waffen.add(new Weapon(11, "Kaliber .44",                 440, Unit.INCH, 6));
		waffen.add(new Weapon(12, "Kaliber .45",                 450, Unit.INCH, 6));
		waffen.add(new Weapon(13, "Vorderlader",               14000, Unit.MM,   6));

		regeln = new Vector<Rule>();
		regeln.add(new Rule("Luftgewehr",        "1.10", scheiben.get(0), waffen.get(0)));
		regeln.add(new Rule("Kleinkaliber 100m", "1.35", scheiben.get(3), waffen.get(2)));
		regeln.add(new Rule("Kleinkaliber 50m",  "1.40", scheiben.get(2), waffen.get(2)));
		regeln.add(new Rule("Luftpistole",       "2.10", scheiben.get(7), waffen.get(0)));
		
		standardRegel = regeln.get(2);

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
		return new Vector<Integer>(linien);
	}

	public int getLinienCount() {
		return linien.size();
	}

	public boolean addLinie(Integer l) {
		boolean b = linien.add(l);
		save();
		return b;
	}

	public boolean removeLinie(Integer l) {
		boolean b = linien.remove(l);
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

	public Rule getRegelTypByNumber(String s) {
		for (Rule rt : regeln) {
			if (rt.getRegelnummer().equals(s)) return rt;
		}
		return standardRegel;
	}

	public Rule getStandardRegel() {
		return standardRegel;
	}

	public Weapon[] getWaffen() {
		return waffen.toArray(new Weapon[0]);
	}

	public TargetModel[] getScheiben() {
		return scheiben.toArray(new TargetModel[0]);
	}

	public Rule[] getRegeln() {
		return regeln.toArray(new Rule[0]);
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