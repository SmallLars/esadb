package controller;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
	public static Set<Member> getActiveMembers() {
		return getMembers(true, "data.mdb");
	}

	public static Set<Member> getAllMembers() {
		return getMembers(false, "data.mdb");
	}

	public static Set<Discipline> getDisciplines(SettingsModel config) {
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
				Map<String, Integer> m = Collections.singletonMap("DisziplinID", (Integer) row.get("DisziplinID"));
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

	public static Set<Member> getMembers() {
		return getMembers(false, "Kampf.mdb");
	}

	public static Set<Club> getClubs() {
		Set<Club> set = new TreeSet<Club>();

		try {
			Database db = getDB("Kampf.mdb");
			Table table = db.getTable("Vereine");
			for(Row row : table) set.add(new Club(row));
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return set;
	}

	private static Database getDB(String filename) throws IOException {
		DatabaseBuilder dbb = new DatabaseBuilder();
		dbb.setReadOnly(true);
		dbb.setFile(new File(Controller.getPath(filename)));
		dbb.setCharset(Charset.forName("ISO-8859-1"));
		return dbb.open();
	}

	private static Set<Member> getMembers(boolean onlyActive, String file) {
		Set<Member> set = new TreeSet<Member>();

		try {
			Database db = getDB(file);
			Table table = db.getTable("WettkampfSchuetzen");
			for (Row row : table) {
				if (!onlyActive || (byte) row.get("Sichtbar") == 1) {
					set.add(new Member(row));
				}
			}
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	

		return set;
	}
}
