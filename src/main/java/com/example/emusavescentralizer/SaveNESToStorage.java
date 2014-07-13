package com.example.emusavescentralizer;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "saveNESToStorage")
public class SaveNESToStorage implements SaveToStorage {

	@Autowired
	private FileCopyService fileCopyService;
	
	private File centralStorage;
	private File localDisk;

	public SaveNESToStorage() {
		centralStorage = new File("c:\\temp\\src");
		localDisk = new File("c:\\temp\\dst");
	}

	@Override
	public void save() {
		System.out.println("Saving NES to storage!");
		fileCopyService.copyDirectoryContent(localDisk, centralStorage);
	}

}
