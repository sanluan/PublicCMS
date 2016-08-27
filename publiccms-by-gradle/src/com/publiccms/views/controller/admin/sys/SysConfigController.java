package com.publiccms.views.controller.admin.sys;

import static com.publiccms.common.tools.ExtendUtils.getExtendString;
import static com.publiccms.common.tools.ExtendUtils.getExtentDataMap;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static org.springframework.web.servlet.support.RequestContextUtils.getLocale;
// Generated 2016-7-16 11:54:16 by com.sanluan.common.source.SourceMaker

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
import com.publiccms.entities.sys.SysConfig;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.ConfigComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysConfigService;
import com.publiccms.views.pojo.SysConfigParamters;

@Controller
@RequestMapping("sysConfig")
public class SysConfigController extends AbstractController {

    @RequestMapping("save")
    public String save(SysConfig entity, @ModelAttribute SysConfigParamters sysConfigParamters, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysConfig oldEntity = service.getEntity(site.getId(), entity.getCode(), entity.getSubcode());
        if (notEmpty(oldEntity) && verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
            return TEMPLATE_ERROR;
        }
        Map<String, String> map = getExtentDataMap(sysConfigParamters.getExtendDataList(),
                configComponent.getExtendFieldList(site.getId(), entity.getCode(), entity.getSubcode(), getLocale(request)));
        entity.setData(getExtendString(map));
        if (notEmpty(oldEntity)) {
            entity = service.update(oldEntity.getId(), entity, new String[] { "id", "siteId", "code", "subcode" });
            if (notEmpty(entity)) {
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.config", getIpAddress(request), getDate(), entity.getCode() + ":" + entity.getSubcode()));
                configComponent.removeCache(site.getId(), entity.getCode(), entity.getSubcode());
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.config", getIpAddress(request), getDate(), entity.getCode() + ":" + entity.getSubcode()));
            configComponent.removeCache(site.getId(), entity.getCode(), entity.getSubcode());
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(String code, String subcode, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysConfig entity = service.getEntity(site.getId(), code, subcode);
        if (notEmpty(entity)) {
            service.delete(entity.getId());
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "delete.config", getIpAddress(request), getDate(), entity.getCode() + ":" + entity.getSubcode()));
            configComponent.removeCache(site.getId(), entity.getCode(), entity.getSubcode());
        }
        return TEMPLATE_DONE;
    }

    @Autowired
    private ConfigComponent configComponent;
    @Autowired
    private SysConfigService service;
}