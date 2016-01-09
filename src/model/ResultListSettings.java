package model;


import java.io.Serializable;


public class ResultListSettings implements Serializable {
	private static final long serialVersionUID = 1L;

	public boolean genderBased;
	public boolean groupBased;
	public boolean oneDiszipline;
	public int discipline;
	public boolean newPage;

	public ResultListSettings() {
		genderBased = true;
		groupBased = true;
		oneDiszipline = false;
		discipline = 0;
		newPage = false;
	}
}