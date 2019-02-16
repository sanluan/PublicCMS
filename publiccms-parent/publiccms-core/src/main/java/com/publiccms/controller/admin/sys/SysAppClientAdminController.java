package com.publiccms.controller.admin.sys;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysAppClientService;

/**
 *
 * SysAppClientAdminController
 * 
 */
@Controller
@RequestMapping("sysAppClient")
public class SysAppClientAdminController {
    @Autowired
    private SysAppClientService service;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param admin
     * @param id
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "enable", method = RequestMethod.POST)
    @Csrf
    public String enable(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, HttpServletRequest request,
            ModelMap model) {
        SysAppClient entity = service.getEntity(id);
        if (null != entity) {
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.updateStatus(id, false);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "enable.appclient", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "disable", method = RequestMethod.POST)
    @Csrf
    public String disable(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, HttpServletRequest request,
            ModelMap model) {
        SysAppClient entity = service.getEntity(id);
        if (null != entity) {
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.updateStatus(id, true);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "disable.appclient", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}