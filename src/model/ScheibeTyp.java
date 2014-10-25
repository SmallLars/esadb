package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;


public enum ScheibeTyp {
	//          |                       |           |       |Band-   |      Durchmesser       |Ring- |Min |  Nummer  |   |     |  Vorhalte-   |
	//          |Bezeichnung            | Kennummer | Karton|vorschub|Spiegel|Aussen|Innenzehn|breite|Ring|Max|Wimkel|Art|Style|Radius|Abstand|
	GEWEHR_10M(   "Gewehr 10m",           "0.4.3.01",  17000,       2,   3050,  4550,        0,   250,   1, 8,      0,  0,    2),
 	GEWEHR_15M(   "Gewehr 15m",           "0.4.3.02",  17000,       3,   4050,  8550,        0,   450,   1, 9),
	GEWEHR_50M(   "Gewehr 50m",           "0.4.3.03",  55000,       5,  11240, 15440,      500,   800,   1, 8),
	GEWEHR_100M(  "Gewehr 100m",          "0.4.3.04",  55000,       5,  20000, 50000,     2500,  2500,   1, 9),
	GEWEHR_300M(  "Gewehr 300m",          "0.4.3.05", 130000,       0,  60000, 100000,       0,  5000,   1, 9,     45,  0,    0),
	MUSKETE(      "Muskete",              "0.4.3.06",  55000,       5,  40000, 80000,     4000,  4000,   1, 8),
	PISTOLE_PRÄZ( "Pistole - Präzision",  "0.4.3.04",  55000,       5,  20000, 50000,     5000,  2500,   1, 9,      0,  4,    0),
	PISTOLE_LUFT( "Pistole 10m",          "0.4.3.20",  17000,       3,   5950, 15550,      500,   800,   1, 8),
	PISTOLE_DUEL( "Pistole - Duell",      "0.4.3.22",  55000,       5,  50000, 50000,     5000,  4000,   5, 9,      0,  4,    0),
	LAUFEND_10M(  "Laufende Scheibe 10m", "0.4.3.40",  17000,       2,   3050,  5050,       50,   250,   1, 9,     45,  0,    1,  1550,   7000),
	LAUFEND_50M(  "Laufende Scheibe 50m", "0.4.3.41",  70000,       0,      2, 36600,     3000,  1700,   1, 9,     45,  5,    0);


	private final String bezeichnung;
	private final String kennnummer;
	private final int karton;
	private final int bandvorschub;
	private final int radius_spiegel;
	private final int radius_aussen;
	private final int radius_innenzehn;
	private final int ringbreite;
	private final int min_ring;
	private final int max_number;
	
	private final int winkel;
	private final int art;
	private final int style;           // 0 => Zehn und Innenzehn: Ringe       1=> Innezehn ausgefüllt    2=>     Zehn ausgefüllt, Innenzehn irrelevant 
	private final int vorhalteradius;
	private final int vorhalteabstand;

	ScheibeTyp(String bezeichnung, String kennnummer, int karton, int bandvorschub,
			   int durchmesser_spiegel, int durchmesser_aussen, int durchmesser_innenzehn, int ringbreite,
			   int min_ring, int max_number) {
		this.bezeichnung = bezeichnung;
		this.kennnummer = kennnummer;
		this.karton = karton;
		this.bandvorschub = bandvorschub;
		this.radius_spiegel = durchmesser_spiegel / 2;
		this.radius_aussen = durchmesser_aussen / 2;
		this.radius_innenzehn = durchmesser_innenzehn / 2;
		this.ringbreite = ringbreite;
		this.min_ring = min_ring;
		this.max_number = max_number;
		
		this.winkel = 0;
		this.art = 0;
		this.style = 0;
		this.vorhalteradius = 0;
		this.vorhalteabstand = 0;
	}

	ScheibeTyp(String bezeichnung, String kennnummer, int karton, int bandvorschub,
			   int durchmesser_spiegel, int durchmesser_aussen, int durchmesser_innenzehn, int ringbreite,
			   int min_ring, int max_number, int winkel, int art, int style) {
		this.bezeichnung = bezeichnung;
		this.kennnummer = kennnummer;
		this.karton = karton;
		this.bandvorschub = bandvorschub;
		this.radius_spiegel = durchmesser_spiegel / 2;
		this.radius_aussen = durchmesser_aussen / 2;
		this.radius_innenzehn = durchmesser_innenzehn / 2;
		this.ringbreite = ringbreite;
		this.min_ring = min_ring;
		this.max_number = max_number;

		this.winkel = winkel;
		this.art = art;
		this.style = style;
		this.vorhalteradius = 0;
		this.vorhalteabstand = 0;
	}

	ScheibeTyp(String bezeichnung, String kennnummer, int karton, int bandvorschub,
			   int durchmesser_spiegel, int durchmesser_aussen, int durchmesser_innenzehn, int ringbreite,
			   int min_ring, int max_number, int winkel, int art, int style, int vorhalteradius, int vorhalteabstand) {
		this.bezeichnung = bezeichnung;
		this.kennnummer = kennnummer;
		this.karton = karton;
		this.bandvorschub = bandvorschub;
		this.radius_spiegel = durchmesser_spiegel / 2;
		this.radius_aussen = durchmesser_aussen / 2;
		this.radius_innenzehn = durchmesser_innenzehn / 2;
		this.ringbreite = ringbreite;
		this.min_ring = min_ring;
		this.max_number = max_number;

		this.winkel = winkel;
		this.art = art;
		this.style = style;
		this.vorhalteradius = vorhalteradius;
		this.vorhalteabstand = vorhalteabstand;
	}

