package model;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;

import view.Scheibe;


public class Start implements Serializable, Comparable<Start>, Printable {
	private static final long serialVersionUID = 1L;

	private int linie;
	private Date datum;
	private Disziplin disziplin;
	private Schuetze schuetze;
	private Map<Treffer, Treffer> treffer;
	
	public Start(int linie, Disziplin disziplin, Schuetze schuetze) {
		this.linie = linie;
		this.datum = new Date();
		this.disziplin = disziplin;
		this.schuetze = schuetze;
		this.treffer = new HashMap<Treffer, Treffer>();
	}

	public boolean isEmpty() {
		return treffer.isEmpty();
	}

	public boolean inMatch() {
		Treffer t = treffer.get(new Treffer(false, 1));
		return t != null;
	}

	public Disziplin getDisziplin() {
		return disziplin;
	}

	public Schuetze getSchuetze() {
		return schuetze;
	}

	public String addTreffer(Treffer t) {
		// TODO schon vorhanden checken
		treffer.put(t,  t);
		String s = String.format("%s(%d) ", t.isProbe() ? "P" : "M", t.getNummer());
		return schuetze + ": " + disziplin + ": " + s + t;
	}

	public Treffer getTreffer(boolean probe, int nummer) {
		return treffer.get(new Treffer(probe, nummer));
	}

	public float getMatch(int nummer) {
		if (nummer >= disziplin.getSchusszahl()) return 0;

		Treffer t = treffer.get(new Treffer(false, nummer + 1));
		if (t == null) return 0;

		float wert = t.getWert();
		if (disziplin.getWertung() == 0) wert = ((int) wert);

		return wert;
	}

	public float getSerie(int serie) {
		float summe = 0;
		int num = disziplin.getSerienlaenge() * serie;
		for (int i = 0; i < disziplin.getSerienlaenge(); i++) {
			summe += getMatch(num + i);
		}
		return summe;
	}

	public float getResult() {
		float summe = 0;
		for (int i = 0; i < disziplin.getSchusszahl(); i++) {
			summe += getMatch(i);
		}
		return summe;
	}

	public int getNumberCount(int number) {
		if (number < 0 || number > 12) return 0;

		int counter = 0;
		for (int i = 0; i < disziplin.getSchusszahl(); i++) {
			Treffer t = treffer.get(new Treffer(false, i + 1));
			if (t == null) continue;
			switch (number) {
				case 12:
					counter++;
					break;
				case 11:
					if (t.isInnenZehner()) counter++;
					break;
				default:
					if (number == (int) t.getWert()) counter++;
			}
		}
		return counter;
	}

	public int lineCount() {
		int i;
		for (i = 0; i < disziplin.getSerienAnzahl(); i++) {
			if (getSerie(i) == 0) break;
		}
		return ((i - 1) / 4) + 1;
	}

	public void draw(Graphics g, int platz) {
		final int lineheight = g.getFontMetrics().getHeight();

		g.drawString(String.format("%2d. %s", platz, schuetze), 0, 0);
		int anzahl = 0;
		for (int i = 0; i < disziplin.getSerienAnzahl(); i++) {
			if (getSerie(i) == 0) break;

			int dx = 800 + ((i % 4) * 225);
			int dy = (i / 4) * lineheight;
			g.drawString(String.format("%5.1f", getSerie(i)), dx, dy);
			anzahl++;
		}
		int height = ((anzahl - 1) / 4) * lineheight;
		g.drawString(String.format("%5.1f", getResult()), 1800, height);
	}

	@Override
	public String toString() {
		String s = "";

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		s = s.concat(String.format("%s %-30s", sdf.format(datum), schuetze));
/*
		s = s.concat(String.format("%s (Linie %d) %-30s %-30s %5.1f", sdf.format(datum), linie, disziplin, schuetze, getResult()));

		s = s.concat("\nAnzahl:");
		for (int i = 11; i >= 0; i--) {
			s = s.concat(String.format(" %d", getNumberCount(i)));
		}

		s = s.concat(" --- Serien:");
		for (int i = 0; i < disziplin.getSerienAnzahl(); i++) {
			s = s.concat(String.format("\n%2d: %5.1f:", i + 1, getSerie(i)));
			for (int n = 0; n < disziplin.getSerienlaenge(); n++) {
				s = s.concat(String.format(" %4.1f", getMatch(i * disziplin.getSerienlaenge() + n)));
			}
		}
*/
		return s;
	}

