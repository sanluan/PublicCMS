package com.publiccms.controller.admin.sys;

// Generated 2016-7-16 11:54:16 by com.publiccms.common.source.SourceGenerator

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysConfigData;
import com.publiccms.entities.sys.SysConfigDataId;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptConfigId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.CorsConfigComponent;
import com.publiccms.logic.component.site.EmailComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysConfigDataService;
import com.publiccms.logic.service.sys.SysDeptConfigService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.views.pojo.model.SysConfigParameters;

/**
 *
 * SysConfigDataAdminController
 * 
 */
@Controller
@RequestMapping("sysConfigData")
public class SysConfigDataAdminController {
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param sysConfigParameters
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysConfigData entity,
            @ModelAttribute SysConfigParameters sysConfigParameters, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (null != entity.getId()) {
            SysDept dept = sysDeptService.getEntity(admin.getDeptId());
            if (ControllerUtils.verifyNotEmpty("deptId", admin.getDeptId(), model)
                    || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                    || ControllerUtils
                            .verifyCustom("noright",
                                    !(dept.isOwnsAllConfig() || null != sysDeptConfigService
                                            .getEntity(new SysDeptConfigId(admin.getDeptId(), entity.getId().getCode()))),
                                    model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity.getId().setSiteId(site.getId());
            SysConfigData oldEntity = service.getEntity(entity.getId());
            if (null != oldEntity
                    && ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getId().getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            Map<String, String> map = ExtendUtils.getExtentDataMap(sysConfigParameters.getExtendDataList(),
                    configComponent.getFieldList(site, entity.getId().getCode(), null, RequestContextUtils.getLocale(request)));
            entity.setData(ExtendUtils.getExtendString(map));
            if (null != oldEntity) {
                entity = service.update(oldEntity.getId(), entity, ignoreProperties);
                if (null != entity) {
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "update.configData",
                                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
                }
            } else {
                entity.getId().setSiteId(site.getId());
                service.save(entity);
                logOperateService
                        .save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.configData",
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
            configComponent.removeCache(site.getId(), entity.getId().getCode());
            if (emailComponent.getCode(site).equals(entity.getId().getCode())) {
                emailComponent.clear(site.getId());
            } else if (corsConfigComponent.getCode(site).equals(entity.getId().getCode())) {
                corsConfigComponent.clear(site.getId());
            }

        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param code
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String code, HttpServletRequest request,
            ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        if (ControllerUtils.verifyNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                || ControllerUtils
                        .verifyCustom("noright",
                                !(dept.isOwnsAllConfig()
                                        || null != sysDeptConfigService.getEntity(new SysDeptConfigId(admin.getDeptId(), code))),
                                model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysConfigData entity = service.getEntity(new SysConfigDataId(site.getId(), code));
        if (null != entity) {
            service.delete(entity.getId());
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "delete.configData", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            configComponent.removeCache(site.getId(), entity.getId().getCode());
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private SysDeptConfigService sysDeptConfigService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private ConfigComponent configComponent;
    @Autowired
    private CorsConfigComponent corsConfigComponent;
    @Autowired
    private EmailComponent emailComponent;
    @Autowired
    private SysConfigDataService service;
}