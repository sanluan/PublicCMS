package com.publiccms.controller.admin.cms;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.cache.CacheComponent;
import com.publiccms.logic.component.site.FileComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateCacheComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDeptPageService;
import com.publiccms.views.pojo.entities.CmsPageMetadata;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

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
     * @return view name
     */
    @RequestMapping("save")
    public String save(String path, String content, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(path)) {
            try {
                String filePath = siteComponent.getWebTemplateFilePath(site, path);
                File templateFile = new File(filePath);
                CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filePath);
                if (CommonUtils.notEmpty(templateFile)) {
                    fileComponent.updateFile(templateFile, content);
                    if (CommonUtils.notEmpty(metadata.getCacheTime()) && metadata.getCacheTime() > 0) {
                        templateCacheComponent.deleteCachedFile(SiteComponent.getFullFileName(site, path));
                    }
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "update.web.template", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), path));
                } else {
                    fileComponent.createFile(templateFile, content);
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "save.web.template", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), path));
                }
                templateComponent.clearTemplateCache();
                cacheComponent.clearViewCache();
                if (CommonUtils.notEmpty(metadata.getPublishPath())) {
                    publish(site, path);
                }
            } catch (IOException | TemplateException e) {
                model.addAttribute(ERROR, e.getMessage());
                log.error(e.getMessage(), e);
                return TEMPLATE_ERROR;
            }
        }
        return TEMPLATE_DONE;
    }

	/**
     * @param path
     * @param content
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("savePlace")
    public String savePlace(String path, String content, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(path)) {
            try {
                String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
				File templateFile = new File(filePath);
                if (CommonUtils.notEmpty(templateFile)) {
                    fileComponent.updateFile(templateFile, content);
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "update.place.template", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), path));
                } else {
                    fileComponent.createFile(templateFile, content);
                    logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "save.place.template", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), path));
                }
				templateComponent.clearTemplateCache();
				if(site.isUseSsi()){
				    CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
	                templateComponent.staticPlace(site, path, metadata);
				}
            } catch (IOException | TemplateException e) {
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
                fileComponent.upload(file, siteComponent.getWebTemplateFilePath(site, path));
                CmsPageMetadata metadata = new CmsPageMetadata();
                metadata.setUseDynamic(true);
                metadataComponent.updateTemplateMetadata(path, metadata);
                templateComponent.clearTemplateCache();
                cacheComponent.clearViewCache();
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "upload.web.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
            } catch (IOException e) {
                model.addAttribute(ERROR, e.getMessage());
                log.error(e.getMessage(), e);
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
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebTemplateFilePath(site, path);
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filePath);
            if (CommonUtils.notEmpty(metadata.getCacheTime()) && metadata.getCacheTime() > 0) {
                templateCacheComponent.deleteCachedFile(SiteComponent.getFullFileName(site, path));
            }
            if (ControllerUtils.verifyCustom("notExist.template", !fileComponent.deleteFile(filePath), model)) {
                return TEMPLATE_ERROR;
            }
            metadataComponent.deleteTemplateMetadata(filePath);
            sysDeptPageService.delete(null, path);
            templateComponent.clearTemplateCache();
            cacheComponent.clearViewCache();
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "delete.web.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
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
    @RequestMapping("deletePlace")
    public String deletePlace(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
            if (ControllerUtils.verifyCustom("notExist.template", !fileComponent.deleteFile(filePath), model)) {
                return TEMPLATE_ERROR;
            }
            metadataComponent.deletePlaceMetadata(filePath);
            cmsPlaceService.delete(site.getId(), path);
            templateComponent.clearTemplateCache();
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "delete.web.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
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
     * @return view name
     */
    @RequestMapping("savePlaceMetaData")
    public String savePlaceMetaData(String path, @ModelAttribute CmsPlaceMetadata metadata, String content,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
            try {
                File templateFile = new File(filePath);
                if (CommonUtils.empty(templateFile)) {
                    fileComponent.createFile(templateFile, content);
                }
                metadataComponent.updatePlaceMetadata(filePath, metadata);
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.template.meta", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                templateComponent.clearTemplateCache();
                if(site.isUseSsi()){
                    templateComponent.staticPlace(site, path, metadata);
                }
            } catch (IOException | TemplateException e) {
                model.addAttribute(ERROR, e.getMessage());
                log.error(e.getMessage(), e);
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
     * @return view name
     */
    @RequestMapping("saveMetaData")
    public String saveMetadata(String path, @ModelAttribute CmsPageMetadata metadata, String content, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            if (path.endsWith(SEPARATOR) || CommonUtils.empty(path)) {
                path += CommonConstants.getDefaultPage();
            }
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebTemplateFilePath(site, path);
            try {
                File templateFile = new File(filePath);
                if (CommonUtils.empty(templateFile)) {
                    fileComponent.createFile(templateFile, content);
                }
                CmsPageMetadata oldmetadata = metadataComponent.getTemplateMetadata(filePath);
                metadata.setExtendDataList(oldmetadata.getExtendDataList());
                metadataComponent.updateTemplateMetadata(filePath, metadata);
                templateComponent.clearTemplateCache();
                cacheComponent.clearViewCache();
                if (CommonUtils.notEmpty(metadata.getPublishPath())) {
                    publish(site, path);
                }
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.template.meta", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
            } catch (IOException | TemplateException e) {
                model.addAttribute(ERROR, e.getMessage());
                log.error(e.getMessage(), e);
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
     * @return view name
     */
    @RequestMapping("publishPlace")
    public String publishPlace(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        try {
            SysSite site = getSite(request);
            if (CommonUtils.notEmpty(path) && site.isUseSsi()) {
                CmsPlaceMetadata metadata = metadataComponent
                        .getPlaceMetadata(siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path));
                templateComponent.staticPlace(site, path, metadata);
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "static", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
            }
            return TEMPLATE_DONE;
        } catch (IOException | TemplateException e) {
            model.addAttribute(ERROR, e.getMessage());
            log.error(e.getMessage(), e);
            return TEMPLATE_ERROR;
        }
    }

    /**
     * @param path
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("publish")
    public String publish(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        try {
            SysSite site = getSite(request);
            publish(site, path);
            return TEMPLATE_DONE;
        } catch (IOException | TemplateException e) {
            model.addAttribute(ERROR, e.getMessage());
            log.error(e.getMessage(), e);
            return TEMPLATE_ERROR;
        }
    }

    private void publish(SysSite site, String path) throws IOException, TemplateException {
        if (CommonUtils.notEmpty(path)) {
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(siteComponent.getWebTemplateFilePath(site, path));
            if (site.isUseStatic() && CommonUtils.notEmpty(metadata.getPublishPath())) {
                templateComponent.createStaticFile(site, SiteComponent.getFullFileName(site, path), metadata.getPublishPath(),
                        null, metadata, null);
            }
        }
    }
}
