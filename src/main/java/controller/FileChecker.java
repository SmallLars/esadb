package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

import model.Hit;
import model.LineReader;

import static java.nio.file.StandardWatchEventKinds.*;


public class FileChecker extends Thread {
	private WatchService watcher;
	private Vector<LineReader> lineReader;
	private boolean running;

	public FileChecker(int linienCount) {
		lineReader = new Vector<LineReader>(linienCount);
		running = true;
		
		Path dir = Paths.get(Controller.getPath("."));
		try {
			watcher = FileSystems.getDefault().newWatchService();
			dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE); 
		} catch (IOException e) {
			watcher = null;
			e.printStackTrace();
		}

		if (watcher != null) start();
	}

	public void exit() {
		try {
			running = false;
			watcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File folder = new File(Controller.getPath("."));
		File[] folderFiles = folder.listFiles();
		if (folderFiles == null) return;

		for (File file : folderFiles) {
			if (file.getName().matches(".*\\.((nrt)|(ctl)|(dat)|(def))")) {
				if (!file.delete()) {
					System.err.println("Can't remove " + file.getAbsolutePath());
				}
			}
		}
	}

	@Override
	public void run() {
		while (running) {
			try {
				WatchKey key = watcher.take();
				sleep(100);
				for (WatchEvent<?> event: key.pollEvents()) {
					Kind<?> kind = event.kind();
					String dateiname = event.context().toString();
					int linie;
					LineReader lr;
					switch (kind.name()) {
						case "ENTRY_CREATE":
							// HTreff<X>PN<Y>.ctl
							// HTreff<X>PN<Y>.nrt
							// HTreff<X>MN<Y>.ctl
							// HTreff<X>MN<Y>.nrt
							if (!dateiname.startsWith("HTreff")) break;

							int endIndex = dateiname.indexOf('N') - 1;
							linie = Integer.parseUnsignedInt(dateiname.substring(6, endIndex));
							lr = getLineReaderByNumber(linie);
							if (lr != null) {
								File datei = new File(dateiname);
								if (dateiname.endsWith(".ctl")) {
									FileInputStream reader = null;
									try {
										reader = new FileInputStream(dateiname);
										List<String> lines = IOUtils.readLines(reader, Charset.forName("UTF-8"));
										if (lines.size() > 0) lr.addTreffer(new Hit(lines.get(0)));
									} catch (IOException e) {
										e.printStackTrace();
									} finally {
										if (reader != null) IOUtils.closeQuietly(reader);
									}
								}
								if (!datei.delete()) {
									System.out.println(dateiname  + " konnte nicht gelöscht werden.");
								}
							}
							break;
						case "ENTRY_DELETE":
							// HServ<X>.ctl
							if (dateiname.startsWith("HServ") && dateiname.endsWith(".ctl")) {
								linie = Integer.parseUnsignedInt(dateiname.substring(5, dateiname.length() - 4));
								lr = getLineReaderByNumber(linie);
								if (lr != null) lr.reenable();
							}
							break;
						default: // OVERFLOW
							continue;
					} 
				}
				key.reset();
			} catch (InterruptedException | ClosedWatchServiceException e) {
				if (running) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean addLineReader(LineReader lr) {
		return lineReader.add(lr);
	}

	public int getLineReaderCount() {
		return lineReader.capacity();
	}
	
	private LineReader getLineReaderByNumber(int nummer) {
		for (LineReader lr : lineReader) {
			if (lr.getNummer() == nummer) return lr;
		}
		return null;
	}
}