package de.janpetzold.migrator.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.janpetzold.migrator.dao.ProductionDao;
import de.janpetzold.migrator.dao.TempDao;
import de.janpetzold.migrator.dao.jdbc.helper.JdbcDaoHelper;
import de.janpetzold.migrator.dao.redis.RedisTempDao;
import de.janpetzold.migrator.model.SourceEntry;
import de.janpetzold.migrator.model.TargetEntry;
import de.janpetzold.migrator.util.Log;


public class JdbcProductionDao implements ProductionDao {
	private TempDao tempDao = new RedisTempDao();
	
	@Override
	public Boolean checkAvailability() {
		Connection con = JdbcDaoHelper.initConnection();
		try {
			if(con.isValid(0)) {
				con.close();
				return true;
			}
		} catch (SQLException e) {
			Log.console("JDBC-database not found - please make sure that your RDBS is running.");
		} 
		return false;
	}
	
	@Override
	public void readAll(List<SourceEntry> fields) {
		// Get a DB connection
		Connection con = JdbcDaoHelper.initConnection();
		
		if(con != null) {
			try {
				// Iterate through all the InputFields and read the unique source values
				for(SourceEntry field : fields) {
					String sql = "SELECT DISTINCT " + field.getSourceColumn() + " FROM " + field.getSourceTable();
					Statement stmt = con.createStatement();

					ResultSet rs = stmt.executeQuery(sql);
					
					while(rs.next()) {
						Log.log4j.debug("Column " + field.getSourceColumn() + " has value " + rs.getString(field.getSourceColumn()));
						// Save field name and value in temporary datastore
						tempDao.save(field.getName(), rs.getString(field.getSourceColumn()));
					}
					rs.close();
					stmt.close();
				}
				con.close();
			} catch (SQLException e) {
				Log.log4j.error("Could not read source data: " + e.getMessage());
			}
		}
	}
	
	@Override
	public void save(int numEntries, List<SourceEntry> sFields, List<TargetEntry> dFields) {
		// Get all the destination tables first
		Set<String> targetTables = this.getTargetTables(sFields);
		
		// Initiate connection
		Connection con = JdbcDaoHelper.initConnection();

		try {
			// Disable autocommit to improve performance
			con.setAutoCommit(false);
			
			int insertCounter = 0;
			
			// Create the SQL-Statements for the INSERTs in all destination tables and execute these
			for(String targetTable : targetTables) {
				Statement stmt = con.createStatement();
				
				for(int i = 0; i < numEntries; i++) {
					insertCounter++;
					String sql = this.getInsertStmt(targetTable, dFields);
					stmt.addBatch(sql);
					
					// Commit at least every 500 datasets - we have the insertCounter for that
					if(insertCounter > 499) {
						stmt.executeBatch();
						con.commit();
						insertCounter = 0;
						Log.log4j.info("Committed 500 entries, continuing...");
					}
				}
				stmt.executeBatch();
				con.commit();
				stmt.close();
			}
			con.close();
		} 
		catch (SQLException e) {
			Log.log4j.error("Could not insert the generated data: " + e.getMessage());
		}
	}
	
	private Set<String> getTargetTables(List<SourceEntry> sFields) {
		Set<String> targetTables = new HashSet<String>();
		for(SourceEntry field : sFields) {
			targetTables.add(field.getTargetTable());
		}
		return targetTables;
	}
	
	private String getInsertStmt(String table, List<TargetEntry> fields) {
		String columns = "";
		String values = "";
		
		for(TargetEntry field : fields) {
			if(field.getTable().equals(table)) {
				columns = columns + field.getColumn() + ",";
				values = values + this.getRandomValidValue(field) + ",";
			}
		}
		
		// Remove the last comma
		columns = columns.substring(0, (columns.length() - 1));
		values = values.substring(0, (values.length() - 1));
		
		return "INSERT INTO " + table + " (" + columns + ") VALUES(" + values + ")";
	}
	
	private String getRandomValidValue(TargetEntry field) {
		List<String> allValues = field.getValues();
		
		int maxRandom = allValues.size() - 1;
		int random = new Random().nextInt(maxRandom + 1);
		
		// Pick a random value from all the values (allValues is an ArrayList)
		String currentValue = allValues.get(random);
		
		// Check the value type and return it properly so that we can use it for the SQL 
		if(currentValue.equals("") || currentValue.equals("NULL")) {
			return "NULL";
		}else if(field.getType().equals("number")) {
			return currentValue;
		} else if(field.getType().equals("date")) {
			return "to_timestamp('" + currentValue + "', 'yyyy-MM-dd HH24:MI:SS.ff3')";
		} else {
			return "'" + String.valueOf(currentValue) + "'";
		}
	}
}
