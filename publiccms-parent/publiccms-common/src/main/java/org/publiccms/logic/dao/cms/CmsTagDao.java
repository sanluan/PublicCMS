package org.publiccms.logic.dao.cms;

// Generated 2015-7-10 16:36:23 by com.publiccms.common.source.SourceGenerator

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.cms.CmsTag;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 * 标签DAO
 * 
 * CmsTag DAO
 * 
 */
@Repository
public class CmsTagDao extends BaseDao<CmsTag> {
    /**
     * 获取标签列表
     * 
     * Get tag list
     * 
     * @param siteId
     * @param typeId
     * @param name
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, Integer typeId, String name, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsTag bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(typeId)) {
            queryHandler.condition("bean.typeId = :typeId").setParameter("typeId", typeId);
        }
        if (notEmpty(name)) {
            queryHandler.condition("bean.name like :name").setParameter("name", rightLike(name));
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = BLANK;
        }
        switch (orderField) {
        case "searchCount":
            queryHandler.order("bean.searchCount " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsTag init(CmsTag entity) {
        return entity;
    }

}