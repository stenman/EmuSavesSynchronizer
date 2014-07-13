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
import org.springframework.stereotype.Component;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

@Component
public class HotFolderService {

	@Autowired
	private AppProperties appProperties;

	private String central;
	private String local;

	@PostConstruct
	private void init() {
		try {
			central = appProperties.getPropValue("nes.dir.central");
			local = appProperties.getPropValue("nes.dir.local");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void watchDir() {
		try {
			final WatchService watchService = FileSystems.getDefault().newWatchService();

			final Path path = Paths.get(local);

			path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

			while (true) {

				final WatchKey key = watchService.take();

				for (WatchEvent<?> watchEvent : key.pollEvents()) {

					final WatchEvent<Path> ev = (WatchEvent<Path>) watchEvent;
					final Path filename = ev.context();

					final WatchEvent.Kind<?> kind = watchEvent.kind();

					System.out.println(kind + ": " + filename);
				}

				boolean valid = key.reset();

				if (!valid) {
					break;
				}
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
