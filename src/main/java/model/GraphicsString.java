package main.java.model;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;


public class GraphicsString {

	private Graphics2D g;
	private int size;
	private boolean underline;
	private boolean bold;
	private Color color;

	public GraphicsString(Graphics2D g) {
		this.g = g;
		setFormat(40, false, false, Color.BLACK);
	}

	public GraphicsString(Graphics2D g, int size, boolean underline, boolean bold, Color color) {
		this.g = g;
		setFormat(size, underline, bold, color);
	}

	public void setFormat(int size, boolean underline, boolean bold, Color color) {
		this.size = size;
		this.underline = underline;
		this.bold = bold;
		this.color = color;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void drawStringLeft(String s, int x, int y) {
		g.drawString(formatString(s), x , y);
	}

	public void drawStringCenter(String s, int x, int y) {
		AttributedCharacterIterator aci = formatString(s);
		int len = getStringWidth(aci);
		g.drawString(aci, x - len / 2, y);
	}
	
	public void drawStringRight(String s, int x, int y) {
		AttributedCharacterIterator aci = formatString(s);
		int len = getStringWidth(aci);
		g.drawString(aci, x - len - 8, y);
	}

	private AttributedCharacterIterator formatString(String s) {
		AttributedString as = new AttributedString(s);
		as.addAttribute(TextAttribute.FONT, (new Font("Bitstream Vera Sans", bold ? Font.BOLD : Font.PLAIN, size)));
		if (underline) as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		as.addAttribute(TextAttribute.FOREGROUND, color);
		return as.getIterator();
	}

	private int getStringWidth(AttributedCharacterIterator s) {
	    FontRenderContext fontRenderContext = g.getFontRenderContext();
	    LineBreakMeasurer lbm = new LineBreakMeasurer(s, fontRenderContext);
	    TextLayout textLayout = lbm.nextLayout(Integer.MAX_VALUE);
	    return (int) textLayout.getBounds().getWidth();
	}
}