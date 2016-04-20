package com.publiccms.logic.dao.plugin;

// Generated 2016-3-1 17:24:24 by com.sanluan.common.source.SourceMaker

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.plugin.PluginSite;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class PluginSiteDao extends BaseDao<PluginSite> {
    public PageHandler getPage(Integer siteId, String pluginCode, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from PluginSite bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(pluginCode)) {
            queryHandler.condition("bean.pluginCode = :pluginCode").setParameter("pluginCode", pluginCode);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @SuppressWarnings("unchecked")
    public List<PluginSite> getEntitys(Integer siteId, String[] pluginCodes) {
        if (notEmpty(siteId) && notEmpty(pluginCodes)) {
            QueryHandler queryHandler = getQueryHandler("from PluginSite bean");
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            queryHandler.condition("bean.pluginCode in (:pluginCodes)").setParameter("pluginCodes", pluginCodes);
            return (List<PluginSite>) getList(queryHandler);
        }
        return new ArrayList<PluginSite>();
    }

    public PluginSite getEntity(Integer siteId, String pluginCode) {
        if (notEmpty(siteId) && notEmpty(pluginCode)) {
            QueryHandler queryHandler = getQueryHandler("from PluginSite bean");
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            queryHandler.condition("bean.pluginCode = :pluginCode").setParameter("pluginCode", pluginCode);
            return getEntity(queryHandler);
        }
        return null;
    }

    @Override
    protected PluginSite init(PluginSite entity) {
        return entity;
    }

}