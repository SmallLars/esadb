package Controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.*;


public class FileChecker extends Thread {
	private WatchService watcher;
	private Controller controller;
	private boolean running;

	public FileChecker(Controller controller) {
		this.controller = controller;
		running = true;
		
		Path dir = Paths.get(".");
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

		File folder = new File(".");
		for (File file : folder.listFiles()) {
			if (file.getName().matches(".*\\.((nrt)|(ctl)|(dat))")) {
				if (!file.delete()) {
					System.err.println("Can't remove " + file.getAbsolutePath());
				}
			}
		}
		File file = new File("Kampf.mdb");
		if (!file.delete()) {
			System.err.println("Can't remove " + file.getAbsolutePath());
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

					switch (kind.name()) {
						case "ENTRY_CREATE":
							// HTreff<X>PN<Y>.ctl
							// HTreff<X>PN<Y>.nrt
							// HTreff<X>MN<Y>.ctl
							// HTreff<X>MN<Y>.nrt
							if (!dateiname.startsWith("HTreff")) break;

							File datei = new File(dateiname);
							if (dateiname.endsWith(".ctl")) {
								int linie = Integer.parseUnsignedInt(dateiname.substring(6, 7)) - 1;
								BufferedReader reader = new BufferedReader(new FileReader(dateiname));
								Linie l = controller.getLinie(linie);
								if (l != null) l.addTreffer(reader.readLine());
								reader.close();
							}
							datei.delete();
							break;
						case "ENTRY_DELETE":
							// HServ<X>.ctl
							if (dateiname.startsWith("HServ") && dateiname.endsWith(".ctl")) {
								int linie = Integer.parseUnsignedInt(dateiname.substring(5, 6)) - 1;
								Linie l = controller.getLinie(linie);
								if (l != null) l.reenable();
							}
							break;
						default: // OVERFLOW
							continue;
					} 
				}
				key.reset();
			} catch (InterruptedException | IOException | ClosedWatchServiceException e) {
				System.out.println("Err");
			}
		}
	}
}
