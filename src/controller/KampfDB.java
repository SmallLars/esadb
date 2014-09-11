package controller;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import model.Disziplin;
import model.Schuetze;
import model.Verein;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;


public class KampfDB {
	public static Set<Schuetze> getSchuetzen() {
		Table table = getTable("Stammdaten.mdb", "WettkampfSchuetzen");
		Set<Schuetze> set = new TreeSet<Schuetze>();

		if (table != null) {
			for (Row row : table) {
				if ((byte) row.get("Sichtbar") == 1) set.add(new Schuetze(row));
			}
		}

		return set;
	}

	public static Set<Disziplin> getDisziplinen() {
		Table table = getTable("Stammdaten.mdb", "Disziplin");
		Set<Disziplin> set = new TreeSet<Disziplin>();

		if (table != null)
			for (Row row : table) set.add(new Disziplin(row));

		return set;
	}

	public static Vector<Schuetze> getAllSchuetzen() {
		Table table = getTable("Kampf.mdb", "WettkampfSchuetzen");
		Vector<Schuetze> list = new Vector<Schuetze>();

		if (table != null) {
			for (Row row : table) list.add(new Schuetze(row));
		}
		list.sort(null);

		return list;
	}

	public static Vector<Verein> getVereine() {
		Table table = getTable("Kampf.mdb", "Vereine");
		Vector<Verein> list = new Vector<Verein>();

		if (table != null)
			for(Row row : table) list.add(new Verein(row));

		return list;
	}

	private static Table getTable(String filename, String tablename) {
		DatabaseBuilder dbb = new DatabaseBuilder();
		dbb.setReadOnly(true);
		dbb.setFile(new File(filename));
		try {
			Database db = dbb.open();
			return db.getTable(tablename);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}