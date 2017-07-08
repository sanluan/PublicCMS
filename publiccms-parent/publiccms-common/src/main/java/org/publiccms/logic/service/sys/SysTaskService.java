package org.publiccms.logic.service.sys;

import java.util.Date;

import org.publiccms.entities.sys.SysTask;
import org.publiccms.logic.dao.sys.SysTaskDao;

// Generated 2015-7-3 16:18:22 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysTaskService
 * 
 */
@Service
@Transactional
public class SysTaskService extends BaseService<SysTask> {

	/**
	 * @param siteId
	 * @param status
	 * @param beginUpdateDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly = true)
	public PageHandler getPage(Integer siteId, Integer status, Date beginUpdateDate, Integer pageIndex, Integer pageSize) {
		return dao.getPage(siteId, status, beginUpdateDate, pageIndex, pageSize);
	}

	/**
	 * @param id
	 * @param status
	 */
	public void updateStatus(Integer id, int status) {
		SysTask entity = getEntity(id);
		if (null != entity) {
			entity.setStatus(status);
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public boolean updateStatusToRunning(Integer id) {
		return 1 == dao.updateStatusToRunning(id);
	}

	@Autowired
	private SysTaskDao dao;
	
}