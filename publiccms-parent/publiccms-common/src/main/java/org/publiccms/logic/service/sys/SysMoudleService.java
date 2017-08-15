package org.publiccms.logic.service.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.publiccms.entities.sys.SysMoudle;
import org.publiccms.logic.dao.sys.SysMoudleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysMoudleService
 * 
 */
@Service
@Transactional
public class SysMoudleService extends BaseService<SysMoudle> {

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
        Set<String> urls = new HashSet<String>();
        for (SysMoudle entity : (List<SysMoudle>) getPage(parentId, null, null, null).getList()) {
            if (notEmpty(entity.getUrl())) {
                int index = entity.getUrl().indexOf("?");
                urls.add(entity.getUrl().substring(0, index > 0 ? index : entity.getUrl().length()));
            }
            urls.addAll(getPageUrl(entity.getId()));
        }
        return urls;
    }

    @Autowired
    private SysMoudleDao dao;
    
}