package com.publiccms.controller.admin.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.LockComponent;

/**
 *
 * SysDeptAdminController
 * 
 */
@Controller
@RequestMapping("common")
public class SysLockAdminController {
    @Autowired
    private LockComponent lockComponent;

    /**
     * @param site
     * @param admin
     * @param itemType
     * @param itemId
     * @return result
     */
    @RequestMapping("lock")
    @ResponseBody
    public boolean lock(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String itemType, String itemId) {
        return null != lockComponent.lock(site.getId(), itemType, itemId, admin.getId(), false);
    }

    /**
     * @param site
     * @param admin
     * @param itemType
     * @param itemId
     * @return result
     */
    @RequestMapping("unlock")
    @ResponseBody
    public boolean unlock(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String itemType, String itemId) {
        lockComponent.unLock(site.getId(), itemType, itemId, admin.getId());
        return true;
    }
}