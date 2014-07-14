package com.example.emusavessynchronizer.loadoperations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.emusavessynchronizer.utils.ProcessFinder;

@Component
public class LoadSaveFilesService {

	@Autowired
	private ProcessFinder processFinder;
	@Autowired
	@Qualifier("loadNESFromNAS")
	private LoadFromNAS loadNES;
	@Autowired
	@Qualifier("loadSNESFromNAS")
	private LoadFromNAS loadSNES;

	private static boolean NES_SAVES_LOADED = false;
	private static boolean SNES_SAVES_LOADED = false;

	public void loadStoredSaveFiles() {

		while (true) {

			try {
				processFinder.updateRunningEmulatorProcesses();

				if (processFinder.isNestopiaRunning()) {
					if (!NES_SAVES_LOADED) {
						loadNES.load();
						NES_SAVES_LOADED = true;
					}
				} else {
					NES_SAVES_LOADED = false;
				}

				if (processFinder.isSnes9XRunning()) {
					if (!SNES_SAVES_LOADED) {
						loadSNES.load();
						SNES_SAVES_LOADED = true;
					}
				} else {
					SNES_SAVES_LOADED = false;
				}

				Thread.sleep(1000);
			} catch (java.lang.InterruptedException ex) {
				return;
			}
		}
	}
}
