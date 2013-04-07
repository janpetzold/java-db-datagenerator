package de.janpetzold.migrator.util;

public class Trace {
	private long startTime = 0;
	
	public void start() {
		this.startTime = System.currentTimeMillis();
	}
	
	public void stop() {
		long passedTime = System.currentTimeMillis() - this.startTime;
		Log.log4j.info("The operation took " + passedTime + " ms");
		this.startTime = 0;
	}
}
