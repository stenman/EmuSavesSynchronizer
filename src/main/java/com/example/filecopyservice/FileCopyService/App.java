package com.example.filecopyservice.FileCopyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		while (true) {

			try {
				logger.info("It's alive!");
				Thread.sleep(2000);
			} catch (java.lang.InterruptedException ex) {
				return;
			}
		}
	}
}
