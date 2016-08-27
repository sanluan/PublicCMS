package com.publiccms.views.controller.admin.sys;

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

@Controller
@RequestMapping("sysFtpUser")
public class SysFtpUserAdminController extends AbstractController {
    @Autowired
    private SysFtpUserService service;

    @RequestMapping("save")
    public String save(SysFtpUser entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (notEmpty(entity.getId())) {
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
            entity = service.update(entity.getId(), entity, new String[] { "id" });
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
            if (0 == entity.getSiteId()) {
                entity.setSiteId(site.getId());
            }
            entity.setPassword(encode(entity.getPassword()));
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.ftpuser", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session) {
        SysFtpUser entity = service.getEntity(id);
        if (notEmpty(entity)) {
            service.delete(id);
            SysSite site = getSite(request);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.ftpuser", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }
}