package com.example.emusavessynchronizer.loadoperations;

import org.springframework.stereotype.Component;

@Component(value="loadSNESFromNAS")
public class LoadSNESFromNAS implements LoadFromNAS {

	@Override
	public void load() {
		System.out.println("Loading SNES from NAS!");
	}

}
