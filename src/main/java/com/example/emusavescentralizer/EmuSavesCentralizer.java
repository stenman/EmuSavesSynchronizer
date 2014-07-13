package com.example.emusavescentralizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmuSavesCentralizer {

	@Autowired
	private ExecutionController executionController;

	public void startService() {
		executionController.start();
	}
}
