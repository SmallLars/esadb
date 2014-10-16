package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public enum RegelTyp {
	R_1_10("Luftgewehr",        "1.10", ScheibeTyp.LUFTGEWEHR,  WaffeTyp.LUFTDRUCK),
	R_1_35("Kleinkaliber 100m", "1.35", ScheibeTyp.GEWEHR100M,  WaffeTyp.KLEINKALIBER),
	R_1_40("Kleinkaliber 50m",  "1.40", ScheibeTyp.GEWEHR50M,   WaffeTyp.KLEINKALIBER),
	R_2_10("Luftpistole",       "2.10", ScheibeTyp.LUFTPISTOLE, WaffeTyp.LUFTDRUCK);

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

	public ScheibeTyp getScheibe() {
		return scheibe;
	}

	public WaffeTyp getWaffe() {
		return waffe;
	}

	public float getValuebyRadius(double radius) {
		// TODO funktioniert eventuell nur für KK50M
		float v = (int) (110 - radius * 10 / scheibe.getRingBreite()) / 10f;
		if (v < 1) return 0.0f;
		if (v > 10.9) return 10.9f;
		return v;
	}

	public double getRadiusByValue(float value) {
		// TODO funktioniert eventuell nur für KK50M
		return Math.round(scheibe.getAussenRadius() + scheibe.getRingBreite() + waffe.getRadius() - value * scheibe.getRingBreite());
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
//			writer.println(String.format("\"%d\"", getRadius()));					TODO

			writer.println("\">MaximalDurchmesser\"");
//			writer.println(String.format("\"%s\"", getDurchmesser(Einheit.MM)));	TODO

			writer.println("\">MinimalDurchmesser\"");
//			writer.println(String.format("\"%s\"", getDurchmesser(Einheit.INCH)));	TODO

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
