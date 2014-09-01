package model;
import java.io.Serializable;

import com.healthmarketscience.jackcess.Row;


public class Disziplin implements Serializable, Comparable<Disziplin> {
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;			// Bezeichnung der Disziplin
	private int wertung;			// 0=Ringwertung, 1=Zehntelwertung
	private int schusszahl;			// Anzahl der Schüsse im Match
	private int schiesszeit;		// Zeit in Minuten für die Schüsse im Match
	private int stellungsanzahl;	// Anzahl der Stellungen
	private int probeschuesse;		// Anzahl der Probeschüsse (-1 = beliebig viele)
	private int probezeit;			// Zeit für Probeschüsse (-1 = ist in der Matchzeit enthalten)
	private int serienlaenge;		// Gibt die Länge der Serien an

	public Disziplin(Row row) {
		id = (int) row.get("DisziplinID");
		name = (String) row.get("Bezeichnung");
		wertung = (byte) row.get("Wertung");
		schusszahl = (short) row.get("Schusszahl");
		schiesszeit = (short) row.get("Schiesszeit");
		stellungsanzahl = (byte) row.get("Stellungsanzahl");
		probeschuesse = (short) row.get("Probeschuesse");
		probezeit = (short) row.get("Probezeit");
		serienlaenge = (short) row.get("Serienlaenge");
	}

	public int getId() {
		return id;
	}

	public int getWertung() {
		return wertung;
	}

	public int getSchusszahl() {
		return schusszahl * stellungsanzahl;
	}

	public int getSchiesszeit() {
		return schiesszeit;
	}

	public int getStellungsanzahl() {
		return stellungsanzahl;
	}
	
	public int getProbeschuesse() {
		return probeschuesse;
	}
	
	public int getProbezeit() {
		return probezeit;
	}
	
	public int getSerienlaenge() {
		return serienlaenge;
	}

	public int getSerienAnzahl() {
		return getSchusszahl() / serienlaenge;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Disziplin d) {
		if (d == null) return 1;
		return id - d.id;
	}
}