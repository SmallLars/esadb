package View;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;


@SuppressWarnings("serial")
public class Seitenannzeige extends JPanel {
	Printable printable;
	private PageFormat pageFormat;
	private int scale;

	private Vector<Seite> seiten;
	
	public Seitenannzeige(Printable printable, PageFormat pageFormat) {
		super();

		this.printable = printable;
		this.pageFormat = pageFormat;
		this.scale = 100;

		seiten = new Vector<Seite>();

		setLayout(null);
		setBackground(Color.LIGHT_GRAY);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		MouseInputAdapter dragScroll = new DragScroll(this);
		addMouseMotionListener(dragScroll);
		addMouseListener(dragScroll);
	}

	public void setPageFormat(PageFormat pageFormat) {
		this.pageFormat = pageFormat;
		seiten.clear();
		repaint();
	}

	public void setScale(int scale) {
		this.scale = scale;
		Seite s = null;
		for (int i = 0; i < seiten.size(); i++) {
			s = seiten.get(i);
			s.setPageScale(scale);
			s.setLocation(10 + i * (s.getWidth() + 10), 10);
		}
		if (s == null) {
			setPreferredSize(new Dimension(20, 20));
		} else {
			setPreferredSize(new Dimension(10 + seiten.size() * (s.getWidth() + 10), s.getHeight() + 20));
		}
		getParent().revalidate();
	}

	@Override
	protected void paintComponent(Graphics g) {
		int anzahl;
		for (anzahl = 0; true; anzahl++) {
			int c = Printable.NO_SUCH_PAGE;
			if (printable != null) {
				try {
					c = printable.print(g.create(0, 0, 0, 0), pageFormat, anzahl);
				} catch (PrinterException e) {
					e.printStackTrace();
				}
			}
			if (c == Printable.NO_SUCH_PAGE) break;
		}
		if (anzahl != seiten.size()) {
			removeAll();
			seiten.clear();

			for (int i = 0; i < anzahl; i++) {
				Seite s = new Seite(printable, pageFormat, i);
				add(s);
				seiten.add(s);
			}
		}
		setScale(scale);
		super.paintComponent(g);
	}
}