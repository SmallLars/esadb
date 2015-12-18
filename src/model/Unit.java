package model;

public enum Unit {
	MM("mm"),
	INCH("Inch");

	private String name;

	private Unit(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}