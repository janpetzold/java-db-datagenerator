package de.janpetzold.migrator.dao;

import java.util.List;

import de.janpetzold.migrator.model.SourceEntry;
import de.janpetzold.migrator.model.TargetEntry;


public interface TempDao {
	
	public Boolean checkAvailability();
	
	public void reset();
	
	public List<TargetEntry> readAll(List<SourceEntry> fields);
	
	public void save(String key, String value);
}
