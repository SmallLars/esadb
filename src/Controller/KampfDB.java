package Controller;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import Model.Disziplin;
import Model.Schuetze;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;


public class KampfDB {

	public static Vector<Schuetze> getSchuetzen() {
		Table table = getTable("WettkampfSchuetzen");
		Vector<Schuetze> vector = new Vector<Schuetze>();

		if (table != null)
			for(Row row : table) vector.add(new Schuetze(row));

		return vector;
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
		dbb.setFile(new File("Kampf.mdb"));
		try {
			Database db = dbb.open();
			return db.getTable(tablename);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
