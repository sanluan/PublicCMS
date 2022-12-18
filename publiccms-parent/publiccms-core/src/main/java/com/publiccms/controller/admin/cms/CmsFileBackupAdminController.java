package com.publiccms.controller.admin.cms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.annotation.Resource;
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
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogUploadService;

/**
 * 
 * CmsFileBackupAdminController
 *
 */
@Controller
@RequestMapping("cmsFileBackup")
public class CmsFileBackupAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    protected LogUploadService logUploadService;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param admin
     * @param type
     * @param paths
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("recycle")
    @Csrf
    public String recycle(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String type, String[] paths,
            HttpServletRequest request, ModelMap model) {
        if (CommonUtils.notEmpty(paths)) {
            for (String path : paths) {
                String backupFilePath;
                String filepath;
                if (CommonUtils.notEmpty(type)) {
                    switch (type) {
                    case "file":
                        backupFilePath = siteComponent.getWebBackupFilePath(site.getId(), path);
                        filepath = siteComponent.getWebFilePath(site.getId(), path);
                        break;
                    case "task":
                        backupFilePath = siteComponent.getTaskTemplateBackupFilePath(site.getId(), path);
                        filepath = siteComponent.getTaskTemplateFilePath(site.getId(), path);
                        break;
                    case "template":
                    default:
                        backupFilePath = siteComponent.getTemplateBackupFilePath(site.getId(), path);
                        filepath = siteComponent.getTemplateFilePath(site.getId(), path);
                    }
                } else {
                    backupFilePath = siteComponent.getTemplateBackupFilePath(site.getId(), path);
                    filepath = siteComponent.getTemplateFilePath(site.getId(), path);
                }
                if (ControllerUtils.errorCustom("notExist.backupfile", !CmsFileUtils.moveFile(backupFilePath, filepath), model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "recycle.backupfile", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), type + ":" + StringUtils.join(paths, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

}
