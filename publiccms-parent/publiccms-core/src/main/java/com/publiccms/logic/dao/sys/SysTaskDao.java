package com.publiccms.logic.dao.sys;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysTask;

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
     * @return results page
     */
    public PageHandler getPage(Short siteId, Integer status, Date beginUpdateDate, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysTask bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(status)) {
            queryHandler.condition("bean.status = :status").setParameter("status", status);
        }
        if (null != beginUpdateDate) {
            queryHandler.condition("bean.updateDate >= :beginUpdateDate").setParameter("beginUpdateDate", beginUpdateDate);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysTask init(SysTask entity) {
        return entity;
    }

}