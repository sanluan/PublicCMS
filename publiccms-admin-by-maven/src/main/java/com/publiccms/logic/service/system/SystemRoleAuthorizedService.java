package com.publiccms.logic.service.system;

// Generated 2015-7-24 16:54:11 by SourceMaker

import static org.apache.commons.lang3.StringUtils.split;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.system.SystemMoudle;
import com.publiccms.entities.system.SystemRoleAuthorized;
import com.publiccms.logic.dao.system.SystemRoleAuthorizedDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SystemRoleAuthorizedService extends BaseService<SystemRoleAuthorized> {

    @Autowired
    private SystemRoleAuthorizedDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer roleId, String authorizedUrl, Integer pageIndex, Integer pageSize) {
        return dao.getPage(roleId, authorizedUrl, pageIndex, pageSize);
    }

    public void dealRoleMoudles(Integer roleId, List<SystemMoudle> moudles) {
        if (notEmpty(roleId)) {
            Set<String> authorizedUrls = new HashSet<String>();
            if (notEmpty(moudles)) {
                for (SystemMoudle moudle : moudles) {
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
            List<SystemRoleAuthorized> list = (List<SystemRoleAuthorized>) getPage(roleId, null, null, null).getList();
            for (SystemRoleAuthorized roleAuthorized : list) {
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
                    save(new SystemRoleAuthorized(roleId, authorizedUrl));
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