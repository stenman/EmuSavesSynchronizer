package com.example.emusavescentralizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SaveFilesLoader {
	
	@Autowired
	private ProcessFinder processFinder;
	@Autowired
	@Qualifier("loadNESFromStorage")
	private LoadFromStorage loadNES;
	@Autowired
	@Qualifier("loadSNESFromStorage")
	private LoadFromStorage loadSNES;

	private static boolean NES_SAVES_LOADED = false;
	private static boolean SNES_SAVES_LOADED = false;
	
	public void loadStoredSaveFiles() {

		processFinder.updateRunningEmulatorProcesses();

		if (processFinder.isNestopiaStarted()) {
			if (!NES_SAVES_LOADED) {
				loadNES.load();
				NES_SAVES_LOADED = true;
			}
		} else {
			NES_SAVES_LOADED = false;
		}

		if (processFinder.isSnes9XStarted()) {
			if (!SNES_SAVES_LOADED) {
				loadSNES.load();
				SNES_SAVES_LOADED = true;
			}
		} else {
			SNES_SAVES_LOADED = false;
		}
	}
}
