package org.hibernate.tool.api.reveng;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Table;

// split up to readonly/writeable interface
/**
 * Only intended to be used internally in reveng. *not* public api.
 */
public interface DatabaseCollector {

	Iterator<Table> iterateTables();

	Table addTable(String schema, String catalog, String name);

	void setOneToManyCandidates(Map<String, List<ForeignKey>> oneToManyCandidates);

	Table getTable(String schema, String catalog, String name);

	Map<String, List<ForeignKey>> getOneToManyCandidates();

	void addSuggestedIdentifierStrategy(String catalog, String schema, String name, String strategy);
	
	String getSuggestedIdentifierStrategy(String catalog, String schema, String name);
	
	
}