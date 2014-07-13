package com.example.emusavescentralizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ExecutionController {

	@Autowired
	@Qualifier("saveNESToStorage")
	private SaveToStorage saveNES;
	@Autowired
	@Qualifier("saveSNESToStorage")
	private SaveToStorage saveSNES;
	@Autowired
	@Qualifier("loadNESFromStorage")
	private LoadFromStorage loadNES;
	@Autowired
	@Qualifier("loadSNESFromStorage")
	private LoadFromStorage loadSNES;

	private static final Logger logger = LoggerFactory.getLogger(ExecutionController.class);

	private static boolean NESTOPIA_STARTED = false;
	private static boolean SNES9X_STARTED = false;
	private static boolean NES_SAVES_LOADED = false;
	private static boolean SNES_SAVES_LOADED = false;

	// TODO: Använd Java JDK7 WatchService för att polla filsystemet för förändringar
	// http://fahdshariff.blogspot.co.uk/2011/08/java-7-watchservice-for-file-change.html

	public void start() {
		while (true) {
			try {
				try {
					String line;
					Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
					BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
					while ((line = input.readLine()) != null) {
						if (line.contains("nestopia.exe")) {
							NESTOPIA_STARTED = true;
						}
						if (line.contains("snes9x-x64.exe") || line.contains("snes9x-x86.exe")) {
							SNES9X_STARTED = true;
						}
					}
					input.close();

					if (NESTOPIA_STARTED) {
						NESTOPIA_STARTED = false;
						if (!NES_SAVES_LOADED) {
							loadNES.load();
							NES_SAVES_LOADED = true;
						}
					} else {
						NES_SAVES_LOADED = false;
					}

					if (SNES9X_STARTED) {
						SNES9X_STARTED = false;
						if (!SNES_SAVES_LOADED) {
							loadSNES.load();
							SNES_SAVES_LOADED = true;
						}
					} else {
						SNES_SAVES_LOADED = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Thread.sleep(1000);
			} catch (java.lang.InterruptedException ex) {
				return;
			}
		}
	}

}
