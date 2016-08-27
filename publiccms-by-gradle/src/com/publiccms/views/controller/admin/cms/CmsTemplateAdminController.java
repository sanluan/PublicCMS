package com.publiccms.views.controller.admin.cms;

import static com.publiccms.logic.component.SiteComponent.getFullFileName;
import static com.publiccms.logic.component.TemplateComponent.INCLUDE_DIRECTORY;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.FileComponent.FileInfo;
import com.publiccms.logic.component.MetadataComponent;
import com.publiccms.logic.component.TemplateCacheComponent;
import com.publiccms.logic.component.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDeptPageService;
import com.publiccms.views.pojo.CmsPageMetadata;
import com.publiccms.views.pojo.CmsPlaceMetadata;

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
    private MetadataComponent metadataComponent;
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private CmsPlaceService cmsPlaceService;
    @Autowired
    private SysDeptPageService sysDeptPageService;

    /**
     * @param path
     * @param type
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
                templateComponent.clear();
                if (notEmpty(metadata.getPublishPath())) {
                    publish(site, path);
                }
            } catch (IOException | TemplateException e) {
                verifyCustom("template.save", true, model);
                log.error(e.getMessage());
                return TEMPLATE_ERROR;
            }
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param type
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
            if (verifyCustom("notExist.template", !fileComponent.deleteFile(filePath), model)) {
                return TEMPLATE_ERROR;
            }
            metadataComponent.deleteTemplateMetadata(filePath);
            sysDeptPageService.delete(null, path);
            templateComponent.clear();
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.web.template", getIpAddress(request), getDate(), path));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param type
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
            templateComponent.clear();
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
            String filePath = siteComponent.getWebTemplateFilePath(site, path);
            try {
                File templateFile = new File(filePath);
                if (empty(templateFile)) {
                    fileComponent.createFile(templateFile, content);
                }
                metadataComponent.updatePlaceMetadata(filePath, metadata);
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.template.meta", getIpAddress(request), getDate(), path));
            } catch (IOException e) {
                verifyCustom("metadata.save", true, model);
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
            SysSite site = getSite(request);
            String filePath = siteComponent.getWebTemplateFilePath(site, path);
            try {
                File templateFile = new File(filePath);
                if (empty(templateFile)) {
                    fileComponent.createFile(templateFile, content);
                }
                CmsPageMetadata oldmetadata = metadataComponent.getTemplateMetadata(filePath);
                if (notEmpty(oldmetadata)) {
                    metadata.setExtendDataList(oldmetadata.getExtendDataList());
                }
                metadataComponent.updateTemplateMetadata(filePath, metadata);
                if (notEmpty(metadata.getPublishPath())) {
                    publish(site, path);
                }
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.template.meta", getIpAddress(request), getDate(), path));
            } catch (IOException | TemplateException e) {
                verifyCustom("metadata.save", true, model);
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
     * @return
     */
    @RequestMapping("publishPlace")
    public String publishPlace(String path, HttpServletRequest request, HttpSession session) {
        try {
            SysSite site = getSite(request);
            if (notEmpty(path) && site.isUseSsi()) {
                CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(siteComponent.getWebTemplateFilePath(site,
                        INCLUDE_DIRECTORY + path));
                templateComponent.staticPlace(site, path, metadata);
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "static", getIpAddress(request), getDate(), path));
            }
            return TEMPLATE_DONE;
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage());
            return TEMPLATE_ERROR;
        }
    }

    /**
     * @param path
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("publish")
    public String publish(String path, HttpServletRequest request, HttpSession session) {
        try {
            SysSite site = getSite(request);
            publish(site, path);
            return TEMPLATE_DONE;
        } catch (IOException | TemplateException e) {
            return TEMPLATE_ERROR;
        }
    }

    private void publish(SysSite site, String path) throws IOException, TemplateException {
        if (notEmpty(path)) {
            if (site.isUseSsi()) {
                String placeListFilePath = siteComponent.getWebTemplateFilePath(site, INCLUDE_DIRECTORY + path + SEPARATOR);
                Map<String, CmsPlaceMetadata> metadataMap = metadataComponent.getPlaceMetadataMap(placeListFilePath);
                for (FileInfo fileInfo : fileComponent.getFileList(placeListFilePath)) {
                    String filePath = path + SEPARATOR + fileInfo.getFileName();
                    CmsPlaceMetadata metadata = metadataMap.get(fileInfo.getFileName());
                    if (empty(metadata)) {
                        metadata = new CmsPlaceMetadata();
                    }
                    templateComponent.staticPlace(site, filePath, metadata);
                }
            }
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(siteComponent.getWebTemplateFilePath(site, path));
            if (site.isUseStatic() && notEmpty(metadata.getPublishPath())) {
                templateComponent.createStaticFile(site, getFullFileName(site, path), metadata.getPublishPath(), null, metadata,
                        null);
            }
        }
    }
}
