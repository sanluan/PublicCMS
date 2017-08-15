package org.publiccms.logic.dao.cms;
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.cms.CmsCategory;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * CmsCategoryDao
 * 
 */
@Repository
public class CmsCategoryDao extends BaseDao<CmsCategory> {
    /**
     * @param siteId
     * @param parentId
     * @param queryAll
     * @param typeId
     * @param allowContribute
     * @param hidden
     * @param disabled
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, Integer parentId, Boolean queryAll, Integer typeId, Boolean allowContribute,
            Boolean hidden, Boolean disabled, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsCategory bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(parentId)) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        } else if (empty(queryAll) || !queryAll) {
            queryHandler.condition("bean.parentId is null");
        }
        if (notEmpty(typeId)) {
            queryHandler.condition("bean.typeId = :typeId").setParameter("typeId", typeId);
        }
        if (notEmpty(allowContribute)) {
            queryHandler.condition("bean.allowContribute = :allowContribute").setParameter("allowContribute", allowContribute);
        }
        if (notEmpty(hidden)) {
            queryHandler.condition("bean.hidden = :hidden").setParameter("hidden", hidden);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        queryHandler.order("bean.sort asc,bean.id asc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsCategory init(CmsCategory entity) {
        if (empty(entity.getChildIds())) {
            entity.setChildIds(null);
        }
        return entity;
    }

}