package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;

public enum WaffeTyp {
	LUFTDRUCK      ( 1, "Luftdruck",                  4500, Einheit.MM,   1),
	ZIMMERSTUTZEBN ( 2, "Zimmerstutzen",              4650, Einheit.MM,   1),
	KLEINKALIBER   ( 3, "Kleinkaliber",               5600, Einheit.MM,   2),
	GROﬂKALIBER_B8 ( 4, "Groﬂkalibergewehr (<=8mm)",  8000, Einheit.MM,   5),
	GROﬂKALIBER_P8 ( 5, "Groﬂkalibergewehr (>8mm)",  10000, Einheit.MM,   6),
	KALIBER30      ( 6, "Kaliber .30",                 300, Einheit.INCH, 6),
	KALIBER32      ( 7, "Kaliber .32",                 320, Einheit.INCH, 6),
	KALIBER357     ( 8, "Kaliber .357",                357, Einheit.INCH, 6),
	KALIBER38      ( 9, "Kaliber .38",                 380, Einheit.INCH, 6),
	KALIBER10MM    (10, "Kaliber 10mm",              10000, Einheit.MM,   6),
	KALIBER44      (11, "Kaliber .44",                 440, Einheit.INCH, 6),
	KALIBER45      (12, "Kaliber .45",                 450, Einheit.INCH, 6),
	VORDERLADER    (13, "Vorderlader",               14000, Einheit.MM,   6);

	private final int kennnummer;
	private final String bezeichnung;
	private final int durchmesser;
	private final Einheit einheit;
	private final int mikofoneinstellung;
	
	WaffeTyp(int kennnummer, String bezeichnung, int durchmesser, Einheit einheit, int mikofoneinstellung) {
		this.kennnummer = kennnummer;
		this.bezeichnung = bezeichnung;
		this.durchmesser = durchmesser;
		this.einheit = einheit;
		this.mikofoneinstellung = mikofoneinstellung;
	}

	public int getRadius() {
		switch (einheit) {
			case MM:
				return durchmesser / 20;
			case INCH:
				return (int) (durchmesser * 1.27);
			default:
				return 0;
		}
	}

	public String toFile() {
		String fileName = String.format("0_hw_%02d.def", kennnummer);
		try {
			PrintWriter writer = new PrintWriter(fileName);

			writer.println("\">Bezeichnung\"");                                        // Bezeichnung oder Name der Waffe
			writer.println(String.format("\"%s\"", bezeichnung));

			writer.println("\">KennNummer\"");                                         // Kennnummer der Waffe nach DSB oder DJV
			writer.println(String.format("\"%02d\"", kennnummer));

			writer.println("\">AnzeigeGeschossRadius\"");                              // Radius in 1/100 mm f¸r die Darstellung auf der Scheibe
			writer.println(String.format("\"%d\"", getRadius()));                      // Ist in der Disziplin beim Punkt Wertungsradius -1 eingetragen, so
			                                                                           // wird dieser Wert als Wertungsradius benutzt.

			writer.println("\">GeschossDurchmesser\"");                                // Geschoss-Durchmesser in Millimeter.
			writer.println(String.format("\"%s\"", getDurchmesser(Einheit.MM)));       // Informativer Wert

			writer.println("\">KaliberDurchmesser\"");                                 // Angabe des Geschoss-Durchmessers in Kaliberwerten.
			writer.println(String.format("\"%s\"", getDurchmesser(Einheit.INCH)));     // Informativer Wert

			writer.println("\">Mikofoneinstellung\"");                                 // Einstellung der Mikrofonempfindlichkeit, die gesetzt wird,
			writer.println(String.format("\"%d\"", mikofoneinstellung));               // wenn der Autosensor aktiviert ist (Kapitel 2, Punkt 2 c)

			writer.println("\">DateiName\"");                                          // Gibt den Namen der Datei an, unter der sie im Verzeichnis
			writer.println(String.format("\"%s\"", fileName));                         // C:\Programme\ESA2002 abgespeichert wird.

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

	private String getDurchmesser(Einheit einheit) {
		NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
		format.setGroupingUsed(false);
		format.setMinimumFractionDigits(1);
		format.setMaximumFractionDigits(3);
		
		if (this.einheit == einheit) {
			return format.format(durchmesser / 1000.0);
		}

		switch (einheit) {
			case MM:
				return format.format(durchmesser * 0.0254);
			case INCH:
				return format.format(durchmesser / 25400.0);
			default:
				return "0.0";
		}
	}
}