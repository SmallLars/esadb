package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;

public enum WaffeTyp {
	LUFTDRUCK      ( 1, "Luftdruck",                  450, 1),
	ZIMMERSTUTZEBN ( 2, "Zimmerstutzen",              465, 1),
	KLEINKALIBER   ( 3, "Kleinkaliber",               560, 2),
	GROﬂKALIBER_B8 ( 5, "Groﬂkalibergewehr (<=8mm)",  800, 5),
	GROﬂKALIBER_P8 ( 6, "Groﬂkalibergewehr (>8mm)",  1000, 6),
	KALIBER30      ( 7, "Kaliber .30",                762, 6),
	KALIBER32      ( 8, "Kaliber .32",                813, 6),
	KALIBER38      ( 9, "Kaliber .38/.357",           965, 6),
	KALIBER10MM    (10, "Kaliber 10mm",              1000, 6),
	KALIBER44      (11, "Kaliber .44",               1118, 6),
	KALIBER45      (12, "Kaliber .45",               1143, 6),
	VORDERLADER    (13, "Vorderlader",               1400, 6);

	private final int kennnummer;
	private final String bezeichnung;
	private final int durchmesser;
	private final int mikofoneinstellung;
	
	WaffeTyp(int kennnummer, String bezeichnung, int durchmesser, int mikofoneinstellung) {
		this.kennnummer = kennnummer;
		this.bezeichnung = bezeichnung;
		this.durchmesser = durchmesser;
		this.mikofoneinstellung = mikofoneinstellung;
	}

	public int getRadius() {
		return durchmesser / 2;
	}

	public String toFile() {
		NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
		format.setGroupingUsed(false);

		String fileName = String.format("0_hw_%02d.def", kennnummer);
		try {
			PrintWriter writer = new PrintWriter(fileName);
			writer.println("\">Bezeichnung\"");
			writer.println(String.format("\"%s\"", bezeichnung));

			writer.println("\">KennNummer\"");
			writer.println(String.format("\"%02d\"", kennnummer));

			writer.println("\">AnzeigeGeschossRadius\"");
			writer.println(String.format("\"%d\"", getRadius()));

			writer.println("\">GeschossDurchmesser\"");
			format.setMinimumFractionDigits(1);
			format.setMaximumFractionDigits(2);
			writer.println(String.format("\"%s\"", format.format(durchmesser / 100d)));

			writer.println("\">KaliberDurchmesser\"");
			format.setMinimumFractionDigits(2);
			format.setMaximumFractionDigits(3);
			writer.println(String.format("\"%s\"", format.format(durchmesser / 2540d)));

			writer.println("\">Mikofoneinstellung\"");
			writer.println(String.format("\"%d\"", mikofoneinstellung));

			writer.println("\">DateiName\"");
			writer.println(String.format("\"%s\"", fileName));
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	@Override
	public String toString() {
		return bezeichnung;
	}
}