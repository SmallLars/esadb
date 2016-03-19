package model;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;

import controller.Controller;
import view.Target;


public class Single extends Result implements Printable {
	private static final long serialVersionUID = 2L;
	
	public static final int PROBE = 0;
	public static final int MATCH = 1;
	public static final int BOTH = 2;

	public static int print = MATCH;

	private int linie;
	private Date datum;
	private Discipline disziplin;
	private Member schuetze;
	private NavigableMap<Hit, Hit> treffer;
	
	public Single(int linie, Discipline disziplin, Member schuetze) {
		this.linie = linie;
		this.datum = new Date();
		this.disziplin = disziplin;
		this.schuetze = schuetze;
		this.treffer = Collections.synchronizedNavigableMap(new TreeMap<Hit, Hit>());
	}

	@Override
	public String toString() {
		String s = "";

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		s = s.concat(String.format("%s %-30s", sdf.format(datum), schuetze));

		return s;
	}

	public String getName() {
		return schuetze.toString();
	}

	public boolean isEmpty() {
		return treffer.isEmpty();
	}

	public boolean isComplete() {
		return getTreffer(false, disziplin.getSchusszahl()) != null;
	}

	public boolean inMatch() {
		Hit t = getTreffer(false, 1);
		return t != null;
	}

	public Discipline getDisziplin() {
		return disziplin;
	}

	@Override
	public Group getGroup(boolean useSettings) {
		SettingsModel settings = Controller.get().getConfig();

		if (useSettings && !settings.getValue("ResultListGroup", true)) {
			if (!settings.getValue("ResultListGender", true)) {
				return Group.getStandardGroup(Gender.ANY, settings.getYear(), null);
			}

			if (schuetze.isMale()) return Group.getStandardGroup(Gender.MALE, settings.getYear(), null);
			if (schuetze.isFemale()) return Group.getStandardGroup(Gender.FEMALE, settings.getYear(), null);
			return Group.getStandardGroup(Gender.ANY, settings.getYear(), null);
		}

		if (useSettings && !settings.getValue("ResultListGender", true)) {
			for (Group g : settings.getGroups()) {
				if (g.isMember(schuetze, false)) return g;
			}
		} else {
			for (Group g : settings.getGroups()) {
				if (g.isMember(schuetze, true)) return g;
			}

			for (Group g : settings.getGroups()) {
				if (g.isMember(schuetze, false)) return g;
			}
		}

		return Group.getStandardGroup(Gender.ANY, settings.getYear(), "Ohne Zuordnung");
	}

	public Member getSchuetze() {
		return schuetze;
	}

	// Rückgabe > 0 falls Treffernummer korrigiert wurde. Dann ist Rückgabe alte Treffernummer. Update der Linie erforderlich.
	public int addTreffer(Hit t) {
		int next = getNextNum(t.isProbe());
		if (t.getNummer() == next) {
			treffer.put(t,  t);
			return 0;
		}
		
		int old = t.getNummer();
		t.setNummer(next);
		treffer.put(t,  t);
		return old;
	}

	public List<Hit> getTreffer() {
		return new Vector<Hit>(treffer.values());
	}

	public void insertTreffer(Hit t) {
		int next = getNextNum(t.isProbe());
		if (t.getNummer() > next) t.setNummer(next);
		
		for (int i = next - 1; i >= t.getNummer(); i--) {
			Hit tr = treffer.remove(new Hit(t.isProbe(), i));
			tr.setNummer(i + 1);
			treffer.put(tr,  tr);
		}
		
		treffer.put(t,  t);
	}

	public void removeTreffer(Hit t) {
		if (treffer.remove(t) != null) {
			for (int i = t.getNummer(); true; i++) {
				Hit tr = treffer.get(new Hit(t.isProbe(), i + 1));
				if (tr == null) return;
				treffer.remove(tr);
				tr.setNummer(i);
				treffer.put(tr,  tr);
			}
		}
	}

	public Hit getTreffer(boolean probe, int nummer) {
		return treffer.get(new Hit(probe, nummer));
	}

	@Override
	public float getResult(boolean probe) {
		float summe = 0;
		for (int i = 0; i < disziplin.getSchusszahl(); i++) {
			float value = getValue(probe, i);
			if (value == -1) break;
			summe += value;
		}
		return summe;
	}

	public float getSerie(boolean probe, int serie) {
		if (probe && serie < 0) return 0;

		float summe = 0;
		int num;
		if (serie >= 0) {
			num = disziplin.getSerienlaenge() * serie;
		} else {
			num = disziplin.getSerienlaenge() * (disziplin.getSerienAnzahl() + serie);
		}
		for (int i = 0; i < disziplin.getSerienlaenge(); i++) {
			float value = getValue(probe, num + i);
			if (value >= 0) summe += value;
		}
		return summe;
	}

	public int getMaxNumber() {
		return Controller.get().getRule(disziplin.getRuleNumber()).getScheibe().getValue(TargetValue.RING_MAX);
	}	

