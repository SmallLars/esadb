package model;

import java.awt.Graphics;
import java.io.Serializable;


public abstract class Start implements Serializable, Comparable<Start> {
	private static final long serialVersionUID = 1L;

	public abstract Disziplin getDisziplin();

	public abstract int lineCount();

	public abstract void draw(Graphics g, int platz);
}