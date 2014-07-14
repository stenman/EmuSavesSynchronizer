package com.example.emusavessynchronizer.saveoperations;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.emusavessynchronizer.AppProperties;
import com.example.emusavessynchronizer.nes.NESOperations;
import com.example.emusavessynchronizer.snes.SNESOperations;
import com.example.emusavessynchronizer.utils.FileCopyService;
import com.example.emusavessynchronizer.utils.ProcessFinder;

@Component
public class StoreSaveFilesService {

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private FileCopyService fileCopyService;

	@Autowired
	private ProcessFinder processFinder;

	@Autowired
	private NESOperations nesOperations;

	@Autowired
	private SNESOperations snesOperations;

	private int checkForRunningProcessesInterval;

	@PostConstruct
	private void init() {
		try {
			checkForRunningProcessesInterval = Integer.parseInt(appProperties.getPropValue("check.for.running.processes.interval"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean NESTOPIA_RUNNING = false;
	public static boolean SNES9X_RUNNING = false;

	public void watchDirs() {

		Runnable r1 = new Runnable() {

			@Override
			public void run() {
				nesOperations.watchDir();
			}
		};
		Runnable r2 = new Runnable() {

			@Override
			public void run() {
				snesOperations.watchDir();
			}
		};

		Thread newThread1 = new Thread(r1);
		Thread newThread2 = new Thread(r2);

		while (true) {
			try {
				processFinder.updateRunningEmulatorProcesses();
				if (processFinder.isNestopiaRunning()) {
					if (!NESTOPIA_RUNNING) {
						newThread1 = new Thread(r1);
						newThread1.start();
					}
					NESTOPIA_RUNNING = true;
				} else {
					newThread1.interrupt();
					NESTOPIA_RUNNING = false;
				}
				if (processFinder.isSnes9XRunning()) {
					if (!SNES9X_RUNNING) {
						newThread2 = new Thread(r2);
						newThread2.start();
					}
					SNES9X_RUNNING = true;
				} else {
					newThread2.interrupt();
					SNES9X_RUNNING = false;
				}
				Thread.sleep(checkForRunningProcessesInterval);
			} catch (java.lang.InterruptedException ex) {
				return;
			}
		}
	}
}
