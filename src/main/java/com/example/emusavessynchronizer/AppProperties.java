package com.example.emusavessynchronizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class AppProperties {

	public String getPropValue(String property) throws IOException {

		Properties prop = new Properties();
		String propFileName = "config.properties";

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		if (null == inputStream) {
			throw new FileNotFoundException("Property file '" + propFileName + "' not found in classpath");
		}
		prop.load(inputStream);

		String result = prop.getProperty(property);

		if (null == result) {
			throw new IOException("Property file containing null values or misspelled property keys");
		}

		return prop.getProperty(property);
	}
}
