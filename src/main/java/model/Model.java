package model;


import java.awt.print.Printable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

import controller.Controller;
import controller.KampfDB;


public class Model implements Serializable {
	private static final long serialVersionUID = 2L;

	private Set<Member> schuetzen;
	private Set<Discipline> disziplinen;
	private List<Result> ergebnisse;
	private List<Hit> treffer;
	private byte[] file;

	transient private List<Member> s;
	transient private List<Discipline> d;

	public Model(SettingsModel config) {
		ergebnisse = new Vector<Result>();
		schuetzen = KampfDB.getActiveMembers();
		disziplinen = KampfDB.getDisciplines(config);
		treffer = new Vector<Hit>();
		try {
			file = Files.readAllBytes(Paths.get(Controller.getPath("data.mdb")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Files.write(Paths.get(Controller.getPath("Kampf.mdb")), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean add(Object o) {
		if (o instanceof Result) {
			return ergebnisse.add((Result) o);
		}
		if (o instanceof Member) {
			s = null;
			return schuetzen.add((Member) o);
		}
		if (o instanceof Discipline) {
			d = null;
			return disziplinen.add((Discipline) o);
		}
		if (o instanceof Hit) {
			return treffer.add((Hit) o);
		}
		return false;
	}

	public boolean contains(Object o) {
		if (o instanceof Member) return schuetzen.contains(o);
		if (o instanceof Discipline) return disziplinen.contains(o);
		return false;
	}

	public boolean remove(Object o) {
		if (o instanceof Result) {
			return ergebnisse.remove((Result) o);
		}
		if (o instanceof Hit) {
			return treffer.remove((Hit) o);
		}
		return false;
	}

	public List<Single> getIncomplete() {
		Vector<Single> incomplete = new Vector<Single>();
		for (Result s : ergebnisse) {
			if (s instanceof Single) {
				Single e = (Single) s;
				if (!e.isEmpty() && !e.isComplete()) incomplete.add(e);
			}
		}
		return incomplete;
	}

	public List<Result> getErgebnisse() {
		return ergebnisse;
	}

	public List<Member> getSchuetzen() {
		if (s == null) s = new Vector<Member>(schuetzen);
		return s;
	}

	public Discipline[] getDisziplinen() {
		if (d == null) d = new Vector<Discipline>(disziplinen); 
		return d.toArray(new Discipline[0]);
	}

	public List<Hit> getTreffer() {
		return treffer;
	}

	public boolean save(File file) {
		File parent = file.getParentFile();
		if (!parent.exists() && !parent.mkdirs()) return false;

		boolean succeed = false;
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			succeed = true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(fos);
		}
		return succeed;
	}

	public static Model load(File file) {
		Model model = null;
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();// hier
			if (obj instanceof Model) {
				model = (Model) obj;
			}
			if (model != null) Files.write(Paths.get(Controller.getPath("Kampf.mdb")), model.file);
		}
		catch (InvalidClassException e) {}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (ois != null) try { ois.close(); } catch (IOException e) {}
			if (fis != null) try { fis.close(); } catch (IOException e) {}
		}
		return model;
	}

	public Printable getPrintable() {
		SettingsModel settings = Controller.get().getConfig();

		List<Result> toPrint;
		if (settings.getValue("ResultListSingleDiscipline", false)) {
			toPrint = new Vector<Result>();
			for (Result s : ergebnisse) if (s.getDisziplin().getId() == settings.getValue("ResultListDiscipline", 0)) toPrint.add(s);
		} else {
			toPrint = new Vector<Result>(ergebnisse);
		}
		toPrint.sort(null);

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		ResultList resultList = new ResultList(Controller.get().getFileName(), sdf.format(new Date()));

		Discipline d = null;
		Group g = null;
		for (Result r : toPrint) {
			if (!r.getDisziplin().equals(d)) {
				if (settings.getValue("ResultListNewPage", false)) resultList.addNewPage();
				d = r.getDisziplin();
				resultList.addDiszipline(d.toString());
				g = null;
			}

			if (!r.getGroup(true).equals(g)) {
				g = r.getGroup(true);
				resultList.addGroup(g.toString());
			}

			if (r instanceof Single) {
				resultList.addSingleResult(r);
			} else {
				resultList.addTeamResult(r);
			}
		}

		return resultList;
	}
}
