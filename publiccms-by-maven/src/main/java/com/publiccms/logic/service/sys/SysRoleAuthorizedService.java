package com.publiccms.logic.service.sys;

// Generated 2015-7-24 16:54:11 by com.sanluan.common.source.SourceMaker

import static org.apache.commons.lang3.StringUtils.split;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysMoudle;
import com.publiccms.entities.sys.SysRoleAuthorized;
import com.publiccms.logic.dao.sys.SysRoleAuthorizedDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysRoleAuthorizedService extends BaseService<SysRoleAuthorized> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer roleId, String url, Integer pageIndex, Integer pageSize) {
        return dao.getPage(roleId, url, pageIndex, pageSize);
    }

    public int deleteByRoleId(Integer roleId) {
        return dao.deleteByRoleId(roleId);
    }

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
                if (urls.contains(roleAuthorized.getUrl())) {
                    urls.remove(roleAuthorized.getUrl());
                } else {
                    delete(roleAuthorized.getId());
                }
            }
            if (notEmpty(urls)) {
                for (String url : urls) {
                    save(new SysRoleAuthorized(roleId, url));
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public List<SysRoleAuthorized> getEntitys(Integer[] roleIds, String[] urls) {
        return dao.getEntitys(roleIds, urls);
    }

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