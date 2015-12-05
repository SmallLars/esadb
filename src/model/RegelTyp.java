package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;

public class RegelTyp  implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String bezeichnung;
	private final String regelnummer;
	private ScheibeModel scheibe;
	private WaffeModel waffe;
	
	public RegelTyp(String bezeichnung, String regelnummer, ScheibeModel scheibe, WaffeModel waffe) {
		this.bezeichnung = bezeichnung;
		this.regelnummer = regelnummer;
		this.scheibe = scheibe;
		this.waffe = waffe;
	}

	@Override
	public String toString() {
		return bezeichnung;
	}

	public String getRegelnummer() {
		return regelnummer;
	}

	public ScheibeModel getScheibe() {
		return scheibe;
	}

	public WaffeModel getWaffe() {
		return waffe;
	}

	public float getValuebyRadius(double radius) {
		int aussenRadius = scheibe.getAussenRadius();
		int zehnerRadius = scheibe.getRingRadius(10);
		int geschossRadius = waffe.getRadius();

		// Berechnung für < 1
		if (radius > aussenRadius + geschossRadius) {
			return 0f;
		}
		
		// Berechnung >= 10,0
		if (radius <= zehnerRadius + geschossRadius) {
			float v = (int) (10 - radius * 10 / (zehnerRadius + geschossRadius)) / 10f;
			if (v > 0.9) return 10.9f;
			return 10f + v;
		}

		// Berechnung für >=1 && < 10,0
		float v = (int) (((aussenRadius - radius + geschossRadius) * 90) / (aussenRadius - zehnerRadius)) / 10f;
		return 1f + v;
	}

	public double getRadiusByValue(double value) {
		int zehnerRadius = scheibe.getRingRadius(10);
		int geschossRadius = waffe.getRadius();
		
		if (value < 10) {
			return Math.round(scheibe.getAussenRadius() + geschossRadius - (value - 1) * scheibe.getRingBreite());
		}
		
		return Math.round(zehnerRadius + geschossRadius - (value - 10) * (zehnerRadius + geschossRadius));
	}

	public boolean isInnenZehn(double radius) {
		return radius <= scheibe.getInnenZehnRadius() + waffe.getRadius();
	}

	public void toFile() {
		String file_scheibe = scheibe.toFile();
		String file_waffe = waffe.toFile();

		String fileName = String.format("0_hd_%s.def", regelnummer.replace('.', '-'));
		try {
			PrintWriter writer = new PrintWriter(fileName);
			writer.println("\">Bezeichnung\"");
			writer.println(String.format("\"%s\"", bezeichnung));

			writer.println("\">KennNummer\"");
			writer.println("\"0\"");

			writer.println("\">RegelNummer\"");
			writer.println(String.format("\"DSB %s\"", regelnummer));

			writer.println("\">WertungsRadius\"");
			writer.println(String.format("\"%d\"", waffe.getRadius()));

			writer.println("\">MaximalDurchmesser\"");
			writer.println(String.format("\"%d\"", waffe.getRadius() * 2));

			writer.println("\">MinimalDurchmesser\"");
			writer.println(String.format("\"%d\"", waffe.getRadius() * 2));

			writer.println("\">Schusszahl\"");
			writer.println("\"40\"");
			writer.println("\">ProbeSchuesse\"");
			writer.println("\"0\"");
			writer.println("\">SchiessZeit\"");
			writer.println("\"75 min\"");
			writer.println("\">ProbeZeit\"");
			writer.println("\"0 min\"");
			writer.println("\">Wertung\"");
			writer.println("\"0\"");
			writer.println("\">SchiessPosition\"");
			writer.println("\"stehend\"");
			writer.println("\">SerienLaenge\"");
			writer.println("\"10\"");
			
			writer.println("\">DateiWaffe\"");
			writer.println(String.format("\"%s\"", file_waffe));

			writer.println("\">DateiScheibe\"");
			writer.println(String.format("\"%s\"", file_scheibe));

			writer.println("\">DateiName\"");
			writer.println(String.format("\"%s\"", fileName));
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
