package com.atoz.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	public static final Properties pros1;
	static{
	InputStream input = PropertiesUtil.class.getResourceAsStream("../../../jdbc.properties");
	InputStream input1 = PropertiesUtil.class.getResourceAsStream("../../../log4j.properties");
	InputStream input2 = PropertiesUtil.class.getResourceAsStream("../../../catchHtml.properties");
	 pros1 = new Properties();
		try {
			pros1.load(input);
			pros1.load(input1);
			pros1.load(input2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
