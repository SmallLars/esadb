package model;


import java.awt.Color;
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

import controller.Controller;
import controller.KampfDB;


public class Model implements Serializable {
	private static final long serialVersionUID = 2L;

	private Set<Member> schuetzen;
	private Set<Discipline> disziplinen;
	private List<Start> ergebnisse;
	private List<Hit> treffer;
	private byte[] file;

	transient private List<Member> s;
	transient private List<Discipline> d;

	public Model(SettingsModel config) {
		ergebnisse = new Vector<Start>();
		schuetzen = KampfDB.getSchuetzen();
		disziplinen = KampfDB.getDisziplinen(config);
		treffer = new Vector<Hit>();
		try {
			file = Files.readAllBytes(Paths.get("data.mdb"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Files.write(Paths.get("Kampf.mdb"), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean add(Object o) {
		if (o instanceof Start) {
			return ergebnisse.add((Start) o);
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
		if (o instanceof Start) {
			return ergebnisse.remove((Start) o);
		}
		if (o instanceof Hit) {
			return treffer.remove((Hit) o);
		}
		return false;
	}

	public List<Single> getIncomplete() {
		Vector<Single> incomplete = new Vector<Single>();
		for (Start s : ergebnisse) {
			if (s instanceof Single) {
				Single e = (Single) s;
				if (!e.isEmpty() && !e.isComplete()) incomplete.add(e);
			}
		}
		return incomplete;
	}

	public List<Start> getErgebnisse() {
		return ergebnisse;
	}

	public List<Member> getSchuetzen() {
		if (s == null) s = new Vector<Member>(schuetzen);
		return s;
	}

	public List<Discipline> getDisziplinen() {
		if (d == null) d = new Vector<Discipline>(disziplinen); 
		return d;
	}

	public List<Hit> getTreffer() {
		return treffer;
	}

	public boolean save(File file) {
		file.getParentFile().mkdirs();

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
			if (oos != null) try { oos.close(); } catch (IOException e) {}
			if (fos != null) try { fos.close(); } catch (IOException e) {}
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
			if (model != null) Files.write(Paths.get("Kampf.mdb"), model.file);
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
		Color red = Color.decode("0xC80000");

		ergebnisse.sort(null);

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		ResultList resultList = new ResultList(Controller.get().getFileName(), sdf.format(new Date()));

		Discipline d = null;
		Group g = null;
		for (Start s : ergebnisse) {
			if (s.getGroup() == null) {
				String name = ((Single) s).getSchuetze().toString();
				Controller.get().println(name + " konnte keine Gruppe zugeordnet werden." , red);
				Controller.get().println("Das Ergebnis in der Diziplin " + s.getDisziplin() + " wird in der Ergebnisliste nicht aufgeführt." , red);
				continue;
			}

			if (s.getDisziplin() != d) {
				d = s.getDisziplin();
				resultList.addDiszipline(d.toString());
				g = null;
			}

			if (s.getGroup() != g) {
				g = s.getGroup();
				resultList.addGroup(g.toString());
			}
			resultList.addSingleResult((Single) s);
		}

		return resultList;
	}
}
