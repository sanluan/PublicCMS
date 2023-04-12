package com.publiccms.controller.admin.sys;

import java.util.Comparator;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.entities.SysConfig;

/**
 *
 * SysConfigAdminController
 *
 */
@Controller
@RequestMapping("sysConfig")
public class SysConfigAdminController {
    @Resource
    private ConfigComponent configComponent;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param admin
     * @param entity
     * @param configCode
     * @param request
     * @return view name
     */
    @RequestMapping("save")
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, @ModelAttribute SysConfig entity,
            String configCode, HttpServletRequest request) {
        if (CommonUtils.notEmpty(entity.getExtendList())) {
            entity.getExtendList().sort(Comparator.comparing(SysExtendField::getSort));
            entity.getExtendList().forEach(e -> {
                if (CommonUtils.empty(e.getName())) {
                    e.setName(e.getId().getCode());
                }
            });
        }
        if (CommonUtils.notEmpty(configCode)) {
            Map<String, SysConfig> map = configComponent.getMap(site.getId());
            map.remove(configCode);
            map.put(entity.getCode(), entity);
            configComponent.save(site.getId(), map);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.config", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            Map<String, SysConfig> map = configComponent.getMap(site.getId());
            map.put(entity.getCode(), entity);
            configComponent.save(site.getId(), map);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.config", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param code
     * @param request
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String code,
            HttpServletRequest request) {
        Map<String, SysConfig> modelMap = configComponent.getMap(site.getId());
        SysConfig entity = modelMap.remove(code);
        if (null != entity) {
            configComponent.save(site.getId(), modelMap);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.config", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}