package model;

public enum TargetType {
	RING(0, "Ringscheibe"),
	Klapp(1, "Trefferzonenscheibe mit Klappscheibensteuerung"),
	JAGD(2, "Trefferzonenscheibe / Jagdscheibe"),
	WEIß(3, "Ringscheibe mit weißem Zehner und schwarzer Schrift"),
	PA25PC(4, "Ringscheibe mit PA25PC - Modul"),
	INVERS(5, "Inverse Ringscheibe"),
	DOPPELSAU(6, "Trefferzonenscheibe mit Doppelsau");

	private int value;
	private String name;

	private TargetType(int value, String name) {
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