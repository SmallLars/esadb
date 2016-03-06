package main.java.model;

public enum Unit {
	MM("mm"),
	INCH("inch");

	private String name;

	private Unit(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}