	public int getNumberCount(boolean probe, int number) {
		int count = 0;

		for (int i = 1; probe || i <= disziplin.getSchusszahl(); i++) {
			Hit t = getTreffer(probe, i);
			if (t == null) break;

			if (number == -1 || number == (int) t.getWert()) count++;
		}
		return count;
	}

	public int getInnerCount(boolean probe) {
		int count = 0;

		for (int i = 1; probe || i <= disziplin.getSchusszahl(); i++) {
			Hit t = getTreffer(probe, i);
			if (t == null) break;

			if (t.isInnenZehner()) count++;
		}
		return count;
	}

	public int lineCount() {
		int hitsDone = getNextNum(false) - 1;		
		int hitsOnLine = disziplin.getSerienlaenge() * 4;
		return ((hitsDone - 1) / hitsOnLine) + 1;
	}

	public void draw(Graphics2D g, int platz, int fontsize, int lineheight, boolean overline) {
		GraphicsString gs = new GraphicsString(g, fontsize, false, false, Color.BLACK);

		gs.drawStringRight(String.format("%d.", platz), 100, lineheight);
		gs.drawStringLeft(schuetze.toString(), 125, lineheight);

		int serien = ((getNextNum(false) - 2) / disziplin.getSerienlaenge()) + 1;
		if (serien > disziplin.getSerienAnzahl()) serien = disziplin.getSerienAnzahl();
		for (int i = 0; i < serien; i++) {
			int dx = 950 + ((i % 4) * 225);
			int dy = (i / 4) * lineheight;
			gs.drawStringRight(String.format("%5.1f", getSerie(false, i)), dx, dy + lineheight);
		}
		gs.drawStringRight(String.format("%.1f", getResult(false)), 1950, lineCount() * lineheight);

		g.drawLine(725, 10, 725, lineCount() * lineheight + 10);
		g.drawLine(1700, 10, 1700, lineCount() * lineheight + 10);
		if (overline) g.drawLine(0, 10, 2000, 10);
	}

