package org.publiccms.logic.dao.cms;

// Generated 2016-3-22 11:21:35 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.cms.CmsWord;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * CmsWordDao
 * 
 */
@Repository
public class CmsWordDao extends BaseDao<CmsWord> {
    
    /**
     * @param siteId
     * @param hidden
     * @param startCreateDate
     * @param endCreateDate
     * @param name
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, Boolean hidden, Date startCreateDate, Date endCreateDate, String name,
            String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsWord bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(hidden)) {
            queryHandler.condition("bean.hidden = :hidden").setParameter("hidden", hidden);
        }
        if (notEmpty(startCreateDate)) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (notEmpty(endCreateDate)) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
        }
        if (notEmpty(name)) {
            queryHandler.condition("bean.name like :name").setParameter("name", like(name));
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
        case "createDate":
            queryHandler.order("bean.createDate " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param name
     * @return
     */
    public CmsWord getEntity(int siteId, String name) {
        if (notEmpty(name)) {
            QueryHandler queryHandler = getQueryHandler("from CmsWord bean");
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            queryHandler.condition("bean.name = :name").setParameter("name", name);
            return getEntity(queryHandler);
        } else {
            return null;
        }
    }

    @Override
    protected CmsWord init(CmsWord entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        if (notEmpty(entity.getName()) && entity.getName().length() > 255) {
            entity.setName(entity.getName().substring(0, 255));
        }
        return entity;
    }

}