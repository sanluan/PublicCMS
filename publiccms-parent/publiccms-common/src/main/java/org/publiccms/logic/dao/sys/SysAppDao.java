package org.publiccms.logic.dao.sys;

// Generated 2016-3-2 10:25:12 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.sys.SysApp;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * SysAppDao
 * 
 */
@Repository
public class SysAppDao extends BaseDao<SysApp> {

    /**
     * @param siteId
     * @param channel
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, String channel, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysApp bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(channel)) {
            queryHandler.condition("bean.channel = :channel").setParameter("channel", channel);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysApp init(SysApp entity) {
        return entity;
    }

}