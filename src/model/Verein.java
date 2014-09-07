package model;

import com.healthmarketscience.jackcess.Row;


public class Verein implements Comparable<Verein> {

	private int id;
	private String name;

	public Verein(Row row) {
		id = (int) row.get("VereinsNr");
		name = (String) row.get("Vereinsname");
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Verein d) {
		if (d == null) return 1;
		return id - d.id;
	}
}