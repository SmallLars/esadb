package model;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;


public class Rule  implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String number;
	private TargetModel target;
	private Weapon weapon;

	public Rule(String regelnummer) {
		this("", regelnummer, (TargetModel) null, (Weapon) null);
	}
	
	public Rule(String name, String regelnummer, TargetModel target, Weapon weapon) {
		this.name = name;
		this.number = regelnummer;
		this.target = target;
		this.weapon = weapon;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getRegelnummer() {
		return number;
	}

	public TargetModel getScheibe() {
		return target;
	}

	public Weapon getWaffe() {
		return weapon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScheibe(TargetModel target) {
		this.target = target;
	}

	public void setWaffe(Weapon weapon) {
		this.weapon = weapon;
	}

	public float getValuebyRadius(double radius) {
		int aussenRadius = target.getAussenRadius();
		int zehnerRadius = target.getRingRadius(10);
		int geschossRadius = weapon.getRadius();

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
		int zehnerRadius = target.getRingRadius(10);
		int geschossRadius = weapon.getRadius();
		
		if (value < 10) {
			return Math.round(target.getAussenRadius() + geschossRadius - (value - 1) * target.getValue(TargetValue.RING_WIDTH));
		}
		
		return Math.round(zehnerRadius + geschossRadius - (value - 10) * (zehnerRadius + geschossRadius));
	}

	public boolean isInnenZehn(double radius) {
		return radius <= target.getInnenZehnRadius() + weapon.getRadius();
	}

	public void toFile() {
		String file_scheibe = target.toFile();
		String file_waffe = weapon.toFile();

		String fileName = String.format("0_hd_%s.def", number.replace('.', '-'));
		try {
			PrintWriter writer = new PrintWriter(fileName);
			writer.println("\">Bezeichnung\"");
			writer.println(String.format("\"%s\"", name));

			writer.println("\">KennNummer\"");
			writer.println("\"0\"");

			writer.println("\">RegelNummer\"");
			writer.println(String.format("\"DSB %s\"", number));

			writer.println("\">WertungsRadius\"");
			writer.println(String.format("\"%d\"", weapon.getRadius()));

			writer.println("\">MaximalDurchmesser\"");
			writer.println(String.format("\"%d\"", weapon.getRadius() * 2));

			writer.println("\">MinimalDurchmesser\"");
			writer.println(String.format("\"%d\"", weapon.getRadius() * 2));

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
