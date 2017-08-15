package org.publiccms.controller.admin.sys;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysApp;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * SysAppAdminController
 * 
 */
@Controller
@RequestMapping("sysApp")
public class SysAppAdminController extends AbstractController {
    @Autowired
    private SysAppService service;

    private String[] ignoreProperties = new String[] { "id", "siteId", "channel" };

    /**
     * @param entity
     * @param apis
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(SysApp entity, String[] apis, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        entity.setAuthorizedApis(arrayToCommaDelimitedString(apis));
        if (null != entity.getId()) {
            SysApp oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                entity.setAppSecret(null);
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.app", getIpAddress(request), getDate(), getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            entity.setAppSecret(null);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.app", getIpAddress(request), getDate(), getString(entity)));
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
    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysApp entity = service.getEntity(id);
        if (null != entity) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.delete(id);
            entity.setAppSecret(null);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.app", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }
}