package com.publiccms.logic.dao.sys;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;

/**
 *
 * SysSiteDao
 * 
 */
@Repository
public class SysSiteDao extends BaseDao<SysSite> {

    /**
     * @param disabled
     * @param parentId
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Boolean disabled, Short parentId, String name, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysSite bean");
        if (null != disabled) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (null != parentId) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (CommonUtils.notEmpty(name)) {
            queryHandler.condition("(bean.name like :name)").setParameter("name", like(name));
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param parentId
     * @param directory
     * @return entity
     */
    public SysSite getEntity(short parentId, String directory) {
        QueryHandler queryHandler = getQueryHandler("from SysSite bean");
        queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        queryHandler.condition("bean.directory = :directory").setParameter("directory", directory);
        return getEntity(queryHandler);
    }

    @Override
    protected SysSite init(SysSite entity) {
        if (null != entity.getParentId()) {
            entity.setHasChild(false);
        }
        if (!entity.isHasChild()) {
            entity.setMultiple(false);
        }
        if (null== entity.getParentId() || CommonUtils.empty(entity.getDirectory())) {
            entity.setDirectory(null);
        }
        if (null == entity.getDynamicPath()) {
            entity.setDynamicPath(Constants.SEPARATOR);
        } else if (!entity.getDynamicPath().endsWith(Constants.SEPARATOR)) {
            entity.setDynamicPath(CommonUtils.joinString(entity.getDynamicPath(), Constants.SEPARATOR));
        }
        if (null == entity.getSitePath()) {
            entity.setSitePath(Constants.SEPARATOR);
        } else if (!entity.getSitePath().endsWith(Constants.SEPARATOR)) {
            entity.setSitePath(CommonUtils.joinString(entity.getSitePath(), Constants.SEPARATOR));
        }
        return entity;
    }

}