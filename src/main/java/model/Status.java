package main.java.model;

public enum Status {
	INIT("0"),
	SPERREN("1"),
	ENTSPERREN("2"),
	START("3"),
	STOP("4"),
	FREI("5"),
	UNKNOWN("6"),
	UPDATE("7"),
	WERTUNG("8"),
	PROBE("9"),
	VISIONCTL("\"H014\""),
	SHUTDOWN("\"99  \""),
	ESADBCTL("\"060\"");

	private final String code;

	Status(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}