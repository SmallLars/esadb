package model;


import java.awt.Graphics2D;
import java.io.Serializable;


public abstract class Start implements Serializable, Comparable<Start> {
	private static final long serialVersionUID = 2L;

	public abstract Discipline getDisziplin();

	public abstract Group getGroup();

	public abstract int lineCount();

	public abstract void draw(Graphics2D g, int platz);
}