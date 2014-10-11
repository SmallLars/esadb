package view;

public enum Kaliber {
	LGLP(450),
	KK(560);

	private final int radius;
	
	Kaliber(int durchmesser) {
		this.radius = durchmesser / 2;
	}

	public int getRadius() {
		return radius;
	}
}