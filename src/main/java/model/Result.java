package model;


import java.awt.Graphics2D;
import java.io.Serializable;


public abstract class Result implements Serializable, Comparable<Result> {
	private static final long serialVersionUID = 2L;

	public abstract String getName();

	public abstract Discipline getDisziplin();

	public abstract Group getGroup(boolean useSettings);

	public abstract float getResult(boolean probe);

	public abstract int lineCount();

	public abstract void draw(Graphics2D g, int platz, int fontsize, int lineheight, boolean overline);
}