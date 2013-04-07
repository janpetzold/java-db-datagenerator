package de.janpetzold.migrator.dao.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.janpetzold.migrator.dao.TempDao;
import de.janpetzold.migrator.model.SourceEntry;
import de.janpetzold.migrator.model.TargetEntry;
import de.janpetzold.migrator.util.Log;

import redis.clients.jedis.Jedis;

public class RedisTempDao implements TempDao {
	private Jedis jedis = new Jedis("localhost");
	
	public Boolean checkAvailability() {
		try {
			String pong = this.jedis.ping();
			if(pong != null && pong.equals("PONG")) {
				return true;
			}
		} catch(Exception e) {
			Log.console("Cannot find Redis - please make sure that Redis is running.");
		}
		return false;
	}
	
	public void reset() {
		jedis.flushAll();
	}
	
	public List<TargetEntry> readAll(List<SourceEntry> fields) {
		List<TargetEntry> destinationFields = new ArrayList<TargetEntry>();
		
		for(SourceEntry field : fields) {
			// Get all stored values for the key
			Set<String> currentValues = this.jedis.smembers(field.getName());
			
			// Convert value to an ArrayList so we can easily get random values later
			List<String> valuesArray = new ArrayList<String>(currentValues);
			
			TargetEntry destinationField = new TargetEntry(field.getType(), field.getTargetColumn(), 
					field.getTargetTable(), valuesArray);
			
			destinationFields.add(destinationField);
		}
		return destinationFields;
	}
	
	public void save(String key, String value) {
		// Redis doesn't accept NULL values, but it might be a valid result
		if(value == null) {
			value = "NULL";
		}
		// Store value in unordered list
		this.jedis.sadd(key, value);
	}
}
