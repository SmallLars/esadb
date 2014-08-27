package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JPanel;

import model.Treffer;


@SuppressWarnings("serial")
public class Scheibe extends JPanel {
	private final int original = 1704;
	private final int ringe[] = {1544, 1384, 1224, 1124, 1064, 904, 744, 584, 424, 264, 104, 50};

	private int mitteX;
	private int mitteY;
	private Vector<Treffer> treffer;

	public Scheibe() {
		treffer = new Vector<Treffer>();
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

		mitteX = getWidth() / 2;
		mitteY = getHeight() / 2;

		for (int i = 0; i < 12; i++) {
			Dimension d = toPixel(ringe[i]);
		    g.setColor(i < 4 ? Color.BLACK : Color.WHITE);
		    if (i == 3) {
		    	g.fillOval(mitteX - d.width/2, mitteY - d.height/2, d.width, d.height);
		    } else {
		    	g.drawOval(mitteX - d.width/2, mitteY - d.height/2, d.width, d.height);
		    }
		}

		g.setFont(new Font("Arial", Font.BOLD, toPixel(45).height));
		int dy = g.getFontMetrics().getAscent() / 2;
		int dx = (int) (g.getFontMetrics().getStringBounds("0", g).getWidth() / -2) + 1;
		for (int i = 1; i <= 8; i++) {
			String s = "" + i;
			Dimension position = toPixel(30 + 80 * i);
			g.setColor(i < 4 ? Color.BLACK : Color.WHITE);
			g.drawString(s, position.width + dx, mitteY + dy);					// Links
			g.drawString(s, mitteX + dx, position.height + dy);					// Oben
			g.drawString(s, getWidth() - position.width + dx , mitteY + dy);	// Rechts
			g.drawString(s, mitteX + dx, getHeight() - position.height + dy);	// Unten
		}

		for (Treffer t : treffer) {
			drawTreffer(g, t.getX(), t.getY());
		}
	}

	public void showTreffer(Treffer t) {
		treffer.clear();
		treffer.add(t);
		this.repaint();
	}

	public void addTreffer(Treffer t) {
		treffer.add(t);
		this.repaint();
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
		d.height = zmm * getHeight() / original;
		d.width = zmm * getWidth() / original;
		return d;
	}

	private Point toPixel(int x, int y) {
		Point p = new Point();
		p.y = y * getHeight() / original;
		p.x = x * getWidth() / original;
		return p;
	}
}
