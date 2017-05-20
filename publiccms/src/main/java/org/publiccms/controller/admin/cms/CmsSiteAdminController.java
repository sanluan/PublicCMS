package org.publiccms.controller.admin.cms;

import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * CmsSiteAdminController
 * 
 */
@Controller
@RequestMapping("cmsSite")
public class CmsSiteAdminController extends AbstractController {
    @Autowired
    private SysSiteService service;
    
    private String[] ignoreProperties = new String[] { "id", "useStatic", "useSsi", "disabled" };

    /**
     * @param entity
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("save")
    public String save(SysSite entity, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        entity = service.update(site.getId(), entity, ignoreProperties);
        logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                LogLoginService.CHANNEL_WEB_MANAGER, "update.site", getIpAddress(request), getDate(), getString(entity)));
        siteComponent.clear();
        return TEMPLATE_DONE;
    }
}