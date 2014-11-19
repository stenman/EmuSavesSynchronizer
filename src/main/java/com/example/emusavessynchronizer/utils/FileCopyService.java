package com.example.emusavessynchronizer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class FileCopyService {

	private static final Logger logger = LoggerFactory.getLogger(FileCopyService.class);

	public void doCopyFile(File src, File dst) throws FileSystemException {
		int count = 0;
		int maxTries = 3;
		while (true) {
			try {
				Files.copy(new File(src.toString()).toPath(), new File(dst.toString()).toPath(), StandardCopyOption.REPLACE_EXISTING,
						LinkOption.NOFOLLOW_LINKS);
				logger.info(src.getName() + " copied successfully!");
				break;
			} catch (FileSystemException e) {
				logger.error("Error copying file " + src.getName() + ", will retry " + (maxTries - count - 1) + " more times. Exception thrown: " + e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (++count == maxTries) {
					throw e;
				}
			} catch (FileNotFoundException e) {
				logger.error("Error copying file " + src.getName() + ": " + e);
			} catch (IOException e) {
				logger.error("Error copying file " + src.getName() + ": " + e);
			}
		}
	}

	// public void doCopyFile(File src, File dst) {
	// try (FileInputStream in = new FileInputStream(src)) {
	// try (FileOutputStream out = new FileOutputStream(dst)) {
	// copyFile(in, out, src.getName());
	// }
	// } catch (FileNotFoundException e) {
	// logger.error("Exception occured when trying to copy file " + src.toString() + " to " + dst.toString() + " ---> " + e);
	// } catch (IOException e) {
	// logger.error("Exception occured when trying to copy file " + src.toString() + " to " + dst.toString() + " ---> " + e);
	// }
	// }
	//
	// private void copyFile(FileInputStream in, FileOutputStream out, String fileName) throws IOException {
	// try {
	// FileChannel cin = in.getChannel();
	// FileChannel cout = out.getChannel();
	// cin.transferTo(0, cin.size(), cout);
	// logger.info(fileName + " copied successfully!");
	// } catch (FileNotFoundException e) {
	// logger.error("Error when copying file " + fileName + e);
	// } catch (IOException e) {
	// logger.error("Error when copying file " + fileName + e);
	// }
	//
	// }

	public void doCopyDirectory(File source, File destination) {
		logger.debug("Copying directory content of " + source + " to " + destination);
		File[] directoryListing = source.listFiles();
		if (directoryListing != null) {
			for (File file : directoryListing) {
				try {
					doCopyFile(file, new File(destination + "\\" + file.getName()));
				} catch (FileSystemException e) {
					logger.error("Error copying file " + file.getName() + ": " + e);
				}
			}
		} else {
			logger.error("Exception occured when trying to copy directory content of " + source.toString() + " to " + destination.toString());
		}
	}
}
