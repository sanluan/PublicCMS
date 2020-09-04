package org.hibernate.tool.internal.export.common;

public interface ExporterSettings {

	String PREFIX_KEY = "hibernatetool.";
	
	/** 
	 * if true exporters are allowed to generate EJB3 constructs
	 */
	String EJB3 = PREFIX_KEY + "ejb3";
	
	/** 
	 * if true then exporters are allowed to generate JDK 5 constructs
	 */
	String JDK5 = PREFIX_KEY + "jdk5";
	
	/** 
	 * the (root) output directory for an exporter
	 */
	String OUTPUT_DIRECTORY = PREFIX_KEY + "output_directory";
	
	/** 
	 * the (root) output directory for an exporter
	 */
	String TEMPLATE_PATH = PREFIX_KEY + "template_path";
	
	
	
}