	public int getRingRadius(int i) {
		if (i < 0 || i > 10) return 0;
	
		return radius_aussen - (i - min_ring) * ringbreite;
	}

	public int getAussenRadius() {
		return radius_aussen;
	}
	
	public int getSpiegelRadius() {
		return radius_spiegel;
	}

	public int getRingBreite() {
		return ringbreite;
	}
	
	public int getInnenZehnRadius() {
		return radius_innenzehn;
	}
	
	public int getFontSize() {
		return (ringbreite) * 9 / 16;
	}

	public boolean drawRing(int i) {
		return i >= min_ring;
	}
	
	public boolean drawNumber(int i) {
		return i >= min_ring && i <= max_number;
	}

	public int getNumberRadius(int i) {
		return (getRingRadius(i) - ringbreite * 3 / 8);
	}

	public boolean blackNumber(int i) {
		return getNumberRadius(i) > radius_spiegel;
	}

	public int getStyle() {
		return style;
	}

	public String toFile() {
		NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
		format.setGroupingUsed(false);

		String fileName = String.format("0_hs_%s.def", kennnummer.replace('.', '-'));
		try {
			PrintWriter writer = new PrintWriter(fileName);

			writer.println("\">Bezeichnung\"");                                // Name der Scheibe der bei der Programmauswahl angezeigt wird
			writer.println(String.format("\"%s\"", bezeichnung));

			writer.println("\">ScheibenArt\"");                                // Systeminterne Scheibenbezeichnung
			writer.println(String.format("\"%d\"", art));
			                                                                   //  1 => Trefferzonenscheibe mit Klappscheibensteuerung
			                                                                   //  2 => Trefferzonenscheibe / Jagdscheibe
			                                                                   // 3 => Ringscheibe mit weißem Zehner und schwarzer Schrift
			                                                                   // 4 => Ringscheibe mit PA25PC - Modul
			                                                                   // 5 => Inverse Ringscheibe
			                                                                   //  6 => Trefferzonenscheibe mit Doppelsau

			writer.println("\">KennNummer\"");                                 // Kennnr. der Scheibe nach DSB/DJV nur Informativ
			writer.println(String.format("\"DSB %s\"", kennnummer));

			writer.println("\">AussenRadius\"");                               // Radius des 1. Rings in 1/100 mm
			writer.println(String.format("\"%d\"", radius_aussen));
			writer.println("\">SpiegelRadius\"");                              // Radius des Spiegels in 1/100 mm
			writer.println(String.format("\"%d\"", radius_spiegel));			

			writer.println("\">ZehnerRadius\"");                               // Radius des 10. Rings in 1/100 mm
			writer.println(String.format("\"%d\"", getRingRadius(10)));
			writer.println("\">ZehnerRingStyle\"");                            // Gibt die Optik des Zehnerrings vor, 0=ausgefüllt, 1=Ring
			writer.println(style >= 2 ? "\"0\"": "\"1\"");

			writer.println("\">InnenZehnerRadius\"");                          // Radius des Innenzehners in 1/100mm
			writer.println(String.format("\"%d\"", radius_innenzehn));
			writer.println("\">InnenZehnerRingStyle\"");                       // Gibt die Optik des Innenzehnerrings vor, 0=ausgefüllt, 1=Ring
			writer.println(style >= 1 ? "\"0\"": "\"1\"");

			writer.println("\">InnenKreuzRadius\"");                           // Länge der Kreuzschenkel, zwei mal ergibt Kreuzlinien, 0=keins
			writer.println("\"0\"");
			writer.println("\">InnenKreuzWinkel\"");                           // Gibt an um wieviel Grad das Kreuz gedreht ist
			writer.println("\"0\"");

			writer.println("\">VorhalteSpiegelRadius\"");                      // Gibt an ob Vorhaltespiegel dargestellt werden, 0=keinen
			writer.println(String.format("\"%d\"", vorhalteradius));
			writer.println("\">VorhalteAbstand\"");                            // Abstand der Vorhaltespiegel zur Scheibenmitte
			writer.println(String.format("\"%d\"", vorhalteabstand));

			writer.println("\">Ringbreite\"");                                 // Breite eines Ringes in 1/100mm
			writer.println(String.format("\"%d\"", ringbreite));

			writer.println("\">RingMin\"");                                    // Nummer des kleinsten dargestellten Rings
			writer.println(String.format("\"%d\"", min_ring));
			writer.println("\">RingMax\"");                                    // Nummer des größten dargestellten Rings
			writer.println(String.format("\"%d\"", max_number));

			writer.println("\">RingAnzahl\"");                                 // Anzahl der abgebildeten Ringe
			writer.println("\"10\"");

			writer.println("\">RingNummerWinkel\"");                           // Anordnungswinkel der Beschriftungszahlen
			writer.println(String.format("\"%d\"", winkel));

			writer.println("\">KartonBreite\"");                               // Breite des Scheibenkartons
			writer.println(String.format("\"%d\"", karton));
			writer.println("\">KartonHoehe\"");                                // Höhe des Scheibenkartons
			writer.println(String.format("\"%d\"", karton));

			writer.println("\">Probe\"");                                      // Anzahl der Probeschüsse eines Durchgangs, bei -1 keine Probe,
			writer.println("\"-1\"");                                          // bei 0 unbegrenzte Probe, an sonsten nach eingestellter Zahl

			writer.println("\">BandVorschub\"");                               // Gibt an wie lange der Motor für den Bandvorschub läuft
			writer.println(String.format("\"%d\"", bandvorschub));

			writer.println("\">DateiName\"");                                  // Gibt den Namen der Datei an, unter der sie im Verzeichnis
			writer.println(String.format("\"%s\"", fileName));                 // C:\Programme\ESA2002 abgespeichert wird.

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
