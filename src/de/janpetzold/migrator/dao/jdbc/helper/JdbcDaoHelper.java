package de.janpetzold.migrator.dao.jdbc.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import de.janpetzold.migrator.util.Config;
import de.janpetzold.migrator.util.Log;


public class JdbcDaoHelper {
	public static Connection initConnection() {
		Properties props = Config.getDbProperties();
		try {
			Class.forName(props.getProperty("DRIVER"));
			return DriverManager.getConnection(props.getProperty("URL"), props.getProperty("USER"), props.getProperty("PASSWORD"));
		} catch (ClassNotFoundException e) {
			Log.log4j.error("Could not load database driver: " + e.getMessage());
		} catch (SQLException e) {
			Log.log4j.error("Database connection failed: " + e.getMessage());
		}
		return null;
	}
}
