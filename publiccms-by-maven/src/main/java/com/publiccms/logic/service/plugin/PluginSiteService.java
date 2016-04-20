package com.publiccms.logic.service.plugin;

// Generated 2016-3-1 17:24:24 by com.sanluan.common.source.SourceMaker

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.plugin.PluginSite;
import com.publiccms.logic.dao.plugin.PluginSiteDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class PluginSiteService extends BaseService<PluginSite> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, String pluginCode, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, pluginCode, pageIndex, pageSize);
    }

    public void delete(Integer siteId, String pluginCode) {
        PluginSite entity = getEntity(siteId, pluginCode);
        if (notEmpty(entity)) {
            delete(entity.getId());
        }
    }

    @Transactional(readOnly = true)
    public PluginSite getEntity(Integer siteId, String pluginCode) {
        return dao.getEntity(siteId, pluginCode);
    }

    @Transactional(readOnly = true)
    public List<PluginSite> getEntitys(Integer siteId, String[] pluginCodes) {
        return dao.getEntitys(siteId, pluginCodes);
    }

    @Autowired
    private PluginSiteDao dao;
}