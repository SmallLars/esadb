package model;


import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

import controller.Controller;


public class TargetModel implements Serializable, Comparable<TargetModel> {
	private static final long serialVersionUID = 1L;

	private String bezeichnung;
	private String kennnummer;
	private String image;
	private Map<TargetValue, Integer> values;

	public TargetModel() {
		bezeichnung = "Neue Scheibe";
		kennnummer = "0.4.3.00";
		image = "bock.bmp";

		values = new EnumMap<TargetValue, Integer>(TargetValue.class);
		for (TargetValue tv : TargetValue.values()) values.put(tv, tv.getStandardValue());
	}

	public TargetModel(String bezeichnung, String kennnummer, int... values) {
		this();
		
		this.bezeichnung = bezeichnung;
		this.kennnummer = kennnummer;

		for (int i = 0; i < values.length; i++) {
			this.values.put(TargetValue.values()[i], values[i]);
		}
	}

	public TargetModel(String bezeichnung, String kennnummer, String image) {
		this();

		this.bezeichnung = bezeichnung;
		this.kennnummer = kennnummer;
		this.image = image;
		this.values.put(TargetValue.TYPE, TargetType.JAGD.getValue());
	}

	public TargetModel(TargetModel tm) {
		bezeichnung = tm.bezeichnung;
		kennnummer = tm.kennnummer;
		image = tm.image;

		values = new EnumMap<TargetValue, Integer>(TargetValue.class);
		for (TargetValue tv : TargetValue.values()) {
			values.put(tv, tm.getValue(tv));
		}
	}

	@Override
	public String toString() {
		return bezeichnung;
	}

