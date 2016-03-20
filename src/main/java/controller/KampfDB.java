package controller;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import model.Club;
import model.Discipline;
import model.Member;
import model.Rule;
import model.SettingsModel;


public class KampfDB {
	public static Set<Member> getSchuetzen() {
		Set<Member> set = new TreeSet<Member>();

		try {
			Database db = getDB("data.mdb");
			Table table = db.getTable("WettkampfSchuetzen");
			for (Row row : table) if ((byte) row.get("Sichtbar") == 1) set.add(new Member(row));
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	

		return set;
	}

	public static Set<Discipline> getDisziplinen(SettingsModel config) {
		Set<Discipline> set = new TreeSet<Discipline>();

		try {
			Database db = getDB("data.mdb");

			// Create or update all rules first
			for (Row row : db.getTable("Waffe")) {
				Rule rule = config.getRule((String) row.get("WaffengattungNr"));
				rule.setName((String) row.get("Beschreibung"));
			}

			// With known rules create disciplines
			for (Row row : db.getTable("Disziplin")) {
				Map<String, Integer> m = Collections.singletonMap("DisziplinID", (int) row.get("DisziplinID"));
				Row r = CursorBuilder.findRow(db.getTable("DisziplinWaffe"), m);
				if (r != null) {
					String number = (String) r.get("WaffengattungNr");
					Discipline d = new Discipline(row, number);
					set.add(d);
				} else {
				  System.out.println("Keine Waffengattung für " + (String) row.get("Bezeichnung") + " definiert. Disziplin wird ignoriert.");
				}
			}
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	

		return set;
	}

	public static Member[] getAllSchuetzen() {
		Vector<Member> list = new Vector<Member>();

		try {
			Database db = getDB("Kampf.mdb");
			Table table = db.getTable("WettkampfSchuetzen");
			for (Row row : table) list.add(new Member(row));
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		list.sort(null);
		return list.toArray(new Member[0]);
	}

	public static Club[] getVereine() {
		Vector<Club> list = new Vector<Club>();

		try {
			Database db = getDB("Kampf.mdb");
			Table table = db.getTable("Vereine");
			for(Row row : table) list.add(new Club(row));
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list.toArray(new Club[0]);
	}

	private static Database getDB(String filename) throws IOException {
		DatabaseBuilder dbb = new DatabaseBuilder();
		dbb.setReadOnly(true);
		dbb.setFile(new File(Controller.getPath(filename)));
		dbb.setCharset(Charset.forName("ISO-8859-1"));
		return dbb.open();
	}
}