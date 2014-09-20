package controller;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.Config;
import model.Disziplin;
import model.Einzel;
import model.LineReader;
import model.Model;
import model.ModelChangeListener;
import model.Schuetze;
import model.Start;
import model.Treffer;

import org.apache.commons.io.FileUtils;

import view.GUI;


public class Controller {
	private Config config;
	private List<ModelChangeListener> modelChangeListener;
	
	private File file;
	private Model model;
	private FileChecker fileChecker;
	private GUI gui;

	private SimpleAttributeSet redStyle;

	public Controller() {
		redStyle = new SimpleAttributeSet();
		StyleConstants.setBold(redStyle, true);
		StyleConstants.setForeground(redStyle, Color.decode("0xC80000"));

		config = Config.load();
		modelChangeListener = new Vector<ModelChangeListener>();
		
		final String[] files = {"esadb.ico", "Stammdaten.mdb"};
		for (String s : files) {
			File file = new File(s);
			if (!file.exists()) {
				URL inputUrl = getClass().getResource("/" + s);
				try {
					FileUtils.copyURLToFile(inputUrl, file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		file = new File(sdf.format(new Date()) + ".esa");
		file = new File("2014-09-19.esa");
		if (file.exists()) {
			model = Model.load(file);
		} else {
			model = new Model();
		}

		Einzel e = (Einzel) model.getErgebnisse().get(17);
		e.addTreffer(new Treffer(6, 11, true,   8.3f, "i", 660f, -2022f, 2127f, 288f, 1403));
		e.addTreffer(new Treffer(6, 12, true,   9.8f, "j", -660f, 646f, 923f, 135f, 1421));
		e.addTreffer(new Treffer(6, 13, true,  10.2f, "m", 402f, -429f, 588f, 313f, 1437));
		e.addTreffer(new Treffer(6, 14, true,  10.1f, "k", 704f, 80f, 708f, 6f, 1464));
		e.addTreffer(new Treffer(6, 15, true,   8.6f, "j", -1287f, 1327f, 1848f, 134f, 1485));
		e.addTreffer(new Treffer(6,  1, false,  9.9f, "i", 13f, -823f, 822f, 270f, 1516));
		e.addTreffer(new Treffer(6,  2, false,  9.6f, "f", -1008f, 281f, 1045f, 164f, 1539));
		e.addTreffer(new Treffer(6,  3, false,  9.1f, "k", 1392f, -536f, 1492f, 338f, 1571));
		e.addTreffer(new Treffer(6,  4, false, 10.7f, "R", 101f, -178f, 204f, 299f, 1592));
		e.addTreffer(new Treffer(6,  5, false,  9.0f, "j", -1006f, 1168f, 1541f, 130f, 1606));
		e.addTreffer(new Treffer(6,  6, false,  9.5f, "i", -356f, -1065f, 1122f, 251f, 1628));
		e.addTreffer(new Treffer(6,  7, false, 10.4f, "k", 449f, 74f, 455f, 9f, 1651));
		e.addTreffer(new Treffer(6,  8, false, 10.2f, "g", 551f, 271f, 614f, 26f, 1683));
		e.addTreffer(new Treffer(6,  9, false,  8.4f, "i", 161f, -2034f, 2039f, 274f, 1719));
		e.addTreffer(new Treffer(6, 10, false,  9.5f, "k", 1162f, -205f, 1179f, 350f, 1733));
		e.addTreffer(new Treffer(6, 11, false, 10.7f, "R", 165f, -95f, 190f, 330f, 1748));
		e.addTreffer(new Treffer(6, 12, false,  9.6f, "m", 900f, -622f, 1094f, 325f, 1770));
		e.addTreffer(new Treffer(6, 13, false,  9.0f, "k", 1529f, -271f, 1553f, 349f, 1831));
		e.addTreffer(new Treffer(6, 14, false,  9.4f, "i", 125f, -1198f, 1204f, 276f, 1860));
		e.addTreffer(new Treffer(6, 15, false,  6.7f, "m", 2698f, -2035f, 3379f, 322f, 1889));
		e.addTreffer(new Treffer(6, 16, false,  7.7f, "m", 1956f, -1753f, 2626f, 318f, 1933));
		e.addTreffer(new Treffer(6, 17, false, 10.5f, "l", -138f, -311f, 339f, 246f, 1960));
		e.addTreffer(new Treffer(6, 18, false,  9.2f, "m", 665f, -1212f, 1382f, 298f, 1981));
		e.addTreffer(new Treffer(6, 19, false,  9.6f, "k", 1086f, -197f, 1103f, 349f, 2013));
		e.addTreffer(new Treffer(6, 20, false, 10.5f, "i", 111f, -347f, 363f, 287f, 2035));
		e.addTreffer(new Treffer(6, 21, false,  9.6f, "h", 64f, 1056f, 1058f, 86f, 2057));
		e.addTreffer(new Treffer(6, 22, false,  9.3f, "i", 406f, -1218f, 1283f, 288f, 2076));
		e.addTreffer(new Treffer(6, 23, false, 10.4f, "i", 70f, -455f, 459f, 278f, 2104));
		e.addTreffer(new Treffer(6, 24, false,  8.8f, "f", -1617f, 605f, 1726f, 159f, 2131));
		e.addTreffer(new Treffer(6, 25, false,  9.3f, "m", 989f, -880f, 1323f, 318f, 2156));
		e.addTreffer(new Treffer(6, 26, false,  9.3f, "m", 1117f, -737f, 1338f, 326f, 2428));
		e.addTreffer(new Treffer(6, 27, false, 10.0f, "i", 264f, -732f, 777f, 289f, 2453));
		e.addTreffer(new Treffer(6, 28, false,  9.7f, "l", -710f, -699f, 995f, 224f, 2481));
		e.addTreffer(new Treffer(6, 29, false,  9.4f, "f", -1172f, -333f, 1218f, 195f, 2510));
		e.addTreffer(new Treffer(6, 30, false, 10.7f, "R", -46f, -167f, 172f, 254f, 2532));
		e.addTreffer(new Treffer(6, 31, false, 10.4f, "h", 47f, 409f, 411f, 83f, 2586));
		e.addTreffer(new Treffer(6, 32, false,  7.9f, "l", -1810f, -1630f, 2434f, 222f, 2761));
		e.addTreffer(new Treffer(6, 33, false,  8.6f, "f", -1757f, -690f, 1886f, 201f, 2817));
		e.addTreffer(new Treffer(6, 34, false,  9.4f, "l", -584f, -1136f, 1276f, 242f, 2844));
		e.addTreffer(new Treffer(6, 35, false,  8.6f, "l", -1142f, -1453f, 1847f, 231f, 2867));
		e.addTreffer(new Treffer(6, 36, false,  9.4f, "m", 1120f, -615f, 1277f, 331f, 2886));
		e.addTreffer(new Treffer(6, 37, false,  8.9f, "i", 265f, -1593f, 1614f, 279f, 2903));
		e.addTreffer(new Treffer(6, 38, false,  9.6f, "h", -260f, 1029f, 1061f, 104f, 2914));
		e.addTreffer(new Treffer(6, 39, false, 10.1f, "h", 177f, 633f, 657f, 74f, 2924));
		e.addTreffer(new Treffer(6, 40, false,  9.0f, "j", -759f, 1389f, 1583f, 118f, 2934));
		e.addTreffer(new Treffer(6, 41, false,  8.4f, "f", -1975f, 350f, 2005f, 169f, 2944));
		e.addTreffer(new Treffer(6, 42, false, 10.0f, "i", -36f, -759f, 759f, 267f, 2957));
		e.addTreffer(new Treffer(6, 43, false,  8.2f, "f", -2165f, -60f, 2165f, 181f, 2967));
		e.addTreffer(new Treffer(6, 44, false,  8.5f, "l", -1770f, -914f, 1991f, 207f, 2982));
		e.addTreffer(new Treffer(6, 45, false,  9.8f, "h", 282f, 835f, 882f, 71f, 2995));
		e.addTreffer(new Treffer(6, 46, false,  8.6f, "l", -1614f, -1008f, 1902f, 211f, 3008));
		e.addTreffer(new Treffer(6, 47, false,  7.6f, "j", -2285f, 1361f, 2659f, 149f, 3032));
		e.addTreffer(new Treffer(6, 48, false,  8.8f, "l", -750f, -1537f, 1709f, 244f, 3048));
		e.addTreffer(new Treffer(6, 49, false,  8.2f, "l", -1804f, -1269f, 2204f, 215f, 3085));
		e.addTreffer(new Treffer(6, 50, false,  9.8f, "k", 906f, -72f, 908f, 355f, 3133));
		e.addTreffer(new Treffer(6, 51, false, 10.0f, "l", -486f, -541f, 726f, 228f, 3155));
		e.addTreffer(new Treffer(6, 52, false, 10.8f, "R", -74f, 96f, 121f, 127f, 3177));
		e.addTreffer(new Treffer(6, 53, false, 10.0f, "f", -780f, -19f, 779f, 181f, 3198));
		e.addTreffer(new Treffer(6, 54, false,  9.5f, "m", 510f, -1073f, 1187f, 295f, 3218));
		e.addTreffer(new Treffer(6, 55, false,  8.9f, "j", -1440f, 775f, 1635f, 151f, 3260));
		e.addTreffer(new Treffer(6, 56, false,  9.9f, "l", -371f, -732f, 819f, 243f, 3283));
		e.addTreffer(new Treffer(6, 57, false,  8.7f, "k", 1774f, -278f, 1796f, 351f, 3312));
		e.addTreffer(new Treffer(6, 58, false,  9.1f, "i", -110f, -1511f, 1514f, 265f, 3333));
		e.addTreffer(new Treffer(6, 59, false,  8.3f, "f", -2135f, -250f, 2148f, 186f, 3347));
		e.addTreffer(new Treffer(6, 60, false,  8.8f, "f", -1612f, -628f, 1729f, 201f, 3362));

		e = (Einzel) model.getErgebnisse().get(20);
		e.addTreffer(new Treffer(4,  1, true,   9.2f, "g", 1019f, 957f, 1398f, 43f, 133));
		e.addTreffer(new Treffer(4,  2, true,  10.5f, "h", -110f, 365f, 381f, 106f, 159));
		e.addTreffer(new Treffer(4,  3, true,   7.6f, "m", 2261f, -1498f, 2712f, 326f, 192));
		e.addTreffer(new Treffer(4,  4, true,   8.7f, "f", -1725f, 581f, 1820f, 161f, 220));
		e.addTreffer(new Treffer(4,  1, false, 10.8f, "R", -1f, -84f, 83f, 270f, 244));
		e.addTreffer(new Treffer(4,  2, false, 10.2f, "i", -233f, -570f, 615f, 247f, 273));
		e.addTreffer(new Treffer(4,  3, false, 10.2f, "l", -391f, -405f, 562f, 226f, 313));
		e.addTreffer(new Treffer(4,  4, false,  9.1f, "f", -1491f, 253f, 1512f, 170f, 346));
		e.addTreffer(new Treffer(4,  5, false,  8.8f, "m", 1544f, -676f, 1685f, 336f, 373));
		e.addTreffer(new Treffer(4,  6, false, 10.2f, "m", 316f, -504f, 594f, 302f, 401));
		e.addTreffer(new Treffer(4,  7, false, 10.4f, "i", 88f, -470f, 477f, 280f, 437));
		e.addTreffer(new Treffer(4,  8, false,  9.5f, "k", 1124f, 142f, 1133f, 7f, 476));
		e.addTreffer(new Treffer(4,  9, false,  9.0f, "i", 24f, -1551f, 1550f, 270f, 523));
		e.addTreffer(new Treffer(4, 10, false,  0.0f, "m", 10766f, -5616f, 12142f, 332f, 549));
		e.addTreffer(new Treffer(4, 11, false,  5.2f, "i", -492f, -4551f, 4577f, 263f, 581));
		e.addTreffer(new Treffer(4, 12, false,  3.4f, "i", 1714f, -5753f, 6002f, 286f, 601));
		e.addTreffer(new Treffer(4, 13, false,  2.4f, "i", -971f, -6759f, 6828f, 261f, 620));

		fileChecker = new FileChecker(config.getLinienCount());
		gui = new GUI(this, config.getLinienCount());

		PrintStream ps = new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				gui.print(String.valueOf((char) b), redStyle);
			}
			
		}, false);
		System.setOut(ps);
		System.setErr(ps);
	}

	public Config getConfig() {
		return config;
	}

	public File getFile() {
		return file;
	}
	
	public Model getModel() {
		return model;
	}

	public void neu(File file) {
		this.file = file;
		model = new Model();
		save(file);
		modelChanged();
	}

	public void load(File file) {
		this.file = file;
		model = Model.load(file);
		modelChanged();
	}

	public void save(File file) {
		this.file = file;
		model.save(file);
	}

	public void exit() {
		fileChecker.exit();
	}

	public boolean contains(Object item) {
		return model.contains(item);
	}

	public List<Schuetze> getSchuetzen() {
		return model.getSchuetzen();
	}

	public List<Disziplin> getDisziplinen() {
		return  model.getDisziplinen();
	}

	public boolean add(Object o) {
		if (o instanceof LineReader) {
			return fileChecker.addLineReader((LineReader) o);
		}

		if (model.add(o)) {
			modelChanged();
			save(file);
			return true;
		}

		return false;
	}

	public void println(String string, SimpleAttributeSet style) {
		gui.println(string, style);
	}

	public boolean remove(Start s) {
		return model.remove(s);
	}

	public void addModelChangeListener(ModelChangeListener l) {
		modelChangeListener.add(l);
	}

	public void removeModelChangeListener(ModelChangeListener l) {
		modelChangeListener.remove(l);
	}

	private void modelChanged() {
		for (ModelChangeListener l : modelChangeListener) {
			l.modelChanged();
		}
	}

	public static void main(String[] args) {
		new Controller();
	}
}