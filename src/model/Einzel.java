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
import java.util.Date;
import java.util.List;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;

import view.Scheibe;


public class Einzel extends Start implements Printable {
	private static final long serialVersionUID = 1L;
	
	public static final int PROBE = 0;
	public static final int MATCH = 1;
	public static final int BOTH = 2;

	public static int print = MATCH;

	private int linie;
	private Date datum;
	private Disziplin disziplin;
	private Schuetze schuetze;
	private NavigableMap<Treffer, Treffer> treffer;
	
	public Einzel(int linie, Disziplin disziplin, Schuetze schuetze) {
		this.linie = linie;
		this.datum = new Date();
		this.disziplin = disziplin;
		this.schuetze = schuetze;
		this.treffer = new TreeMap<Treffer, Treffer>();
	}

	public boolean isEmpty() {
		return treffer.isEmpty();
	}

	public boolean inMatch() {
		Treffer t = getTreffer(false, 1);
		return t != null;
	}

	public Disziplin getDisziplin() {
		return disziplin;
	}

	public Schuetze getSchuetze() {
		return schuetze;
	}

	// Rückgabe > 0 falls Treffernummer korrigiert wurde. Dann ist Rückgabe alte Treffernummer. Update der Linie erforderlich.
	public int addTreffer(Treffer t) {
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

	public List<Treffer> getTreffer() {
		return new Vector<Treffer>(treffer.values());
	}

	public void insertTreffer(Treffer t) {
		int next = getNextNum(t.isProbe());
		if (t.getNummer() > next) t.setNummer(next);
		
		for (int i = next - 1; i >= t.getNummer(); i--) {
			Treffer tr = treffer.get(new Treffer(t.isProbe(), i));
			treffer.remove(tr);
			tr.setNummer(i + 1);
			treffer.put(tr,  tr);
		}
		
		treffer.put(t,  t);
	}

	public void removeTreffer(Treffer t) {
		if (treffer.remove(t) != null) {
			for (int i = t.getNummer(); true; i++) {
				Treffer tr = treffer.get(new Treffer(t.isProbe(), i + 1));
				if (tr == null) return;
				treffer.remove(tr);
				tr.setNummer(i);
				treffer.put(tr,  tr);
			}
		}
	}

	public Treffer getTreffer(boolean probe, int nummer) {
		return treffer.get(new Treffer(probe, nummer));
	}

	public int lineCount() {
		int i;
		for (i = 1; i <= disziplin.getSerienAnzahl(); i++) {
			if (getSerie(false, i) == 0) break;
		}
		return ((i - 2) / 4) + 1;
	}

	public void draw(Graphics g, int platz) {
		final int lineheight = g.getFontMetrics().getHeight();

		g.drawString(String.format("%2d. %s", platz, schuetze), 0, 0);
		int anzahl = 0;
		for (int i = 0; i < disziplin.getSerienAnzahl(); i++) {
			if (getSerie(false, i) == 0) break;

			int dx = 800 + ((i % 4) * 225);
			int dy = (i / 4) * lineheight;
			g.drawString(String.format("%5.1f", getSerie(false, i)), dx, dy);
			anzahl++;
		}
		int height = ((anzahl - 1) / 4) * lineheight;
		g.drawString(String.format("%5.1f", getResult(false)), 1800, height);
	}
	
	@Override
	public String toString() {
		String s = "";

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		s = s.concat(String.format("%s %-30s", sdf.format(datum), schuetze));

		return s;
	}

	public boolean toFile(String filename, boolean probe) {
		File file = new File(filename);
		PrintWriter writer;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			return false;
		}
		writer.println(probe ? "\"0\"" : "\"-1\"");
		int anzahl = getNumberCount(probe)[12];
		writer.println(anzahl);
		for (int i = 1; i <= anzahl; i++) {
			getTreffer(probe, i).print(writer);
		}
		writer.close();
		return true;
	}
	
	@Override
	public int compareTo(Start s) {
		int c;

		c = disziplin.compareTo(s.getDisziplin());
		if (c != 0) return c;
		
		if (s instanceof Mannschaft) return 1;

		Einzel e = (Einzel) s;

		// 1. Endergebnis vergleichen
		c = (int) (e.getResult(false) * 10) - (int) (getResult(false) * 10);
		if (c != 0) return c;
		
		// 2. Serien rückwärts vergleichen
		for (int i = disziplin.getSerienAnzahl() - 1; i >=0; i--) {
			c = (int) (e.getSerie(false, i) * 10) - (int) (getSerie(false, i) * 10);
			if (c != 0) return c;
		}

		int[] count = e.getNumberCount(false);
		int[] mycount = getNumberCount(false);
		// 3. Höchste Zahl der 10er, dann 9er, .... dann 1er
		for (int i = 10; i >=0; i--) {
			c = count[i] - mycount[i];
			if (c != 0) return c;
		}

		// 4. Höchste Zahl der InnenZehner (5 mm durchmesser)
		return count[11] - mycount[11];
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		drawStringRight(g2, sdf.format(datum), 2000, lineHeight);
		g2.translate(0, lineHeight);

		g2.drawString(schuetze.toString(), 0, lineHeight);
		drawStringRight(g2, disziplin.toString(), 2000, lineHeight);
		g2.translate(0, 1.5 * lineHeight);

		// Scheibe(n)
		int scheibenSize = print == BOTH ? 995 : 2000;
		Graphics gs = g2.create(0, 0, 2000, scheibenSize);
		Scheibe s = new Scheibe(this);
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
		int[] count = getNumberCount(probe);
		g.drawString("Ring", 0, 0);
		g.drawString("Anzahl", 0, lineHeight);
		for (int i = 0; i < 13; i++) {
			final int width = 135;
			final int rectDiff = - g.getFontMetrics().getHeight() + g.getFontMetrics().getLeading();
			drawStringRight(g, i == 12 ? "Sum" : i == 11 ? "10i" : "" + i, 2000 - i * width, 0);
			g.drawRect(2000 - width*(i+1), rectDiff, width, lineHeight);
			drawStringRight(g, "" + count[i], 2000 - i * width, lineHeight);
			g.drawRect(2000 - width*(i+1), lineHeight + rectDiff, width, lineHeight);
		}
		g.translate(0, 3 * lineHeight);
	}

	private float getValue(boolean probe, int nummer) {
		Treffer t = getTreffer(probe, nummer + 1);
		if (t == null) return -1;

		float wert = t.getWert();
		if (disziplin.getWertung() == 0) wert = ((int) wert);

		return wert;
	}

	private float getSerie(boolean probe, int serie) {
		float summe = 0;
		int num = disziplin.getSerienlaenge() * serie;
		for (int i = 0; i < disziplin.getSerienlaenge(); i++) {
			float value = getValue(probe, num + i);
			if (value >= 0) summe += value;
		}
		return summe;
	}

	private float getResult(boolean probe) {
		float summe = 0;
		for (int i = 0; true; i++) {
			float value = getValue(probe, i);
			if (value == -1) break;
			summe += value;
		}
		return summe;
	}

	private int[] getNumberCount(boolean probe) {
		int[] count = new int[13];

		for (int i = 1; probe || i <= disziplin.getSchusszahl(); i++) {
			Treffer t = getTreffer(probe, i);
			if (t == null) break;
			
			count[12]++;
			if (t.isInnenZehner()) count[11]++;
			count[(int) t.getWert()]++;
		}
		return count;
	}

	private int getNextNum(boolean probe) {
		Treffer t;

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

		t = treffer.lowerKey(new Treffer(false, 1));
		if (t == null) {
			return 1;
		}
		return t.getNummer() + 1;
	}
}