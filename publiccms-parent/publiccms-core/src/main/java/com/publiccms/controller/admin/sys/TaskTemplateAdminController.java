package com.publiccms.controller.admin.sys;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.FileComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.log.LogLoginService;

import freemarker.template.TemplateException;

/**
 *
 * CmsTemplateAdminController
 *
 */
@Controller
@RequestMapping("taskTemplate")
public class TaskTemplateAdminController extends AbstractController {
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private FileComponent fileComponent;

    /**
     * @param path
     * @param content
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    public String save(String path, String content, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(path)) {
            try {
                String filePath = siteComponent.getTaskTemplateFilePath(site, path);
                File templateFile = new File(filePath);
                if (CommonUtils.notEmpty(templateFile)) {
                    fileComponent.updateFile(templateFile, content);
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "update.task.template", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), path));
                } else {
                    fileComponent.createFile(templateFile, content);
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "save.task.template", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), path));
                }
                templateComponent.clearTaskTemplateCache();
            } catch (IOException e) {
                model.addAttribute(ERROR, e.getMessage());
                log.error(e.getMessage(), e);
                return TEMPLATE_ERROR;
            }
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param filePath
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("runTask")
    public String runTask(String filePath, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        model.addAttribute("filePath", filePath);
        try {
            String fulllPath = SiteComponent.getFullFileName(site, filePath);
            Map<String, Object> map = new HashMap<>();
            AbstractFreemarkerView.exposeAttribute(map, request.getScheme(), request.getServerName(), request.getServerPort(),
                    request.getContextPath());
            model.addAttribute("result",
                    FreeMarkerUtils.generateStringByFile(fulllPath, templateComponent.getTaskConfiguration(), map));
        } catch (IOException | TemplateException e) {
            model.addAttribute(ERROR, e.getMessage());
            log.error(e.getMessage(), e);
        }
        logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                LogLoginService.CHANNEL_WEB_MANAGER, "run.task.template", RequestUtils.getIpAddress(request),
                CommonUtils.getDate(), JsonUtils.getString(model)));
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(request);
            String filePath = siteComponent.getTaskTemplateFilePath(site, path);
            if (ControllerUtils.verifyCustom("notExist.template", !fileComponent.deleteFile(filePath), model)) {
                return TEMPLATE_ERROR;
            }
            templateComponent.clearTaskTemplateCache();
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "delete.task.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return TEMPLATE_DONE;
    }

}
