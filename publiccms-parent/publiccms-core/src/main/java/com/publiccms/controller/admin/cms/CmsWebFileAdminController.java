package com.publiccms.controller.admin.cms;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.ZipUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.file.FileComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;

/**
 * 
 * CmsWebFileAdminController
 *
 */
@Controller
@RequestMapping("cmsWebFile")
public class CmsWebFileAdminController extends AbstractController {
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    protected LogUploadService logUploadService;

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
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(path)) {
            try {
                String filePath = siteComponent.getWebFilePath(site, path);
                if (fileComponent.createFile(filePath, content)) {
                    logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "save.web.webfile", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), path));
                } else {
                    String historyFilePath = siteComponent.getWebHistoryFilePath(site, path);
                    fileComponent.updateFile(filePath, historyFilePath, content);
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
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("doUpload")
    public String upload(MultipartFile[] files, String path, String _csrf, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null != files) {
            SysSite site = getSite(request);
            try {
                for (MultipartFile file : files) {
                    String originalName = file.getOriginalFilename();
                    String filePath = path + CommonConstants.SEPARATOR + originalName;
                    fileComponent.upload(file, siteComponent.getWebFilePath(site, filePath));
                    logUploadService.save(new LogUpload(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, originalName,
                            LogUploadService.getFileType(fileComponent.getSuffix(originalName)), file.getSize(),
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
            SysSite site = getSite(request);
            for (String path : paths) {
                String filePath = siteComponent.getWebFilePath(site, path);
                String backupFilePath = siteComponent.getWebBackupFilePath(site, path);
                if (ControllerUtils.verifyCustom("notExist.webfile", !fileComponent.moveFile(filePath, backupFilePath), model)) {
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
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebFilePath(site, path);
            if (fileComponent.isDirectory(filePath)) {
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
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebFilePath(site, path);
            if (fileComponent.isFile(filePath)) {
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
            SysSite site = getSite(request);
            path = path + CommonConstants.SEPARATOR + fileName;
            String filePath = siteComponent.getWebFilePath(site, path);
            fileComponent.mkdirs(filePath);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "createDirectory.web.webfile", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}
