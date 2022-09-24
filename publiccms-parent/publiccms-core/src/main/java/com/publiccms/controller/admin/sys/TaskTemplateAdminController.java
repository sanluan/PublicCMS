package com.publiccms.controller.admin.sys;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Paths;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipOutputStream;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.common.tools.ZipUtils;
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
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
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
        if (CommonUtils.notEmpty(path)) {
            try {
                String filepath = siteComponent.getTaskTemplateFilePath(site, path);
                content = new String(VerificationUtils.base64Decode(content), CommonConstants.DEFAULT_CHARSET);
                if (CmsFileUtils.createFile(filepath, content)) {
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                    "save.task.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                } else {
                    String historyFilePath = siteComponent.getTaskTemplateHistoryFilePath(site, path);
                    CmsFileUtils.updateFile(filepath, historyFilePath, content);
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
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
     * @param files
     * @param path
     * @param encoding
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("doUpload")
    @Csrf
    public String upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile[] files, String path,
            String encoding, HttpServletRequest request, ModelMap model) {
        if (null != files) {
            try {
                for (MultipartFile file : files) {
                    String filepath = path + CommonConstants.SEPARATOR + file.getOriginalFilename();
                    String destFullFileName = siteComponent.getTaskTemplateFilePath(site, filepath);
                    CmsFileUtils.upload(file, destFullFileName);
                    if (destFullFileName.endsWith(".zip") && CmsFileUtils.isFile(destFullFileName)) {
                        ZipUtils.unzipHere(destFullFileName, encoding);
                        CmsFileUtils.delete(destFullFileName);
                    }
                    templateComponent.clearTaskTemplateCache();
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                    "upload.task.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), filepath));
                }
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
     * @param response
     */
    @RequestMapping("export")
    @Csrf
    public void export(@RequestAttribute SysSite site, HttpServletResponse response) {
        String filepath = siteComponent.getTaskTemplateFilePath(site, CommonConstants.SEPARATOR);
        try {
            response.setHeader("content-disposition",
                    "attachment;fileName=" + URLEncoder.encode(site.getName() + "_tasktemplate.zip", "utf-8"));
        } catch (UnsupportedEncodingException e1) {
        }
        try (ServletOutputStream outputStream = response.getOutputStream();
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            zipOutputStream.setEncoding(Constants.DEFAULT_CHARSET_NAME);
            ZipUtils.compress(Paths.get(filepath), zipOutputStream, Constants.BLANK);
        } catch (IOException e) {
        }
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
        if (CommonUtils.notEmpty(path)) {
            String filepath = siteComponent.getTaskTemplateFilePath(site, path);
            String backupFilePath = siteComponent.getTaskTemplateBackupFilePath(site, path);
            if (ControllerUtils.errorCustom("notExist.template", !CmsFileUtils.moveFile(filepath, backupFilePath), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            templateComponent.clearTaskTemplateCache();
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "delete.task.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

}
