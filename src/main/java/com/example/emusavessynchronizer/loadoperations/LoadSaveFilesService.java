package com.example.emusavessynchronizer.loadoperations;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.emusavessynchronizer.AppProperties;
import com.example.emusavessynchronizer.nes.NESOperations;
import com.example.emusavessynchronizer.snes.SNESOperations;
import com.example.emusavessynchronizer.utils.ProcessFinder;

@Component
public class LoadSaveFilesService {

	private static final Logger logger = LoggerFactory.getLogger(LoadSaveFilesService.class);
	
	@Autowired
	private AppProperties appProperties;

	@Autowired
	private ProcessFinder processFinder;

	@Autowired
	private NESOperations nesOperations;

	@Autowired
	private SNESOperations snesOperations;

	private static boolean NES_SAVES_LOADED = false;
	private static boolean SNES_SAVES_LOADED = false;

	private int checkForRunningProcessesInterval;

	@PostConstruct
	private void init() {
		try {
			checkForRunningProcessesInterval = Integer.parseInt(appProperties.getPropValue("check.for.running.processes.interval"));
		} catch (IOException e) {
			logger.error("Fatal error: "+e);
			e.printStackTrace();
		}
	}

	public void loadStoredSaveFiles() {

		while (true) {

			try {
				processFinder.updateRunningEmulatorProcesses();

				if (processFinder.isNestopiaRunning()) {
					if (!NES_SAVES_LOADED) {
						logger.info("nestopia.exe process started");
						nesOperations.load();
						NES_SAVES_LOADED = true;
					}
				} else {
					NES_SAVES_LOADED = false;
				}

				if (processFinder.isSnes9XRunning()) {
					if (!SNES_SAVES_LOADED) {
						logger.info("snes9x-x64.exe or snes9x-x86.exe process started");
						snesOperations.load();
						SNES_SAVES_LOADED = true;
					}
				} else {
					SNES_SAVES_LOADED = false;
				}
				Thread.sleep(checkForRunningProcessesInterval);
			} catch (java.lang.InterruptedException ex) {
				return;
			}
		}
	}
}
