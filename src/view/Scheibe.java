package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

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

	private ScheibeTyp typ;
	private Einzel einzel;
	private Treffer treffer;

	private int nummer;
	private int force;

	// TrefferAdd
	public Scheibe(ScheibeTyp typ) {
		this(null, 0);
		this.typ = typ;
	}

	// Einzel im Druck mit this
	public Scheibe(Einzel einzel) {
		this(einzel, 0);
	}

	// GUI
	/**
	 * @wbp.parser.constructor
	 */
	public Scheibe(int nummer) {
		this(null, nummer);
	}

	private Scheibe(Einzel einzel, int nummer) {
		this.einzel = einzel;
		this.treffer = null;
		this.nummer = nummer;
		this.force = 0;
		updateTyp();
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

		Graphics2D g1 = (Graphics2D) g.create();
		g1.scale(new Double(getWidth()) / 1000, new Double(getHeight()) / 1000);
		g1.setColor(Color.BLACK);

		if (nummer > 0) {
			g1.setFont(new Font("Arial", Font.BOLD, 128));
			g1.drawString("" + nummer, 25, 25 + g1.getFontMetrics().getAscent());
		}
		
		if (showProbe()) {
			g1.fillPolygon(new int[] {700, 975, 975}, new int[] {25, 25, 300}, 3);
		}

		int r;
		int mitte = typ.getRingRadius(0);;

		Graphics2D g2 = (Graphics2D) g.create();
		g2.scale(new Double(getWidth()) / new Double(2 * mitte), new Double(getHeight()) / new Double(2 * mitte));

		g2.setColor(Color.BLACK);

		r =  typ.getSpiegelRadius();
	    g2.fillOval(mitte - r, mitte - r, 2 * r, 2 * r);

		for (int i = 1; i <= 10; i++) {
			r = typ.getRingRadius(i);
		    g2.setColor(r > typ.getSpiegelRadius() ? Color.BLACK : Color.WHITE);
	    	g2.drawOval(mitte - r, mitte - r, r * 2, r * 2);
		}

		if (typ.getInnenZehnRadius() > 0) {
			r = typ.getInnenZehnRadius();
		    g2.setColor(Color.WHITE);
		    g2.drawOval(mitte - r, mitte - r, 2 * r, 2 * r);
		}

		g2.setFont(new Font("Arial", Font.BOLD, typ.getFontSize()));
		int dy = g2.getFontMetrics().getAscent() / 2;
		int dx = (int) (g2.getFontMetrics().getStringBounds("0", g2).getWidth() / -2) + 1;
		for (int i = 1; typ.drawNumber(i); i++) {
			g2.setColor(typ.blackNumber(i) ? Color.BLACK : Color.WHITE);
			String s = "" + i;
			r = typ.getNumberRadius(i);
			g2.drawString(s, mitte - r + dx, mitte + dy);	// Links
			g2.drawString(s, mitte + dx, mitte - r + dy);	// Oben
			g2.drawString(s, mitte + r + dx , mitte + dy);	// Rechts
			g2.drawString(s, mitte + dx, mitte + r + dy);	// Unten
		}

		for (int i = 1; einzel != null; i++) {
			Treffer t = einzel.getTreffer(showProbe(), i);
			if (t == null) break;
			drawTreffer(g2, t, mitte);
		}

		if (treffer != null) drawTreffer(g2, treffer, mitte);
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

	private void drawTreffer(Graphics g, Treffer t, int mitte) {
		int r = typ.getSchussRadius();
		int nx = (int) (t.getX());
		int ny = (int) (t.getY());
		g.setColor(Color.GREEN);
		g.fillOval(mitte + nx - r, mitte - ny - r, 2 * r, 2 * r);
		g.setColor(Color.BLUE);
		g.drawOval(mitte + nx - r, mitte - ny - r, 2 * r, 2 * r);
	}

	private void updateTyp() {
		if (einzel != null) {
			typ = ScheibeTyp.getTypByGattung(einzel.getDisziplin().getWaffengattung());
		} else if (typ == null) {
			typ = ScheibeTyp.KK50M;
		}
	}

	@Override
	public void lineChanged(LineModel lm, int type) {
		if (type == DefaultLineModel.RESULT_CHANGED) {
			einzel = lm.getResult();
			updateTyp();
			repaint();
		}
	}
}