package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.apache.commons.lang.Validate;

import model.DefaultLineModel;
import model.Unit;
import model.Single;
import model.LineListener;
import model.LineModel;
import model.Rule;
import model.TargetModel;
import model.Hit;
import model.Weapon;


@SuppressWarnings("serial")
public class Target extends JPanel implements LineListener {
	private final int PROBE = 1;
	private final int MATCH = 2;

	private TargetModel target;
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
							new Weapon( 3, "Kleinkaliber", 5600, Unit.MM, 2)
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
		setRule(single.getDisziplin().getRegel());
	}
	
	private void init() {
		this.target = null;
		this.weapon = null;
		this.single = null;
		this.hit = null;
		this.number = 0;
		this.force = 0;
	}

	public void setTarget(TargetModel target) {
		Validate.notNull(target, "target can't be null");
		this.target = target;
		repaint();
	}

	public void setRule(Rule rule) {
		Validate.notNull(rule, "rule can't be null");
		this.target = rule.getScheibe();
		this.weapon = rule.getWaffe();
		repaint();
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

		if (number > 0) {
			g1.setFont(new Font("Arial", Font.BOLD, 128));
			g1.drawString("" + number, 25, 25 + g1.getFontMetrics().getAscent());
		}
		
		if (showProbe()) {
			g1.fillPolygon(new int[] {700, 975, 975}, new int[] {25, 25, 300}, 3);
		}

		int r;
		int mitte = target.getRingRadius(0);;

		Graphics2D g2 = (Graphics2D) g.create();
		g2.scale(new Double(getWidth()) / new Double(2 * mitte), new Double(getHeight()) / new Double(2 * mitte));

		g2.setColor(Color.BLACK);

		r =  target.getSpiegelRadius();
	    g2.fillOval(mitte - r, mitte - r, 2 * r, 2 * r);

		for (int i = 1; i <= 10; i++) {
			r = target.getRingRadius(i);
		    g2.setColor(r > target.getSpiegelRadius() ? Color.BLACK : Color.WHITE);
		    if (i == 10 && target.getStyle() > 1) {
		    	g2.fillOval(mitte - r, mitte - r, 2 * r, 2 * r);
		    } else {
		    	g2.drawOval(mitte - r, mitte - r, r * 2, r * 2);
		    }
		}

		if (target.getInnenZehnRadius() > 0) {
			r = target.getInnenZehnRadius();
		    g2.setColor(Color.WHITE);
		    if (target.getStyle() > 0) {
		    	g2.fillOval(mitte - r, mitte - r, 2 * r, 2 * r);
		    } else {
		    	g2.drawOval(mitte - r, mitte - r, 2 * r, 2 * r);
		    }
		}

		g2.setFont(new Font("Arial", Font.BOLD, target.getFontSize()));
		int dy = g2.getFontMetrics().getAscent() / 2;
		int dx = (int) (g2.getFontMetrics().getStringBounds("0", g2).getWidth() / -2) + 1;
		for (int i = 1; target.drawNumber(i); i++) {
			g2.setColor(target.blackNumber(i) ? Color.BLACK : Color.WHITE);
			String s = "" + i;
			r = target.getNumberRadius(i);
			g2.drawString(s, mitte - r + dx, mitte + dy);	// Links
			g2.drawString(s, mitte + dx, mitte - r + dy);	// Oben
			g2.drawString(s, mitte + r + dx , mitte + dy);	// Rechts
			g2.drawString(s, mitte + dx, mitte + r + dy);	// Unten
		}

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
		g.setColor(Color.GREEN);
		g.fillOval(mitte + nx - r, mitte - ny - r, 2 * r, 2 * r);
		g.setColor(Color.BLUE);
		g.drawOval(mitte + nx - r, mitte - ny - r, 2 * r, 2 * r);
	}

	@Override
	public void lineChanged(LineModel lm, int type) {
		if (type == DefaultLineModel.RESULT_CHANGED) {
			single = lm.getResult();
			target = single.getDisziplin().getRegel().getScheibe();
			weapon = single.getDisziplin().getRegel().getWaffe();
			repaint();
		}
	}
}