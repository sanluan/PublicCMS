package com.publiccms.controller.admin.cms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.Comparator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipOutputStream;
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
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.common.tools.ZipUtils;
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
import com.publiccms.logic.service.sys.SysDeptItemService;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;
import com.publiccms.views.pojo.model.TemplateReplaceParameters;

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
    private SysDeptItemService sysDeptItemService;
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
                String filepath = siteComponent.getTemplateFilePath(site, path);
                CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filepath);
                content = new String(VerificationUtils.base64Decode(content), CommonConstants.DEFAULT_CHARSET);
                if (CmsFileUtils.createFile(filepath, content)) {
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                    "save.web.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                } else {
                    String historyFilePath = siteComponent.getTemplateHistoryFilePath(site, path, true);
                    CmsFileUtils.updateFile(filepath, historyFilePath, content);
                    if (CommonUtils.notEmpty(metadata.getCacheTime()) && 0 < metadata.getCacheTime()) {
                        templateCacheComponent.deleteCachedFile(SiteComponent.getFullTemplatePath(site, path));
                    }
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
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
        if (CommonUtils.notEmpty(path)) {
            try {
                String filepath = siteComponent.getTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
                content = new String(VerificationUtils.base64Decode(content), CommonConstants.DEFAULT_CHARSET);
                if (CmsFileUtils.createFile(filepath, content)) {
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                    "save.place.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                } else {
                    String historyFilePath = siteComponent.getTemplateHistoryFilePath(site,
                            TemplateComponent.INCLUDE_DIRECTORY + path, true);
                    CmsFileUtils.updateFile(filepath, historyFilePath, content);
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                    "update.place.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                }
                templateComponent.clearTemplateCache();
                if (site.isUseSsi()
                        || CmsFileUtils.exists(siteComponent.getWebFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path))) {
                    CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
                    CmsPageData data = metadataComponent.getTemplateData(filepath);
                    templateComponent.staticPlace(site, path, metadata, data);
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
     * @param replaceParameters
     * @param word
     * @param replace
     * @param request
     * @return view name
     */
    @RequestMapping("replace")
    @Csrf
    public String replace(@RequestAttribute SysSite site, @SessionAttribute SysUser admin,
            @ModelAttribute TemplateReplaceParameters replaceParameters, String word, String replace,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(word)) {
            String filePath = siteComponent.getTemplateFilePath(site, CommonConstants.SEPARATOR);
            CmsFileUtils.replaceFileList(filePath, replaceParameters.getReplaceList(), word, replace);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "replace.template", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), word + " to " + replace + " in " + replaceParameters.getReplaceList().toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param files
     * @param path
     * @param encoding
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("doUpload")
    @Csrf
    public String upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile[] files, String path,
            String encoding, HttpServletRequest request, ModelMap model) {
        if (null != files) {
            try {
                for (MultipartFile file : files) {
                    String filepath = path + CommonConstants.SEPARATOR + file.getOriginalFilename();
                    String destFullFileName = siteComponent.getTemplateFilePath(site, filepath);
                    CmsFileUtils.upload(file, destFullFileName);
                    if (destFullFileName.endsWith(".zip") && CmsFileUtils.isFile(destFullFileName)) {
                        ZipUtils.unzipHere(destFullFileName, encoding);
                        CmsFileUtils.delete(destFullFileName);
                        metadataComponent.clear();
                    } else {
                        CmsPageMetadata metadata = new CmsPageMetadata();
                        metadata.setUseDynamic(true);
                        metadataComponent.updateTemplateMetadata(filepath, metadata);
                    }
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                    "upload.web.template", RequestUtils.getIpAddress(request), CommonUtils.getDate(), filepath));
                }
                templateComponent.clearTemplateCache();
                cacheComponent.clearViewCache();
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
     * @param response
     */
    @RequestMapping("export")
    @Csrf
    public void export(@RequestAttribute SysSite site, HttpServletResponse response) {
        String filepath = siteComponent.getTemplateFilePath(site, CommonConstants.SEPARATOR);
        try {
            response.setHeader("content-disposition",
                    "attachment;fileName=" + URLEncoder.encode(site.getName() + "_template.zip", "utf-8"));
        } catch (UnsupportedEncodingException e1) {
        }
        try (ServletOutputStream outputStream = response.getOutputStream();
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            zipOutputStream.setEncoding(Constants.DEFAULT_CHARSET_NAME);
            ZipUtils.compress(Paths.get(filepath), zipOutputStream, Constants.BLANK);
        } catch (IOException e) {
        }
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
        if (CommonUtils.notEmpty(path)) {
            String filepath = siteComponent.getTemplateFilePath(site, path);
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filepath);
            if (CommonUtils.notEmpty(metadata.getCacheTime()) && metadata.getCacheTime() > 0) {
                templateCacheComponent.deleteCachedFile(SiteComponent.getFullTemplatePath(site, path));
            }
            String backupFilePath = siteComponent.getTemplateBackupFilePath(site, path);
            if (ControllerUtils.errorCustom("notExist.template", !CmsFileUtils.moveFile(filepath, backupFilePath), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            metadataComponent.deleteTemplateMetadata(filepath);
            metadataComponent.deleteTemplateData(filepath);
            sysDeptItemService.delete(null, "page", path);
            templateComponent.clearTemplateCache();
            cacheComponent.clearViewCache();
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
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
        if (CommonUtils.notEmpty(path)) {
            String filepath = siteComponent.getTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
            String backupFilePath = siteComponent.getTemplateBackupFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
            if (ControllerUtils.errorCustom("notExist.template", !CmsFileUtils.moveFile(filepath, backupFilePath), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            metadataComponent.deletePlaceMetadata(filepath);
            cmsPlaceService.delete(site.getId(), path);
            templateComponent.clearTemplateCache();
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
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
     * @param model
     * @return view name
     */
    @RequestMapping("savePlaceMetaData")
    @Csrf
    public String savePlaceMetaData(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path,
            @ModelAttribute CmsPlaceMetadata metadata, String content, HttpServletRequest request, ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            String filepath = siteComponent.getTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
            try {
                CmsFileUtils.createFile(filepath, content);
                if (CommonUtils.notEmpty(metadata.getExtendList())) {
                    metadata.getExtendList().sort(Comparator.comparing(e -> e.getSort()));
                }
                if (CommonUtils.notEmpty(metadata.getMetadataExtendList())) {
                    metadata.getMetadataExtendList().sort(Comparator.comparing(e -> e.getSort()));
                }
                metadataComponent.updatePlaceMetadata(filepath, metadata);
                logOperateService
                        .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.template.meta", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
                templateComponent.clearTemplateCache();
                if (site.isUseSsi()
                        || CmsFileUtils.exists(siteComponent.getWebFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path))) {
                    CmsPageData data = metadataComponent.getTemplateData(filepath);
                    templateComponent.staticPlace(site, path, metadata, data);
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
     * @param model
     * @return view name
     */
    @RequestMapping("saveMetaData")
    @Csrf
    public String saveMetadata(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path,
            @ModelAttribute CmsPageMetadata metadata, String content, HttpServletRequest request, ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            if (path.endsWith(CommonConstants.SEPARATOR) || CommonUtils.empty(path)) {
                path += CommonConstants.getDefaultPage();
            }

            String filepath = siteComponent.getTemplateFilePath(site, path);
            try {
                CmsFileUtils.createFile(filepath, content);
                if (CommonUtils.notEmpty(metadata.getExtendList())) {
                    metadata.getExtendList().sort(Comparator.comparing(e -> e.getSort()));
                }
                metadataComponent.updateTemplateMetadata(filepath, metadata);
                templateComponent.clearTemplateCache();
                cacheComponent.clearViewCache();
                if (CommonUtils.notEmpty(metadata.getPublishPath())) {
                    publish(site, path);
                }
                logOperateService
                        .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
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
            if (CommonUtils.notEmpty(path) && (site.isUseSsi()
                    || CmsFileUtils.exists(siteComponent.getWebFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path)))) {
                String filepath = siteComponent.getTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
                CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
                CmsPageData data = metadataComponent.getTemplateData(filepath);
                templateComponent.staticPlace(site, path, metadata, data);
                logOperateService
                        .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "static", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
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
     * @param model
     * @return view name
     */
    @RequestMapping("publish")
    @Csrf
    public String publish(@RequestAttribute SysSite site, String path, ModelMap model) {
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
            String filepath = siteComponent.getTemplateFilePath(site, path);
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filepath);
            if (site.isUseStatic() && CommonUtils.notEmpty(metadata.getPublishPath())) {
                String templatePath = SiteComponent.getFullTemplatePath(site, path);
                CmsPageData data = metadataComponent.getTemplateData(filepath);
                templateComponent.createStaticFile(site, templatePath, metadata.getPublishPath(), null, metadata.getAsMap(data),
                        null, null);
            }
        }
    }
}
