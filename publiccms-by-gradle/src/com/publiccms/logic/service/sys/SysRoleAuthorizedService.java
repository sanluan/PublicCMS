package com.publiccms.logic.service.sys;

// Generated 2015-7-24 16:54:11 by SourceMaker

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

    @Autowired
    private SysRoleAuthorizedDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer roleId, String authorizedUrl, Integer pageIndex, Integer pageSize) {
        return dao.getPage(roleId, authorizedUrl, pageIndex, pageSize);
    }

    public void dealRoleMoudles(Integer roleId, List<SysMoudle> moudles) {
        if (notEmpty(roleId)) {
            Set<String> authorizedUrls = new HashSet<String>();
            if (notEmpty(moudles)) {
                for (SysMoudle moudle : moudles) {
                    if (notEmpty(moudle.getUrl())) {
                        authorizedUrls.add(moudle.getUrl());
                    }
                    if (notEmpty(moudle.getAuthorizedUrl())) {
                        for (String url : split(moudle.getAuthorizedUrl(), ',')) {
                            authorizedUrls.add(url);
                        }
                    }
                }
            }

            @SuppressWarnings("unchecked")
            List<SysRoleAuthorized> list = (List<SysRoleAuthorized>) getPage(roleId, null, null, null).getList();
            for (SysRoleAuthorized roleAuthorized : list) {
                if (notEmpty(roleAuthorized.getAuthorizedUrl())) {
                    for (String url : split(roleAuthorized.getAuthorizedUrl(), ',')) {
                        if (authorizedUrls.contains(url)) {
                            authorizedUrls.remove(url);
                        } else {
                            delete(roleAuthorized.getId());
                        }
                    }
                }
            }

            if (notEmpty(authorizedUrls)) {
                for (String authorizedUrl : authorizedUrls) {
                    save(new SysRoleAuthorized(roleId, authorizedUrl));
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public int count(String roleIds, String authorizedUrl) {
        String[] roleIdArray = split(roleIds, ',');
        Integer[] intRoleIds = null;
        if (notEmpty(roleIds) && 0 < roleIdArray.length) {
            intRoleIds = new Integer[roleIdArray.length];
            for (int i = 0; i < roleIdArray.length; i++) {
                intRoleIds[i] = Integer.parseInt(roleIdArray[i]);
            }
            return dao.count(intRoleIds, authorizedUrl);
        }
        return 0;
    }
}