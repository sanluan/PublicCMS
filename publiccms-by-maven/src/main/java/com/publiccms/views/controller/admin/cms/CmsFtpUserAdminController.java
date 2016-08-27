package com.publiccms.views.controller.admin.cms;

// Generated 2016-2-15 21:14:49 by com.sanluan.common.source.SourceMaker

import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static com.sanluan.common.tools.VerificationUtils.encode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysFtpUser;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysFtpUserService;

/**
 * 
 * CmsFtpUserController
 *
 */
@Controller
@RequestMapping("cmsFtpUser")
public class CmsFtpUserAdminController extends AbstractController {
    @Autowired
    private SysFtpUserService service;

    /**
     * @param entity
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(SysFtpUser entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (notEmpty(entity.getId())) {
            SysFtpUser oldEntity = service.getEntity(entity.getId());
            if (empty(oldEntity) || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            SysFtpUser user = service.getEntity(entity.getId());
            if ((!user.getName().equals(entity.getName()) && verifyHasExist("username", service.findByName(entity.getName()),
                    model))) {
                return TEMPLATE_ERROR;
            }
            if (notEmpty(entity.getPassword())) {
                entity.setPassword(encode(entity.getPassword()));
            } else {
                entity.setPassword(user.getPassword());
            }
            entity = service.update(entity.getId(), entity, new String[] { "id", "siteId" });
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.ftpuser", getIpAddress(request), getDate(), entity.getId()
                                + ":" + entity.getName()));
            }
        } else {
            if (verifyNotEmpty("password", entity.getPassword(), model)
                    || verifyHasExist("username", service.findByName(entity.getName()), model)) {
                return TEMPLATE_ERROR;
            }
            entity.setSiteId(site.getId());
            entity.setPassword(encode(entity.getPassword()));
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.ftpuser", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
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
        SysFtpUser entity = service.getEntity(id);
        if (notEmpty(entity)) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.delete(id);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.ftpuser", getIpAddress(request), getDate(), id + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }
}