package com.publiccms.controller.admin.sys;

// Generated 2016-7-16 11:54:16 by com.publiccms.common.source.SourceGenerator

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysConfigData;
import com.publiccms.entities.sys.SysConfigDataId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysConfigDataService;
import com.publiccms.views.pojo.model.SysConfigParamters;

/**
 *
 * SysConfigDataAdminController
 * 
 */
@Controller
@RequestMapping("sysConfigData")
public class SysConfigDataAdminController extends AbstractController {

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param entity
     * @param sysConfigParamters
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    public String save(SysConfigData entity, @ModelAttribute SysConfigParamters sysConfigParamters, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            entity.getId().setSiteId(site.getId());
            SysConfigData oldEntity = service.getEntity(entity.getId());
            if (null != oldEntity
                    && ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getId().getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            Map<String, String> map = ExtendUtils.getExtentDataMap(sysConfigParamters.getExtendDataList(),
                    configComponent.getFieldList(site, entity.getId().getCode(), null, RequestContextUtils.getLocale(request)));
            entity.setData(ExtendUtils.getExtendString(map));
            if (null != oldEntity) {
                entity = service.update(oldEntity.getId(), entity, ignoreProperties);
                if (null != entity) {
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "update.configData", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), JsonUtils.getString(entity)));
                    configComponent.removeCache(site.getId(), entity.getId().getCode());
                }
            } else {
                entity.getId().setSiteId(site.getId());
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "save.configData", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
                configComponent.removeCache(site.getId(), entity.getId().getCode());
            }
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param code
     * @param request
     * @param session
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(String code, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        SysConfigData entity = service.getEntity(new SysConfigDataId(site.getId(), code));
        if (null != entity) {
            service.delete(entity.getId());
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.configData", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
            configComponent.removeCache(site.getId(), entity.getId().getCode());
        }
        return TEMPLATE_DONE;
    }

    @Autowired
    private ConfigComponent configComponent;
    @Autowired
    private SysConfigDataService service;
}