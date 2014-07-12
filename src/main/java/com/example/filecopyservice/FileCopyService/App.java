package com.example.filecopyservice.FileCopyService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		try {
			String line;
			Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				if (line.contains("nestopia.exe")) {
					// ladda nes-saves från NASEN till lokal disk
				}
				if (line.contains("snes9x-x64.exe") || line.contains("snes9x-x86.exe")) {
					// ladda snes-saves från NASEN till lokal disk
				}
			}
			input.close();
		} catch (Exception err) {
			err.printStackTrace();
		}

		// while (true) {
		//
		// try {
		// logger.info("It's alive!");
		// Thread.sleep(2000);
		// } catch (java.lang.InterruptedException ex) {
		// return;
		// }
		// }
	}
}
