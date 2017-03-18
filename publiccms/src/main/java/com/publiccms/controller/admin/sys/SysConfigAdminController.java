package com.publiccms.controller.admin.sys;

import static com.sanluan.common.tools.JsonUtils.getString;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.views.pojo.SysConfig;

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
     * @return
     */
    @RequestMapping("save")
    public String save(@ModelAttribute SysConfig entity, String configCode, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(configCode)) {
            Map<String, SysConfig> map = configComponent.getMap(site);
            map.remove(configCode);
            map.put(entity.getCode(), entity);
            configComponent.save(site, map);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.config", getIpAddress(request), getDate(), getString(entity)));
        } else {
            Map<String, SysConfig> map = configComponent.getMap(site);
            map.put(entity.getCode(), entity);
            configComponent.save(site, map);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.config", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param code
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("delete")
    public String delete(String code, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        Map<String, SysConfig> modelMap = configComponent.getMap(site);
        SysConfig entity = modelMap.remove(code);
        if (null != entity) {
            configComponent.save(site, modelMap);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.config", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }
}