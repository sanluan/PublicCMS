package org.publiccms.logic.dao.cms;

// Generated 2016-11-20 14:50:37 by com.publiccms.common.source.SourceGenerator

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.cms.CmsDictionary;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * CmsDictionaryDao
 * 
 */
@Repository
public class CmsDictionaryDao extends BaseDao<CmsDictionary> {
    
    /**
     * @param multiple
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Boolean multiple, 
                Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionary bean");
        if (notEmpty(multiple)) {
            queryHandler.condition("bean.multiple = :multiple").setParameter("multiple", multiple);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsDictionary init(CmsDictionary entity) {
        return entity;
    }

}