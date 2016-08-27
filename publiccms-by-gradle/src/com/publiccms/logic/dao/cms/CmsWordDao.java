package com.publiccms.logic.dao.cms;

// Generated 2016-3-22 11:21:35 by com.sanluan.common.source.SourceMaker

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsWord;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsWordDao extends BaseDao<CmsWord> {
	public PageHandler getPage(Integer siteId, Boolean hidden, Date startCreateDate, Date endCreateDate, String name,
			String orderField, String orderType, Integer pageIndex, Integer pageSize) {
		QueryHandler queryHandler = getQueryHandler("from CmsWord bean");
		if (notEmpty(siteId)) {
			queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
		}
		if (notEmpty(hidden)) {
			queryHandler.condition("bean.hidden = :hidden").setParameter("hidden", hidden);
		}
		if (notEmpty(startCreateDate)) {
			queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate",
					startCreateDate);
		}
		if (notEmpty(endCreateDate)) {
			queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
		}
		if (notEmpty(name)) {
			queryHandler.condition("bean.name like :name").setParameter("name", like(name));
		}
		if ("asc".equalsIgnoreCase(orderType)) {
			orderType = "asc";
		} else {
			orderType = "desc";
		}
		if (null == orderField) {
			orderField = "";
		}
		switch (orderField) {
		case "searchCount":
			queryHandler.order("bean.searchCount " + orderType);
			break;
		case "createDate":
			queryHandler.order("bean.createDate " + orderType);
			break;
		default:
			queryHandler.order("bean.id " + orderType);
		}
		return getPage(queryHandler, pageIndex, pageSize);
	}

	public CmsWord getEntity(int siteId, String name) {
		if (notEmpty(name)) {
			QueryHandler queryHandler = getQueryHandler("from CmsWord bean");
			queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
			queryHandler.condition("bean.name = :name").setParameter("name", name);
			return getEntity(queryHandler);
		} else {
			return null;
		}
	}

	@Override
	protected CmsWord init(CmsWord entity) {
		if (empty(entity.getCreateDate())) {
			entity.setCreateDate(getDate());
		}
		return entity;
	}

}