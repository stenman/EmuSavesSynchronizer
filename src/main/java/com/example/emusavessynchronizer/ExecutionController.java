package com.example.emusavessynchronizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.emusavessynchronizer.loadoperations.LoadSaveFilesService;
import com.example.emusavessynchronizer.saveoperations.StoreSaveFilesService;

@Component
public class ExecutionController {

	@Autowired
	private LoadSaveFilesService loadSaveFilesService;
	@Autowired
	private StoreSaveFilesService storeSaveFilesService;

	private static final Logger logger = LoggerFactory.getLogger(ExecutionController.class);

	public void start() {

		// TODO: watch folder for changes --> save files to storage if changes
		Runnable r1 = new Runnable() {

			@Override
			public void run() {
				storeSaveFilesService.watchDirs();
			}
		};
		Runnable r2 = new Runnable() {

			@Override
			public void run() {
				loadSaveFilesService.loadStoredSaveFiles();
			}
		};

		Thread newThread1 = new Thread(r1);
		Thread newThread2 = new Thread(r2);
		newThread1.start();
		newThread2.start();

		// while (true) {
		// try {
		// saveFilesLoader.loadStoredSaveFiles();
		// if (processFinder.isNestopiaStarted()) {
		// }
		// if (processFinder.isNestopiaStarted()) {
		//
		// }
		// Thread.sleep(1000);
		// } catch (java.lang.InterruptedException ex) {
		// return;
		// }
		// }
	}

}
