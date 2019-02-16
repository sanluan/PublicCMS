package com.publiccms.controller.admin.sys;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;

/**
 *
 * CmsTemplateAdminController
 *
 */
@Controller
@RequestMapping("taskTemplate")
public class TaskTemplateAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param admin
     * @param path
     * @param content
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, String content,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            try {
                String filePath = siteComponent.getTaskTemplateFilePath(site, path);
                content = new String(VerificationUtils.base64Decode(content), CommonConstants.DEFAULT_CHARSET);
                if (CmsFileUtils.createFile(filePath, content)) {
                    logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.task.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                } else {
                    String historyFilePath = siteComponent.getTaskTemplateHistoryFilePath(site, path);
                    CmsFileUtils.updateFile(filePath, historyFilePath, content);
                    logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "update.task.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                }
                templateComponent.clearTaskTemplateCache();
            } catch (IOException e) {
                model.addAttribute(CommonConstants.ERROR, e.getMessage());
                log.error(e.getMessage(), e);
                return CommonConstants.TEMPLATE_ERROR;
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param path
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            String filePath = siteComponent.getTaskTemplateFilePath(site, path);
            String backupFilePath = siteComponent.getTaskTemplateBackupFilePath(site, path);
            if (ControllerUtils.verifyCustom("notExist.template", !CmsFileUtils.moveFile(filePath, backupFilePath), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            templateComponent.clearTaskTemplateCache();
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "delete.task.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

}
