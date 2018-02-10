package com.publiccms.logic.service.sys;

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
    public PageHandler getPage(Integer parentId, Boolean menu, Integer pageIndex, Integer pageSize) {
        return dao.getPage(parentId, menu, pageIndex, pageSize);
    }

    /**
     * @param parentId
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set<String> getPageUrl(Integer parentId) {
        Set<String> urls = new HashSet<>();
        for (SysModule entity : (List<SysModule>) getPage(parentId, null, null, null).getList()) {
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