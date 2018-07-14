package com.publiccms.logic.service.sys;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysModule;
import com.publiccms.logic.dao.sys.SysModuleDao;

/**
 *
 * SysModuleService
 * 
 */
@Service
@Transactional
public class SysModuleService extends BaseService<SysModule> {

    /**
     * @param parentId
     * @param menu
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(String parentId, Boolean menu, Integer pageIndex, Integer pageSize) {
        return dao.getPage(parentId, menu, pageIndex, pageSize);
    }
    
    @Override
    public SysModule update(Serializable id, SysModule newEntity) {
        delete(id);
        save(newEntity);
        return newEntity;
    }

    /**
     * @param oldParentId
     * @param parentId
     */
    @SuppressWarnings("unchecked")
    public void updateParentId(String oldParentId, String parentId) {
        if (CommonUtils.notEmpty(oldParentId)) {
            for (SysModule entity : (List<SysModule>) getPage(oldParentId, null, null, PageHandler.MAX_PAGE_SIZE).getList()) {
                entity.setParentId(parentId);
            }
        }
    }

    /**
     * @param parentId
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set<String> getPageUrl(String parentId) {
        Set<String> urls = new HashSet<>();
        for (SysModule entity : (List<SysModule>) getPage(parentId, null, null, PageHandler.MAX_PAGE_SIZE).getList()) {
            if (CommonUtils.notEmpty(entity.getUrl())) {
                int index = entity.getUrl().indexOf("?");
                urls.add(entity.getUrl().substring(0, index > 0 ? index : entity.getUrl().length()));
            }
            urls.addAll(getPageUrl(entity.getId()));
        }
        return urls;
    }

    @Autowired
    private SysModuleDao dao;

}