package com.publiccms.views.controller.admin.plugin;

// Generated 2016-3-1 17:24:24 by com.sanluan.common.source.SourceMaker

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.plugin.PluginLottery;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.plugin.PluginLotteryService;

@Controller
@RequestMapping("pluginLottery")
public class PluginLotteryAdminController extends AbstractController {
    @Autowired
    private PluginLotteryService service;

    @RequestMapping("save")
    public String save(PluginLottery entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (notEmpty(entity.getId())) {
            PluginLottery oldEntity = service.getEntity(entity.getId());
            if (empty(oldEntity) || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, new String[] { "id", "siteId", "extendId" });
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.plugin.lottery", getIpAddress(request), getDate(), entity
                                .getId() + ":" + entity.getTitle()));
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.plugin.lottery", getIpAddress(request), getDate(), entity.getId()
                            + ":" + entity.getTitle()));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        PluginLottery entity = service.getEntity(id);
        if (notEmpty(entity)) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.delete(id);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.plugin.site", getIpAddress(request), getDate(), id + ":"
                            + entity.getTitle()));
        }
        return TEMPLATE_DONE;
    }
}