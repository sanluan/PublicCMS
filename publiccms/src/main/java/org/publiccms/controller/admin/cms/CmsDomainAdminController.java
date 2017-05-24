package org.publiccms.controller.admin.cms;

import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysDomain;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private String[] ignoreProperties = new String[] { "siteId", "name", "wild" };

    /**
     * @param entity
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(SysDomain entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(entity.getName())) {
            SysSite site = getSite(request);
            SysDomain oldEntity = service.getEntity(entity.getName());
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getName(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.domain", getIpAddress(request), getDate(), getString(entity)));
            }
            siteComponent.clear();
        }
        return TEMPLATE_DONE;
    }
}