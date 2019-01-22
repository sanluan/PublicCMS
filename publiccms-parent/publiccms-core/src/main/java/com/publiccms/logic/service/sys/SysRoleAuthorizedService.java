package com.publiccms.logic.service.sys;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysModule;
import com.publiccms.entities.sys.SysRoleAuthorized;
import com.publiccms.entities.sys.SysRoleAuthorizedId;
import com.publiccms.logic.dao.sys.SysRoleAuthorizedDao;

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
     * @param showAllModule
     * @param modules
     * @param pageUrls
     */
    public void dealRoleModules(Integer roleId, boolean showAllModule, List<SysModule> modules, Set<String> pageUrls) {
        if (CommonUtils.notEmpty(roleId)) {
            Set<String> urls = new HashSet<>();
            if (CommonUtils.notEmpty(modules)) {
                for (SysModule module : modules) {
                    if (CommonUtils.notEmpty(module.getUrl()) && !showAllModule) {
                        int index = module.getUrl().indexOf("?");
                        urls.add(module.getUrl().substring(0, 0 < index ? index : module.getUrl().length()));
                    }
                    if (CommonUtils.notEmpty(module.getAuthorizedUrl())) {
                        for (String url : StringUtils.split(module.getAuthorizedUrl(), CommonConstants.COMMA)) {
                            urls.add(url);
                        }
                    }
                }
            }

            if (showAllModule) {
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
    public long count(String roleIds, String url) {
        String[] roleIdArray = StringUtils.split(roleIds, CommonConstants.COMMA);
        if (CommonUtils.notEmpty(roleIds) && 0 < roleIdArray.length) {
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