	@Override
	public int compareTo(Start s) {
		int c;
		
		c = disziplin.compareTo(s.disziplin);
		if (c != 0) return c;

		// 1. Endergebnis vergleichen
		c = (int) (s.getResult() * 10) - (int) (getResult() * 10);
		if (c != 0) return c;
		
		// 2. Serien rückwärts vergleichen
		for (int i = disziplin.getSerienAnzahl() - 1; i >=0; i--) {
			c = (int) (s.getSerie(i) * 10) - (int) (getSerie(i) * 10);
			if (c != 0) return c;
		}
		
		// 3. Höchste Zahl der 10er, dann 9er, .... dann 1er
		for (int i = 10; i >=0; i--) {
			c = s.getNumberCount(i) - getNumberCount(i);
			if (c != 0) return c;
		}

		// 4. Höchste Zahl der InnenZehner (5 mm durchmesser)
		return s.getNumberCount(11) - getNumberCount(11);
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

		final double SCALE = 2000 / pageFormat.getImageableWidth();
		final String format = disziplin.getWertung() == 0 ? "%2.0f" : "%4.1f";

		Graphics2D g2 = (Graphics2D) g;
		g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		g2.scale(1.0 / SCALE, 1.0 / SCALE);
		g2.setFont(new Font("Consolas", Font.PLAIN, 48));
		final int lineHeight = g2.getFontMetrics().getHeight();

		g2.drawString("Linie " + linie, 0, lineHeight);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		drawStringRight(g2, sdf.format(datum), 2000, lineHeight);

		g2.translate(0, lineHeight);
		g2.drawString(schuetze.toString(), 0, lineHeight);
		drawStringRight(g2, disziplin.toString(), 2000, lineHeight);

		g2.translate(0, 1.5 * lineHeight);
		Graphics gs = g2.create(0, 0, 2000, 2000);
		Scheibe s = new Scheibe(this);
		s.setBackground(Color.WHITE);
		s.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
		s.setSize(2000, 2000);
		s.print(gs);

		g2.translate(0, 2000 + 0.5 * lineHeight);
		for (int i = 0; i < disziplin.getSerienAnzahl(); i++) {
			int serienHoehe = 0;
			drawStringRight(g2, (i + 1) + ". Serie", 300, lineHeight);
			for (int t = 0; t < disziplin.getSerienlaenge(); t++) {
				if (t % 10 == 0) {
					serienHoehe -= lineHeight;
					g2.translate(0, lineHeight);
				}
				float wert = getMatch(i * disziplin.getSerienlaenge() + t);
				drawStringRight(g2, "" + String.format(format, wert), 450 + (t % 10) * 150, 0);
			}
			drawStringRight(g2, String.format(format, getSerie(i)), 2000, 0);
			g2.drawLine(300, serienHoehe + g2.getFontMetrics().getLeading(), 300, g2.getFontMetrics().getLeading());
			g2.drawLine(1800, serienHoehe + g2.getFontMetrics().getLeading(), 1800, g2.getFontMetrics().getLeading());
			g2.drawLine(0, g2.getFontMetrics().getLeading(), 2000, g2.getFontMetrics().getLeading());
		}
		drawStringRight(g2, "Gesamt", 300, lineHeight);
		drawStringRight(g2, "" + String.format(format, getResult()), 2000, lineHeight);
		g2.drawLine(300, g2.getFontMetrics().getLeading(), 300, lineHeight + g2.getFontMetrics().getLeading());
		g2.drawLine(1800, g2.getFontMetrics().getLeading(), 1800, lineHeight + g2.getFontMetrics().getLeading());

		g2.translate(0, 3 * lineHeight);
		g2.drawString("Ring", 0, 0);
		g2.drawString("Anzahl", 0, lineHeight);
		for (int i = 0; i < 13; i++) {
			final int width = 135;
			final int rectDiff = - g2.getFontMetrics().getHeight() + g2.getFontMetrics().getLeading();
			drawStringRight(g2, i == 12 ? "Sum" : i == 11 ? "10i" : "" + i, 2000 - i * width, 0);
			g2.drawRect(2000 - width*(i+1), rectDiff, width, lineHeight);
			drawStringRight(g2, "" + getNumberCount(i), 2000 - i * width, lineHeight);
			g2.drawRect(2000 - width*(i+1), lineHeight + rectDiff, width, lineHeight);
		}

		return Printable.PAGE_EXISTS;
	}
	
	private void drawStringRight(Graphics2D g, String s, int x, int y) {
		int len = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, x - len - 10, y);
	}
}