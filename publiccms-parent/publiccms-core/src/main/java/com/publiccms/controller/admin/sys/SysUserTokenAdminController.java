package com.publiccms.controller.admin.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysUserTokenService;

/**
 *
 * SysUserTokenAdminController
 * 
 */
@Controller
@RequestMapping("sysUserToken")
public class SysUserTokenAdminController extends AbstractController {

    /**
     * @param authToken
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(String authToken, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysUserToken entity = service.getEntity(authToken);
        Long userId = getAdminFromSession(session).getId();
        if (null != entity) {
            if (ControllerUtils.verifyNotEquals("siteId", userId, entity.getUserId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.delete(authToken);
            logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "delete.usertoken",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    @Autowired
    private SysUserTokenService service;

}