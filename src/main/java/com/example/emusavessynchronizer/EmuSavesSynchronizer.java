package com.example.emusavessynchronizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmuSavesSynchronizer {

	@Autowired
	private ExecutionController executionController;

	public void startService() {
		executionController.start();
	}
}
