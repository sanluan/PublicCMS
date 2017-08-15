package org.publiccms.logic.dao.sys;

// Generated 2015-7-3 16:18:22 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.sys.SysTask;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * SysTaskDao
 * 
 */
@Repository
public class SysTaskDao extends BaseDao<SysTask> {
    
	/**
	 * @param siteId
	 * @param status
	 * @param beginUpdateDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
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

	/**
	 * @param id
	 * @return
	 */
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