	@Override
	public int compareTo(TargetModel t) {
		return t.kennnummer.compareTo(kennnummer) * -1;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TargetModel) {
			return kennnummer.equals(((TargetModel) o).kennnummer);
		}
		return false;
	}

	public void setName(String name) {
		this.bezeichnung = name;
	}

	protected void setNumber(String number) {
		this.kennnummer = number;
	}

	public String getNumber() {
		return kennnummer;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return this.image;
	}

	public void setValue(TargetValue type, int value) {
		int dia_outside = values.get(TargetValue.DIA_OUTSIDE);
		int ring_width = values.get(TargetValue.RING_WIDTH);
		int dia_black = values.get(TargetValue.DIA_BLACK);
		int dia_inner_ten = values.get(TargetValue.DIA_INNER_TEN);
		int ring_min = values.get(TargetValue.RING_MIN);
		int ring_max = values.get(TargetValue.RING_MAX);
		int num_max = values.get(TargetValue.NUM_MAX);
		int fill = values.get(TargetValue.FILL);

		switch (type) {
			case DIA_BLACK:
				if (value > dia_outside) {
					values.put(TargetValue.DIA_OUTSIDE, value);
				}
				break;
			case DIA_OUTSIDE:
				if (value < dia_inner_ten + 2 * (ring_max - ring_min) * ring_width) {
					if (ring_max - ring_min < 2) return;

					values.put(TargetValue.RING_MAX, --ring_max);
					if (num_max >= ring_max) {
						values.put(TargetValue.NUM_MAX, --num_max);
					}
				}
				if (value < dia_black) {
					values.put(TargetValue.DIA_BLACK, value);
				}
				break;
			case DIA_INNER_TEN:
				if (dia_outside < value + 2 * (ring_max - ring_min) * ring_width) {
					if (ring_max - ring_min < 2) return;

					values.put(TargetValue.RING_MAX, --ring_max);
					if (num_max >= ring_max) {
						values.put(TargetValue.NUM_MAX, --num_max);
					}
				}
				if (value > 0 && fill == 2) {
					values.put(TargetValue.FILL, 1);
				}
				break;
			case RING_WIDTH:
				if (dia_outside < dia_inner_ten + 2 * (ring_max - ring_min) * value) {
					if (ring_max - ring_min < 2) return;

					values.put(TargetValue.RING_MAX, --ring_max);
					if (num_max >= ring_max) {
						values.put(TargetValue.NUM_MAX, --num_max);
					}
				}
				break;
			case RING_MIN:
				if (value >= ring_max) {
					values.put(TargetValue.RING_MAX, ++ring_max);
				}
				if (value > num_max) {
					values.put(TargetValue.NUM_MAX, ++num_max);
				}
				if (dia_outside < dia_inner_ten + 2 * (ring_max - value) * ring_width) {
					values.put(TargetValue.RING_MAX, --ring_max);
					if (num_max >= ring_max) {
						values.put(TargetValue.NUM_MAX, --num_max);
					}
				}
				break;
			case RING_MAX:
				if (value < 1) return;

				if (value <= ring_min && ring_min > 0) {
					values.put(TargetValue.RING_MIN, --ring_min);
				}
				if (value <= num_max && num_max > 0) {
					values.put(TargetValue.NUM_MAX, --num_max);
				}
				if (dia_outside < dia_inner_ten + 2 * (value - ring_min) * ring_width) {
					values.put(TargetValue.RING_MIN, ++ring_min);
					if (num_max < ring_min) {
						values.put(TargetValue.NUM_MAX, ++num_max);
					}
				}
				break;
			case NUM_MAX:
				if (value >= ring_max) return;
				if (value < ring_min) return;

				break;
			case FILL:
				if (value == 2 && dia_inner_ten > 0) {
					values.put(TargetValue.DIA_INNER_TEN, 0);
				}
				break;
			default:
		}
		values.put(type, value);
	}

	public int getValue(TargetValue type) {
		return values.get(type);
	}

	public boolean isRingTarget() {
		int art = values.get(TargetValue.TYPE);
		return art == 0 || art == 3 || art == 4 || art == 5;
	}

	public boolean isDeerTarget() {
		int art = values.get(TargetValue.TYPE);
		return art == 1 || art == 2 || art == 6;
	}

	public int getRingRadius(int i) {
		int dia_outside = values.get(TargetValue.DIA_OUTSIDE);
		int ring_width = values.get(TargetValue.RING_WIDTH);
		int ring_min = values.get(TargetValue.RING_MIN);
		int ring_max = values.get(TargetValue.RING_MAX);

		if (i < 0 || i > ring_max) return 0;
	
		return (dia_outside / 2) - (i - ring_min) * ring_width;
	}

	public int getAussenRadius() {
		int dia_outside = values.get(TargetValue.DIA_OUTSIDE);
		return dia_outside / 2;
	}
	
	public int getSpiegelRadius() {
		int dia_black = values.get(TargetValue.DIA_BLACK);
		return dia_black / 2;
	}

	public int getInnenZehnRadius() {
		int dia_inner_ten = values.get(TargetValue.DIA_INNER_TEN);
		return dia_inner_ten / 2;
	}
	
	public int getFontSize() {
		int ring_width = values.get(TargetValue.RING_WIDTH);
		return (ring_width) * 9 / 16;
	}

	public boolean drawRing(int i) {
		int ring_min = values.get(TargetValue.RING_MIN);
		int ring_max = values.get(TargetValue.RING_MAX);
		return i >= ring_min && i <= ring_max;
	}
	
	public boolean drawNumber(int i) {
		int ring_min = values.get(TargetValue.RING_MIN);
		int num_max = values.get(TargetValue.NUM_MAX);
		return i >= ring_min && i <= num_max;
	}

	public int getNumberRadius(int i) {
		int ring_width = values.get(TargetValue.RING_WIDTH);
		return (getRingRadius(i) - ring_width * 3 / 8);
	}

	public boolean blackNumber(int i) {
		int dia_black = values.get(TargetValue.DIA_BLACK);
		return getNumberRadius(i) > (dia_black / 2);
	}

	public String toFile() {
		NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
		format.setGroupingUsed(false);

		String fileName = Controller.getPath(String.format("0_hs_%s.def", kennnummer.replace('.', '-')));
		try {
			FileWriter writer = new FileWriter(fileName);

			// Name der Scheibe der bei der Programmauswahl angezeigt wird
			writer.println("\">Bezeichnung\"");
			writer.println(String.format("\"%s\"", bezeichnung));

			// Systeminterne Scheibenbezeichnung
			// 1 => Trefferzonenscheibe mit Klappscheibensteuerung
			// 2 => Trefferzonenscheibe / Jagdscheibe
			// 3 => Ringscheibe mit weißem Zehner und schwarzer Schrift
			// 4 => Ringscheibe mit PA25PC - Modul
			// 5 => Inverse Ringscheibe
			// 6 => Trefferzonenscheibe mit Doppelsau
			writer.println("\">ScheibenArt\"");
			writer.println(String.format("\"%d\"", values.get(TargetValue.TYPE)));

			// Kennnr. der Scheibe nach DSB/DJV nur Informativ
			writer.println("\">KennNummer\"");
			writer.println(String.format("\"DSB %s\"", kennnummer));

			if (isRingTarget()) {
				// Radius des 1. Rings in 1/100 mm
				writer.println("\">AussenRadius\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.DIA_OUTSIDE) / 2));

				// Radius des Spiegels in 1/100 mm
				writer.println("\">SpiegelRadius\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.DIA_BLACK) / 2));			

				// Radius des kleinsten Rings in 1/100 mm
				writer.println("\">ZehnerRadius\"");
				writer.println(String.format("\"%d\"", getRingRadius(values.get(TargetValue.RING_MAX))));

				// Gibt die Optik des Zehnerrings vor, 0=ausgefüllt, 1=Ring
				writer.println("\">ZehnerRingStyle\"");
				writer.println(values.get(TargetValue.FILL) >= 2 ? "\"0\"": "\"1\"");

				// Radius des Innenzehners in 1/100mm
				writer.println("\">InnenZehnerRadius\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.DIA_INNER_TEN) / 2));

				// Gibt die Optik des Innenzehnerrings vor, 0=ausgefüllt, 1=Ring
				writer.println("\">InnenZehnerRingStyle\"");
				writer.println(values.get(TargetValue.FILL) >= 1 ? "\"0\"": "\"1\"");

				// Länge der Kreuzschenkel, zwei mal ergibt Kreuzlinien, 0=keins
				writer.println("\">InnenKreuzRadius\"");
				writer.println("\"0\"");

				// Gibt an um wieviel Grad das Kreuz gedreht ist
				writer.println("\">InnenKreuzWinkel\"");
				writer.println("\"0\"");

				// Gibt an ob Vorhaltespiegel dargestellt werden, 0=keinen
				writer.println("\">VorhalteSpiegelRadius\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.SUSP_DIA) / 2));

				// Abstand der Vorhaltespiegel zur Scheibenmitte
				writer.println("\">VorhalteAbstand\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.SUSP_DISTANCE)));

				// Breite eines Ringes in 1/100mm
				writer.println("\">Ringbreite\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.RING_WIDTH)));
	
				// Nummer des kleinsten dargestellten Rings
				writer.println("\">RingMin\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.RING_MIN)));

				// Nummer des größten dargestellten Rings
				writer.println("\">RingMax\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.NUM_MAX)));

				// Anzahl der abgebildeten Ringe
				writer.println("\">RingAnzahl\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.RING_MAX)));

				// Anordnungswinkel der Beschriftungszahlen
				writer.println("\">RingNummerWinkel\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.NUM_ANGLE) * 45));

				// Breite des Scheibenkartons
				writer.println("\">KartonBreite\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.SIZE_WIDTH)));

				// Höhe des Scheibenkartons
				writer.println("\">KartonHoehe\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.SIZE_HEIGHT)));

				// Anzahl der Probeschüsse eines Durchgangs, bei -1 keine Probe,
				// bei 0 unbegrenzte Probe, an sonsten nach eingestellter Zahl
				writer.println("\">Probe\"");
				writer.println("\"-1\"");

				// Gibt an wie lange der Motor für den Bandvorschub läuft
				writer.println("\">BandVorschub\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.FEED)));
			}

			if (isDeerTarget()) {
				writer.println("\">Zielbild\"");
				writer.println(String.format("\"%s\"", this.image));

				// Bezeichnet die tatsächliche Breite des Zielbildes in 100tel mm
				writer.println("\">Kartonbreite\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.SIZE_WIDTH)));

				// Bezeichnet die tatsächliche Höhe des Zielbildes in 100tel mm
				writer.println("\">Kartonhöhe\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.SIZE_HEIGHT)));

				// Verschiebt die Treffermitte der Scheibe auf dem Anzeigebildschirm in 100tel mm nach rechts
				// und links. Wenn ein Schuß auf der Scheibe 10 cm zu weit links angezeigt wurde, kann hier,
				// durch Eingabe des Werts 10000 eine Korrektur vorgenommen werden.
				writer.println("\">ZoomzentrumX\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.ZOOM_CENTER_X)));

				// Verschiebt die Treffermitte der Scheibe auf dem Anzeigebildschirm in 100tel mm nach oben
				// und unten. Wenn ein Schuß auf der Scheibe 10 cm zu weit oben angezeigt wurde, kann hier,
				// durch Eingabe des Werts 10000 eine Korrektur vorgenommen werden.
				writer.println("\">ZoomzentrumY\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.ZOOM_CENTER_Y)));

				// Bezeichnet die Anzahl der Zoomstufen, die durchlaufen werden.
				writer.println("\">Zoomlevels\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.ZOOM_LEVELS)));

				// Verschiebt die Darstellung der Scheibe auf dem Anzeigebildschirm zur tatsächlichen
				// Messmitte der Anlage nach rechts und links. Dies ist nur von Nöten, wenn der Mittelpunkt
				// der Scheibe am Ziel, nicht auch der Messmittelpunkt der Messelektronik ist.
				writer.println("\">OffsetX\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.OFFSET_X)));

				// Verschiebt die Darstellung der Scheibe auf dem Anzeigebildschirm zur tatsächlichen
				// Messmitte der Anlage nach oben und unten. Dies ist nur von Nöten, wenn der Mittelpunkt
				// der Scheibe am Ziel, nicht auch der Messmittelpunkt der Messelektronik ist.
				writer.println("\">OffsetY\"");
				writer.println(String.format("\"%d\"", values.get(TargetValue.OFFSET_Y)));
			}

			// Gibt den Namen der Datei an, unter der sie im Verzeichnis C:\Programme\ESA2002 abgespeichert wird.
			writer.println("\">DateiName\"");
			writer.println(String.format("\"%s\"", fileName));

			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileName;
	}
}
