package org.publiccms.controller.admin.home;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.ControllerUtils.verifyEquals;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.home.HomeBroadcast;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.home.HomeBroadcastService;
import org.publiccms.logic.service.log.LogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * HomeBroadcastAdminController
 * 
 */
@Controller
@RequestMapping("homeBroadcast")
public class HomeBroadcastAdminController extends AbstractController {

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "enable", method = RequestMethod.POST)
    public String enable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (verifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        HomeBroadcast entity = service.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, false);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "enable.home.broadcast", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "disable", method = RequestMethod.POST)
    public String disable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (verifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        HomeBroadcast entity = service.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, true);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "disable.home.broadcast", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    @Autowired
    private HomeBroadcastService service;
}