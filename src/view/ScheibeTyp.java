package view;

import java.awt.Polygon;

public enum ScheibeTyp {
	LG10M(305, 455, 50, 0, 8, 45),
	LP10M(595, 1555, 160, 50, 8, 45),
	KK50M(1124, 1544, 160, 50, 8, 56),
	KK100M(2000, 5000, 500, 250, 9, 56);

	private final int durchmesser_spiegel;
	private final int durchmesser_aussen;
	private final int durchmesser_step;
	private final int durchmesser_innenzehn;
	private final int maxNumber;
	private final int durchmesser_schuss;

	ScheibeTyp(int durchmesser_spiegel, int durchmesser_aussen, int durchmesser_step, int durchmesser_innenzehn, int maxNumber, int durchmesser_schuss) {
		this.durchmesser_spiegel = durchmesser_spiegel;
		this.durchmesser_aussen = durchmesser_aussen;
		this.durchmesser_step = durchmesser_step;
		this.durchmesser_innenzehn = durchmesser_innenzehn;
		this.maxNumber = maxNumber;
		this.durchmesser_schuss = durchmesser_schuss;
	}

	public int getRing(int i) {
		if (i < 0 || i > 10) return 0;
	
		return durchmesser_aussen - (i - 1) * durchmesser_step;
	}
	
	public int getSpiegel() {
		return durchmesser_spiegel;
	}
	
	public int getInnenZehn() {
		return durchmesser_innenzehn;
	}
	
	public int getFontSize() {
		return (durchmesser_step) * 9 / 32;
	}
	
	public boolean drawNumber(int i) {
		return i <= maxNumber;
	}

	public int getNumberRadius(int i) {
		return (getRing(i) - durchmesser_step * 3 / 8) / 2;
	}

	public boolean blackNumber(int i) {
		return getNumberRadius(i) > durchmesser_spiegel / 2;
	}

	public Polygon getProbe() {
		int r = durchmesser_step / 2;
		return new Polygon(new int[]{getRing(0) - r, getRing(0) - r, getRing(0) - r * 6}, new int[]{r, r * 6, r}, 3);
	}

	public int getSchuss() {
		return durchmesser_schuss;
	}
}
