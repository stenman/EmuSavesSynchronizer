package com.example.emusavescentralizer;

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

	private static final Logger logger = LoggerFactory.getLogger(AppMain.class);

	public FileCopyService() {
		File source = new File("c:\\temp\\src");
		File dest = new File("c:\\temp\\dst");
		copyFileApacheCommons(source, dest);
	}

	private void copyFileApacheCommons(File source, File dest) {
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
