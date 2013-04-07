package de.janpetzold.migrator.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.janpetzold.migrator.model.SourceEntry;

public class XmlInputParser {
	private List<SourceEntry> sourceFields = new ArrayList<SourceEntry>();
	
	public final List<SourceEntry> getTablesAndFields(String fileName) {
		Log.log4j.debug("Reading the XML configuration at " + fileName);
		
		File file = new File(fileName);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);
			
			NodeList allFields = doc.getElementsByTagName("field");
			
			for(int x = 0; x < allFields.getLength(); x++){
				Node fieldNode = allFields.item(x);
				if(fieldNode.getNodeType() == Node.ELEMENT_NODE) {
					Element nodeElement = (Element) fieldNode;
					String name = nodeElement.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue().trim();
					String type = nodeElement.getElementsByTagName("type").item(0).getChildNodes().item(0).getNodeValue().trim();
					String sourceTable = nodeElement.getElementsByTagName("source_table").item(0).getChildNodes().item(0).getNodeValue().trim();
					String sourceColumn = nodeElement.getElementsByTagName("source_column").item(0).getChildNodes().item(0).getNodeValue().trim();
					String targetTable = nodeElement.getElementsByTagName("target_table").item(0).getChildNodes().item(0).getNodeValue().trim();
					String targetColumn = nodeElement.getElementsByTagName("target_column").item(0).getChildNodes().item(0).getNodeValue().trim();
					
					SourceEntry item = new SourceEntry(name, type, sourceTable, sourceColumn, targetTable, targetColumn);
					this.sourceFields.add(item);
				}
			}
		} catch (ParserConfigurationException e) {
			Log.log4j.error("Can't read the XML file " + fileName + ": " + e.getMessage());
		} catch (SAXException e) {
			Log.log4j.error("Can't parse the XML file " + fileName + ": " + e.getMessage());
		} catch (IOException e) {
			Log.log4j.error("Can't open the XML file " + fileName + ": " + e.getMessage());
		}
		
		return this.sourceFields;
	}
}
