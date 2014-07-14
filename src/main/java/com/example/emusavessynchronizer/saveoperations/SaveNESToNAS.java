package com.example.emusavessynchronizer.saveoperations;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.emusavessynchronizer.utils.FileCopyService;

@Component(value = "saveNESToNAS")
public class SaveNESToNAS implements SaveToNAS {

	@Autowired
	private FileCopyService fileCopyService;
	
	private File nas;
	private File localDisk;

	public SaveNESToNAS() {
		nas = new File("c:\\temp\\src");
		localDisk = new File("c:\\temp\\dst");
	}

	@Override
	public void save() {
		System.out.println("Saving NES to NAS!");
		fileCopyService.copyDirectoryContent(localDisk, nas);
	}

}
