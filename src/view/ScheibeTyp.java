package view;


public enum ScheibeTyp {
	LG10M(3050, 4550, 500, 0, 8, 450),
	LP10M(5950, 15550, 1600, 500, 8, 450),
	KK50M(11240, 15440, 1600, 500, 8, 560),
	KK100M(20000, 50000, 5000, 2500, 9, 560);

	private final int radius_spiegel;
	private final int radius_aussen;
	private final int radius_step;
	private final int radius_innenzehn;
	private final int maxNumber;
	private final int radius_schuss;

	ScheibeTyp(int durchmesser_spiegel, int durchmesser_aussen, int durchmesser_step, int durchmesser_innenzehn, int maxNumber, int durchmesser_schuss) {
		this.radius_spiegel = durchmesser_spiegel / 2;
		this.radius_aussen = durchmesser_aussen / 2;
		this.radius_step = durchmesser_step / 2;
		this.radius_innenzehn = durchmesser_innenzehn / 2;
		this.maxNumber = maxNumber;
		this.radius_schuss = durchmesser_schuss / 2;
	}

	public int getRingRadius(int i) {
		if (i < 0 || i > 10) return 0;
	
		return radius_aussen - (i - 1) * radius_step;
	}
	
	public int getSpiegelRadius() {
		return radius_spiegel;
	}
	
	public int getInnenZehnRadius() {
		return radius_innenzehn;
	}
	
	public int getFontSize() {
		return (radius_step) * 9 / 16;
	}
	
	public boolean drawNumber(int i) {
		return i <= maxNumber;
	}

	public int getNumberRadius(int i) {
		return (getRingRadius(i) - radius_step * 3 / 8);
	}

	public boolean blackNumber(int i) {
		return getNumberRadius(i) > radius_spiegel;
	}

	public int getSchussRadius() {
		return radius_schuss;
	}

	public float getValuebyRadius(double radius) {
		// TODO funktioniert eventuell nur für KK50M
		float v = (int) (110 - radius * 10 / radius_step) / 10f;
		if (v < 1) return 0.0f;
		if (v > 10.9) return 10.9f;
		return v;
	}

	public double getRadiusByValue(float value) {
		// TODO funktioniert eventuell nur für KK50M
		return Math.round(radius_aussen + radius_step + radius_schuss - value * radius_step);
	}

	public boolean isInnenZehn(double radius) {
		return radius <= radius_innenzehn + radius_schuss;
	}

	public static ScheibeTyp getTypByGattung(String s) {
		switch (s) {
			case "1.40": return KK50M;
			case "1.35": return KK100M;
			default:  return KK50M;
		}
	}
}
