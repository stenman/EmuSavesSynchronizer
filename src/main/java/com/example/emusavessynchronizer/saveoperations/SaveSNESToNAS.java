package com.example.emusavessynchronizer.saveoperations;

import org.springframework.stereotype.Component;

@Component(value="saveSNESToNAS")
public class SaveSNESToNAS implements SaveToNAS {

	@Override
	public void save() {
		System.out.println("Saving SNES to NAS!");
	}

}
