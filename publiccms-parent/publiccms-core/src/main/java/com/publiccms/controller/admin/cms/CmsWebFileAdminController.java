package com.publiccms.controller.admin.cms;

import java.io.File;
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
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.ZipUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.FileComponent;
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
                String filePath = siteComponent.getWebFilePath(site, path);
                File webFile = new File(filePath);
                if (CommonUtils.notEmpty(webFile)) {
                    fileComponent.updateFile(webFile, content);
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "update.web.webfile", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), path));
                } else {
                    fileComponent.createFile(webFile, content);
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "save.web.webfile", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), path));
                }
            } catch (IOException e) {
                model.addAttribute(ERROR, e.getMessage());
                log.error(e.getMessage(), e);
                return TEMPLATE_ERROR;
            }
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param file
     * @param path
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("doUpload")
    public String upload(MultipartFile file, String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            try {
                SysSite site = getSite(request);
                path = path + SEPARATOR + file.getOriginalFilename();
                fileComponent.upload(file, siteComponent.getWebFilePath(site, path));
                logUploadService.save(
                        new LogUpload(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                false, file.getSize(), RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
            } catch (IOException e) {
                model.addAttribute(ERROR, e.getMessage());
                log.error(e.getMessage(), e);
                return TEMPLATE_ERROR;
            }
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param paths
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(String[] paths, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(paths)) {
            SysSite site = getSite(request);
            for (String path : paths) {
                String filePath = siteComponent.getWebFilePath(site, path);
                if (ControllerUtils.verifyCustom("notExist.webfile", !fileComponent.deleteFile(filePath), model)) {
                    return TEMPLATE_ERROR;
                }
            }
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.web.webfile", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(paths, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("zip")
    public String doZip(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebFilePath(site, path);
            File file = new File(filePath);
            if (CommonUtils.notEmpty(file) && file.isDirectory()) {
                try {
                    ZipUtils.zip(filePath, filePath + ".zip");
                } catch (IOException e) {
                    model.addAttribute(ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                }
            }
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "zip.web.webfile", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("unzip")
    public String doUnzip(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        doUnzip(path, false, request, session, model);
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("unzipHere")
    public String doUnzipHere(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        doUnzip(path, true, request, session, model);
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param here
     * @param request
     * @param session
     * @param model
     */
    private void doUnzip(String path, boolean here, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(path) && path.toLowerCase().endsWith(".zip")) {
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebFilePath(site, path);
            File file = new File(filePath);
            if (CommonUtils.notEmpty(file) && file.isFile()) {
                try {
                    if (here) {
                        ZipUtils.unzipHere(filePath);
                    } else {
                        ZipUtils.unzip(filePath, filePath.substring(0, filePath.length() - 4), true);
                    }
                } catch (IOException e) {
                    model.addAttribute(ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                }
            }
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "unzip.web.webfile", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
    }

    /**
     * @param path
     * @param fileName
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("createDirectory")
    public String createDirectory(String path, String fileName, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (null != path && CommonUtils.notEmpty(fileName)) {
            SysSite site = getSite(request);
            path = path + SEPARATOR + fileName;
            String filePath = siteComponent.getWebFilePath(site, path);
            File file = new File(filePath);
            file.mkdirs();
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "createDirectory.web.webfile", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return TEMPLATE_DONE;
    }
}
