package model;


public enum ScheibeTyp {
	LUFTGEWEHR(3050, 4550, 0, 250, 1, 8),
	LUFTPISTOLE(5950, 15550, 500, 800, 1, 8),
	GEWEHR50M(11240, 15440, 500, 800, 1, 8),
	GEWEHR100M(20000, 50000, 2500, 2500, 1, 9);

	private final int radius_spiegel;
	private final int radius_aussen;
	private final int radius_innenzehn;
	private final int ringbreite;
	private final int min_ring;
	private final int max_number;

	ScheibeTyp(int durchmesser_spiegel, int durchmesser_aussen, int durchmesser_innenzehn, int ringbreite, int min_ring, int max_number) {
		this.radius_spiegel = durchmesser_spiegel / 2;
		this.radius_aussen = durchmesser_aussen / 2;
		this.radius_innenzehn = durchmesser_innenzehn / 2;
		this.ringbreite = ringbreite;
		this.min_ring = min_ring;
		this.max_number = max_number;
	}

	public int getRingRadius(int i) {
		if (i < 0 || i > 10) return 0;
	
		return radius_aussen - (i - min_ring) * ringbreite;
	}

	public int getAussenRadius() {
		return radius_aussen;
	}
	
	public int getSpiegelRadius() {
		return radius_spiegel;
	}

	public int getRingBreite() {
		return ringbreite;
	}
	
	public int getInnenZehnRadius() {
		return radius_innenzehn;
	}
	
	public int getFontSize() {
		return (ringbreite) * 9 / 16;
	}

	public boolean drawRing(int i) {
		return i >= min_ring;
	}
	
	public boolean drawNumber(int i) {
		return i >= min_ring && i <= max_number;
	}

	public int getNumberRadius(int i) {
		return (getRingRadius(i) - ringbreite * 3 / 8);
	}

	public boolean blackNumber(int i) {
		return getNumberRadius(i) > radius_spiegel;
	}
}
