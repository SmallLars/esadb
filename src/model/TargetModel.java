package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang.Validate;


public class TargetModel implements Serializable, Comparable<TargetModel> {
	private static final long serialVersionUID = 1L;

	private String bezeichnung;
	private String kennnummer;
	private int size_width;
	private int size_height;
	private int bandvorschub;
	private int dia_spiegel;
	private int dia_aussen;
	private int dia_innenzehn;
	private int ringbreite;
	private int min_ring;
	private int max_ring;
	private int max_number;
	
	private int winkel;
	private int art;
	private int fill;           // 0 => Zehn und Innenzehn: Ringe       1=> Innezehn ausgefüllt    2=>     Zehn ausgefüllt, Innenzehn irrelevant 
	private int vorhaltedia;
	private int vorhalteabstand;

	private String image;
	private int zoom_center_x;
	private int zoom_center_y;
	private int zoom_levels;
	private int offset_x;
	private int offset_y;

	public TargetModel(String bezeichnung, String kennnummer, int... values) {
		Validate.isTrue(values.length >= 9, "need 9 or more values in array");
		
		this.bezeichnung = bezeichnung;
		this.kennnummer = kennnummer;
		this.image = "";
		for (int i = 0; i < 21; i++) {
			setValue(TargetValue.values()[i], i < values.length ? values[i] : 0);
		}
	}

