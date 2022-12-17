package com.publiccms.controller.admin.cms;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsEditorHistory;
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
import com.publiccms.logic.service.cms.CmsEditorHistoryService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDeptItemService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;
import com.publiccms.views.pojo.model.ExtendDataParameters;

/**
 * 
 * CmsPageController
 *
 */
@Controller
@RequestMapping("cmsPage")
public class CmsPageAdminController {
    @Autowired
    private MetadataComponent metadataComponent;
    @Autowired
    private SysDeptItemService sysDeptItemService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private TemplateCacheComponent templateCacheComponent;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    protected DiyComponent diyComponent;
    @Autowired
    private CmsEditorHistoryService editorHistoryService;

    /**
     * @param site
     * @param admin
     * @param path
     * @param type 
     * @param extendDataParameters
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String saveMetadata(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, String type,
            @ModelAttribute ExtendDataParameters extendDataParameters, HttpServletRequest request, ModelMap model) {
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
            CmsPageData olddata = metadataComponent.getTemplateData(filepath);
            CmsPageData pageDate = new CmsPageData();
            pageDate.setExtendDataList(extendDataParameters.getExtendDataList());
            metadataComponent.updateTemplateData(filepath, pageDate);
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "update.template.data", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
            if (null != olddata && null != olddata.getExtendData()) {
                List<SysExtendField> extendList = null;
                if ("place".equalsIgnoreCase(type)) {
                    CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
                    extendList = metadata.getMetadataExtendList();
                } else {
                    CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filepath);
                    extendList = metadata.getExtendList();
                }
                if (CommonUtils.notEmpty(olddata.getExtendData()) && CommonUtils.notEmpty(extendList)) {
                    Map<String, String> oldMap = olddata.getExtendData();
                    Map<String, String> map = pageDate.getExtendData();
                    for (SysExtendField extendField : extendList) {
                        if (ArrayUtils.contains(Config.INPUT_TYPE_EDITORS, extendField.getInputType())) {
                            if (CommonUtils.notEmpty(oldMap) && CommonUtils.notEmpty(oldMap.get(extendField.getId().getCode()))
                                    && (CommonUtils.notEmpty(map) || !oldMap.get(extendField.getId().getCode())
                                            .equals(map.get(extendField.getId().getCode())))) {
                                CmsEditorHistory history = new CmsEditorHistory(site.getId(),CmsEditorHistoryService.ITEM_TYPE_METADATA_EXTEND,
                                        path, extendField.getId().getCode(), CommonUtils.getDate(), admin.getId(),
                                        map.get(extendField.getId().getCode()));
                                editorHistoryService.save(history);
                            }
                        }
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
