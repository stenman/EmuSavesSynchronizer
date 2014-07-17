package com.example.emusavessynchronizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.emusavessynchronizer.utils.ProcessFinder;

@Component
public class EmuSavesSynchronizer {

	@Autowired
	private ExecutionController executionController;

	@Autowired
	private ProcessFinder processFinder;

	private static final Logger logger = LoggerFactory.getLogger(EmuSavesSynchronizer.class);

	public void startService() {
		processFinder.updateRunningEmulatorProcesses();
		if (processFinder.isEmuSavesSynchronizerRunning()) {
			logger.error("An instance of Emulator Saves Synchronizer is already running. Stopping execution.");
			System.exit(0);
		} else {
			executionController.start();
		}
	}
}
