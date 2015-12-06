package model;

public enum TargetFill {
	NONE(     0, "Keiner"),
	INNER_TEN(1, "Innenzehner"),
	TEN(      2, "Zehner");
	
	private int value;
	private String name;
	
	private TargetFill(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() { 
		return value;
	}

	@Override
	public String toString() {
		return name;
	}
}