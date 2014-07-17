package com.example.emusavessynchronizer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

@Component
public class ProcessFinder {

	public static boolean NESTOPIA_RUNNING = false;
	public static boolean SNES9X_RUNNING = false;
	public static boolean EMUSAVESSYNCHRONIZER_RUNNING = false;

	public boolean isNestopiaRunning() {
		return NESTOPIA_RUNNING;
	}

	public boolean isSnes9XRunning() {
		return SNES9X_RUNNING;
	}

	public boolean isEmuSavesSynchronizerRunning() {
		return EMUSAVESSYNCHRONIZER_RUNNING;
	}

	public void updateRunningEmulatorProcesses() {

		NESTOPIA_RUNNING = false;
		SNES9X_RUNNING = false;
		EMUSAVESSYNCHRONIZER_RUNNING = false;

		String line;
		BufferedReader input = null;
		try {
			Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
			input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				if (line.contains("nestopia.exe")) {
					NESTOPIA_RUNNING = true;
				}
				if (line.contains("snes9x-x64.exe") || line.contains("snes9x-x86.exe")) {
					SNES9X_RUNNING = true;
				}
				if (line.contains("emusavessynchronizer.exe")) {
					EMUSAVESSYNCHRONIZER_RUNNING = true;
				}
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
