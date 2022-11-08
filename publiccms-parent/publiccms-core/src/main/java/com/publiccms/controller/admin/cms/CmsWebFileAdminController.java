package com.publiccms.controller.admin.cms;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ImageUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.common.tools.ZipUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.views.pojo.entities.FileSize;

/**
 * 
 * CmsWebFileAdminController
 *
 */
@Controller
@RequestMapping("cmsWebFile")
public class CmsWebFileAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    protected LogUploadService logUploadService;
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
        if (CommonUtils.notEmpty(path)) {
            try {
                String filepath = siteComponent.getWebFilePath(site, path);
                content = new String(VerificationUtils.base64Decode(content), CommonConstants.DEFAULT_CHARSET);
                if (CmsFileUtils.createFile(filepath, content)) {
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                    "save.web.webfile", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                } else {
                    String historyFilePath = siteComponent.getWebHistoryFilePath(site, path, true);
                    CmsFileUtils.updateFile(filepath, historyFilePath, content);
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                    "update.web.webfile", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
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
     * @param admin
     * @param files
     * @param path
     * @param override
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("doUpload")
    @Csrf
    public String upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile[] files, String path,
            Boolean override, HttpServletRequest request, ModelMap model) {
        if (null != files) {
            try {
                for (MultipartFile file : files) {
                    String originalName = file.getOriginalFilename();
                    String suffix = CmsFileUtils.getSuffix(originalName);
                    String filepath = path + CommonConstants.SEPARATOR + originalName;
                    String fuleFilePath = siteComponent.getWebFilePath(site, filepath);
                    if (null != override && override || !CmsFileUtils.exists(fuleFilePath)) {
                        CmsFileUtils.upload(file, fuleFilePath);
                        FileSize fileSize = CmsFileUtils.getFileSize(fuleFilePath, suffix);
                        logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                originalName, CmsFileUtils.getFileType(CmsFileUtils.getSuffix(originalName)), file.getSize(),
                                fileSize.getWidth(), fileSize.getHeight(), RequestUtils.getIpAddress(request),
                                CommonUtils.getDate(), filepath));
                    }
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
     * @param admin
     * @param file
     * @param filename
     * @param base64File
     * @param originalFilename
     * @param size
     * @param override
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("doUploadIco")
    @Csrf
    public String uploadIco(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file, String filename,
            String base64File, String originalFilename, int size, Boolean override, HttpServletRequest request, ModelMap model) {
        if (null != file && !file.isEmpty() || CommonUtils.notEmpty(base64File)) {
            String originalName;
            String suffix;
            if (null != file && !file.isEmpty()) {
                originalName = file.getOriginalFilename();
            } else {
                originalName = originalFilename;
            }
            suffix = CmsFileUtils.getSuffix(originalName);
            try {
                String filepath = CommonConstants.SEPARATOR + filename;
                String fuleFilePath = siteComponent.getWebFilePath(site, filepath);
                if (null != override && override || !CmsFileUtils.exists(fuleFilePath)) {
                    CmsFileUtils.mkdirsParent(fuleFilePath);
                    if (CommonUtils.notEmpty(base64File)) {
                        ImageUtils.image2Ico(new ByteArrayInputStream(VerificationUtils.base64Decode(base64File)), suffix, size,
                                new File(fuleFilePath));
                    } else {
                        ImageUtils.image2Ico(file.getInputStream(), suffix, size, new File(fuleFilePath));
                    }
                    FileSize fileSize = CmsFileUtils.getFileSize(fuleFilePath, suffix);
                    logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            filename, CmsFileUtils.FILE_TYPE_IMAGE, file.getSize(), fileSize.getWidth(), fileSize.getHeight(),
                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), filepath));
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
     * @param fileNames
     * @param path
     * @return view name
     */
    @RequestMapping("check")
    @Csrf
    @ResponseBody
    public boolean check(@RequestAttribute SysSite site, @RequestParam("fileNames[]") String[] fileNames, String path) {
        if (null != fileNames) {
            for (String fileName : fileNames) {
                String filepath = path + CommonConstants.SEPARATOR + fileName;
                if (CmsFileUtils.exists(siteComponent.getWebFilePath(site, filepath))) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * @param site
     * @param admin
     * @param paths
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String[] paths,
            HttpServletRequest request, ModelMap model) {
        if (CommonUtils.notEmpty(paths)) {
            for (String path : paths) {
                String filepath = siteComponent.getWebFilePath(site, path);
                String backupFilePath = siteComponent.getWebBackupFilePath(site, path);
                if (ControllerUtils.errorCustom("notExist.webfile", !CmsFileUtils.moveFile(filepath, backupFilePath), model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.web.webfile", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(paths, CommonConstants.COMMA)));
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
    @RequestMapping("zip")
    @Csrf
    public String doZip(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, HttpServletRequest request,
            ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            String filepath = siteComponent.getWebFilePath(site, path);
            if (CmsFileUtils.isDirectory(filepath)) {
                try {
                    String zipFileName = null;
                    if (path.endsWith("/") || path.endsWith("\\")) {
                        zipFileName = filepath + "files.zip";
                    } else {
                        zipFileName = filepath + ".zip";
                    }
                    ZipUtils.zip(filepath, zipFileName);
                } catch (IOException e) {
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                }
            }
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "zip.web.webfile", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param path
     * @param encoding
     * @param here
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("unzip")
    @Csrf
    public String doUnzip(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, String encoding,
            boolean here, HttpServletRequest request, ModelMap model) {
        if (CommonUtils.notEmpty(path) && path.toLowerCase().endsWith(".zip")) {
            String filepath = siteComponent.getWebFilePath(site, path);
            if (CmsFileUtils.isFile(filepath)) {
                try {
                    if (here) {
                        ZipUtils.unzipHere(filepath, encoding);
                    } else {
                        ZipUtils.unzip(filepath, filepath.substring(0, filepath.length() - 4), encoding, true);
                    }
                } catch (IOException e) {
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                }
            }
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "unzip.web.webfile", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param path
     * @param fileName
     * @param request
     * @return view name
     */
    @RequestMapping("createDirectory")
    @Csrf
    public String createDirectory(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, String fileName,
            HttpServletRequest request) {
        if (null != path && CommonUtils.notEmpty(fileName)) {
            path = path + CommonConstants.SEPARATOR + fileName;
            String filepath = siteComponent.getWebFilePath(site, path);
            CmsFileUtils.mkdirs(filepath);
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "createDirectory.web.webfile", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}
