package view;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.commons.lang.Validate;

import controller.Controller;
import model.DefaultLineModel;
import model.Hit;
import model.LineListener;
import model.LineModel;
import model.Rule;
import model.Single;
import model.TargetModel;
import model.TargetValue;
import model.Unit;
import model.Weapon;


@SuppressWarnings("serial")
public class Target extends JPanel implements LineListener {
	private static final int PROBE = 1;
	private static final int MATCH = 2;

	private TargetModel model;
	private Weapon weapon;
	private Single single;
	private Hit hit;

	private int number;
	private int force;

	/**
	 * @wbp.parser.constructor
	 */
	public Target() {
		this(	new Rule(	"Kleinkaliber 50m",  "1.40",
							new TargetModel("Gewehr 50m", "0.4.3.03", 55000, 5, 11240, 15440, 500, 800, 1, 10, 8),
							new Weapon("Kleinkaliber",  "03", 5600, Unit.MM, 2)
				),
				1
		);
		setSize(400, 400);
	}

	public Target(TargetModel target) {
		Validate.notNull(target, "target can't be null");
		init();
		setTarget(target);
		
	}

	public Target(Rule type) {
		Validate.notNull(type, "typ can't be null");
		init();
		setRule(type);
	}

	public Target(Rule type, int number) {
		this(type);
		this.number = number;
	}

	public Target(Single single) {
		Validate.notNull(single, "single can't be null");
		init();
		this.single = single;
		setRule(Controller.get().getRule(single.getDisziplin().getRuleNumber()));
	}
	
	private void init() {
		this.model = null;
		this.weapon = null;
		this.single = null;
		this.hit = null;
		this.number = 0;
		this.force = 0;
	}

	public void setTarget(TargetModel model) {
		Validate.notNull(model, "model can't be null");
		this.model = model;
		repaint();
	}

	public void setRule(Rule rule) {
		Validate.notNull(rule, "rule can't be null");
		this.model = rule.getScheibe();
		this.weapon = rule.getWaffe();
		repaint();
	}

