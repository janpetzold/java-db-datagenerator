package de.janpetzold.migrator.model;

import java.util.List;

public class TargetEntry {
	private String type;
	private String column;
	private String table;
	private List<String> values;

	public TargetEntry(String type, String column, String table, List<String> values) {
		super();
		this.type = type;
		this.column = column;
		this.table = table;
		this.values = values;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
}
