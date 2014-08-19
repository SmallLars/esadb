package Model;

import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Config implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final File file = new File("config.esc");
	private static final double mmToDots = 72 / 2.54;

	private Rectangle mainWindow;
	private double pageWidht;
	private double pageHeight;
	private double pageImageableX;
	private double pageImageableY;
	private double pageImageableWidth;
	private double pageImageableHeight;
	private int pageOrientation;
	

	private Config() {
		mainWindow = new Rectangle(42, 42, 1196, 829);
		
		pageWidht =           21.0 * mmToDots;
		pageHeight =          29.7 * mmToDots;
		pageImageableX =       2.5 * mmToDots;
		pageImageableY =       1.0 * mmToDots;
		pageImageableWidth =  17.5 * mmToDots;
		pageImageableHeight = 27.7 * mmToDots;
		pageOrientation = PageFormat.PORTRAIT;

		save();
	}

	public Rectangle getMainWindowBouds() {
		return mainWindow;
	}

	public void setMainWindowBouds(Rectangle mainWindow) {
		this.mainWindow = mainWindow;
		save();
	}

	public PageFormat getPageFormat() {
		Paper p = new Paper();
		p.setSize(pageWidht, pageHeight);
		p.setImageableArea(pageImageableX, pageImageableY, pageImageableWidth, pageImageableHeight);
		PageFormat pf = new PageFormat();
		pf.setPaper(p);
		pf.setOrientation(pageOrientation);
		return pf;
	}

	public void setPageFormat(PageFormat pf) {
		pageWidht = pf.getPaper().getWidth();
		pageHeight = pf.getPaper().getHeight();
		pageImageableX = pf.getPaper().getImageableX();
		pageImageableY = pf.getPaper().getImageableY();
		pageImageableWidth = pf.getPaper().getImageableWidth();
		pageImageableHeight = pf.getPaper().getImageableHeight();
		pageOrientation = pf.getOrientation();
		save();
	}

	private boolean save() {
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

	public static Config load() {
		if (!file.exists()) return new Config();

		Config config = null;
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			if (obj instanceof Config) {
				config = (Config) obj;
			}
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (ois != null) try { ois.close(); } catch (IOException e) {}
			if (fis != null) try { fis.close(); } catch (IOException e) {}
		}

		return config;
	}
}