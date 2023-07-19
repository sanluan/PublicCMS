package com.publiccms.controller.admin.sys;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jakarta.annotation.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.DateFormatUtils;
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
                String filepath = siteComponent.getTaskTemplateFilePath(site.getId(), path);
                content = new String(VerificationUtils.base64Decode(content), Constants.DEFAULT_CHARSET);
                if (CmsFileUtils.createFile(filepath, content)) {
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                    "save.task.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                } else {
                    String historyFilePath = siteComponent.getTaskTemplateHistoryFilePath(site.getId(), path, true);
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
     * @param overwrite
     * @param path
     * @param encoding
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("doUpload")
    @Csrf
    public String upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile[] files,
            boolean overwrite, String path, String encoding, HttpServletRequest request, ModelMap model) {
        if (null != files) {
            try {
                for (MultipartFile file : files) {
                    String filepath = CommonUtils.joinString(path, Constants.SEPARATOR, file.getOriginalFilename());
                    String destFullFileName = siteComponent.getTaskTemplateFilePath(site.getId(), filepath);
                    if (!CmsFileUtils.exists(destFullFileName) || overwrite || destFullFileName.endsWith(".zip")) {
                        CmsFileUtils.upload(file, destFullFileName);
                    }
                    if (destFullFileName.endsWith(".zip") && CmsFileUtils.isFile(destFullFileName)) {
                        ZipUtils.unzipHere(destFullFileName, encoding, overwrite, (f, e) -> {
                            String historyFilePath = siteComponent.getTaskTemplateHistoryFilePath(site.getId(), e.getName(),
                                    true);
                            try {
                                CmsFileUtils.copyInputStreamToFile(f.getInputStream(e), historyFilePath);
                            } catch (IOException e1) {
                            }
                            return true;
                        });
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
     * @return response entity
     */
    @RequestMapping("export")
    @Csrf
    public ResponseEntity<StreamingResponseBody> export(@RequestAttribute SysSite site) {
        String filepath = siteComponent.getTaskTemplateFilePath(site.getId(), Constants.SEPARATOR);
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(CommonUtils.joinString(site.getName(), dateFormat.format(new Date()), "-tasktemplate.zip"),
                        Constants.DEFAULT_CHARSET)
                .build());
        StreamingResponseBody body = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                try (ArchiveOutputStream archiveOutputStream = new ZipArchiveOutputStream(outputStream)) {
                    ZipUtils.compress(Paths.get(filepath), archiveOutputStream, Constants.BLANK);
                }
            }
        };
        return ResponseEntity.ok().headers(headers).body(body);
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
            String filepath = siteComponent.getTaskTemplateFilePath(site.getId(), path);
            String backupFilePath = siteComponent.getTaskTemplateBackupFilePath(site.getId(), path);
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
