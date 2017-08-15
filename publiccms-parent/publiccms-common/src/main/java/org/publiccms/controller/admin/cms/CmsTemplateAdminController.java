package org.publiccms.controller.admin.cms;
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyCustom;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static org.publiccms.common.constants.CommonConstants.getDefaultPage;
import static org.publiccms.logic.component.site.SiteComponent.getFullFileName;
import static org.publiccms.logic.component.template.TemplateComponent.INCLUDE_DIRECTORY;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.cache.CacheComponent;
import org.publiccms.logic.component.site.FileComponent;
import org.publiccms.logic.component.template.MetadataComponent;
import org.publiccms.logic.component.template.TemplateCacheComponent;
import org.publiccms.logic.component.template.TemplateComponent;
import org.publiccms.logic.service.cms.CmsPlaceService;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysDeptPageService;
import org.publiccms.views.pojo.CmsPageMetadata;
import org.publiccms.views.pojo.CmsPlaceMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import freemarker.template.TemplateException;

/**
 *
 * CmsTemplateAdminController
 * 
 */
@Controller
@RequestMapping("cmsTemplate")
public class CmsTemplateAdminController extends AbstractController {
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private TemplateCacheComponent templateCacheComponent;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private MetadataComponent metadataComponent;
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private CmsPlaceService cmsPlaceService;
    @Autowired
    private SysDeptPageService sysDeptPageService;

    /**
     * @param path
     * @param content
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(String path, String content, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (notEmpty(path)) {
            try {
                String filePath = siteComponent.getWebTemplateFilePath(site, path);
                File templateFile = new File(filePath);
                CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filePath);
                if (notEmpty(templateFile)) {
                    fileComponent.updateFile(templateFile, content);
                    if (notEmpty(metadata.getCacheTime()) && metadata.getCacheTime() > 0) {
                        templateCacheComponent.deleteCachedFile(getFullFileName(site, path));
                    }
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "update.web.template", getIpAddress(request), getDate(), path));
                } else {
                    fileComponent.createFile(templateFile, content);
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "save.web.template", getIpAddress(request), getDate(), path));
                }
                templateComponent.clearTemplateCache();
                cacheComponent.clearViewCache();
                if (notEmpty(metadata.getPublishPath())) {
                    publish(site, path);
                }
            } catch (IOException | TemplateException e) {
                model.put(ERROR, e.getMessage());
                log.error(e.getMessage());
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
     * @return
     */
    @RequestMapping("doUpload")
    public String upload(MultipartFile file, String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            try {
                SysSite site = getSite(request);
                path = path + SEPARATOR + file.getOriginalFilename();
                fileComponent.upload(file, siteComponent.getWebTemplateFilePath(site, path));
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "upload.web.template", getIpAddress(request), getDate(), path));
            } catch (IOException e) {
                model.put(ERROR, e.getMessage());
                log.error(e.getMessage());
                return TEMPLATE_ERROR;
            }
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
    @RequestMapping("delete")
    public String delete(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(path)) {
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebTemplateFilePath(site, path);
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filePath);
            if (notEmpty(metadata.getCacheTime()) && metadata.getCacheTime() > 0) {
                templateCacheComponent.deleteCachedFile(getFullFileName(site, path));
            }
            if (verifyCustom("notExist.template", !fileComponent.deleteFile(filePath), model)) {
                return TEMPLATE_ERROR;
            }
            metadataComponent.deleteTemplateMetadata(filePath);
            sysDeptPageService.delete(null, path);
            templateComponent.clearTemplateCache();
            cacheComponent.clearViewCache();
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.web.template", getIpAddress(request), getDate(), path));
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
    @RequestMapping("deletePlace")
    public String deletePlace(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(path)) {
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebTemplateFilePath(site, INCLUDE_DIRECTORY + path);
            if (verifyCustom("notExist.template", !fileComponent.deleteFile(filePath), model)) {
                return TEMPLATE_ERROR;
            }
            metadataComponent.deletePlaceMetadata(filePath);
            cmsPlaceService.delete(site.getId(), path);
            templateComponent.clearTemplateCache();
            cacheComponent.clearViewCache();
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.web.template", getIpAddress(request), getDate(), path));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param metadata
     * @param content
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("savePlaceMetaData")
    public String savePlaceMetaData(String path, @ModelAttribute CmsPlaceMetadata metadata, String content,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(path)) {
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebTemplateFilePath(site, INCLUDE_DIRECTORY + path);
            try {
                File templateFile = new File(filePath);
                if (empty(templateFile)) {
                    fileComponent.createFile(templateFile, content);
                }
                metadataComponent.updatePlaceMetadata(filePath, metadata);
                templateComponent.clearTemplateCache();
                cacheComponent.clearViewCache();
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.template.meta", getIpAddress(request), getDate(), path));
            } catch (IOException e) {
                model.put(ERROR, e.getMessage());
                log.error(e.getMessage());
                return TEMPLATE_ERROR;
            }
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param metadata
     * @param content
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("saveMetaData")
    public String saveMetadata(String path, @ModelAttribute CmsPageMetadata metadata, String content, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        if (notEmpty(path)) {
            if (path.endsWith(SEPARATOR) || empty(path)) {
                path += getDefaultPage();
            }
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebTemplateFilePath(site, path);
            try {
                File templateFile = new File(filePath);
                if (empty(templateFile)) {
                    fileComponent.createFile(templateFile, content);
                }
                CmsPageMetadata oldmetadata = metadataComponent.getTemplateMetadata(filePath);
                if (null != oldmetadata) {
                    metadata.setExtendDataList(oldmetadata.getExtendDataList());
                }
                metadataComponent.updateTemplateMetadata(filePath, metadata);
                templateComponent.clearTemplateCache();
                cacheComponent.clearViewCache();
                if (notEmpty(metadata.getPublishPath())) {
                    publish(site, path);
                }
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.template.meta", getIpAddress(request), getDate(), path));
            } catch (IOException | TemplateException e) {
                model.put(ERROR, e.getMessage());
                log.error(e.getMessage());
                return TEMPLATE_ERROR;
            }
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
    @RequestMapping("publishPlace")
    public String publishPlace(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        try {
            SysSite site = getSite(request);
            if (notEmpty(path) && site.isUseSsi()) {
                CmsPlaceMetadata metadata = metadataComponent
                        .getPlaceMetadata(siteComponent.getWebTemplateFilePath(site, INCLUDE_DIRECTORY + path));
                templateComponent.staticPlace(site, path, metadata);
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "static", getIpAddress(request), getDate(), path));
            }
            return TEMPLATE_DONE;
        } catch (IOException | TemplateException e) {
            model.put(ERROR, e.getMessage());
            log.error(e.getMessage());
            return TEMPLATE_ERROR;
        }
    }

    /**
     * @param path
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("publish")
    public String publish(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        try {
            SysSite site = getSite(request);
            publish(site, path);
            return TEMPLATE_DONE;
        } catch (IOException | TemplateException e) {
            model.put(ERROR, e.getMessage());
            log.error(e.getMessage());
            return TEMPLATE_ERROR;
        }
    }

    private void publish(SysSite site, String path) throws IOException, TemplateException {
        if (notEmpty(path)) {
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(siteComponent.getWebTemplateFilePath(site, path));
            if (site.isUseStatic() && notEmpty(metadata.getPublishPath())) {
                templateComponent.createStaticFile(site, getFullFileName(site, path), metadata.getPublishPath(), null, metadata,
                        null);
            }
        }
    }
}
