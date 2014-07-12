package com.example.emusavescentralizer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AppMain {

	private static final Logger logger = LoggerFactory.getLogger(AppMain.class);

//	@Autowired
//	private FileCopyService fileCopier;

	public static void main(String[] args) {
		
		File source = new File("c:\\temp\\src");
		File dest = new File("c:\\temp\\dst");
		copyFileApacheCommons(source, dest);

		// while (true) {
		// try {
		// try {
		// String line;
		// Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
		// BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		// while ((line = input.readLine()) != null) {
		// if (line.contains("nestopia.exe")) {
		// // ladda nes-saves från NASEN till lokal disk
		// }
		// if (line.contains("snes9x-x64.exe") || line.contains("snes9x-x86.exe")) {
		// // ladda snes-saves från NASEN till lokal disk
		// }
		// }
		// input.close();
		// } catch (Exception err) {
		// err.printStackTrace();
		// }
		// Thread.sleep(5000);
		// } catch (java.lang.InterruptedException ex) {
		// return;
		// }
		// }
	}

	private static void copyFileApacheCommons(File source, File dest) {
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
