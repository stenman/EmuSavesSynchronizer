package com.example.emusavescentralizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

@Component
public class ProcessFinder {

	public static boolean NESTOPIA_STARTED = false;
	public static boolean SNES9X_STARTED = false;

	public boolean isNestopiaStarted() {
		return NESTOPIA_STARTED;
	}

	public boolean isSnes9XStarted() {
		return SNES9X_STARTED;
	}

	public void updateRunningEmulatorProcesses() {

		NESTOPIA_STARTED = false;
		SNES9X_STARTED = false;

		String line;
		BufferedReader input = null;
		try {
			Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
			input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				if (line.contains("nestopia.exe")) {
					NESTOPIA_STARTED = true;
				}
				if (line.contains("snes9x-x64.exe") || line.contains("snes9x-x86.exe")) {
					SNES9X_STARTED = true;
				}
			}

			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
