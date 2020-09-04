/*
 * Created on 2004-12-01
 */
package org.hibernate.tool.api.export;

import java.util.Properties;

/**
 * @author max and david
 * @author koen
 */
public interface Exporter {
	

	Properties getProperties();
	
	/**
	 * Called when exporter should start generating its output
	 */
	void start();
	
}
