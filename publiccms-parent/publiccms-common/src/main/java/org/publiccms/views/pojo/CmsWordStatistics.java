package org.publiccms.views.pojo;

import org.publiccms.entities.cms.CmsWord;

/**
 *
 * CmsWordStatistics
 * 
 */
public class CmsWordStatistics implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private int searchCounts;
	private CmsWord entity;

	/**
	 * @param id
	 * @param searchCounts
	 * @param entity
	 */
	public CmsWordStatistics(long id, int searchCounts, CmsWord entity) {
		this.searchCounts = searchCounts;
		this.id = id;
		this.entity = entity;
	}

	/**
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public int getSearchCounts() {
		return searchCounts;
	}

	/**
	 * @param searchCounts
	 */
	public void setSearchCounts(int searchCounts) {
		this.searchCounts = searchCounts;
	}

	/**
	 * @return
	 */
	public CmsWord getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 */
	public void setEntity(CmsWord entity) {
		this.entity = entity;
	}
}