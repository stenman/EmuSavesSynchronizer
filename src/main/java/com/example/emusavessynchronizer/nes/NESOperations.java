package com.example.emusavessynchronizer.nes;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
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

@Component
public class NESOperations {

	private static final Logger logger = LoggerFactory.getLogger(NESOperations.class);

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
		logger.info("Loading NES saves from NAS");
		fileCopyService.copyDirectoryContent(nas.toFile(), localDisk.toFile());
	}

	public void watchDir() {

		validatePath(localDisk);

		logger.info("Starting NES watchdir service on directory: " + localDisk);

		try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {

			localDisk.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

			while (!Thread.currentThread().isInterrupted()) {

				final WatchKey key = watchService.take();

				for (WatchEvent<?> watchEvent : key.pollEvents()) {

					// to get the event type
					final WatchEvent.Kind<?> kind = watchEvent.kind();

					File localFile = new File(localDisk + "\\" + watchEvent.context().toString());

					if (OVERFLOW == kind) {
						continue;
					} else if (ENTRY_CREATE == kind) {
						logger.info("Created: " + watchEvent.context());
						fileCopyService.copyFile(localFile, nas.toFile());
					} else if (ENTRY_DELETE == kind) {
						logger.info("Deleted: " + watchEvent.context());
					} else if (ENTRY_MODIFY == kind) {
						logger.info("Modified: " + watchEvent.context());
						fileCopyService.copyFile(localFile, nas.toFile());
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
