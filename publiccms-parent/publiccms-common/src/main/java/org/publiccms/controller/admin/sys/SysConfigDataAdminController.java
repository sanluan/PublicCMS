package org.publiccms.controller.admin.sys;

import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static org.publiccms.common.tools.ExtendUtils.getExtendString;
import static org.publiccms.common.tools.ExtendUtils.getExtentDataMap;
import static org.springframework.web.servlet.support.RequestContextUtils.getLocale;
// Generated 2016-7-16 11:54:16 by com.publiccms.common.source.SourceGenerator

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysConfigData;
import org.publiccms.entities.sys.SysConfigDataId;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.config.ConfigComponent;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysConfigDataService;
import org.publiccms.views.pojo.SysConfigParamters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * @return
     */
    @RequestMapping("save")
    public String save(SysConfigData entity, @ModelAttribute SysConfigParamters sysConfigParamters, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            entity.getId().setSiteId(site.getId());
            SysConfigData oldEntity = service.getEntity(entity.getId());
            if (null != oldEntity && verifyNotEquals("siteId", site.getId(), oldEntity.getId().getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            Map<String, String> map = getExtentDataMap(sysConfigParamters.getExtendDataList(),
                    configComponent.getFieldList(site, entity.getId().getCode(), null, getLocale(request)));
            entity.setData(getExtendString(map));
            if (null != oldEntity) {
                entity = service.update(oldEntity.getId(), entity, ignoreProperties);
                if (null != entity) {
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "update.configData", getIpAddress(request), getDate(),
                            getString(entity)));
                    configComponent.removeCache(site.getId(), entity.getId().getCode());
                }
            } else {
                entity.getId().setSiteId(site.getId());
                service.save(entity);
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "save.configData", getIpAddress(request), getDate(), getString(entity)));
                configComponent.removeCache(site.getId(), entity.getId().getCode());
            }
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param code
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("delete")
    public String delete(String code, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        SysConfigData entity = service.getEntity(new SysConfigDataId(site.getId(), code));
        if (null != entity) {
            service.delete(entity.getId());
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "delete.configData", getIpAddress(request), getDate(), getString(entity)));
            configComponent.removeCache(site.getId(), entity.getId().getCode());
        }
        return TEMPLATE_DONE;
    }

    @Autowired
    private ConfigComponent configComponent;
    @Autowired
    private SysConfigDataService service;
}