package com.example.emusavessynchronizer.nes;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.emusavessynchronizer.AppProperties;
import com.example.emusavessynchronizer.utils.FileCopyService;

@Component
public class NESOperations {

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private FileCopyService fileCopyService;

	private Path localDisk;
	private Path nas;

	@PostConstruct
	private void init() {
		try {
			nas = Paths.get(appProperties.getPropValue("nes.dir.nas"));
			localDisk = Paths.get(appProperties.getPropValue("nes.dir.local"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		System.out.println("Loading NES from NAS!");
		fileCopyService.copyDirectoryContent(nas.toFile(), localDisk.toFile());
	}

	public void watchDir() {

		validatePath(localDisk);

		try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {

			localDisk.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

			while (!Thread.currentThread().isInterrupted()) {

				final WatchKey key = watchService.take();

				for (WatchEvent<?> watchEvent : key.pollEvents()) {

					// to get the event type
					final WatchEvent.Kind<?> kind = watchEvent.kind();

					if (OVERFLOW == kind) {
						continue;
					} else if (ENTRY_CREATE == kind) {
						System.out.println("created: " + watchEvent.context());
						fileCopyService.copyDirectoryContent(localDisk.toFile(), nas.toFile());
					} else if (ENTRY_DELETE == kind) {
						System.out.println("deleted: " + watchEvent.context());
					} else if (ENTRY_MODIFY == kind) {
						System.out.println("modified: " + watchEvent.context());
						fileCopyService.copyDirectoryContent(localDisk.toFile(), nas.toFile());
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

}
