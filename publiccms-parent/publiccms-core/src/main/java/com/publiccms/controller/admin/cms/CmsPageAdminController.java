package com.publiccms.controller.admin.cms;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptItemId;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.DiyComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateCacheComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsEditorHistoryService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDeptItemService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

import freemarker.template.TemplateException;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * CmsPageController
 *
 */
@Controller
@RequestMapping("cmsPage")
public class CmsPageAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private MetadataComponent metadataComponent;
    @Resource
    private SysDeptItemService sysDeptItemService;
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private TemplateCacheComponent templateCacheComponent;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected DiyComponent diyComponent;
    @Resource
    protected TemplateComponent templateComponent;
    @Resource
    private CmsEditorHistoryService editorHistoryService;

    /**
     * @param site
     * @param admin
     * @param path
     * @param type
     * @param pageDate
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String saveMetadata(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, String type,
            @ModelAttribute CmsPageData pageDate, HttpServletRequest request, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils.errorCustom("noright",
                        !(dept.isOwnsAllPage() || null != sysDeptItemService
                                .getEntity(new SysDeptItemId(admin.getDeptId(), SysDeptItemService.ITEM_TYPE_PAGE, path))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            String filepath = siteComponent.getTemplateFilePath(site.getId(), path);
            List<SysExtendField> extendList = null;
            CmsPlaceMetadata placeMetadata = null;
            CmsPageMetadata pageMetadata = null;
            if ("place".equalsIgnoreCase(type)) {
                placeMetadata = metadataComponent.getPlaceMetadata(filepath);
                extendList = placeMetadata.getMetadataExtendList();
            } else {
                pageMetadata = metadataComponent.getTemplateMetadata(filepath);
                extendList = pageMetadata.getExtendList();
            }
            
            ExtendUtils.decodeField(pageDate.getExtendData(), site.getSitePath(), extendList);
            CmsPageData olddata = metadataComponent.getTemplateData(filepath);
            metadataComponent.updateTemplateData(filepath, pageDate);
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "update.template.data", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
            if (null != olddata && null != olddata.getExtendData()) {
                editorHistoryService.saveHistory(site.getId(), admin.getId(), CmsEditorHistoryService.ITEM_TYPE_METADATA_EXTEND,
                        path, olddata.getExtendData(), pageDate.getExtendData(), extendList);
            }
            if ("place".equalsIgnoreCase(type)) {
                if (path.startsWith(TemplateComponent.INCLUDE_DIRECTORY)
                        && (site.isUseSsi() || CmsFileUtils.exists(siteComponent.getWebFilePath(site.getId(), path)))) {
                    try {
                        templateComponent.staticPlace(site, path.substring(TemplateComponent.INCLUDE_DIRECTORY.length()),
                                placeMetadata, pageDate);
                    } catch (IOException | TemplateException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            } else {
                if (site.isUseStatic() && null != pageMetadata && CommonUtils.notEmpty(pageMetadata.getPublishPath())) {
                    String templatePath = SiteComponent.getFullTemplatePath(site.getId(), path);
                    try {
                        templateComponent.createStaticFile(site, templatePath, pageMetadata.getPublishPath(), null,
                                pageMetadata.getAsMap(pageDate), null, null);
                    } catch (IOException | TemplateException e) {
                        log.error(e.getMessage(), e);
                    }
                }
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
    @RequestMapping("clearCache")
    @Csrf
    public String clearCache(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path,
            HttpServletRequest request, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils.errorCustom("noright",
                        !(dept.isOwnsAllPage() || null != sysDeptItemService
                                .getEntity(new SysDeptItemId(admin.getDeptId(), SysDeptItemService.ITEM_TYPE_PAGE, path))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            templateCacheComponent.deleteCachedFile(SiteComponent.getFullTemplatePath(site.getId(), path));
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "clear.pageCache", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}
