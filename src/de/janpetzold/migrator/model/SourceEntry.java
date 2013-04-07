package de.janpetzold.migrator.model;

public class SourceEntry {
	private String name;
	private String type;
	private String sourceTable;
	private String targetTable;
	private String sourceColumn;
	private String targetColumn;
	
	public SourceEntry(String name, String type, String sourceTable, String sourceColumn, 
			String targetTable, String targetColumn) {
		super();
		this.name = name;
		this.type = type;
		this.sourceTable = sourceTable;
		this.targetTable = targetTable;
		this.sourceColumn = sourceColumn;
		this.targetColumn = targetColumn;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSourceTable() {
		return sourceTable;
	}
	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}

	public String getTargetTable() {
		return targetTable;
	}
	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

	public String getSourceColumn() {
		return sourceColumn;
	}
	public void setSourceColumn(String sourceColumn) {
		this.sourceColumn = sourceColumn;
	}

	public String getTargetColumn() {
		return targetColumn;
	}
	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}
}
