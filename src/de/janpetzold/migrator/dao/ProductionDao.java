package de.janpetzold.migrator.dao;

import java.util.List;

import de.janpetzold.migrator.model.SourceEntry;
import de.janpetzold.migrator.model.TargetEntry;


public interface ProductionDao {
	
	public Boolean checkAvailability();

	public void readAll(List<SourceEntry> fields);

	public void save(int numEntries, List<SourceEntry> sFields,
			List<TargetEntry> dFields);

}