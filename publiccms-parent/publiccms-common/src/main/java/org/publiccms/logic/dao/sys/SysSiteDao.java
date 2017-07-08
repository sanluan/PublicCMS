package org.publiccms.logic.dao.sys;

// Generated 2016-1-20 11:19:19 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.sys.SysSite;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * SysSiteDao
 * 
 */
@Repository
public class SysSiteDao extends BaseDao<SysSite> {
    
    /**
     * @param disabled
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Boolean disabled,String name, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysSite bean");
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (notEmpty(name)) {
            queryHandler.condition("(bean.name like :name)").setParameter("name", like(name));
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysSite init(SysSite entity) {
        return entity;
    }

}