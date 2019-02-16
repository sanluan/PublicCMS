package com.publiccms.controller.admin.cms;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.cache.CacheComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateCacheComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDeptPageService;
import com.publiccms.views.pojo.entities.CmsPageData;
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
public class CmsTemplateAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private TemplateCacheComponent templateCacheComponent;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private MetadataComponent metadataComponent;
    @Autowired
    private CmsPlaceService cmsPlaceService;
    @Autowired
    private SysDeptPageService sysDeptPageService;
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
        if (ControllerUtils.verifyCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            try {
                String filePath = siteComponent.getWebTemplateFilePath(site, path);
                CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filePath);
                content = new String(VerificationUtils.base64Decode(content), CommonConstants.DEFAULT_CHARSET);
                if (CmsFileUtils.createFile(filePath, content)) {
                    logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.web.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                } else {
                    String historyFilePath = siteComponent.getWebTemplateHistoryFilePath(site, path);
                    CmsFileUtils.updateFile(filePath, historyFilePath, content);
                    if (CommonUtils.notEmpty(metadata.getCacheTime()) && 0 < metadata.getCacheTime()) {
                        templateCacheComponent.deleteCachedFile(SiteComponent.getFullTemplatePath(site, path));
                    }
                    logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "update.web.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                }
                templateComponent.clearTemplateCache();
                cacheComponent.clearViewCache();
                if (CommonUtils.notEmpty(metadata.getPublishPath())) {
                    publish(site, path);
                }
            } catch (IOException | TemplateException e) {
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
     * @param path
     * @param content
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("savePlace")
    @Csrf
    public String savePlace(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, String content,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            try {
                String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
                content = new String(VerificationUtils.base64Decode(content), CommonConstants.DEFAULT_CHARSET);
                if (CmsFileUtils.createFile(filePath, content)) {
                    logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.place.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                } else {
                    String historyFilePath = siteComponent.getWebTemplateHistoryFilePath(site,
                            TemplateComponent.INCLUDE_DIRECTORY + path);
                    CmsFileUtils.updateFile(filePath, historyFilePath, content);
                    logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "update.place.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                }
                templateComponent.clearTemplateCache();
                if (site.isUseSsi()) {
                    CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
                    templateComponent.staticPlace(site, path, metadata);
                }
            } catch (IOException | TemplateException e) {
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
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("doUpload")
    @Csrf
    public String upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile[] files, String path,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null != files) {
            try {
                for (MultipartFile file : files) {
                    String filePath = path + CommonConstants.SEPARATOR + file.getOriginalFilename();
                    CmsFileUtils.upload(file, siteComponent.getWebTemplateFilePath(site, filePath));
                    CmsPageMetadata metadata = new CmsPageMetadata();
                    metadata.setUseDynamic(true);
                    metadataComponent.updateTemplateMetadata(filePath, metadata);
                    templateComponent.clearTemplateCache();
                    cacheComponent.clearViewCache();
                    logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "upload.web.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), filePath));
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
     * @param path
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, HttpServletRequest request,
            ModelMap model) {

        if (ControllerUtils.verifyCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            String filePath = siteComponent.getWebTemplateFilePath(site, path);
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filePath);
            if (CommonUtils.notEmpty(metadata.getCacheTime()) && metadata.getCacheTime() > 0) {
                templateCacheComponent.deleteCachedFile(SiteComponent.getFullTemplatePath(site, path));
            }
            String backupFilePath = siteComponent.getWebTemplateBackupFilePath(site, path);
            if (ControllerUtils.verifyCustom("notExist.template", !CmsFileUtils.moveFile(filePath, backupFilePath), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            metadataComponent.deleteTemplateMetadata(filePath);
            metadataComponent.deleteTemplateData(filePath);
            sysDeptPageService.delete(null, path);
            templateComponent.clearTemplateCache();
            cacheComponent.clearViewCache();
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "delete.web.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
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
    @RequestMapping("deletePlace")
    @Csrf
    public String deletePlace(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
            String backupFilePath = siteComponent.getWebTemplateBackupFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
            if (ControllerUtils.verifyCustom("notExist.template", !CmsFileUtils.moveFile(filePath, backupFilePath), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            metadataComponent.deletePlaceMetadata(filePath);
            cmsPlaceService.delete(site.getId(), path);
            templateComponent.clearTemplateCache();
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "delete.web.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param path
     * @param metadata
     * @param content
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("savePlaceMetaData")
    @Csrf
    public String savePlaceMetaData(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path,
            @ModelAttribute CmsPlaceMetadata metadata, String content, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
            try {
                CmsFileUtils.createFile(filePath, content);
                metadataComponent.updatePlaceMetadata(filePath, metadata);
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.template.meta", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                templateComponent.clearTemplateCache();
                if (site.isUseSsi()) {
                    templateComponent.staticPlace(site, path, metadata);
                }
            } catch (IOException | TemplateException e) {
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
     * @param path
     * @param metadata
     * @param content
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("saveMetaData")
    @Csrf
    public String saveMetadata(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path,
            @ModelAttribute CmsPageMetadata metadata, String content, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            if (path.endsWith(CommonConstants.SEPARATOR) || CommonUtils.empty(path)) {
                path += CommonConstants.getDefaultPage();
            }

            String filePath = siteComponent.getWebTemplateFilePath(site, path);
            try {
                CmsFileUtils.createFile(filePath, content);
                metadataComponent.updateTemplateMetadata(filePath, metadata);
                templateComponent.clearTemplateCache();
                cacheComponent.clearViewCache();
                if (CommonUtils.notEmpty(metadata.getPublishPath())) {
                    publish(site, path);
                }
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.template.meta", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
            } catch (IOException | TemplateException e) {
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
     * @param path
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("publishPlace")
    @Csrf
    public String publishPlace(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path,
            HttpServletRequest request, ModelMap model) {
        try {
            if (CommonUtils.notEmpty(path) && site.isUseSsi()) {
                CmsPlaceMetadata metadata = metadataComponent
                        .getPlaceMetadata(siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path));
                templateComponent.staticPlace(site, path, metadata);
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "static",
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
            }
            return CommonConstants.TEMPLATE_DONE;
        } catch (IOException | TemplateException e) {
            model.addAttribute(CommonConstants.ERROR, e.getMessage());
            log.error(e.getMessage(), e);
            return CommonConstants.TEMPLATE_ERROR;
        }
    }

    /**
     * @param site
     * @param path
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("publish")
    @Csrf
    public String publish(@RequestAttribute SysSite site, String path, HttpServletRequest request, ModelMap model) {
        try {
            publish(site, path);
            return CommonConstants.TEMPLATE_DONE;
        } catch (IOException | TemplateException e) {
            model.addAttribute(CommonConstants.ERROR, e.getMessage());
            log.error(e.getMessage(), e);
            return CommonConstants.TEMPLATE_ERROR;
        }
    }

    private void publish(SysSite site, String path) throws IOException, TemplateException {
        if (CommonUtils.notEmpty(path)) {
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(siteComponent.getWebTemplateFilePath(site, path));
            if (site.isUseStatic() && CommonUtils.notEmpty(metadata.getPublishPath())) {
                String templatePath = SiteComponent.getFullTemplatePath(site, path);
                CmsPageData data = metadataComponent.getTemplateData(siteComponent.getCurrentSiteWebTemplateFilePath(site, path));
                templateComponent.createStaticFile(site, templatePath, metadata.getPublishPath(), null, metadata.getAsMap(data),
                        null);
            }
        }
    }
}
