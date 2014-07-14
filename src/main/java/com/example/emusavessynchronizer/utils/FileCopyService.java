package com.example.emusavessynchronizer.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class FileCopyService {

	private static final Logger logger = LoggerFactory.getLogger(FileCopyService.class);

	// TODO: These methods probably needs to be synchronized in order to avoid the "Failed to copy full contents from..." IOException that is
	// sometimes thrown due to lockdown of the file in question.

	public void copyFile(File source, File destination) {
		try {
			logger.debug("Copying " + source + " to " + destination);
			FileUtils.copyFileToDirectory(source, destination);
		} catch (IOException e) {
			logger.error("" + e);
			e.printStackTrace();
		}
	}

	public void copyDirectoryContent(File source, File destination) {
		try {
			logger.debug("Copying " + source + " to " + destination);
			FileUtils.copyDirectory(source, destination);
		} catch (IOException e) {
			logger.error("" + e);
			e.printStackTrace();
		}
	}
}
