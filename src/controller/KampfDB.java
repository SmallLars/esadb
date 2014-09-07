package controller;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

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
			for(Row row : table) {
				if ((byte) row.get("Sichtbar") == 1) set.add(new Schuetze(row));
			}
		}

		return set;
	}

	public static Set<Disziplin> getDisziplinen() {
		Table table = getTable("Stammdaten.mdb", "Disziplin");
		Set<Disziplin> set = new TreeSet<Disziplin>();

		if (table != null)
			for(Row row : table) set.add(new Disziplin(row));

		return set;
	}

	public static Set<Schuetze> getNewSchuetzen() {
		Table table = getTable("Kampf.mdb", "WettkampfSchuetzen");
		Set<Schuetze> set = new TreeSet<Schuetze>();

		if (table != null) {
			for(Row row : table) {
				if ((byte) row.get("Sichtbar") == 0) set.add(new Schuetze(row));
			}
		}

		return set;
	}

	public static Verein[] getVereine() {
		Table table = getTable("Kampf.mdb", "Vereine");
		Set<Verein> set = new TreeSet<Verein>();

		if (table != null) {
			for(Row row : table) {
				set.add(new Verein(row));
			}
		}

		return set.toArray(new Verein[0]);
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