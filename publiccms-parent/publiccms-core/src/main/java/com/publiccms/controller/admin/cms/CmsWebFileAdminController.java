package com.publiccms.controller.admin.cms;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogUploadService;

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
     * @param path
     * @param content
     *            // * @param _csrf
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    public String save(String path, String content, String _csrf, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = siteComponent.getSite(request.getServerName());
        if (CommonUtils.notEmpty(path)) {
            try {
                String filePath = siteComponent.getWebFilePath(site, path);
                content = new String(VerificationUtils.base64Decode(content), CommonConstants.DEFAULT_CHARSET);
                if (CmsFileUtils.createFile(filePath, content)) {
                    logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "save.web.webfile", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), path));
                } else {
                    String historyFilePath = siteComponent.getWebHistoryFilePath(site, path);
                    CmsFileUtils.updateFile(filePath, historyFilePath, content);
                    logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "update.web.webfile", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), path));
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
     * @param files
     * @param path
     * @param _csrf
     * @param override
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("doUpload")
    public String upload(MultipartFile[] files, String path, String _csrf, Boolean override, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null != files) {
            SysSite site = siteComponent.getSite(request.getServerName());
            try {
                for (MultipartFile file : files) {
                    String originalName = file.getOriginalFilename();
                    String filePath = path + CommonConstants.SEPARATOR + originalName;
                    String fuleFilePath = siteComponent.getWebFilePath(site, filePath);
                    if (null != override && override || !CmsFileUtils.exists(fuleFilePath)) {
                        CmsFileUtils.upload(file, fuleFilePath);
                    }
                    logUploadService.save(new LogUpload(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, originalName,
                            CmsFileUtils.getFileType(CmsFileUtils.getSuffix(originalName)), file.getSize(),
                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), filePath));
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
     * @param fileNames
     * @param path
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("check")
    @ResponseBody
    public boolean check(@RequestParam("fileNames[]") String[] fileNames, String path, String _csrf, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return false;
        }
        if (null != fileNames) {
            SysSite site = siteComponent.getSite(request.getServerName());
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
     * @param paths
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(String[] paths, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(paths)) {
            SysSite site = siteComponent.getSite(request.getServerName());
            for (String path : paths) {
                String filePath = siteComponent.getWebFilePath(site, path);
                String backupFilePath = siteComponent.getWebBackupFilePath(site, path);
                if (ControllerUtils.verifyCustom("notExist.webfile", !CmsFileUtils.moveFile(filePath, backupFilePath), model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
            }
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.web.webfile", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(paths, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("zip")
    public String doZip(String path, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            SysSite site = siteComponent.getSite(request.getServerName());
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
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "zip.web.webfile", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param encoding
     * @param here
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("unzip")
    public String doUnzip(String path, String encoding, boolean here, String _csrf, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path) && path.toLowerCase().endsWith(".zip")) {
            SysSite site = siteComponent.getSite(request.getServerName());
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
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "unzip.web.webfile", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param fileName
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("createDirectory")
    public String createDirectory(String path, String fileName, String _csrf, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null != path && CommonUtils.notEmpty(fileName)) {
            SysSite site = siteComponent.getSite(request.getServerName());
            path = path + CommonConstants.SEPARATOR + fileName;
            String filePath = siteComponent.getWebFilePath(site, path);
            CmsFileUtils.mkdirs(filePath);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "createDirectory.web.webfile", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}
