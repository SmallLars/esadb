package model;


import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

import controller.Controller;


public class Weapon implements Serializable, Comparable<Weapon> {
	private static final long serialVersionUID = 1L;

	private String name;
	private String number;
	private int diameter;
	private Unit unit;
	private int micro;
	
	public Weapon(String name, String number, int diameter, Unit unit, int micro) {
		this.number = number;
		this.name = name;
		this.diameter = diameter;
		this.unit = unit;
		this.micro = micro;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Weapon w) {
		return w.number.compareTo(number) * -1;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Weapon) {
			return number.equals(((Weapon) o).number);
		}
		return false;
	}

	public String getNumber() {
		return number;
	}

	public int getDiameter() {
		return diameter;
	}

	public Unit getUnit() {
		return unit;
	}

	public int getMikro() {
		return micro;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setDiameter(int dia) {
		this.diameter = dia;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public void setMikro(int micro) {
		this.micro = micro;
	}

	public int getRadius() {
		switch (unit) {
			case MM:
				return diameter / 20;
			case INCH:
				return (int) (diameter * 1.27);
			default:
				return 0;
		}
	}

	public String toFile() {
		String fileName = Controller.getPath(String.format("0_hw_%s.def", number));
		try {
			FileWriter writer = new FileWriter(fileName);

			writer.println("\">Bezeichnung\"");                                        // Bezeichnung oder Name der Waffe
			writer.println(String.format("\"%s\"", name));

			writer.println("\">KennNummer\"");                                         // Kennnummer der Waffe nach DSB oder DJV
			writer.println(String.format("\"%s\"", number));

			writer.println("\">AnzeigeGeschossRadius\"");                              // Radius in 1/100 mm für die Darstellung auf der Scheibe
			writer.println(String.format("\"%d\"", getRadius()));                      // Ist in der Disziplin beim Punkt Wertungsradius -1 eingetragen, so
			                                                                           // wird dieser Wert als Wertungsradius benutzt.

			writer.println("\">GeschossDurchmesser\"");                                // Geschoss-Durchmesser in Millimeter.
			writer.println(String.format("\"%s\"", getDiamter(Unit.MM)));          // Informativer Wert

			writer.println("\">KaliberDurchmesser\"");                                 // Angabe des Geschoss-Durchmessers in Kaliberwerten.
			writer.println(String.format("\"%s\"", getDiamter(Unit.INCH)));        // Informativer Wert

			writer.println("\">Mikofoneinstellung\"");                                 // Einstellung der Mikrofonempfindlichkeit, die gesetzt wird,
			writer.println(String.format("\"%d\"", micro));               // wenn der Autosensor aktiviert ist (Kapitel 2, Punkt 2 c)

			writer.println("\">DateiName\"");                                          // Gibt den Namen der Datei an, unter der sie im Verzeichnis
			writer.println(String.format("\"%s\"", fileName));                         // C:\Programme\ESA2002 abgespeichert wird.

			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	private String getDiamter(Unit einheit) {
		NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
		format.setGroupingUsed(false);
		format.setMinimumFractionDigits(1);
		format.setMaximumFractionDigits(3);
		
		if (this.unit == einheit) {
			return format.format(diameter / 1000.0);
		}

		switch (einheit) {
			case MM:
				return format.format(diameter * 0.0254);
			case INCH:
				return format.format(diameter / 25400.0);
			default:
				return "0.0";
		}
	}
}