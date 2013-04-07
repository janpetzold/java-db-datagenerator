package de.janpetzold.migrator.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Configuration class for application-wide and immutable properties
 * 
 * @author Jan Petzold
 */
public class Config {
	private static String dbProperties = "db.properties";
	
	/**
	 * Read the properties file that is used to configure the database connection
	 * 
	 * @return Properties
	 */
	public static final Properties getDbProperties() {
		Properties props = new Properties();
		try {
			props.load(Config.class.getClassLoader().getResourceAsStream(dbProperties));			
		} catch (IOException e) {
			Log.log4j.error("Can't read the properties file " + dbProperties);
		}
		return props;
	}
}
