package com.publiccms.logic.dao.log;

// Generated 2015-7-3 16:15:25 by SourceMaker

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.log.LogTask;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class LogTaskDao extends BaseDao<LogTask> {
	public PageHandler getPage(Integer taskId, Integer pageIndex, Integer pageSize) {
		QueryHandler queryHandler = getQueryHandler("from LogTask bean");
		if (notEmpty(taskId)) {
			queryHandler.condition("bean.taskId = :taskId").setParameter("taskId", taskId);
		}
		queryHandler.append("order by bean.id desc");
		return getPage(queryHandler, pageIndex, pageSize);
	}

	public int delete(Date createDate) {
		QueryHandler queryHandler = getDeleteQueryHandler("from LogTask bean where bean.endtime <= :endtime");
		queryHandler.setParameter("endtime", createDate);
		return delete(queryHandler);
	}

	@Override
	protected LogTask init(LogTask entity) {
		return entity;
	}

	@Override
	protected Class<LogTask> getEntityClass() {
		return LogTask.class;
	}

}