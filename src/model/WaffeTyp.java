package model;

public enum WaffeTyp {
	LUFTDRUCK(450),
	KLEINKALIBER(560);

	private final int radius;
	
	WaffeTyp(int durchmesser) {
		this.radius = durchmesser / 2;
	}

	public int getRadius() {
		return radius;
	}
}