package model;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

import controller.KampfDB;


public class Model implements Serializable, Printable {
	private static final long serialVersionUID = 1L;

	private Set<Schuetze> schuetzen;
	private Set<Disziplin> disziplinen;
	private List<Start> ergebnisse;
	private byte[] file;

	transient private List<Schuetze> s;
	transient private List<Disziplin> d;

	public Model() {
		ergebnisse = new Vector<Start>();
		schuetzen = KampfDB.getSchuetzen();
		disziplinen = KampfDB.getDisziplinen();
		try {
			file = Files.readAllBytes(Paths.get("Stammdaten.mdb"));
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
		if (o instanceof Start) return ergebnisse.add((Start) o);
		if (o instanceof Schuetze) {
			s = null;
			return schuetzen.add((Schuetze) o);
		}
		if (o instanceof Disziplin) {
			d = null;
			return disziplinen.add((Disziplin) o);
		}
		return false;
	}

	public boolean contains(Object o) {
		if (o instanceof Schuetze) return schuetzen.contains(o);
		if (o instanceof Disziplin) return disziplinen.contains(o);
		return false;
	}

	public boolean remove(Start start) {
		return ergebnisse.remove(start);
	}

	public List<Start> getErgebnisse() {
		return ergebnisse;
	}

	public List<Schuetze> getSchuetzen() {
		if (s == null) s = new Vector<Schuetze>(schuetzen);
		return s;
	}

	public List<Disziplin> getDisziplinen() {
		if (d == null) d = new Vector<Disziplin>(disziplinen); 
		return d;
	}

	public boolean save(File file) {
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
			Object obj = ois.readObject();
			if (obj instanceof Model) {
				model = (Model) obj;
			}
			if (model != null) Files.write(Paths.get("Kampf.mdb"), model.file);
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (ois != null) try { ois.close(); } catch (IOException e) {}
			if (fis != null) try { fis.close(); } catch (IOException e) {}
		}
		return model;
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (ergebnisse.size() == 0) return Printable.NO_SUCH_PAGE;
		ergebnisse.sort(null);

		int status = Printable.NO_SUCH_PAGE;
		final double SCALE = 2000 / pageFormat.getImageableWidth();

		Graphics2D g2 = (Graphics2D) g;
		g2.scale(1.0 / SCALE, 1.0 / SCALE);
		g2.setFont(new Font("Consolas", Font.PLAIN, 48));
		
		final int lineHeight = g2.getFontMetrics().getHeight();
		final int pageLines = (int) (pageFormat.getImageableHeight() * SCALE / lineHeight) - 2;
		final int startY = (int) (pageFormat.getImageableY() * SCALE) + 3 * lineHeight;
		final int startX = (int) (pageFormat.getImageableX() * SCALE);

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String headline = String.format("%s - Seite %2d", sdf.format(new Date()), pageIndex + 1);
		double headLen = g2.getFontMetrics().getStringBounds(headline, null).getCenterX();
		g2.drawString(headline, startX + 1000 - (int) headLen, startY - 2 * lineHeight);

        int lineIndex = pageIndex * -pageLines;
		int platz = 1;
		Disziplin d = ergebnisse.get(0).getDisziplin();
		for (Start s : ergebnisse) {
			if (s.getDisziplin() != d) {
				if (lineIndex % pageLines != 0) lineIndex++;
				platz = 1;
				d = s.getDisziplin();
			}

			if (platz == 1) {
				if (lineIndex >= 0 && lineIndex < pageLines) {
					g2.drawString(s.getDisziplin().toString(), startX, startY + lineHeight * lineIndex);
					status = Printable.PAGE_EXISTS;
				}
				lineIndex++;
			}

			int size = s.lineCount();

			int available = (lineIndex < 0 ? -(lineIndex % pageLines) : pageLines - (lineIndex % pageLines));
			if (size > available) lineIndex += available;

			if (lineIndex >= 0 && lineIndex < pageLines) {
				Graphics ge = g2.create();
				ge.translate(startX, startY + lineHeight * lineIndex);
				s.draw(ge, platz);
				status = Printable.PAGE_EXISTS;
			}
			platz++;
			lineIndex += size;

			if (lineIndex >= pageLines) break;
		}

		return status;
	}
}