	public boolean toFile(String filename, boolean probe) {
		File file = new File(Controller.getPath() + filename);
		PrintWriter writer;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			return false;
		}
		writer.println(probe ? "\"0\"" : "\"-1\"");
		int anzahl = getNumberCount(probe, -1);
		writer.println(anzahl);
		for (int i = 1; i <= anzahl; i++) {
			getTreffer(probe, i).print(writer);
		}
		writer.close();
		return true;
	}
	
	@Override
	public int compareTo(Result s) {
		int c;

		// Zuerst nach Disziplin sortieren
		c = disziplin.compareTo(s.getDisziplin());
		if (c != 0) return c;

		// Sortierung nach Gruppen
		c = getGroup(true).compareTo(s.getGroup(true));
		if (c != 0) return c;

		// Mannschaften kommen vor Einzel
		if (s instanceof Team) return 1;

		// Zwei Einzelstarts werden nun verglichen
		Single e = (Single) s;

		// 1. Endergebnis vergleichen
		c = (int) (e.getResult(false) * 10) - (int) (getResult(false) * 10);
		if (c != 0) return c;
		
		// 2. Serien rückwärts vergleichen
		for (int i = disziplin.getSerienAnzahl() - 1; i >=0; i--) {
			c = (int) (e.getSerie(false, i) * 10) - (int) (getSerie(false, i) * 10);
			if (c != 0) return c;
		}

		// 3. Höchste Zahl der 10er, dann 9er, .... dann 1er
		for (int i = 10; i >=0; i--) {
			c = e.getNumberCount(false, i) - getNumberCount(false, i);
			if (c != 0) return c;
		}

		// 4. Höchste Zahl der InnenZehner
		return e.getInnerCount(false) - this.getInnerCount(false);
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

		final double SCALE = pageFormat.getImageableWidth() / 2000;

		Graphics2D g2 = (Graphics2D) g;
		g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		g2.scale(SCALE, SCALE);
		g2.setFont(new Font("Consolas", Font.PLAIN, 48));
		final int lineHeight = g2.getFontMetrics().getHeight();

		// Überschriften
		g2.drawString("Linie " + linie, 0, lineHeight);
		drawStringCenter(g2, Controller.get().getFileName(), 1000, lineHeight);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		drawStringRight(g2, sdf.format(datum), 2000, lineHeight);
		g2.translate(0, lineHeight);

		g2.drawString(schuetze.toString(), 0, lineHeight);
		drawStringRight(g2, disziplin.toString(), 2000, lineHeight);
		g2.translate(0, 1.5 * lineHeight);

		// Scheibe(n)
		int scheibenSize = print == BOTH ? 995 : 2000;
		Graphics gs = g2.create(0, 0, 2000, scheibenSize);
		Target s = new Target(this);
		s.setBackground(Color.WHITE);
		s.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
		s.setSize(scheibenSize, scheibenSize);
		if (print == MATCH) s.forceMatch(); else s.forceProbe();
		s.print(gs);
		if (print == BOTH) {
			gs.translate(1005, 0);
			s.forceMatch();
			s.print(gs);
		}
		g2.translate(0, scheibenSize + 1.5 * lineHeight);

		// Überschrift falls Probe und Match
		if (print == BOTH) {
			drawHeadline(g2, lineHeight, "Probe:");
		}

		// Probeschüsse
		if (print != MATCH) {
			drawValues(g2, lineHeight, true);
			drawNumberCountTable(g2, lineHeight, true);
		}

		// Überschrift falls Probe und Match
		if (print == BOTH) {
			drawHeadline(g2, lineHeight, "Match:");
		}

		// Wertungsschüsse
		if (print != PROBE) {
			drawValues(g2, lineHeight,false);
			drawNumberCountTable(g2, lineHeight, false);
		}

		return Printable.PAGE_EXISTS;
	}

	private void drawStringCenter(Graphics2D g, String s, int x, int y) {
		int len = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, x - len / 2, y);
	}
	
	private void drawStringRight(Graphics2D g, String s, int x, int y) {
		int len = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, x - len - 10, y);
	}

	private void drawHeadline(Graphics2D g, int lineHeight, String text) {
		g.drawString(text, 0, 0);
		g.translate(0, 0.5 * lineHeight);
	}

	private void drawValues(Graphics2D g, int lineHeight, boolean probe) {
		final String format = disziplin.getWertung() == 0 ? "%2.0f" : "%4.1f";

		boolean run = true;
		for (int i = 0; run; i++) {
			int serienHoehe = 0;
			drawStringRight(g, (i + 1) + ". Serie", 300, lineHeight);
			for (int t = 0; run && t < disziplin.getSerienlaenge(); t++) {
				if (t % 10 == 0) {
					serienHoehe -= lineHeight;
					g.translate(0, lineHeight);
				}
				float wert = getValue(probe, i * disziplin.getSerienlaenge() + t);
				if (wert >= 0) {
					drawStringRight(g, "" + String.format(format, wert), 450 + (t % 10) * 150, 0);
				}
				// Falls der Nächste nicht existiert -> Abbruch
				if (getValue(probe, i * disziplin.getSerienlaenge() + t + 1) == -1) {
					run = false;
				}
			}
			drawStringRight(g, String.format(format, getSerie(probe, i)), 2000, 0);
			g.drawLine(300, serienHoehe + g.getFontMetrics().getLeading(), 300, g.getFontMetrics().getLeading());
			g.drawLine(1800, serienHoehe + g.getFontMetrics().getLeading(), 1800, g.getFontMetrics().getLeading());
			g.drawLine(0, g.getFontMetrics().getLeading(), 2000, g.getFontMetrics().getLeading());
		}
		drawStringRight(g, "Gesamt", 300, lineHeight);
		drawStringRight(g, "" + String.format(format, getResult(probe)), 2000, lineHeight);
		g.drawLine(300, g.getFontMetrics().getLeading(), 300, lineHeight + g.getFontMetrics().getLeading());
		g.drawLine(1800, g.getFontMetrics().getLeading(), 1800, lineHeight + g.getFontMetrics().getLeading());
		g.translate(0, 2.5 * lineHeight);
	}

	private void drawNumberCountTable(Graphics2D g, int lineHeight, boolean probe) {
		int maxNum = getMaxNumber();
		int[] count = new int[maxNum + 3];
		for (int i = 0; i <= maxNum; i++) {
			count[i] = getNumberCount(probe, i);
			count[maxNum + 2] += count[i];
		}
		count[maxNum + 1] = getInnerCount(probe);

		g.drawString("Ring", 0, 0);
		g.drawString("Anzahl", 0, lineHeight);
		for (int i = 0; i < maxNum + 3; i++) { // 13
			final int width = 1755 / (maxNum + 3);
			final int rectDiff = - g.getFontMetrics().getHeight() + g.getFontMetrics().getLeading();
			drawStringRight(g, i == maxNum + 2 ? "Sum" : i == maxNum + 1 ? maxNum + "i" : "" + i, 2000 - i * width, 0);
			g.drawRect(2000 - width*(i+1), rectDiff, width, lineHeight);
			drawStringRight(g, "" + count[i], 2000 - i * width, lineHeight);
			g.drawRect(2000 - width*(i+1), lineHeight + rectDiff, width, lineHeight);
		}
		g.translate(0, 3 * lineHeight);
	}

	private float getValue(boolean probe, int nummer) {
		Hit t = getTreffer(probe, nummer + 1);
		if (t == null) return -1;

		float wert = t.getWert();
		if (disziplin.getWertung() == 0) wert = ((int) wert);

		return wert;
	}

	private int getNextNum(boolean probe) {
		Hit t;

		try {
			t = treffer.lastKey();
		} catch (NoSuchElementException e) {
			return 1;
		}
		if (t.isProbe() == probe) {
			return t.getNummer() + 1;
		}
		if (!probe) {
			return 1;
		}

		t = treffer.lowerKey(new Hit(false, 1));
		if (t == null) {
			return 1;
		}
		return t.getNummer() + 1;
	}
}