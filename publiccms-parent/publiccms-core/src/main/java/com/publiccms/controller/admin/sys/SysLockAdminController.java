package com.publiccms.controller.admin.sys;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.entities.sys.SysLock;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.views.pojo.entities.Lock;

/**
 *
 * SysDeptAdminController
 * 
 */
@Controller
@RequestMapping("common")
public class SysLockAdminController {
    @Resource
    private LockComponent lockComponent;
    @Resource
    private SysUserService userService;

    /**
     * @param site
     * @param admin
     * @param itemType
     * @param itemId
     * @return result
     */
    @RequestMapping("lock")
    @ResponseBody
    public Lock lock(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String itemType, String itemId) {
        SysLock lock = lockComponent.lock(site.getId(), itemType, itemId, admin.getId());
        if (null == lock || admin.getId().equals(lock.getUserId())) {
            return null;
        } else {
            return new Lock(lock, userService.getEntity(lock.getUserId()));
        }
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