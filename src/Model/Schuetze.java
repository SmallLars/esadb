package Model;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.healthmarketscience.jackcess.Row;


public class Schuetze implements Serializable, Comparable<Schuetze> {
	private static final long serialVersionUID = 1L;

	public int wettkampfID;			// Identifikationsnummer des Wettkampf
	public String passnummer;		// Passnumer des Sch�tzen
	public String nachname;			// Nachname des Sch�tzen
	public String vorname;			// Vorname des Sch�tzen
	public String titelname;		// Bezeichnung in der Titelleiste
	public Date geburtsdatum;		// Geburtsdatum des Sch�tzen
	public String geschlecht;		// -1 f�r m�nnlich, 0 f�r weiblich
	public short startnummer;		// Startnummer des Sch�tzen

	public Schuetze() {
		wettkampfID = 0;
		passnummer = "0000000-0000";
		nachname = "Demosch�tz";
		vorname = "Hugo";
		titelname = "Demosch�tz, Hugo";
		Calendar cal = Calendar.getInstance();
		cal.set(2000, Calendar.JANUARY, 1); 
		geburtsdatum = cal.getTime();
		geschlecht = "m";
		startnummer = 0;
	}

	public Schuetze(Row row) {
		wettkampfID = (int) row.get("WettkampfID");
		passnummer = (String) row.get("Passnummer");
		nachname = (String) row.get("Nachname");
		vorname = (String) row.get("Vorname");
		titelname = (String) row.get("Titelname");
		geburtsdatum = (Date) row.get("Geburtsdatum");
		geschlecht = (String) row.get("Geschlecht");
		startnummer = (short) row.get("Startnummer");
	}

	@Override
	public String toString() {
		return nachname + ", " + vorname;
	}

	@Override
	public int compareTo(Schuetze s) {
		if (s == null) return 1;
		int c = nachname.compareTo(s.nachname);
		if (c != 0) return c;
		return vorname.compareTo(s.vorname);
	}
}