package com.example.emusavescentralizer;

import org.springframework.stereotype.Component;

@Component(value="loadSNESFromStorage")
public class LoadSNESFromStorage implements LoadFromStorage {

	@Override
	public void load() {
		System.out.println("Loading SNES from storage!");
	}

}
