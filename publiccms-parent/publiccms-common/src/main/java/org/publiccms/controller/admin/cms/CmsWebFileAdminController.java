package org.publiccms.controller.admin.cms;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyCustom;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static com.publiccms.common.tools.ZipUtils.unzip;
import static com.publiccms.common.tools.ZipUtils.unzipHere;
import static com.publiccms.common.tools.ZipUtils.zip;
import static org.apache.commons.lang3.StringUtils.join;
import static org.publiccms.logic.service.log.LogLoginService.CHANNEL_WEB_MANAGER;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.log.LogUpload;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.site.FileComponent;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.log.LogUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

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
     * @param file
     * @param path
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("doUpload")
    public String upload(MultipartFile file, String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            try {
                SysSite site = getSite(request);
                path = path + SEPARATOR + file.getOriginalFilename();
                fileComponent.upload(file, siteComponent.getWebFilePath(site, path));
                logUploadService.save(new LogUpload(site.getId(), getAdminFromSession(session).getId(), CHANNEL_WEB_MANAGER,
                        false, file.getSize(), getIpAddress(request), getDate(), path));
            } catch (IOException e) {
                model.put(ERROR, e.getMessage());
                log.error(e.getMessage());
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
     * @return
     */
    @RequestMapping("delete")
    public String delete(String[] paths, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(paths)) {
            SysSite site = getSite(request);
            for (String path : paths) {
                String filePath = siteComponent.getWebFilePath(site, path);
                if (verifyCustom("notExist.webfile", !fileComponent.deleteFile(filePath), model)) {
                    return TEMPLATE_ERROR;
                }
            }
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "delete.web.webfile", getIpAddress(request), getDate(), join(paths, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("zip")
    public String doZip(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(path)) {
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebFilePath(site, path);
            File file = new File(filePath);
            if (notEmpty(file) && file.isDirectory()) {
                try {
                    zip(filePath, filePath + ".zip");
                } catch (IOException e) {
                    model.put(ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                }
            }
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "zip.web.webfile", getIpAddress(request), getDate(), path));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param request
     * @param session
     * @param model
     * @return
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
     * @return
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
        if (notEmpty(path) && path.toLowerCase().endsWith(".zip")) {
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebFilePath(site, path);
            File file = new File(filePath);
            if (notEmpty(file) && file.isFile()) {
                try {
                    if (here) {
                        unzipHere(filePath);
                    } else {
                        unzip(filePath, filePath.substring(0, filePath.length() - 4), true);
                    }
                } catch (IOException e) {
                    model.put(ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                }
            }
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "unzip.web.webfile", getIpAddress(request), getDate(), path));
        }
    }

    /**
     * @param path
     * @param fileName
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("createDirectory")
    public String createDirectory(String path, String fileName, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (null != path && notEmpty(fileName)) {
            SysSite site = getSite(request);
            path = path + SEPARATOR + fileName;
            String filePath = siteComponent.getWebFilePath(site, path);
            File file = new File(filePath);
            file.mkdirs();
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "createDirectory.web.webfile", getIpAddress(request), getDate(), path));
        }
        return TEMPLATE_DONE;
    }
}
