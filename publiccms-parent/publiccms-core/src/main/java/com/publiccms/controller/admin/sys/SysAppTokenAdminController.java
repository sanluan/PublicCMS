package com.publiccms.controller.admin.sys;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;

/**
 *
 * SysAppTokenAdminController
 * 
 */
@Controller
@RequestMapping("sysAppToken")
public class SysAppTokenAdminController {
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param admin
     * @param id
     * @param expiryDate
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("issue")
    @Csrf
    public String issue(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Integer id,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expiryDate, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        SysApp entity = appService.getEntity(id);
        if (null != entity) {
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            Date now = CommonUtils.getDate();
            service.save(new SysAppToken(UUID.randomUUID().toString(), entity.getId(), now, expiryDate));
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "issue.apptoken", RequestUtils.getIpAddress(request), CommonUtils.getDate(), entity.getId().toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param authToken
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String authToken,
            HttpServletRequest request, ModelMap model) {
        SysAppToken entity = service.getEntity(authToken);
        Long userId = admin.getId();
        if (null != entity) {
            SysApp app = appService.getEntity(entity.getAppId());
            if (null != app) {
                if (ControllerUtils.verifyNotEquals("siteId", site.getId(), app.getSiteId(), model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
                service.delete(authToken);
                logOperateService
                        .save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "delete.apptoken",
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private SysAppTokenService service;

    @Autowired
    private SysAppService appService;

}