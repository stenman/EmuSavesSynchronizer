package com.example.emusavessynchronizer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

@Component
public class StoreSaveFilesService {

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private FileCopyService fileCopyService;

	@Autowired
	private ProcessFinder processFinder;

	@Autowired
	@Qualifier("saveNESToNAS")
	private SaveToNAS saveNES;
	@Autowired
	@Qualifier("saveSNESToNAS")
	private SaveToNAS saveSNES;

	public static boolean NESTOPIA_RUNNING = false;
	public static boolean SNES9X_RUNNING = false;
	private Path localNES;
	private Path localSNES;

	@PostConstruct
	private void init() {
		try {
			localNES = Paths.get(appProperties.getPropValue("nes.dir.local"));
			localSNES = Paths.get(appProperties.getPropValue("snes.dir.local"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void validatePath(Path path) {
		try {
			Boolean isFolder = (Boolean) Files.getAttribute(path, "basic:isDirectory", NOFOLLOW_LINKS);
			if (!isFolder) {
				throw new IllegalArgumentException("Path: " + path + " is not a directory");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void watchDirs() {

		Runnable r1 = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("running 1!");
				watchDir(localNES);
			}
		};
		Runnable r2 = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("running 2!");
				watchDir(localSNES);
			}
		};

		Thread newThread1 = new Thread(r1);
		Thread newThread2 = new Thread(r2);

		while (true) {
			try {
				processFinder.updateRunningEmulatorProcesses();
				if (processFinder.isNestopiaRunning()) {
					if (!NESTOPIA_RUNNING) {
						System.out.println("startar övervakning av NES dir");
						newThread1 = new Thread(r1);
						newThread1.start();
						// watchDir(localNES);
					}
					NESTOPIA_RUNNING = true;
				} else {
					newThread1.interrupt();
					NESTOPIA_RUNNING = false;
				}
				if (processFinder.isSnes9XRunning()) {
					if (!SNES9X_RUNNING) {
						System.out.println("startar övervakning av SNES dir");
						newThread2 = new Thread(r2);
						newThread2.start();
						// watchDir(localSNES);
					}
					SNES9X_RUNNING = true;
				} else {
					newThread2.interrupt();
					SNES9X_RUNNING = false;
				}
				Thread.sleep(1000);
			} catch (java.lang.InterruptedException ex) {
				return;
			}
		}

		// Thread newThread1 = new Thread(r1);
		// Thread newThread2 = new Thread(r2);
		// newThread1.start();
		// newThread2.start();

	}

	private void watchDir(Path path) {

		validatePath(path);

		try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {

			path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

			while (!Thread.currentThread().isInterrupted()) {

				final WatchKey key = watchService.take();

				for (WatchEvent<?> watchEvent : key.pollEvents()) {

					// to get the filename
					final WatchEvent<Path> ev = (WatchEvent<Path>) watchEvent;
					// to get the event type
					final WatchEvent.Kind<?> kind = watchEvent.kind();

					if (OVERFLOW == kind) {
						continue;
					} else if (ENTRY_CREATE == kind) {
						System.out.println("created: " + watchEvent.context());

					} else if (ENTRY_DELETE == kind) {
						System.out.println("deleted: " + watchEvent.context());
					} else if (ENTRY_MODIFY == kind) {
						System.out.println("modified: " + watchEvent.context());
					}
				}

				if (!key.reset()) {
					break;
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
