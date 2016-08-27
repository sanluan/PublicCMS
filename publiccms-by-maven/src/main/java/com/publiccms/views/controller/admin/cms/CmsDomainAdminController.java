package com.publiccms.views.controller.admin.cms;

// Generated 2016-1-27 21:06:08 by com.sanluan.common.source.SourceMaker

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDomainService;

/**
 * 
 * CmsDomainController
 *
 */
@Controller
@RequestMapping("cmsDomain")
public class CmsDomainAdminController extends AbstractController {
    @Autowired
    private SysDomainService service;

    /**
     * @param entity
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(SysDomain entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(entity.getId())) {
            SysSite site = getSite(request);
            SysDomain oldEntity = service.getEntity(entity.getId());
            if (empty(oldEntity) || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, new String[] { "id", "siteId", "name" });
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.domain", getIpAddress(request), getDate(), entity.getId()
                                + ":" + entity.getName()));
            }
            siteComponent.clear();
        }
        return TEMPLATE_DONE;
    }
}