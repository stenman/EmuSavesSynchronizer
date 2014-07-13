package com.example.emusavescentralizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ExecutionController {

	@Autowired
	private SaveFilesLoader saveFilesLoader;

	@Autowired
	@Qualifier("saveNESToStorage")
	private SaveToStorage saveNES;
	@Autowired
	@Qualifier("saveSNESToStorage")
	private SaveToStorage saveSNES;

	private static final Logger logger = LoggerFactory.getLogger(ExecutionController.class);

	public void start() {
		while (true) {
			try {
				saveFilesLoader.loadStoredSaveFiles();
				// TODO: watch folder for changes --> save files to storage if changes
				// TODO: Använd Java JDK7 WatchService för att polla filsystemet för förändringar
				// TODO: http://fahdshariff.blogspot.co.uk/2011/08/java-7-watchservice-for-file-change.html
				Thread.sleep(1000);
			} catch (java.lang.InterruptedException ex) {
				return;
			}
		}
	}

}