	public Point getPointForPixel(int x, int y) {
		x = x - getWidth() / 2;
		y = (y - getHeight() / 2) * -1;
		
		int scale = 0;
		int ox = 0;
		int oy = 0;
		if (model.isDeerTarget()) {
			scale = Math.max(model.getValue(TargetValue.SIZE_WIDTH), model.getValue(TargetValue.SIZE_HEIGHT));
			ox = model.getValue(TargetValue.OFFSET_X);
			oy = model.getValue(TargetValue.OFFSET_Y);
		}
		if (model.isRingTarget()) {
			scale = (model.getRingRadius(model.getValue(TargetValue.RING_MIN)) + model.getValue(TargetValue.RING_WIDTH)) * 2;
		}

		return new Point(x * scale / getWidth() - ox, y * scale / getHeight() - oy);
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
		g1.scale(((double) getWidth()) / 1000, ((double) getHeight()) / 1000);
		g1.setColor(Color.BLACK);

		Graphics2D g2 = (Graphics2D) g.create();
		int mitte;

		if (model.isDeerTarget()) {
			int width = model.getValue(TargetValue.SIZE_WIDTH);
			int height = model.getValue(TargetValue.SIZE_HEIGHT);
			mitte = Math.max(width, height) / 2;
			g2.scale(((double) getWidth()) / (2 * mitte), ((double) getHeight()) / (2 * mitte));

			String image = model.getImage();
			if (model.getValue(TargetValue.IMAGE) == 0) image = "HZ_" + image;
			try {
				BufferedImage bi = ImageIO.read(new File(Controller.getPath(image)));
				int dx = width > height ? 0 : (height - width) / 2;
				int dy = height > width ? 0 : (width - height) / 2;
				g2.drawImage(bi, dx, dy, model.getValue(TargetValue.SIZE_WIDTH), model.getValue(TargetValue.SIZE_HEIGHT), null);
			} catch (IOException e) {
				g1.setFont(new Font("Bitstream Vera Sans", Font.BOLD, 64));
				g1.drawString(image, 32, 708);
				g1.drawString("Bilddatei konnte", 32, 876);
				g1.drawString("nicht gefunden werden.", 32, 960);
			}
		} else {
			int r;
			mitte = model.getRingRadius(model.getValue(TargetValue.RING_MIN)) + model.getValue(TargetValue.RING_WIDTH);
			g2.scale(((double) getWidth()) / (2 * mitte), ((double) getHeight()) / (2 * mitte));

			Color black = model.getValue(TargetValue.TYPE) == 5 ? Color.WHITE : Color.BLACK;
			Color white = model.getValue(TargetValue.TYPE) == 5 ? Color.BLACK : Color.WHITE;
			
			g2.setColor(white);
			r =  model.getAussenRadius();
			g2.fillOval(mitte - r, mitte - r, 2 * r, 2 * r);
	
			g2.setColor(black);
			r =  model.getSpiegelRadius();
		    g2.fillOval(mitte - r, mitte - r, 2 * r, 2 * r);
	
			for (int i = model.getValue(TargetValue.RING_MIN); i <= model.getValue(TargetValue.RING_MAX); i++) {
				r = model.getRingRadius(i);
			    g2.setColor(r < model.getSpiegelRadius() ? white : black);
			    if (i == model.getValue(TargetValue.RING_MAX) && model.getValue(TargetValue.FILL) > 1) {
			    	g2.fillOval(mitte - r, mitte - r, 2 * r, 2 * r);
			    } else {
			    	g2.drawOval(mitte - r, mitte - r, r * 2, r * 2);
			    }
			}
	
			if (model.getInnenZehnRadius() > 0) {
				r = model.getInnenZehnRadius();
				g2.setColor(r < model.getSpiegelRadius() ? white : black);
			    if (model.getValue(TargetValue.FILL) > 0) {
			    	g2.fillOval(mitte - r, mitte - r, 2 * r, 2 * r);
			    } else {
			    	g2.drawOval(mitte - r, mitte - r, 2 * r, 2 * r);
			    }
			}
	
			g2.setFont(new Font("Bitstream Vera Sans", Font.BOLD, model.getFontSize()));
			if (model.getValue(TargetValue.NUM_ANGLE) == 0) {
				int dy = g2.getFontMetrics().getAscent() / 2 - 2;
				for (int i = model.getValue(TargetValue.RING_MIN); model.drawNumber(i); i++) {
					int dx = (int) (g2.getFontMetrics().getStringBounds("" + i, g2).getWidth() / -2) + 2;
					g2.setColor(model.blackNumber(i) ? black : white);
					String s = "" + i;
					r = model.getNumberRadius(i);
					g2.drawString(s, mitte - r + dx, mitte + dy);	// Links
					g2.drawString(s, mitte + dx, mitte - r + dy);	// Oben
					g2.drawString(s, mitte + r + dx , mitte + dy);	// Rechts
					g2.drawString(s, mitte + dx, mitte + r + dy);	// Unten
				}
			} else {
				for (int w = 0; w < 4; w++) {
					Graphics2D gn = (Graphics2D) g2.create();
					gn.rotate(Math.PI / 4 + Math.PI / 2 * w, mitte, mitte);
					int dy = gn.getFontMetrics().getAscent() / 2 - 2;
					for (int i = model.getValue(TargetValue.RING_MIN); model.drawNumber(i); i++) {
						int dx = (int) (gn.getFontMetrics().getStringBounds("" + i, gn).getWidth() / -2) + 2;
						gn.setColor(model.blackNumber(i) ? black : white);
						gn.drawString("" + i, mitte + dx, mitte - model.getNumberRadius(i) + dy);
					}
				}
			}
		}

		// Standnummer und Probeecke zeichen
		if (number > 0) {
			g1.setFont(new Font("Bitstream Vera Sans", Font.BOLD, 128));
			g1.drawString("" + number, 25, 25 + g1.getFontMetrics().getAscent());
		}

		if (showProbe()) {
			g1.fillPolygon(new int[] {700, 975, 975}, new int[] {25, 25, 300}, 3);
		}

		// Einen oder mehrere Treffer zeichnen
		for (int i = 1; single != null; i++) {
			Hit t = single.getTreffer(showProbe(), i);
			if (t == null) break;
			drawTreffer(g2, t, mitte);
		}

		if (hit != null) drawTreffer(g2, hit, mitte);
	}

	public void forceProbe() {
		force = PROBE;
	}

	public void forceMatch() {
		force = MATCH;
	}

	public void setHit(Hit hit) {
		this.hit = hit;
		repaint();
	}

	private boolean showProbe() {
		if (hit != null) return false;
		return single == null || force == PROBE || (force != MATCH && !single.inMatch());
	}

	private void drawTreffer(Graphics g, Hit t, int mitte) {
		int r = weapon.getRadius();
		int nx = (int) (t.getX());
		int ny = (int) (t.getY());
		
		int ox = 0;
		int oy = 0;
		if (model.isDeerTarget()) {
			ox = model.getValue(TargetValue.OFFSET_X);
			oy = model.getValue(TargetValue.OFFSET_Y);
		}

		g.setColor(Color.GREEN);
		g.fillOval(mitte + nx + ox - r, mitte - ny - oy - r, 2 * r, 2 * r);
		g.setColor(Color.BLUE);
		g.drawOval(mitte + nx + ox - r, mitte - ny - oy - r, 2 * r, 2 * r);
	}

	@Override
	public void lineChanged(LineModel lm, int type) {
		if (type == DefaultLineModel.RESULT_CHANGED) {
			single = lm.getResult();
			if (single == null) {
				setRule(Controller.get().getStandardRule());
			} else {
				setRule(Controller.get().getRule(single.getDisziplin().getRuleNumber()));
			}
			repaint();
		}
	}
}