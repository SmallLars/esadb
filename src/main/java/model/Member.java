package model;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.healthmarketscience.jackcess.Row;


public class Member implements Serializable, Comparable<Member> {
	private static final long serialVersionUID = 2L;

	public int wettkampfID;			// Identifikationsnummer des Wettkampf
	public String passnummer;		// Passnumer des Schützen
	public String nachname;			// Nachname des Schützen
	public String vorname;			// Vorname des Schützen
	public String titelname;		// Bezeichnung in der Titelleiste
	public Date geburtsdatum;		// Geburtsdatum des Schützen
	public String geschlecht;		// m für männlich, w für weiblich
	public short startnummer;		// Startnummer des Schützen
	public int vereinsnummer;		// Identifikationsnummer des Vereins

	public Member() {
		wettkampfID = 0;
		passnummer = "0000000-0000";
		nachname = "Demoschütz";
		vorname = "Hugo";
		titelname = "Demoschütz, Hugo";
		Calendar cal = Calendar.getInstance();
		cal.set(2000, Calendar.JANUARY, 1); 
		geburtsdatum = cal.getTime();
		geschlecht = "m";
		startnummer = 0;
		vereinsnummer = 0;
	}

	public Member(Row row) {
		wettkampfID = (int) row.get("WettkampfID");
		passnummer = (String) row.get("Passnummer");
		nachname = (String) row.get("Nachname");
		vorname = (String) row.get("Vorname");
		titelname = (String) row.get("Titelname");
		geburtsdatum = (Date) row.get("Geburtsdatum");
		geschlecht = (String) row.get("Geschlecht");
		startnummer = (short) row.get("Startnummer");
		vereinsnummer = (int) row.get("VereinsNr");
	}

	@Override
	public String toString() {
		return nachname + ", " + vorname;
	}

	@Override
	public int hashCode() {
		return passnummer.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Member) {
			return ((Member) o).passnummer.equals(passnummer);
		}
		return false;
	}

	@Override
	public int compareTo(Member m) {
		if (m == this) return 0;
		if (m == null) return 1;

		int c = nachname.compareTo(m.nachname);
		if (c != 0) return c;

		c = vorname.compareTo(m.vorname);
		if (c != 0) return c;

		return passnummer.compareTo(m.passnummer);
	}

	public boolean isMale() {
		return geschlecht.equals("m");
	}

	public boolean isFemale() {
		return geschlecht.equals("w");
	}

	public int getBirthYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(geburtsdatum); 
		return cal.get(Calendar.YEAR);
	}
}