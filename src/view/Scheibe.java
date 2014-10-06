package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import model.DefaultLineModel;
import model.Einzel;
import model.LineListener;
import model.LineModel;
import model.Treffer;


@SuppressWarnings("serial")
public class Scheibe extends JPanel implements LineListener {
	private final int PROBE = 1;
	private final int MATCH = 2;

	private int mitteX;
	private int mitteY;

	private ScheibeTyp typ = ScheibeTyp.KK50M;
	private Einzel einzel;
	private Treffer treffer;

	private int nummer;
	private int force;

	public Scheibe() {
		this(null);
	}

	public Scheibe(Einzel einzel) {
		this(einzel, 0);
	}

	public Scheibe(Einzel einzel, int nummer) {
		this.einzel = einzel;
		this.treffer = null;
		this.nummer = nummer;
		this.force = 0;
		setSize(400, 400);
	}

	@Override
	public void setSize(int w, int h) {
		super.setSize(w < h ? w : h, w < h ? w : h);
	}

	@Override
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w < h ? w : h, w < h ? w : h);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension d;
		
		if (showProbe()) {
			d = toPixel(80);
			g.setColor(Color.BLACK);
			g.fillPolygon(new int[]{getWidth() - d.width, getWidth() - d.width, getWidth() - d.height * 6}, new int[]{d.height, d.height * 6, d.height}, 3);
		}

		mitteX = getWidth() / 2;
		mitteY = getHeight() / 2;

		d = toPixel(typ.getSpiegel());
	    g.setColor(Color.BLACK);
	    g.fillOval(mitteX - d.width/2, mitteY - d.height/2, d.width, d.height);

		for (int i = 1; i <= 10; i++) {
			d = toPixel(typ.getRing(i));
		    g.setColor(typ.getRing(i) > typ.getSpiegel() ? Color.BLACK : Color.WHITE);
	    	g.drawOval(mitteX - d.width/2, mitteY - d.height/2, d.width, d.height);
		}

		if (typ.getInnenZehn() > 0) {
			d = toPixel(typ.getInnenZehn());
		    g.setColor(Color.WHITE);
		    g.drawOval(mitteX - d.width/2, mitteY - d.height/2, d.width, d.height);
		}

		g.setFont(new Font("Arial", Font.BOLD, toPixel(typ.getFontSize()).height));
		int dy = g.getFontMetrics().getAscent() / 2;
		int dx = (int) (g.getFontMetrics().getStringBounds("0", g).getWidth() / -2) + 1;
		for (int i = 1; typ.drawNumber(i); i++) {
			g.setColor(typ.blackNumber(i) ? Color.BLACK : Color.WHITE);
			String s = "" + i;
			d = toPixel(typ.getNumberRadius(i));
			g.drawString(s, mitteX - d.width + dx, mitteY + dy);	// Links
			g.drawString(s, mitteX + dx, mitteY - d.height + dy);	// Oben
			g.drawString(s, mitteX + d.width + dx , mitteY + dy);	// Rechts
			g.drawString(s, mitteX + dx, mitteY + d.height + dy);	// Unten
		}

		if (nummer > 0) {
			d = toPixel(80);
			g.setFont(new Font("Arial", Font.BOLD, toPixel(180).height));
			g.setColor(Color.BLACK);
			g.drawString("" + nummer, d.width, d.height + g.getFontMetrics().getAscent());
		}

		for (int i = 1; einzel != null; i++) {
			Treffer t = einzel.getTreffer(showProbe(), i);
			if (t == null) break;
			drawTreffer(g, t.getX(), t.getY());
		}

		if (treffer != null) drawTreffer(g, treffer.getX(), treffer.getY());
	}

	public void forceProbe() {
		force = PROBE;
	}

	public void forceMatch() {
		force = MATCH;
	}

	public void setTreffer(Treffer treffer) {
		this.treffer = treffer;
		repaint();
	}

	private boolean showProbe() {
		if (treffer != null) return false;
		return einzel == null || force == PROBE || (force != MATCH && !einzel.inMatch());
	}

	private void drawTreffer(Graphics g, double x, double y) {
		Dimension d = toPixel(56);
		Point p = toPixel((int) x/10, (int) y/10);
		g.setColor(Color.GREEN);
		g.fillOval(mitteX + p.x - d.width/2, mitteY - p.y - d.height/2, d.width, d.height);
		g.setColor(Color.BLUE);
		g.drawOval(mitteX + p.x - d.width/2, mitteY - p.y - d.height/2, d.width, d.height);
	}

	private Dimension toPixel(int zmm) {		
		Dimension d = new Dimension();
		d.height = zmm * getHeight() / typ.getRing(0);
		d.width = zmm * getWidth() / typ.getRing(0);
		return d;
	}

	private Point toPixel(int x, int y) {
		Point p = new Point();
		p.y = y * getHeight() / typ.getRing(0);
		p.x = x * getWidth() / typ.getRing(0);
		return p;
	}

	@Override
	public void lineChanged(LineModel lm, int type) {
		if (type == DefaultLineModel.RESULT_CHANGED) {
			einzel = lm.getResult();
			repaint();
		}
	}
}