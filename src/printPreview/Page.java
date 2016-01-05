package printPreview;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Page extends JPanel {
	private Printable p;
	private PageFormat pf;
	private int scale;
	private int index;

	public Page(Printable p, PageFormat pf, int index) {
		this.p = p;
		this.pf = pf;
		this.scale = 100;
		this.index = index;
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createRaisedBevelBorder());
		setSize();
	}

	public void setPageScale(int value) {
		this.scale = value;
		setSize();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Insets i = this.getInsets();
		int width = getWidth() - i.left - i.right;
		int height = getHeight() - i.top - i.bottom;
		Graphics2D g2 = (Graphics2D) g.create(i.left, i.top, width, height);
		g2.scale(width / pf.getWidth(), height / pf.getHeight());
		try {
			p.print(g2, pf, index);
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}

	private void setSize() {
		setSize((int) pf.getWidth() * scale / 100 + getInsets().left + getInsets().right,
				(int) pf.getHeight() * scale / 100 + getInsets().top + getInsets().bottom
		);
	}
}