package com.example.emusavessynchronizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.emusavessynchronizer.loadoperations.SynchronizingService;
import com.example.emusavessynchronizer.saveoperations.WatchDirectoryService;

@Component
public class ExecutionController {

	@Autowired
	private SynchronizingService loadSaveFilesService;
	@Autowired
	private WatchDirectoryService storeSaveFilesService;

	private static final Logger logger = LoggerFactory.getLogger(ExecutionController.class);

	public void start() {

		Runnable r1 = new Runnable() {

			@Override
			public void run() {
				loadSaveFilesService.loadStoredSaveFiles();
			}
		};
		Runnable r2 = new Runnable() {

			@Override
			public void run() {
				storeSaveFilesService.watchDirs();
			}
		};

		Thread newThread1 = new Thread(r1);
		Thread newThread2 = new Thread(r2);
		newThread1.start();
		logger.info("loadSaveFilesService started");
		newThread2.start();
		logger.info("storeSaveFilesService started");

	}

}
