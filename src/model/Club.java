package model;

import com.healthmarketscience.jackcess.Row;


public class Club implements Comparable<Club> {

	private int id;
	private String name;

	public Club(Row row) {
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
	public int compareTo(Club d) {
		if (d == null) return 1;
		return id - d.id;
	}
}