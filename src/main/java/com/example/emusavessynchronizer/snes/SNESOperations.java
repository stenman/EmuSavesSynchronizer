package com.example.emusavessynchronizer.snes;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.emusavessynchronizer.AppProperties;
import com.example.emusavessynchronizer.utils.FileCopyService;

// Watching its own directory for changes
// Loading saves to its own directory (when triggered by LoadSaveFilesService)
@Component
public class SNESOperations {

	private static final Logger logger = LoggerFactory.getLogger(SNESOperations.class);

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private FileCopyService fileCopyService;

	private Path nas;
	private Path localDisk;

	@PostConstruct
	private void init() {
		try {
			nas = Paths.get(appProperties.getPropValue("snes.dir.nas"));
			localDisk = Paths.get(appProperties.getPropValue("snes.dir.local"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		logger.info("Loading SNES saves from NAS");
		fileCopyService.copyDirectoryContent(nas.toFile(), localDisk.toFile());
	}

	public void watchDir() {

		validatePath(localDisk);

		logger.info("Starting SNES watchdir service on directory: " + localDisk);

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
						logger.info("created: " + watchEvent.context());
						fileCopyService.copyDirectoryContent(localDisk.toFile(), nas.toFile());
					} else if (ENTRY_DELETE == kind) {
						logger.info("deleted: " + watchEvent.context());
					} else if (ENTRY_MODIFY == kind) {
						logger.info("modified: " + watchEvent.context());
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
			logger.error("" + e);
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
			logger.error("" + ioe);
			ioe.printStackTrace();
		}
	}

}
