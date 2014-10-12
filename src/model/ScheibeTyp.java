package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;


public enum ScheibeTyp {
	LUFTGEWEHR("Gewehr 10m", "0.4.3.01", 3050, 4550, 0, 250, 1, 8),
	LUFTPISTOLE("Pistole 10m", "0.4.3.20", 5950, 15550, 500, 800, 1, 8),
	GEWEHR50M("Gewehr 50m", "0.4.3.03", 11240, 15440, 500, 800, 1, 8),
	GEWEHR100M("Gewehr 100m/Pistole 25m", "0.4.3.04", 20000, 50000, 2500, 2500, 1, 9);

	private final String bezeichnung;
	private final String kennnummer;
	private final int radius_spiegel;
	private final int radius_aussen;
	private final int radius_innenzehn;
	private final int ringbreite;
	private final int min_ring;
	private final int max_number;

	ScheibeTyp(String bezeichnung, String kennnummer, int durchmesser_spiegel, int durchmesser_aussen, int durchmesser_innenzehn, int ringbreite, int min_ring, int max_number) {
		this.bezeichnung = bezeichnung;
		this.kennnummer = kennnummer;
		this.radius_spiegel = durchmesser_spiegel / 2;
		this.radius_aussen = durchmesser_aussen / 2;
		this.radius_innenzehn = durchmesser_innenzehn / 2;
		this.ringbreite = ringbreite;
		this.min_ring = min_ring;
		this.max_number = max_number;
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

	public String toFile() {
		NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
		format.setGroupingUsed(false);

		String fileName = String.format("0_hs_%s.def", kennnummer.replace('.', '-'));
		try {
			PrintWriter writer = new PrintWriter(fileName);
			writer.println("\">Bezeichnung\"");
			writer.println(String.format("\"%s\"", bezeichnung));

			writer.println("\">ScheibenArt\"");
			writer.println("\"0\"");                                     // TODO

			writer.println("\">KennNummer\"");
			writer.println(String.format("\"DSB %s\"", kennnummer));

			writer.println("\">AussenRadius\"");
			writer.println(String.format("\"%d\"", radius_aussen));
			writer.println("\">SpiegelRadius\"");
			writer.println(String.format("\"%d\"", radius_spiegel));

			writer.println("\">ZehnerRadius\"");
			writer.println(String.format("\"%d\"", getRingRadius(10)));
			writer.println("\">ZehnerRingStyle\"");
			writer.println("\"1\"");                                     // TODO

			writer.println("\">InnenZehnerRadius\"");
			writer.println(String.format("\"%d\"", radius_innenzehn));
			writer.println("\">InnenZehnerRingStyle\"");
			writer.println("\"1\"");                                     // TODO

			writer.println("\">InnenKreuzRadius\"");
			writer.println("\"0\"");
			writer.println("\">InnenKreuzWinkel\"");
			writer.println("\"0\"");

			writer.println("\">VorhalteSpiegelRadius\"");
			writer.println("\"0\"");                                     // TODO
			writer.println("\">VorhalteAbstand\"");
			writer.println("\"0\"");                                     // TODO

			writer.println("\">Ringbreite\"");
			writer.println(String.format("\"%d\"", ringbreite));

			writer.println("\">RingMin\"");
			writer.println(String.format("\"%d\"", min_ring));
			writer.println("\">RingMax\"");
			writer.println(String.format("\"%d\"", max_number));

			writer.println("\">RingAnzahl\"");
			writer.println("\"10\"");

			writer.println("\">RingNummerWinkel\"");
			writer.println("\"0\"");                                     // TODO

			writer.println("\">KartonBreite\"");
			writer.println("\"55000\"");                                     // TODO
			writer.println("\">KartonHoehe\"");
			writer.println("\"55000\"");                                     // TODO

			writer.println("\">Probe\"");
			writer.println("\"-1\"");

			writer.println("\">BandVorschub\"");
			writer.println("\"5\"");                                     // TODO

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
