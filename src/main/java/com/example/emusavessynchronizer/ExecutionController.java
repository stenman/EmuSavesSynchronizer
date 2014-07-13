package com.example.emusavessynchronizer;

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
	private HotFolderService hotFolderService;

	private static final Logger logger = LoggerFactory.getLogger(ExecutionController.class);

	public void start() {

		// TODO: watch folder for changes --> save files to storage if changes
		hotFolderService.watchDir();

		// while (true) {
		// try {
		// saveFilesLoader.loadStoredSaveFiles();
		//
		// Thread.sleep(1000);
		// } catch (java.lang.InterruptedException ex) {
		// return;
		// }
		// }
	}

}
