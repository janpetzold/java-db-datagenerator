package de.janpetzold.migrator;

import java.util.List;

import de.janpetzold.migrator.dao.ProductionDao;
import de.janpetzold.migrator.dao.redis.RedisTempDao;
import de.janpetzold.migrator.jdbc.JdbcProductionDao;
import de.janpetzold.migrator.model.SourceEntry;
import de.janpetzold.migrator.model.TargetEntry;
import de.janpetzold.migrator.util.Log;
import de.janpetzold.migrator.util.Parameters;
import de.janpetzold.migrator.util.XmlInputParser;


class App {
	public static void main(String[] args) {
		Log.log4j.info("Initializing the application");
		
		int mode = Parameters.checkParameters(args);
		
		RedisTempDao redisDao = new RedisTempDao();
		ProductionDao jdbcDao = new JdbcProductionDao();
		
		// Check if the necessary DBMs are available
		if(redisDao.checkAvailability() && jdbcDao.checkAvailability()) {
			// Mode 1 reads data from source
			if(mode == 1) {
				readData(redisDao, jdbcDao);
			}
			
			// Mode 2 writes (randomized) data to destination
			if(mode == 2) {
				// Set the amount of entries by command-line parameter
				Integer entries = Integer.parseInt(args[1]);
				if(entries != null && entries > 0) {
					writeData(redisDao, jdbcDao, entries);
				}
			}
		} 
	}
	
	private static void readData(RedisTempDao redisDao, ProductionDao jdbcDao) {
		Log.log4j.info("Reading existing data from database...");
		// Reset Redis
		redisDao.reset();
		
		// Get source table names, fields and types
		XmlInputParser parser = new XmlInputParser();
		List<SourceEntry> fields = parser.getTablesAndFields("config/dbfields.xml");
		
		// Read the unique values from all the fields and save them to Redis
		jdbcDao.readAll(fields);
		Log.log4j.info("All data was read and stored into Redis");
	}
	
	private static void writeData(RedisTempDao redisDao, ProductionDao jdbcDao, Integer entries) {
		Log.log4j.info("Writing " + entries + " new entries into the database...");
		
		// Get source table names, fields and types
		XmlInputParser parser = new XmlInputParser();
		List<SourceEntry> sFields = parser.getTablesAndFields("config/dbfields.xml");
		
		// Get the source data from Redis
		List<TargetEntry> dFields = redisDao.readAll(sFields);

		jdbcDao.save(entries, sFields, dFields);
		Log.log4j.info("Done! " + entries + " new entries have been written.");
	}
}
