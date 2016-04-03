package model;


import java.io.FileNotFoundException;
import java.io.Serializable;

import controller.Controller;


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

	public double getValuebyRadius(double radius) {
		int aussenRadius = target.getAussenRadius();
		int minRing = target.getValue(TargetValue.RING_MIN);
		int maxRing = target.getValue(TargetValue.RING_MAX);
		int zehnerRadius = target.getRingRadius(maxRing);
		int geschossRadius = weapon.getRadius();

		// Berechnung für < MIN_RING
		if (radius > aussenRadius + geschossRadius) {
			return 0f;
		}
		
		// Berechnung >= MAX_RING
		int range = zehnerRadius + geschossRadius;
		if (radius <= range) {
			double value = (int) (10 - radius * 10 / range) / 10.;
			if (value > 0.9) value = 0.9;
			return maxRing + value;
		}

		// Berechnung für >= MIN_RING && < MAX_RING
		double value = (int) (((aussenRadius - radius + geschossRadius) * (maxRing - minRing) * 10) / (aussenRadius - zehnerRadius)) / 10.;
		return minRing + value;
	}

	public double getRadiusByValue(double value) {
		int minRing = target.getValue(TargetValue.RING_MIN);
		int maxRing = target.getValue(TargetValue.RING_MAX);
		int zehnerRadius = target.getRingRadius(maxRing);
		int geschossRadius = weapon.getRadius();
		
		if (value < maxRing) {
			if (value == 0) minRing = 1;
			return Math.round(target.getAussenRadius() + geschossRadius - (value - minRing) * target.getValue(TargetValue.RING_WIDTH));
		}
		
		return Math.round(zehnerRadius + geschossRadius - (value - maxRing) * (zehnerRadius + geschossRadius));
	}

	public boolean isInnenZehn(double radius) {
		if (target.getInnenZehnRadius() == 0) {
			int maxRing = target.getValue(TargetValue.RING_MAX);
			return radius <= getRadiusByValue(maxRing + 0.2);
		}

		return radius <= target.getInnenZehnRadius() + weapon.getRadius();
	}

	public void toFile() {
		String file_scheibe = target.toFile();
		String file_waffe = weapon.toFile();

		String fileName = Controller.getPath(String.format("0_hd_%s.def", number.replace('.', '-')));
		try {
			FileWriter writer = new FileWriter(fileName);
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
