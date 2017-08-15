package org.publiccms.logic.service.sys;

// Generated 2015-7-24 16:54:11 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.lang3.StringUtils.split;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.publiccms.entities.sys.SysMoudle;
import org.publiccms.entities.sys.SysRoleAuthorized;
import org.publiccms.entities.sys.SysRoleAuthorizedId;
import org.publiccms.logic.dao.sys.SysRoleAuthorizedDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysRoleAuthorizedService
 * 
 */
@Service
@Transactional
public class SysRoleAuthorizedService extends BaseService<SysRoleAuthorized> {

    /**
     * @param roleId
     * @param url
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer roleId, String url, Integer pageIndex, Integer pageSize) {
        return dao.getPage(roleId, url, pageIndex, pageSize);
    }

    /**
     * @param roleId
     * @return
     */
    public int deleteByRoleId(Integer roleId) {
        return dao.deleteByRoleId(roleId);
    }

    /**
     * @param roleId
     * @param showAllMoudle
     * @param moudles
     * @param pageUrls
     */
    public void dealRoleMoudles(Integer roleId, boolean showAllMoudle, List<SysMoudle> moudles, Set<String> pageUrls) {
        if (notEmpty(roleId)) {
            Set<String> urls = new HashSet<String>();
            if (notEmpty(moudles)) {
                for (SysMoudle moudle : moudles) {
                    if (notEmpty(moudle.getUrl()) && !showAllMoudle) {
                        int index = moudle.getUrl().indexOf("?");
                        urls.add(moudle.getUrl().substring(0, index > 0 ? index : moudle.getUrl().length()));
                    }
                    if (notEmpty(moudle.getAuthorizedUrl())) {
                        for (String url : split(moudle.getAuthorizedUrl(), ',')) {
                            urls.add(url);
                        }
                    }
                }
            }

            if (showAllMoudle) {
                urls.addAll(pageUrls);
            }

            @SuppressWarnings("unchecked")
            List<SysRoleAuthorized> list = (List<SysRoleAuthorized>) getPage(roleId, null, null, null).getList();
            for (SysRoleAuthorized roleAuthorized : list) {
                if (urls.contains(roleAuthorized.getId().getUrl())) {
                    urls.remove(roleAuthorized.getId().getUrl());
                } else {
                    delete(roleAuthorized.getId());
                }
            }
            if (!urls.isEmpty()) {
                for (String url : urls) {
                    save(new SysRoleAuthorized(new SysRoleAuthorizedId(roleId, url)));
                }
            }
        }
    }

    /**
     * @param roleIds
     * @param url
     * @return
     */
    @Transactional(readOnly = true)
    public int count(String roleIds, String url) {
        String[] roleIdArray = split(roleIds, ',');
        if (notEmpty(roleIds) && 0 < roleIdArray.length) {
            Integer[] intRoleIds = new Integer[roleIdArray.length];
            for (int i = 0; i < roleIdArray.length; i++) {
                intRoleIds[i] = Integer.parseInt(roleIdArray[i]);
            }
            return dao.count(intRoleIds, url);
        }
        return 0;
    }

    @Autowired
    private SysRoleAuthorizedDao dao;
    
}