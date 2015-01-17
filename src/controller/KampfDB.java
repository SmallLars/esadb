package controller;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import model.Disziplin;
import model.Schuetze;
import model.Verein;

import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;


public class KampfDB {
	public static Set<Schuetze> getSchuetzen() {
		Set<Schuetze> set = new TreeSet<Schuetze>();

		try {
			Database db = getDB("data.mdb");
			Table table = db.getTable("WettkampfSchuetzen");
			for (Row row : table) if ((byte) row.get("Sichtbar") == 1) set.add(new Schuetze(row));
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	

		return set;
	}

	public static Set<Disziplin> getDisziplinen() {
		Set<Disziplin> set = new TreeSet<Disziplin>();

		try {
			Database db = getDB("data.mdb");
			for (Row row : db.getTable("Disziplin")) {
				Disziplin d = new Disziplin(row);
				Table t = db.getTable("DisziplinWaffe");
				Map<String, Integer> m = Collections.singletonMap("DisziplinID", d.getId());
				Row r = CursorBuilder.findRow(t, m);
				if (r != null) {
					d.setRegel(r);
					set.add(d);
				} else {
				  System.out.println("Keine Waffengattung für " + d.toString() + " definiert. Disziplin wird ignoriert.");
				}
			}
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	

		return set;
	}

	public static Vector<Schuetze> getAllSchuetzen() {
		Vector<Schuetze> list = new Vector<Schuetze>();

		try {
			Database db = getDB("Kampf.mdb");
			Table table = db.getTable("WettkampfSchuetzen");
			for (Row row : table) list.add(new Schuetze(row));
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		list.sort(null);
		return list;
	}

	public static Vector<Verein> getVereine() {
		Vector<Verein> list = new Vector<Verein>();

		try {
			Database db = getDB("Kampf.mdb");
			Table table = db.getTable("Vereine");
			for(Row row : table) list.add(new Verein(row));
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	private static Database getDB(String filename) throws IOException {
		DatabaseBuilder dbb = new DatabaseBuilder();
		dbb.setReadOnly(true);
		dbb.setFile(new File(filename));
		return dbb.open();
	}
}