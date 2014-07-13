package com.example.emusavessynchronizer;

import org.springframework.stereotype.Component;

@Component(value="saveSNESToStorage")
public class SaveSNESToStorage implements SaveToStorage {

	@Override
	public void save() {
		System.out.println("Saving SNES to storage!");
	}

}
