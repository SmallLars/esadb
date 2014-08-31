package controller;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import model.Disziplin;
import model.Schuetze;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;


public class KampfDB {
	public static Set<Schuetze> getSchuetzen() {
		Table table = getTable("WettkampfSchuetzen");
		Set<Schuetze> set = new TreeSet<Schuetze>();

		if (table != null) {
			for(Row row : table) {
				if ((byte) row.get("Sichtbar") == 1) set.add(new Schuetze(row));
			}
		}

		return set;
	}

	public static Vector<Disziplin> getDisziplinen() {
		Table table = getTable("Disziplin");
		Vector<Disziplin> vector = new Vector<Disziplin>();

		if (table != null)
			for(Row row : table) vector.add(new Disziplin(row));

		return vector;
	}

	private static Table getTable(String tablename) {
		DatabaseBuilder dbb = new DatabaseBuilder();
		dbb.setReadOnly(true);
		dbb.setFile(new File("Stammdaten.mdb"));
		try {
			Database db = dbb.open();
			return db.getTable(tablename);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}