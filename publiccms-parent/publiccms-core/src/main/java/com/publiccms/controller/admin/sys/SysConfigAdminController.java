package com.publiccms.controller.admin.sys;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.views.pojo.entities.SysConfig;

/**
 *
 * SysConfigAdminController
 *
 */
@Controller
@RequestMapping("sysConfig")
public class SysConfigAdminController extends AbstractController {
    @Autowired
    private ConfigComponent configComponent;

    /**
     * @param entity
     * @param configCode
     * @param request
     * @param session
     * @return view name
     */
    @RequestMapping("save")
    public String save(@ModelAttribute SysConfig entity, String configCode, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(configCode)) {
            Map<String, SysConfig> map = configComponent.getMap(site);
            map.remove(configCode);
            map.put(entity.getCode(), entity);
            configComponent.save(site, map);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.config", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            Map<String, SysConfig> map = configComponent.getMap(site);
            map.put(entity.getCode(), entity);
            configComponent.save(site, map);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.config", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param code
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(String code, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        Map<String, SysConfig> modelMap = configComponent.getMap(site);
        SysConfig entity = modelMap.remove(code);
        if (null != entity) {
            configComponent.save(site, modelMap);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.config", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return TEMPLATE_DONE;
    }
}