	public TargetModel(TargetModel tm) {
		bezeichnung = tm.bezeichnung;
		kennnummer = tm.kennnummer;
		image = tm.image;
		for (TargetValue tv : TargetValue.values()) {
			setValue(tv, tm.getValue(tv));
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

	public void setNumber(String number) {
		this.kennnummer = number;
	}

	public String getNumber() {
		return kennnummer;
	}

	public void setValue(TargetValue type, int value) {
		switch (type) {
			case SIZE:
				size_width = value;
				size_height = value;
				break;
			case FEED:
				bandvorschub = value;
				break;
			case DIA_BLACK:
				if (value > dia_aussen) {
					dia_aussen = value;
				}
				dia_spiegel = value;
				break;
			case DIA_OUTSIDE:
				if (value < dia_spiegel) {
					dia_spiegel = value;
				}
				if (value < dia_innenzehn + 2 * (max_ring - min_ring) * ringbreite) {
					if (max_ring - min_ring > 1) max_ring --;
					else break;
					if (max_number >= max_ring) max_number--;
				}
				dia_aussen = value;
				break;
			case DIA_INNER_TEN:
				if (dia_aussen < value + 2 * (max_ring - min_ring) * ringbreite) {
					if (max_ring - min_ring > 1) max_ring --;
					else break;
					if (max_number >= max_ring) max_number--;
				}
				if (value > 0 && fill == 2) {
					fill = 1;
				}
				dia_innenzehn = value;
				break;
			case RING_WIDTH:
				if (dia_aussen < dia_innenzehn + 2 * (max_ring - min_ring) * value) {
					if (max_ring - min_ring > 1) max_ring --;
					else break;
					if (max_number >= max_ring) max_number--;
				}
				ringbreite = value;
				break;
			case RING_MIN:
				if (value < 0) {
					break;
				}
				if (value >= max_ring) {
					max_ring++;
				}
				if (value > max_number) {
					max_number++;
				}
				if (dia_aussen < dia_innenzehn + 2 * (max_ring - value) * ringbreite) {
					max_ring--;
					if (max_number >= max_ring) max_number--;
				}
				min_ring = value;
				break;
			case RING_MAX:
				if (value < 1) {
					break;
				}
				if (value <= min_ring && min_ring > 0) {
					min_ring--;
				}
				if (value <= max_number && max_number > 0) {
					max_number--;
				}
				if (dia_aussen < dia_innenzehn + 2 * (value - min_ring) * ringbreite) {
					min_ring++;
					if (max_number < min_ring) max_number++;
				}
				max_ring = value;
				break;
			case NUM_MAX:
				if (value >= max_ring)  {
					break;
				}
				if (value < min_ring) {
					break;
				}
				max_number = value;
				break;
			case NUM_ANGLE:
				winkel = value;
				break;
			case TYPE:
				art = value;
				break;
			case FILL:
				if (value == 2 && dia_innenzehn > 0) {
					dia_innenzehn = 0;
				}
				fill = value;
				break;
			case SUSP_DIA:
				vorhaltedia = value;
				break;
			case SUSP_DISTANCE:
				vorhalteabstand = value;
				break;
			case SIZE_WIDTH:
				size_width = value;
				break;
			case SIZE_HEIGHT:
				size_height = value;
				break;
			case ZOOM_CENTER_X:
				zoom_center_x = value;
				break;
			case ZOOM_CENTER_Y:
				zoom_center_y = value;
				break;
			case ZOOM_LEVELS:
				zoom_levels = value;
				break;
			case OFFSET_X:
				offset_x = value;
				break;
			case OFFSET_Y:
				offset_y = value;
				break;
		}
	}

	public int getValue(TargetValue type) {
		switch (type) {
			case SIZE:          return size_width;
			case FEED:          return bandvorschub;
			case DIA_BLACK:     return dia_spiegel;
			case DIA_OUTSIDE:   return dia_aussen;
			case DIA_INNER_TEN: return dia_innenzehn;
			case RING_WIDTH:    return ringbreite;
			case RING_MIN:      return min_ring;
			case RING_MAX:      return max_ring;
			case NUM_MAX:       return max_number;
			case NUM_ANGLE:     return winkel;
			case TYPE:          return art;
			case FILL:          return fill;
			case SUSP_DIA:      return vorhaltedia;
			case SUSP_DISTANCE: return vorhalteabstand;
			case SIZE_WIDTH:    return size_width;
			case SIZE_HEIGHT:   return size_height;
			case ZOOM_CENTER_X: return zoom_center_x;
			case ZOOM_CENTER_Y: return zoom_center_y;
			case ZOOM_LEVELS:   return zoom_levels;
			case OFFSET_X:      return offset_x;
			case OFFSET_Y:      return offset_y;
			default: return 0;
		}
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return this.image;
	}

	public boolean isRingTarget() {
		return art == 0 || art == 3 || art == 4 || art == 5;
	}

	public boolean isDeerTarget() {
		return art == 1 || art == 2 || art == 6;
	}

	public int getRingRadius(int i) {
		if (i < 0 || i > max_ring) return 0;
	
		return (dia_aussen / 2) - (i - min_ring) * ringbreite;
	}

	public int getAussenRadius() {
		return dia_aussen / 2;
	}
	
	public int getSpiegelRadius() {
		return dia_spiegel / 2;
	}

	public int getInnenZehnRadius() {
		return dia_innenzehn / 2;
	}
	
	public int getFontSize() {
		return (ringbreite) * 9 / 16;
	}

	public boolean drawRing(int i) {
		return i >= min_ring && i <= max_ring;
	}
	
	public boolean drawNumber(int i) {
		return i >= min_ring && i <= max_number;
	}

	public int getNumberRadius(int i) {
		return (getRingRadius(i) - ringbreite * 3 / 8);
	}

	public boolean blackNumber(int i) {
		return getNumberRadius(i) > (dia_spiegel / 2);
	}

	public String toFile() {
		NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
		format.setGroupingUsed(false);

		String fileName = String.format("0_hs_%s.def", kennnummer.replace('.', '-'));
		try {
			PrintWriter writer = new PrintWriter(fileName);

			writer.println("\">Bezeichnung\"");                                    // Name der Scheibe der bei der Programmauswahl angezeigt wird
			writer.println(String.format("\"%s\"", bezeichnung));

			writer.println("\">ScheibenArt\"");                                    // Systeminterne Scheibenbezeichnung
			writer.println(String.format("\"%d\"", art));
			                                                                       // 1 => Trefferzonenscheibe mit Klappscheibensteuerung
			                                                                       // 2 => Trefferzonenscheibe / Jagdscheibe
			                                                                       // 3 => Ringscheibe mit weißem Zehner und schwarzer Schrift
			                                                                       // 4 => Ringscheibe mit PA25PC - Modul
			                                                                       // 5 => Inverse Ringscheibe
			                                                                       // 6 => Trefferzonenscheibe mit Doppelsau

			writer.println("\">KennNummer\"");                                     // Kennnr. der Scheibe nach DSB/DJV nur Informativ
			writer.println(String.format("\"DSB %s\"", kennnummer));

			if (isRingTarget()) {
				writer.println("\">AussenRadius\"");                               // Radius des 1. Rings in 1/100 mm
				writer.println(String.format("\"%d\"", dia_aussen / 2));
				writer.println("\">SpiegelRadius\"");                              // Radius des Spiegels in 1/100 mm
				writer.println(String.format("\"%d\"", dia_spiegel / 2));			
	
				writer.println("\">ZehnerRadius\"");                               // Radius des kleinsten Rings in 1/100 mm
				writer.println(String.format("\"%d\"", getRingRadius(max_ring)));
				writer.println("\">ZehnerRingStyle\"");                            // Gibt die Optik des Zehnerrings vor, 0=ausgefüllt, 1=Ring
				writer.println(fill >= 2 ? "\"0\"": "\"1\"");
	
				writer.println("\">InnenZehnerRadius\"");                          // Radius des Innenzehners in 1/100mm
				writer.println(String.format("\"%d\"", dia_innenzehn / 2));
				writer.println("\">InnenZehnerRingStyle\"");                       // Gibt die Optik des Innenzehnerrings vor, 0=ausgefüllt, 1=Ring
				writer.println(fill >= 1 ? "\"0\"": "\"1\"");
	
				writer.println("\">InnenKreuzRadius\"");                           // Länge der Kreuzschenkel, zwei mal ergibt Kreuzlinien, 0=keins
				writer.println("\"0\"");
				writer.println("\">InnenKreuzWinkel\"");                           // Gibt an um wieviel Grad das Kreuz gedreht ist
				writer.println("\"0\"");
	
				writer.println("\">VorhalteSpiegelRadius\"");                      // Gibt an ob Vorhaltespiegel dargestellt werden, 0=keinen
				writer.println(String.format("\"%d\"", vorhaltedia / 2));
				writer.println("\">VorhalteAbstand\"");                            // Abstand der Vorhaltespiegel zur Scheibenmitte
				writer.println(String.format("\"%d\"", vorhalteabstand));
	
				writer.println("\">Ringbreite\"");                                 // Breite eines Ringes in 1/100mm
				writer.println(String.format("\"%d\"", ringbreite));
	
				writer.println("\">RingMin\"");                                    // Nummer des kleinsten dargestellten Rings
				writer.println(String.format("\"%d\"", min_ring));
				writer.println("\">RingMax\"");                                    // Nummer des größten dargestellten Rings
				writer.println(String.format("\"%d\"", max_number));
	
				writer.println("\">RingAnzahl\"");                                 // Anzahl der abgebildeten Ringe
				writer.println(String.format("\"%d\"", max_ring));
	
				writer.println("\">RingNummerWinkel\"");                           // Anordnungswinkel der Beschriftungszahlen
				writer.println(String.format("\"%d\"", winkel * 45));
	
				writer.println("\">KartonBreite\"");                               // Breite des Scheibenkartons
				writer.println(String.format("\"%d\"", size_width));
				writer.println("\">KartonHoehe\"");                                // Höhe des Scheibenkartons
				writer.println(String.format("\"%d\"", size_height));
	
				writer.println("\">Probe\"");                                      // Anzahl der Probeschüsse eines Durchgangs, bei -1 keine Probe,
				writer.println("\"-1\"");                                          // bei 0 unbegrenzte Probe, an sonsten nach eingestellter Zahl
	
				writer.println("\">BandVorschub\"");                               // Gibt an wie lange der Motor für den Bandvorschub läuft
				writer.println(String.format("\"%d\"", bandvorschub));
			}

			if (isDeerTarget()) {
				writer.println("\">Kartonbreite\"");                               // Bezeichnet die tatsächliche Breite des Zielbildes in 100tel mm
				writer.println(String.format("\"%d\"", size_width));
				writer.println("\">Kartonhöhe\"");                                 // Bezeichnet die tatsächliche Höhe des Zielbildes in 100tel mm
				writer.println(String.format("\"%d\"", size_height));

				writer.println("\">ZoomzentrumX\"");                               // Verschiebt die Treffermitte der Scheibe auf dem Anzeigebildschirm in 100tel mm nach rechts
				writer.println(String.format("\"%d\"", zoom_center_x));            // und links. Wenn ein Schuß auf der Scheibe 10 cm zu weit links angezeigt wurde, kann hier,
				                                                                   // durch Eingabe des Werts 10000 eine Korrektur vorgenommen werden.
				writer.println("\">ZoomzentrumY\"");                               // Verschiebt die Treffermitte der Scheibe auf dem Anzeigebildschirm in 100tel mm nach oben
				writer.println(String.format("\"%d\"", zoom_center_y));            // und unten. Wenn ein Schuß auf der Scheibe 10 cm zu weit oben angezeigt wurde, kann hier,
				                                                                   // durch Eingabe des Werts 10000 eine Korrektur vorgenommen werden.

				writer.println("\">Zoomlevels\"");                                 // Bezeichnet die Anzahl der Zoomstufen, die durchlaufen werden.
				writer.println(String.format("\"%d\"", zoom_levels));
	
				writer.println("\">OffsetX\"");                                    // Verschiebt die Darstellung der Scheibe auf dem Anzeigebildschirm zur tatsächlichen
				writer.println(String.format("\"%d\"", offset_x));                 // Messmitte der Anlage nach rechts und links. Dies ist nur von Nöten, wenn der Mittelpunkt
				                                                                   // der Scheibe am Ziel, nicht auch der Messmittelpunkt der Messelektronik ist.
				writer.println("\">OffsetY\"");                                    // Verschiebt die Darstellung der Scheibe auf dem Anzeigebildschirm zur tatsächlichen
				writer.println(String.format("\"%d\"", offset_y));                 // Messmitte der Anlage nach oben und unten. Dies ist nur von Nöten, wenn der Mittelpunkt
				                                                                   // der Scheibe am Ziel, nicht auch der Messmittelpunkt der Messelektronik ist.
			}

			writer.println("\">DateiName\"");                                      // Gibt den Namen der Datei an, unter der sie im Verzeichnis
			writer.println(String.format("\"%s\"", fileName));                     // C:\Programme\ESA2002 abgespeichert wird.

			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileName;
	}
}
