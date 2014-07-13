package com.example.emusavescentralizer;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "loadNESFromStorage")
public class LoadNESFromStorage implements LoadFromStorage {

	@Autowired
	private FileCopyService fileCopyService;

	@Autowired
	private AppProperties appProperties;

	private static final Logger logger = LoggerFactory.getLogger(LoadNESFromStorage.class);

	private File centralStorage;
	private File localDisk;

	@PostConstruct
	private void init() {
		try {
			centralStorage = new File(appProperties.getPropValue("nes.dir.central"));
			localDisk = new File(appProperties.getPropValue("nes.dir.local"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load() {
		System.out.println("Loading NES from storage!");
		fileCopyService.copyDirectoryContent(centralStorage, localDisk);
	}

}
