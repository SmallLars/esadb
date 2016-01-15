package model;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;


public class ResultList implements Printable {

	final static int pageWidth = 2000;

	private String filename;
	private String date;
	private ArrayList<Entry> entrys;
	private ArrayList<Integer> pages;

	private static final int VSPACE = 0;
	private static final int NEWPAGE = 1;
	private static final int DISCIPLINE = 2;
	private static final int GROUP = 3;
	private static final int RESULT_TEAM = 4;
	private static final int RESULT_SINGLE = 5;	

	private static final int HEADLINE_HEIGHT = 100;
	private static final int DISCIPLINE_HEIGHT = 70;
	private static final int GROUP_HEIGHT = 60;
	//private static final int RESULT_TEAM_HEIGHT = 49;
	private static final int RESULT_SINGLE_HEIGHT = 49;

	public ResultList(String filename, String date) {
		this.filename = filename;
		this.date = date;
		this.entrys = new ArrayList<Entry>();
		this.pages = new ArrayList<Integer>();
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (entrys.size() == 0) return Printable.NO_SUCH_PAGE;

		final double SCALE = pageWidth / pageFormat.getImageableWidth();
		final int pageHeight = (int) (pageFormat.getImageableHeight() * SCALE);

		Graphics2D g = (Graphics2D) graphics;
		g.scale(1.0 / SCALE, 1.0 / SCALE);
		g.translate(pageFormat.getImageableX() * SCALE, pageFormat.getImageableY() * SCALE);

		if (pageIndex == 0) {
			pages.clear();
			pages.add(0);

			int usedHeight = HEADLINE_HEIGHT;
			
			for (int i = 0; i < entrys.size(); i++) {
				Entry e = entrys.get(i);
				usedHeight += e.height;

				if (e.type == VSPACE || e.type == NEWPAGE) continue;

				int forwardCheck = 0;
				if (e.type == DISCIPLINE || e.type == GROUP) {
					for (int c = i + 1; c < entrys.size(); c++) {
						Entry e1 = entrys.get(c);
						forwardCheck += e1.height;
						if (e1.type == RESULT_SINGLE || e1.type == RESULT_TEAM) break;
					}
				}

				if (usedHeight + forwardCheck >= pageHeight) {
					usedHeight = HEADLINE_HEIGHT + e.height;
					pages.add(i);
				}
			}
		}

		if (pageIndex >= pages.size()) return Printable.NO_SUCH_PAGE;

		int number = 0;
		for (int i = pages.get(pageIndex); i >= 0; i--) {
			int type = entrys.get(i).type;
			if (type == RESULT_SINGLE || type == RESULT_TEAM) {
				number++;
				continue;
			}
			break;
		}
		int nextPage = pageIndex + 1;
		drawHeadline(g, nextPage);
		g.translate(0, HEADLINE_HEIGHT);

		int lastEntry = (nextPage == pages.size() ? entrys.size() : pages.get(nextPage));
		for (int i = pages.get(pageIndex); i < lastEntry; i++) {
			Entry e = entrys.get(i);
			switch (e.type) {
				case DISCIPLINE:
					drawDiszipline(g, e.value);
					break;
				case GROUP:
					drawGroup(g, e.value);
					number = 1;
					break;
				case RESULT_SINGLE:
					if (number > 1) {
						if (((Start) e.value).compareTo((Start) entrys.get(i - 1).value) == 0) {
							// Es liegt völlige Gleicheit vor: Gleiche Platzierung
							number--;
						}
					}
					drawSingleResult(g, e.value, number++);
					if (i != pages.get(pageIndex) && number > 2) g.drawLine(0, 10, 2000, 10);
					g.drawLine(700, 10, 700, e.height + 10);
					g.drawLine(1700, 10, 1700, e.height + 10);
					break;
			}
			// VSPACE wir nur erzeugt, falls er nicht der erste Eintrag auf einer Seite ist.
			if (e.type != VSPACE || i != pages.get(pageIndex)) g.translate(0, e.height);
		}

		return Printable.PAGE_EXISTS;
	}

	public void addNewPage() {
		if (entrys.size() > 0) entrys.add(new Entry(NEWPAGE, null, Integer.MAX_VALUE / 2));
	}

	public void addDiszipline(String name) {
		if (entrys.size() > 0) entrys.add(new Entry(VSPACE, null, 100));
		entrys.add(new Entry(DISCIPLINE, name, DISCIPLINE_HEIGHT));
	}

	public void addGroup(String name) {
		entrys.add(new Entry(GROUP, name, GROUP_HEIGHT));
	}

	public void addSingleResult(Start start) {
		entrys.add(new Entry(RESULT_SINGLE, start, start.lineCount() * RESULT_SINGLE_HEIGHT));
	}

	private void drawHeadline(Graphics2D g, int page) {
		drawStringLeft(g, formatString(filename, 32, false, false, Color.BLACK), 0, 32);
		drawStringCenter(g, formatString(date, 32, false, false, Color.BLACK), pageWidth / 2, 32);
		drawStringRight(g, formatString(page + " / " + pages.size(), 32, false, false, Color.BLACK), pageWidth, 32);
	}

	private void drawDiszipline(Graphics2D g, Object diszipline) {
		drawStringCenter(g, formatString("---- " + (String) diszipline + " ----", 60, false, true, Color.BLACK), pageWidth / 2, 60);
	}

	private void drawGroup(Graphics2D g, Object group) {
		drawStringLeft(g, formatString((String) group, 45, true, true, Color.BLACK), 0, 56);
	}

	private void drawSingleResult(Graphics2D g, Object start, int number) {
		g.setFont(new Font("Bitstream Vera Sans", Font.PLAIN, 40));
		((Start) start).draw(g, number);
	}

	private void drawStringLeft(Graphics2D g, AttributedCharacterIterator s, int x, int y) {
		g.drawString(s, x , y);
	}

	private void drawStringCenter(Graphics2D g, AttributedCharacterIterator s, int x, int y) {
		int len = getStringWidth(g, s);
		g.drawString(s, x - len / 2, y);
	}
	
	private void drawStringRight(Graphics2D g, AttributedCharacterIterator s, int x, int y) {
		int len = getStringWidth(g, s);
		g.drawString(s, x - len - 8, y);
	}

	private int getStringWidth(Graphics2D g, AttributedCharacterIterator s) {
	    FontRenderContext fontRenderContext = g.getFontRenderContext();
	    LineBreakMeasurer lbm = new LineBreakMeasurer(s, fontRenderContext);
	    TextLayout textLayout = lbm.nextLayout(Integer.MAX_VALUE);
	    return (int) textLayout.getBounds().getWidth();
	}

	private AttributedCharacterIterator formatString(String s, int size, boolean underline, boolean bold, Color color) {
		AttributedString as = new AttributedString(s);
		as.addAttribute(TextAttribute.FONT, (new Font("Bitstream Vera Sans", bold ? Font.BOLD : Font.PLAIN, size)));
		if (underline) as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		as.addAttribute(TextAttribute.FOREGROUND, color);
		return as.getIterator();
	}

	private class Entry {
		public int type;
		public Object value;
		public int height;

		public Entry(int type, Object value, int height) {
			this.type = type;
			this.value = value;
			this.height = height;
		}
	}
}