package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

public class WaffeModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private final int kennnummer;
	private final String bezeichnung;
	private final int durchmesser;
	private final Einheit einheit;
	private final int mikofoneinstellung;
	
	public WaffeModel(int kennnummer, String bezeichnung, int durchmesser, Einheit einheit, int mikofoneinstellung) {
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

			writer.println("\">AnzeigeGeschossRadius\"");                              // Radius in 1/100 mm für die Darstellung auf der Scheibe
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