package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public enum RegelTyp {
	R_1_10("Luftgewehr",        "1.10", ScheibeTyp.GEWEHR_10M,  WaffeTyp.LUFTDRUCK),
	R_1_35("Kleinkaliber 100m", "1.35", ScheibeTyp.GEWEHR_100M,  WaffeTyp.KLEINKALIBER),
	R_1_40("Kleinkaliber 50m",  "1.40", ScheibeTyp.GEWEHR_50M,   WaffeTyp.KLEINKALIBER),
	R_2_10("Luftpistole",       "2.10", ScheibeTyp.PISTOLE_LUFT, WaffeTyp.LUFTDRUCK);

	private final String bezeichnung;
	private final String regelnummer;
	private ScheibeTyp scheibe;
	private WaffeTyp waffe;
	
	RegelTyp(String bezeichnung, String regelnummer, ScheibeTyp scheibe, WaffeTyp waffe) {
		this.bezeichnung = bezeichnung;
		this.regelnummer = regelnummer;
		this.scheibe = scheibe;
		this.waffe = waffe;
	}

	@Override
	public String toString() {
		return bezeichnung;
	}

	public ScheibeTyp getScheibe() {
		return scheibe;
	}

	public WaffeTyp getWaffe() {
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

	public double getRadiusByValue(float value) {
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

	public static RegelTyp getTypByGattung(String s) {
		switch (s) {
			case "1.10": return R_1_10;
			case "1.35": return R_1_35;
			case "1.40": return R_1_40;
			case "2.10": return R_2_10;
			default:  return R_1_40;
		}
	}
}
