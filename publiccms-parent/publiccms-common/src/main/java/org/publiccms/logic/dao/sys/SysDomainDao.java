package org.publiccms.logic.dao.sys;

// Generated 2016-1-20 11:19:18 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.sys.SysDomain;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * SysDomainDao
 * 
 */
@Repository
public class SysDomainDao extends BaseDao<SysDomain> {

    /**
     * @param siteId
     * @param wild
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, Boolean wild, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysDomain bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(wild)) {
            queryHandler.condition("bean.wild = :wild").setParameter("wild", wild);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysDomain init(SysDomain entity) {
        return entity;
    }

}