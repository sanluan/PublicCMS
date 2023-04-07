package com.publiccms.controller.admin.cms;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.view.ExcelView;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.cms.CmsPlaceAttribute;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptItemId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.exchange.PlaceExchangeComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsEditorHistoryService;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDeptItemService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;
import com.publiccms.views.pojo.model.ExtendDataParameters;

import freemarker.template.TemplateException;

/**
 * 
 * cmsPlaceController
 *
 */
@Controller
@RequestMapping("cmsPlace")
public class CmsPlaceAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private CmsPlaceService service;
    @Resource
    private CmsPlaceAttributeService attributeService;
    @Resource
    private MetadataComponent metadataComponent;
    @Resource
    private SysDeptItemService sysDeptItemService;
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private CmsEditorHistoryService editorHistoryService;
    @Resource
    private PlaceExchangeComponent exchangeComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId", "status", "userId", "type", "clicks", "path", "createDate",
            "disabled" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param extendDataParameters
     * @param request
     * @param model
     * @return view name
     */
    @PostMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsPlace entity,
            @ModelAttribute ExtendDataParameters extendDataParameters, HttpServletRequest request, ModelMap model) {
        if (null != entity && CommonUtils.notEmpty(entity.getPath())) {
            if (!entity.getPath().startsWith(Constants.SEPARATOR)) {
                entity.setPath(CommonUtils.joinString(Constants.SEPARATOR, entity.getPath()));
            }
            entity.setPath(entity.getPath().replace("//", Constants.SEPARATOR));

            SysDept dept = sysDeptService.getEntity(admin.getDeptId());
            if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                    || ControllerUtils.errorNotEmpty("deptId", dept, model)
                    || ControllerUtils
                            .errorCustom("noright",
                                    !(dept.isOwnsAllPage()
                                            || null != sysDeptItemService.getEntity(
                                                    new SysDeptItemId(admin.getDeptId(), SysDeptItemService.ITEM_TYPE_PAGE,
                                                            CommonUtils.joinString(Constants.SEPARATOR,
                                                                    TemplateComponent.INCLUDE_DIRECTORY, entity.getPath())))),
                                    model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (CommonUtils.empty(entity.getItemType()) || CommonUtils.empty(entity.getItemId())) {
                entity.setItemType(CmsPlaceService.ITEM_TYPE_CUSTOM);
                entity.setItemId(null);
            }
            CmsPlace oldEntity = service.getEntity(entity.getId());
            if (null != oldEntity) {
                if (ControllerUtils.errorNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
                entity = service.update(entity.getId(), entity, ignoreProperties);
                if (null != entity) {
                    logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "update.place", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), JsonUtils.getString(entity)));
                }
            } else {
                entity.setUserId(admin.getId());
                entity.setSiteId(site.getId());
                entity.setStatus(CmsPlaceService.STATUS_NORMAL);
                entity.setCheckUserId(admin.getId());
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "save.place", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
            String filepath = siteComponent.getTemplateFilePath(site.getId(),
                    CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, entity.getPath()));
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);

            Map<String, String> map = extendDataParameters.getExtendData();
            CmsPlaceAttribute oldAttribute = attributeService.getEntity(entity.getId());
            attributeService.updateAttribute(entity.getId(),
                    ExtendUtils.getExtendString(map, site.getSitePath(), metadata.getExtendList()));

            if (null != oldAttribute && CommonUtils.notEmpty(oldAttribute.getData())) {
                Map<String, String> oldMap = ExtendUtils.getExtendMap(oldAttribute.getData());
                editorHistoryService.saveHistory(site.getId(), admin.getId(), CmsEditorHistoryService.ITEM_TYPE_PLACE_EXTEND,
                        String.valueOf(entity.getId()), oldMap, map, metadata.getExtendList());
            }

            staticPlace(site, entity.getPath());
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param path
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("refresh")
    @Csrf
    public String refresh(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, Long[] ids,
            HttpServletRequest request, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils
                        .errorCustom("noright",
                                !(dept.isOwnsAllPage() || null != sysDeptItemService.getEntity(new SysDeptItemId(
                                        admin.getDeptId(), SysDeptItemService.ITEM_TYPE_PAGE,
                                        CommonUtils.joinString(Constants.SEPARATOR, TemplateComponent.INCLUDE_DIRECTORY, path)))),
                                model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            service.refresh(site.getId(), ids, path);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "refresh.place", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, Constants.COMMA)));
            staticPlace(site, path);
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param path
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("check")
    @Csrf
    public String check(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, Long[] ids,
            HttpServletRequest request, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils
                        .errorCustom("noright",
                                !(dept.isOwnsAllPage() || null != sysDeptItemService.getEntity(new SysDeptItemId(
                                        admin.getDeptId(), SysDeptItemService.ITEM_TYPE_PAGE,
                                        CommonUtils.joinString(Constants.SEPARATOR, TemplateComponent.INCLUDE_DIRECTORY, path)))),
                                model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            service.check(site.getId(), admin.getId(), ids, path);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "check.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    StringUtils.join(ids, Constants.COMMA)));
            staticPlace(site, path);
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param path
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("uncheck")
    @Csrf
    public String uncheck(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, Long[] ids,
            HttpServletRequest request, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils
                        .errorCustom("noright",
                                !(dept.isOwnsAllPage() || null != sysDeptItemService.getEntity(new SysDeptItemId(
                                        admin.getDeptId(), SysDeptItemService.ITEM_TYPE_PAGE,
                                        CommonUtils.joinString(Constants.SEPARATOR, TemplateComponent.INCLUDE_DIRECTORY, path)))),
                                model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            service.uncheck(site.getId(), ids, path);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "check.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    StringUtils.join(ids, Constants.COMMA)));
            staticPlace(site, path);
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param path
     * @param userId
     * @param status
     * @param itemType
     * @param itemId
     * @param startPublishDate
     * @param endPublishDate
     * @param orderField
     * @param orderType
     * @param request
     * @return view name
     */
    @RequestMapping("export")
    @Csrf
    public ExcelView export(@RequestAttribute SysSite site, String path, Long userId, Integer[] status, String itemType,
            Long itemId, @DateTimeFormat(pattern = "yyyy-MM-dd") Date startPublishDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endPublishDate, String orderField, String orderType,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(path)) {
            path = path.replace("//", Constants.SEPARATOR);
        }
        Locale locale = RequestContextUtils.getLocale(request);
        return exchangeComponent.exportExcelByQuery(site, path, userId, status, itemType, itemId, startPublishDate,
                endPublishDate, orderField, orderType, locale);
    }

    /**
     * @param site
     * @param admin
     * @param path
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("clear")
    @Csrf
    public String clear(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, HttpServletRequest request,
            ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils
                        .errorCustom("noright",
                                !(dept.isOwnsAllPage() || null != sysDeptItemService.getEntity(new SysDeptItemId(
                                        admin.getDeptId(), SysDeptItemService.ITEM_TYPE_PAGE,
                                        CommonUtils.joinString(Constants.SEPARATOR, TemplateComponent.INCLUDE_DIRECTORY, path)))),
                                model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            service.delete(site.getId(), path);
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "clear.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
            staticPlace(site, path);
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param path
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String path, Long[] ids,
            HttpServletRequest request, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils
                        .errorCustom("noright",
                                !(dept.isOwnsAllPage() || null != sysDeptItemService.getEntity(new SysDeptItemId(
                                        admin.getDeptId(), SysDeptItemService.ITEM_TYPE_PAGE,
                                        CommonUtils.joinString(Constants.SEPARATOR, TemplateComponent.INCLUDE_DIRECTORY, path)))),
                                model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            service.delete(site.getId(), ids, path);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.place", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, Constants.COMMA)));
            staticPlace(site, path);
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    private void staticPlace(SysSite site, String path) {
        String placePath = CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, path);
        if (CmsFileUtils.exists(siteComponent.getWebFilePath(site.getId(), placePath))) {
            try {
                String filepath = siteComponent.getTemplateFilePath(site.getId(), placePath);
                CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
                CmsPageData data = metadataComponent.getTemplateData(filepath);
                templateComponent.staticPlace(site, path, metadata, data);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
