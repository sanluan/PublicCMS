package com.publiccms.logic.dao.sys;

import java.util.Date;

// Generated 2015-7-3 16:18:22 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysTask;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysTaskDao extends BaseDao<SysTask> {
	public PageHandler getPage(Integer siteId, Integer status, Date beginUpdateDate, Integer pageIndex, Integer pageSize) {
		QueryHandler queryHandler = getQueryHandler("from SysTask bean");
		if (notEmpty(siteId)) {
			queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
		}
		if (notEmpty(status)) {
			queryHandler.condition("bean.status = :status").setParameter("status", status);
		}
		if (notEmpty(beginUpdateDate)) {
			queryHandler.condition("bean.updateDate > :beginUpdateDate").setParameter("beginUpdateDate", beginUpdateDate);
		}
		queryHandler.order("bean.id desc");
		return getPage(queryHandler, pageIndex, pageSize);
	}

	public int updateStatusToRunning(Integer id) {
		if (notEmpty(id)) {
			QueryHandler queryHandler = getQueryHandler("update SysTask bean set bean.status = 1");
			queryHandler.condition("bean.id = :id").setParameter("id", id);
			queryHandler.condition("bean.status = 0");
			return update(queryHandler);
		}
		return 0;
	}

	@Override
	protected SysTask init(SysTask entity) {
		return entity;
	}

}