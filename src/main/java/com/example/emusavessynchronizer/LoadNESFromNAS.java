package com.example.emusavessynchronizer;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "loadNESFromNAS")
public class LoadNESFromNAS implements LoadFromNAS {

	@Autowired
	private FileCopyService fileCopyService;

	@Autowired
	private AppProperties appProperties;

	private static final Logger logger = LoggerFactory.getLogger(LoadNESFromNAS.class);

	private File nas;
	private File localDisk;

	@PostConstruct
	private void init() {
		try {
			nas = new File(appProperties.getPropValue("nes.dir.central"));
			localDisk = new File(appProperties.getPropValue("nes.dir.local"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load() {
		System.out.println("Loading NES from NAS!");
		fileCopyService.copyDirectoryContent(nas, localDisk);
	}

}
