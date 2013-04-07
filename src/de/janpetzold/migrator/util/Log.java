package de.janpetzold.migrator.util;

import org.apache.log4j.Logger;

/**
 * This is just a simple log4j-based logger used by all classes in 
 * the project. It may easily be replaced by something more advanced.
 * 
 * @author Jan Petzold
 *
 */
public class Log {
	public static final Logger log4j = Logger.getLogger(Log.class);
	
	public static final void console(String message) {
		System.out.println(message);
	}
}
