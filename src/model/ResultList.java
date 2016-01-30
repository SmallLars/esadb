package model;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
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
	private static final int GROUP_HEIGHT = 80;
	private static final int RESULT_TEAM_HEIGHT = 49;
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
					break;
				case RESULT_SINGLE:
					drawSingleResult(g, e.value, e.number);
					if (i != pages.get(pageIndex) && entrys.get(i - 1).type == RESULT_SINGLE) g.drawLine(0, 10, 2000, 10);
					g.drawLine(725, 10, 725, e.height + 10);
					g.drawLine(1700, 10, 1700, e.height + 10);
					break;
				case RESULT_TEAM:
					drawTeamResult(g, e.value, e.number);
					if (i != pages.get(pageIndex) && entrys.get(i - 1).type == RESULT_TEAM) g.drawLine(350, 10, 1650, 10);
					g.drawLine(1350, 10, 1350, e.height + 10);
					break;
			}
			// VSPACE wir nur erzeugt, falls er nicht der erste Eintrag auf einer Seite ist.
			if (e.type != VSPACE || i != pages.get(pageIndex)) g.translate(0, e.height);
		}

		return Printable.PAGE_EXISTS;
	}

	public void addNewPage() {
		if (entrys.size() > 0) entrys.add(new Entry(NEWPAGE, 0, null, Integer.MAX_VALUE / 2));
	}

	public void addDiszipline(String name) {
		if (entrys.size() > 0) entrys.add(new Entry(VSPACE, 0, null, 100));
		entrys.add(new Entry(DISCIPLINE, 0, name, DISCIPLINE_HEIGHT));
	}

	public void addGroup(String name) {
		entrys.add(new Entry(GROUP, 0, name, GROUP_HEIGHT));
	}

	public void addSingleResult(Result start) {
		if (entrys.get(entrys.size() - 1).type == RESULT_TEAM) entrys.add(new Entry(VSPACE, 0, null, 100));
		int number = 1;
		if (entrys.size() > 0) {
			Entry e = entrys.get(entrys.size() - 1);
			if (e.type == RESULT_SINGLE) {
				number = e.number;
				if (((Result) e.value).compareTo(start) != 0) number++;
			}
		}
		entrys.add(new Entry(RESULT_SINGLE, number, start, start.lineCount() * RESULT_SINGLE_HEIGHT));
	}

	public void addTeamResult(Result start) {
		int number = 1;
		if (entrys.size() > 0) {
			Entry e = entrys.get(entrys.size() - 1);
			if (e.type == RESULT_TEAM) {
				number = e.number;
				if (((Result) e.value).compareTo(start) != 0) number++;
			}
		}
		entrys.add(new Entry(RESULT_TEAM, number, start, start.lineCount() * RESULT_TEAM_HEIGHT));
	}

	private void drawHeadline(Graphics2D g, int page) {
		GraphicsString gs = new GraphicsString(g, 32, false, false, Color.BLACK);
		gs.drawStringLeft(filename, 0, 32);
		gs.drawStringCenter(date, pageWidth / 2, 32);
		gs.drawStringRight(page + " / " + pages.size(), pageWidth, 32);
	}

	private void drawDiszipline(Graphics2D g, Object diszipline) {
		GraphicsString gs = new GraphicsString(g, 60, false, true, Color.BLACK);
		gs.drawStringCenter("---- " + (String) diszipline + " ----", pageWidth / 2, DISCIPLINE_HEIGHT - 10);
	}

	private void drawGroup(Graphics2D g, Object group) {
		GraphicsString gs = new GraphicsString(g,  45, true, true, Color.BLACK);
		gs.drawStringLeft((String) group, 0, GROUP_HEIGHT - 4);
	}

	private void drawSingleResult(Graphics2D g, Object start, int number) {
		g.setFont(new Font("Bitstream Vera Sans", Font.PLAIN, 40));
		((Result) start).draw(g, number, 40, RESULT_SINGLE_HEIGHT);
	}

	private void drawTeamResult(Graphics2D g, Object start, int number) {
		((Result) start).draw(g, number, 40, RESULT_TEAM_HEIGHT);
	}

	private class Entry {
		public int type;
		public int number;
		public Object value;
		public int height;

		public Entry(int type, int number, Object value, int height) {
			this.type = type;
			this.number = number;
			this.value = value;
			this.height = height;
		}
	}
}