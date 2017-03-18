package com.publiccms.views.pojo;

import com.publiccms.entities.cms.CmsWord;

public class CmsWordStatistics implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private int searchCounts;
	private CmsWord entity;

	public CmsWordStatistics(long id, int searchCounts, CmsWord entity) {
		this.searchCounts = searchCounts;
		this.id = id;
		this.entity = entity;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSearchCounts() {
		return searchCounts;
	}

	public void setSearchCounts(int searchCounts) {
		this.searchCounts = searchCounts;
	}

	public CmsWord getEntity() {
		return entity;
	}

	public void setEntity(CmsWord entity) {
		this.entity = entity;
	}
}