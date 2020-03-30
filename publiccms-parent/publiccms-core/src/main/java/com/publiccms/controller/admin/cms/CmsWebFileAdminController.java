package com.publiccms.controller.admin.cms;

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
                String filePath = siteComponent.getWebFilePath(site, path);
                content = new String(VerificationUtils.base64Decode(content), CommonConstants.DEFAULT_CHARSET);
                if (CmsFileUtils.createFile(filePath, content)) {
                    logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.web.webfile", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                } else {
                    String historyFilePath = siteComponent.getWebHistoryFilePath(site, path);
                    CmsFileUtils.updateFile(filePath, historyFilePath, content);
                    logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
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
                    String filePath = path + CommonConstants.SEPARATOR + originalName;
                    String fuleFilePath = siteComponent.getWebFilePath(site, filePath);
                    if (null != override && override || !CmsFileUtils.exists(fuleFilePath)) {
                        CmsFileUtils.upload(file, fuleFilePath);
                        FileSize fileSize = CmsFileUtils.getFileSize(fuleFilePath, suffix);
                        logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                originalName, CmsFileUtils.getFileType(CmsFileUtils.getSuffix(originalName)), file.getSize(),
                                fileSize.getWidth(), fileSize.getHeight(), RequestUtils.getIpAddress(request),
                                CommonUtils.getDate(), filePath));
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
     * @param fileNames
     * @param path
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("check")
    @Csrf
    @ResponseBody
    public boolean check(@RequestAttribute SysSite site, @RequestParam("fileNames[]") String[] fileNames, String path,
            HttpServletRequest request, ModelMap model) {
        if (null != fileNames) {
            for (String fileName : fileNames) {
                String filePath = path + CommonConstants.SEPARATOR + fileName;
                if (CmsFileUtils.exists(siteComponent.getWebFilePath(site, filePath))) {
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
                String filePath = siteComponent.getWebFilePath(site, path);
                String backupFilePath = siteComponent.getWebBackupFilePath(site, path);
                if (ControllerUtils.verifyCustom("notExist.webfile", !CmsFileUtils.moveFile(filePath, backupFilePath), model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "delete.web.webfile", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    StringUtils.join(paths, CommonConstants.COMMA)));
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
            String filePath = siteComponent.getWebFilePath(site, path);
            if (CmsFileUtils.isDirectory(filePath)) {
                try {
                    String zipFileName = null;
                    if (path.endsWith("/") || path.endsWith("\\")) {
                        zipFileName = filePath + "files.zip";
                    } else {
                        zipFileName = filePath + ".zip";
                    }
                    ZipUtils.zip(filePath, zipFileName);
                } catch (IOException e) {
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
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
            String filePath = siteComponent.getWebFilePath(site, path);
            if (CmsFileUtils.isFile(filePath)) {
                try {
                    if (here) {
                        ZipUtils.unzipHere(filePath, encoding);
                    } else {
                        ZipUtils.unzip(filePath, filePath.substring(0, filePath.length() - 4), encoding, true);
                    }
                } catch (IOException e) {
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
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
     * @param model
     * @return view name
     */
    @RequestMapping("createDirectory")
    @Csrf
    public String createDirectory(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, String fileName,
            HttpServletRequest request, ModelMap model) {
        if (null != path && CommonUtils.notEmpty(fileName)) {
            path = path + CommonConstants.SEPARATOR + fileName;
            String filePath = siteComponent.getWebFilePath(site, path);
            CmsFileUtils.mkdirs(filePath);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "createDirectory.web.webfile", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}
