package Model;
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
import java.util.Vector;

import javax.swing.BorderFactory;

import View.Scheibe;


public class Start implements Serializable, Comparable<Start>, Printable {
	private static final long serialVersionUID = 1L;

	private int linie;
	private Date datum;
	private Disziplin disziplin;
	private Schuetze schuetze;
	private Vector<Treffer> probe;
	private Treffer match[];
	private Vector<Treffer> addMatch;

	public Start(int linie, Disziplin disziplin, Schuetze schuetze) {
		this.linie = linie;
		this.datum = new Date();
		this.disziplin = disziplin;
		this.schuetze = schuetze;
		probe = new Vector<Treffer>(0, disziplin.getSerienlaenge());
		match = new Treffer[disziplin.getSchusszahl()];
		addMatch = new Vector<Treffer>(0, disziplin.getSerienlaenge());
	}

	public boolean isEmpty() {
		if (probe.size() > 0 || addMatch.size() > 0) return false;
		for (Treffer t : match) if (t != null) return false;
		return true;
	}

	public Disziplin getDisziplin() {
		return disziplin;
	}

	public Schuetze getSchuetze() {
		return schuetze;
	}

	public String addTreffer(Treffer treffer) {
		String s = String.format("M(%d) ", treffer.getNummer());
		if (treffer.isProbe()) {
			probe.add(treffer);
			s = String.format("P(%d) ", treffer.getNummer());
		}
		else if (treffer.getNummer() <= disziplin.getSchusszahl()) {
			match[treffer.getNummer() - 1] = treffer;
		} else {
			addMatch.add(treffer);
		}
		
		return schuetze + ": " + disziplin + ": " + s + treffer;
	}

	public float getMatch(int nummer) {
		if (nummer >= disziplin.getSchusszahl()) return 0;

		Treffer t =  match[nummer];
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
		if (number < 0 || number > 11) return 0;

		int counter = 0;
		if (number == 11) {
			// Innenzehner
			for (Treffer t : match) {
				if (t != null && t.isInnenZehner()) counter++;
			}
		} else {
			// 0 bis 10
			for (Treffer t : match) {
				if (t != null && number == (int) t.getWert()) counter++;
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

	public void draw(Graphics g, int platz, int x, int y) {
		final int lineheight = g.getFontMetrics().getHeight();

		g.drawString(String.format("%2d. %s", platz, schuetze), x, y);
		int anzahl = 0;
		for (int i = 0; i < disziplin.getSerienAnzahl(); i++) {
			if (getSerie(i) == 0) break;

			int dx = x + 800 + ((i % 4) * 225);
			int dy = y + ((i / 4) * lineheight);
			g.drawString(String.format("%5.1f", getSerie(i)), dx, dy);
			anzahl++;
		}
		int height = ((anzahl - 1) / 4) * lineheight;
		g.drawString(String.format("%5.1f", getResult()), x + 1800, y + height);
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
		Graphics gs = g2.create();
		Scheibe s = new Scheibe();
		s.setBackground(Color.WHITE);
		s.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
		s.setSize(2000, 2000);
		s.addTreffer(match);
		s.print(gs);

		g2.translate(0, 2000 + 0.5 * lineHeight);
		for (int i = 0; i < disziplin.getSerienAnzahl(); i++) {
			drawStringRight(g2, (i + 1) + ". Serie", 300, lineHeight);
			for (int t = 0; t < disziplin.getSerienlaenge(); t++) {
				float wert = getMatch(i * disziplin.getSerienlaenge() + t);
				drawStringRight(g2, "" + String.format(format, wert), 450 + t * 150, lineHeight);
			}
			drawStringRight(g2, String.format(format, getSerie(i)), 2000, lineHeight);
			g2.drawLine(300, g2.getFontMetrics().getLeading(), 300, lineHeight + g2.getFontMetrics().getLeading());
			g2.drawLine(1800, g2.getFontMetrics().getLeading(), 1800, lineHeight + g2.getFontMetrics().getLeading());
			g2.translate(0, lineHeight);
			g2.drawLine(0, g2.getFontMetrics().getLeading(), 2000, g2.getFontMetrics().getLeading());
		}
		drawStringRight(g2, "Gesamt", 300, lineHeight);
		drawStringRight(g2, "" + String.format(format, getResult()), 2000, lineHeight);
		g2.drawLine(300, g2.getFontMetrics().getLeading(), 300, lineHeight + g2.getFontMetrics().getLeading());
		g2.drawLine(1800, g2.getFontMetrics().getLeading(), 1800, lineHeight + g2.getFontMetrics().getLeading());

		g2.translate(0, 3 * lineHeight);
		g2.drawString("Ring", 0, 0);
		g2.drawString("Anzahl", 0, lineHeight);
		for (int i = 0; i < 12; i++) {
			final int width = 150;
			final int rectDiff = - g2.getFontMetrics().getHeight() + g2.getFontMetrics().getLeading();
			drawStringRight(g2, i == 11 ? "10i" : "" + i, 2000 - i * width, 0);
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