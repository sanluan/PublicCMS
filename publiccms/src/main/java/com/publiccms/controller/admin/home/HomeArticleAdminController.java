package com.publiccms.controller.admin.home;

import static com.sanluan.common.tools.JsonUtils.getString;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceGenerator

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.home.HomeArticle;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.home.HomeArticleService;
import com.publiccms.logic.service.log.LogLoginService;
@Controller
@RequestMapping("homeArticle")
public class HomeArticleAdminController extends AbstractController {

    @RequestMapping(value = "enable", method = RequestMethod.POST)
    public String enable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (verifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        HomeArticle entity = service.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, false);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "enable.home.article", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = "disable", method = RequestMethod.POST)
    public String disable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (verifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        HomeArticle entity = service.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, true);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "disable.home.article", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }
    
    @Autowired
    private HomeArticleService service;
}