package com.example.emusavessynchronizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppMain {

	private static final Logger logger = LoggerFactory.getLogger(AppMain.class);

	private static ApplicationContext context;

	public static void main(String[] args) {

		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		EmuSavesSynchronizer ess = (EmuSavesSynchronizer) context.getBean("emuSavesSynchronizer");
		logger.info("Service starting");
		ess.startService();
		logger.info("Service stopped");
	}
}
