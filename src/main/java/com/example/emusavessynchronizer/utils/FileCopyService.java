package com.example.emusavessynchronizer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class FileCopyService {

	private static final Logger logger = LoggerFactory.getLogger(FileCopyService.class);

	public void doCopyFile(File src, File dst) {
		try (FileInputStream in = new FileInputStream(src)) {
			try (FileOutputStream out = new FileOutputStream(dst)) {
				copyFile(in, out, src.getName());
			}
		} catch (FileNotFoundException e) {
			logger.error("Exception occured when trying to copy file " + src.toString() + " to " + dst.toString() + " ---> " + e);
		} catch (IOException e) {
			logger.error("Exception occured when trying to copy file " + src.toString() + " to " + dst.toString() + " ---> " + e);
		}
	}

	private void copyFile(FileInputStream in, FileOutputStream out, String fileName) throws IOException {
		try {
			FileChannel cin = in.getChannel();
			FileChannel cout = out.getChannel();
			cin.transferTo(0, cin.size(), cout);
			logger.info(fileName + " copied successfully!");
		} catch (FileNotFoundException e) {
			logger.error("" + e);
		} catch (IOException e) {
			logger.error("" + e);
		}

	}

	public void doCopyDirectory(File source, File destination) {
		logger.debug("Copying directory content of " + source + " to " + destination);
		File[] directoryListing = source.listFiles();
		if (directoryListing != null) {
			for (File file : directoryListing) {
				doCopyFile(file, new File(destination + "\\" + file.getName()));
			}
		} else {
			logger.error("Exception occured when trying to copy directory content of " + source.toString() + " to " + destination.toString());
		}
	}
}
