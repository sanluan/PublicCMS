package com.publiccms.controller.admin.cms;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.HtmlUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.common.view.ExcelView;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsContentRelated;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptItemId;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDeptItemService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.model.CmsContentParameters;
import com.publiccms.views.pojo.query.CmsContentQuery;

import freemarker.template.TemplateException;

/**
 *
 * CmsContentController
 *
 */
@Controller
@RequestMapping("cmsContent")
public class CmsContentAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private CmsContentService service;
    @Autowired
    private CmsContentAttributeService attributeService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysExtendFieldService extendFieldService;
    @Autowired
    private SysDeptItemService sysDeptItemService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private CmsContentRelatedService cmsContentRelatedService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private ModelComponent modelComponent;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    protected ConfigComponent configComponent;
    @Autowired
    private SysSiteService siteService;

    public static final String[] ignoreProperties = new String[] { "siteId", "userId", "deptId", "categoryId", "tagIds", "sort",
            "createDate", "updateDate", "clicks", "comments", "scores", "scoreUsers", "score", "childs", "checkUserId",
            "disabled" };

    public static final String[] ignorePropertiesWithUrl = ArrayUtils.addAll(ignoreProperties, new String[] { "url" });

    /**
     * 保存内容
     *
     * @param site
     * @param admin
     * @param entity
     * @param attribute
     * @param contentParameters
     * @param draft
     * @param checked
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsContent entity,
            CmsContentAttribute attribute, @ModelAttribute CmsContentParameters contentParameters, Boolean draft, Boolean checked,
            HttpServletRequest request, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils.errorCustom("noright",
                        !(dept.isOwnsAllCategory() || null != sysDeptItemService.getEntity(new SysDeptItemId(admin.getDeptId(),
                                SysDeptItemService.ITEM_TYPE_CATEGORY, String.valueOf(entity.getCategoryId())))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (null != category && site.getId() != category.getSiteId()) {
            category = null;
        }

        CmsModel cmsModel = modelComponent.getModelMap(site).get(entity.getModelId());
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));

        if (ControllerUtils.errorNotEmpty("category", category, model) || ControllerUtils.errorNotEmpty("model", cmsModel, model)
                || ControllerUtils.errorNotEmpty("categoryModel", categoryModel, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        Date now = CommonUtils.getDate();
        initContent(entity, cmsModel, draft, checked, attribute, true, now);
        CmsContent parent = service.getEntity(entity.getParentId());
        if (null != parent) {
            entity.setQuoteContentId(null == parent.getParentId() ? parent.getId() : parent.getQuoteContentId());
        }
        if (null != entity.getId()) {
            CmsContent oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.errorNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)
                    || ControllerUtils.errorCustom("noright", !ControllerUtils.hasContentPermissions(admin, oldEntity), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity.setUpdateDate(now);
            entity.setUpdateUserId(admin.getId());
            entity = service.update(entity.getId(), entity, entity.isOnlyUrl() ? ignoreProperties : ignorePropertiesWithUrl);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.content", RequestUtils.getIpAddress(request), now,
                        JsonUtils.getString(new Object[] { entity, attribute, contentParameters })));
            }
        } else {
            service.save(site.getId(), admin, entity);
            if (CommonUtils.notEmpty(entity.getParentId())) {
                service.updateChilds(entity.getParentId(), 1);
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.content", RequestUtils.getIpAddress(request), now,
                    JsonUtils.getString(new Object[] { entity, attribute, contentParameters })));
        }
        entity = service.saveTagAndAttribute(site.getId(), admin.getId(), entity.getId(), contentParameters, cmsModel,
                category.getExtendId(), attribute);
        if (null != checked && checked) {
            entity = service.check(site.getId(), admin, entity.getId());
        }
        try {
            templateComponent.createContentFile(site, entity, category, categoryModel); // 静态化
            if (null == entity.getParentId() && null == entity.getQuoteContentId()) {
                Set<Integer> categoryIdsSet = service.updateQuote(entity.getId(), contentParameters);
                if (CommonUtils.notEmpty(contentParameters.getCategoryIds())) {
                    List<CmsCategory> categoryList = categoryService.getEntitys(
                            contentParameters.getCategoryIds().toArray(new Integer[contentParameters.getCategoryIds().size()]));
                    service.saveQuote(entity.getId(), categoryList, category);
                    if (null != checked && checked) {
                        if (!categoryIdsSet.isEmpty()) {
                            categoryList.addAll(
                                    categoryService.getEntitys(categoryIdsSet.toArray(new Integer[categoryIdsSet.size()])));
                        }
                        for (CmsCategory c : categoryList) {
                            templateComponent.createCategoryFile(site, c, null, null);
                        }
                    }
                }
            }
            if (null != checked && checked) {
                templateComponent.createCategoryFile(site, category, null, null);
                if (null != parent) {
                    publish(site, parent, admin);
                }
            }
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
            model.put(CommonConstants.ERROR, e.getMessage());
            return CommonConstants.TEMPLATE_ERROR;
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    public static void initContent(CmsContent entity, CmsModel cmsModel, Boolean draft, Boolean checked,
            CmsContentAttribute attribute, boolean base64, Date now) {
        entity.setHasFiles(cmsModel.isHasFiles());
        entity.setHasImages(cmsModel.isHasImages());
        entity.setHasProducts(cmsModel.isHasProducts());
        entity.setOnlyUrl(cmsModel.isOnlyUrl());
        if ((null == checked || !checked) && null != draft && draft) {
            entity.setStatus(CmsContentService.STATUS_DRAFT);
        } else {
            entity.setStatus(CmsContentService.STATUS_PEND);
        }
        if (null == entity.getPublishDate()) {
            entity.setPublishDate(now);
        }
        if (null != attribute.getText() && base64) {
            attribute.setText(HtmlUtils.cleanUnsafeHtml(
                    new String(VerificationUtils.base64Decode(attribute.getText()), CommonConstants.DEFAULT_CHARSET)));
        }
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("check")
    @Csrf
    public String check(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request,
            ModelMap model) {
        return checkOrUncheck(site, admin, false, ids, request, model);
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @return view name
     */
    @RequestMapping("reject")
    @Csrf
    public String reject(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(ids)) {
            service.reject(site.getId(), admin, ids);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "reject.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("uncheck")
    @Csrf
    public String uncheck(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request,
            ModelMap model) {
        return checkOrUncheck(site, admin, true, ids, request, model);
    }

    private String checkOrUncheck(SysSite site, SysUser admin, boolean uncheck, Long[] ids, HttpServletRequest request,
            ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            List<CmsContent> entityList;
            if (uncheck) {
                entityList = service.uncheck(site.getId(), admin, ids);
            } else {
                entityList = service.check(site.getId(), admin, ids);
            }
            Set<Integer> categoryIdSet = new HashSet<>();
            Set<Long> parentIdSet = new HashSet<>();
            try {
                for (CmsContent entity : entityList) {
                    if (null != entity && site.getId() == entity.getSiteId()) {
                        if (CommonUtils.notEmpty(entity.getParentId())) {
                            parentIdSet.add(entity.getParentId());
                        }
                        publish(site, entity, admin);
                        categoryIdSet.add(entity.getCategoryId());
                    }
                }
                for (CmsContent parent : service.getEntitys(parentIdSet.toArray(new Long[parentIdSet.size()]))) {
                    publish(site, parent, admin);
                }
                for (CmsCategory category : categoryService
                        .getEntitys(categoryIdSet.toArray(new Integer[categoryIdSet.size()]))) {
                    templateComponent.createCategoryFile(site, category, null, null);
                }
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
                model.put(CommonConstants.ERROR, e.getMessage());
                return CommonConstants.TEMPLATE_ERROR;
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, uncheck ? "uncheck.content" : "check.content",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("refresh")
    @Csrf
    public String refresh(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request,
            ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            Set<Integer> categoryIdSet = new HashSet<>();
            for (CmsContent entity : service.refresh(site.getId(), admin, ids)) {
                categoryIdSet.add(entity.getCategoryId());
            }
            if (!categoryIdSet.isEmpty()) {
                Integer[] categoryIds = categoryIdSet.toArray(new Integer[categoryIdSet.size()]);
                try {
                    for (CmsCategory entity : categoryService.getEntitys(categoryIds)) {
                        templateComponent.createCategoryFile(site, entity, null, null);
                    }
                } catch (IOException | TemplateException e) {
                    log.error(e.getMessage(), e);
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                    return CommonConstants.TEMPLATE_ERROR;
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "refresh.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("unrelated")
    @Csrf
    public String unrelated(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, HttpServletRequest request,
            ModelMap model) {
        CmsContentRelated entity = cmsContentRelatedService.getEntity(id);
        if (null != entity) {
            CmsContent content = service.getEntity(entity.getContentId());
            if (null == content || site.getId() == content.getSiteId()) {
                if (ControllerUtils.errorCustom("noright", !ControllerUtils.hasContentPermissions(admin, content), model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
                cmsContentRelatedService.delete(id);
                try {
                    publish(site, content, admin);
                } catch (IOException | TemplateException e) {
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                    return CommonConstants.TEMPLATE_ERROR;
                }
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "unrelated.content", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param categoryId
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("move")
    @Csrf
    public String move(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, Integer categoryId,
            HttpServletRequest request, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        CmsCategory category = categoryService.getEntity(categoryId);
        if (ControllerUtils.errorNotEquals("siteId", site.getId(), category.getSiteId(), model)
                || ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils.errorCustom("noright",
                        !(dept.isOwnsAllCategory() || null != sysDeptItemService.getEntity(new SysDeptItemId(admin.getDeptId(),
                                SysDeptItemService.ITEM_TYPE_CATEGORY, String.valueOf(category.getId())))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids) && null != category && site.getId() == category.getSiteId()) {
            StringBuilder sb = new StringBuilder();
            Set<Integer> categoryIdSet = new HashSet<>();
            try {
                for (CmsContent entity : service.getEntitys(ids)) {
                    if (null != entity && entity.getCategoryId() != categoryId && site.getId() == entity.getSiteId()
                            && null == entity.getParentId() && ControllerUtils.hasContentPermissions(admin, entity)
                            && move(site, entity, categoryId)) {
                        categoryIdSet.add(entity.getCategoryId());
                    } else {
                        sb.append(entity.getId()).append(CommonConstants.COMMA_DELIMITED);
                    }
                }
            } catch (IOException | TemplateException e) {
                model.addAttribute(CommonConstants.ERROR, e.getMessage());
                log.error(e.getMessage(), e);
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (!categoryIdSet.isEmpty()) {
                categoryIdSet.add(categoryId);
                Integer[] categoryIds = categoryIdSet.toArray(new Integer[categoryIdSet.size()]);
                try {
                    for (CmsCategory entity : categoryService.getEntitys(categoryIds)) {
                        templateComponent.createCategoryFile(site, entity, null, null);
                    }
                } catch (IOException | TemplateException e) {
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                    return CommonConstants.TEMPLATE_ERROR;
                }
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
                model.addAttribute("message", LanguagesUtils.getMessage(CommonConstants.applicationContext,
                        RequestContextUtils.getLocale(request), "message.content_move_fail", sb.toString()));
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "move.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder(StringUtils.join(ids, CommonConstants.COMMA)).append(" to ")
                            .append(category.getId()).append(":").append(category.getName()).toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param entity
     * @param categoryId
     * @throws TemplateException
     * @throws IOException
     */
    private boolean move(SysSite site, CmsContent entity, Integer categoryId) throws IOException, TemplateException {
        CmsCategoryModel categoryModel = categoryModelService.getEntity(new CmsCategoryModelId(categoryId, entity.getModelId()));
        if (null != categoryModel) {
            entity = service.updateCategoryId(entity.getSiteId(), entity.getId(), categoryId);
            templateComponent.createContentFile(site, entity, null, categoryModel);
            return true;
        }
        return false;
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param modelId
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("changeModel")
    @Csrf
    public String changeModel(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, String modelId,
            HttpServletRequest request, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        CmsContent content = service.getEntity(id);
        if (null != content && CommonUtils.notEmpty(modelId)) {
            if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                    || ControllerUtils.errorNotEmpty("deptId", dept, model)
                    || ControllerUtils.errorCustom("noright",
                            !(dept.isOwnsAllCategory()
                                    || null != sysDeptItemService.getEntity(new SysDeptItemId(admin.getDeptId(),
                                            SysDeptItemService.ITEM_TYPE_CATEGORY, String.valueOf(content.getCategoryId())))),
                            model)
                    || ControllerUtils.errorCustom("noright", !ControllerUtils.hasContentPermissions(admin, content), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.changeModel(id, modelId);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "changeModel.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder().append(id).append(" to ").append(modelId).toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param sort
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("sort")
    @Csrf
    public String sort(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, int sort,
            HttpServletRequest request, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        CmsContent content = service.getEntity(id);
        if (null != content) {
            if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                    || ControllerUtils.errorNotEmpty("deptId", dept, model)
                    || ControllerUtils.errorCustom("noright",
                            !(dept.isOwnsAllCategory()
                                    || null != sysDeptItemService.getEntity(new SysDeptItemId(admin.getDeptId(),
                                            SysDeptItemService.ITEM_TYPE_CATEGORY, String.valueOf(content.getCategoryId())))),
                            model)
                    || ControllerUtils.errorCustom("noright", !ControllerUtils.hasContentPermissions(admin, content), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            CmsContent entity = service.sort(site.getId(), id, sort);
            CmsCategory category = categoryService.getEntity(entity.getCategoryId());
            if (null != category) {
                try {
                    templateComponent.createCategoryFile(site, category, null, null);
                } catch (IOException | TemplateException e) {
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                    return CommonConstants.TEMPLATE_ERROR;
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "sort.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder().append(entity.getId()).append(":").append(entity.getTitle())
                            .append(" to ").append(sort).toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("publish")
    @Csrf
    public String publish(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request,
            ModelMap model) {
        boolean flag = false;
        if (CommonUtils.notEmpty(ids)) {
            try {
                for (CmsContent entity : service.getEntitys(ids)) {
                    if (!publish(site, entity, admin)) {
                        flag = true;
                    }
                }
            } catch (IOException | TemplateException e) {
                model.addAttribute(CommonConstants.ERROR, e.getMessage());
                log.error(e.getMessage(), e);
                return CommonConstants.TEMPLATE_ERROR;
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "static.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
            if (flag) {
                ControllerUtils.errorCustom("static", true, model);
                return CommonConstants.TEMPLATE_ERROR;
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    private boolean publish(SysSite site, CmsContent entity, SysUser admin) throws IOException, TemplateException {
        if (ControllerUtils.hasContentPermissions(admin, entity)) {
            return templateComponent.createContentFile(site, entity, null, null);
        }
        return false;
    }

    /**
     * @param site
     * @param queryEntity
     * @param orderField
     * @param orderType
     * @param request
     * @return view name
     */
    @RequestMapping("export")
    @Csrf
    public ExcelView export(@RequestAttribute SysSite site, CmsContentQuery queryEntity, String orderField, String orderType,
            HttpServletRequest request) {
        queryEntity.setSiteId(site.getId());
        queryEntity.setDisabled(false);
        queryEntity.setEmptyParent(true);
        Locale locale = RequestContextUtils.getLocale(request);

        PageHandler page = service.getPage(queryEntity, null, orderField, orderType, null, 1, PageHandler.MAX_PAGE_SIZE, null);
        @SuppressWarnings("unchecked")
        List<CmsContent> entityList = (List<CmsContent>) page.getList();
        Map<String, List<Serializable>> pksMap = new HashMap<>();
        for (CmsContent entity : entityList) {
            List<Serializable> userIds = pksMap.computeIfAbsent("userIds", k -> new ArrayList<>());
            userIds.add(entity.getUserId());
            userIds.add(entity.getCheckUserId());
            List<Serializable> deptIds = pksMap.computeIfAbsent("deptIds", k -> new ArrayList<>());
            deptIds.add(entity.getDeptId());
            List<Serializable> categoryIds = pksMap.computeIfAbsent("categoryIds", k -> new ArrayList<>());
            categoryIds.add(entity.getCategoryId());
            List<Serializable> modelIds = pksMap.computeIfAbsent("modelIds", k -> new ArrayList<>());
            modelIds.add(entity.getModelId());
            List<Serializable> contentIds = pksMap.computeIfAbsent("contentIds", k -> new ArrayList<>());
            contentIds.add(entity.getId());
        }
        Map<Long, SysUser> userMap = new HashMap<>();
        if (null != pksMap.get("userIds")) {
            List<Serializable> userIds = pksMap.get("userIds");
            List<SysUser> entitys = sysUserService.getEntitys(userIds.toArray(new Serializable[userIds.size()]));
            for (SysUser entity : entitys) {
                userMap.put(entity.getId(), entity);
            }
        }
        Map<Integer, SysDept> deptMap = new HashMap<>();
        if (null != pksMap.get("deptIds")) {
            List<Serializable> deptIds = pksMap.get("deptIds");
            List<SysDept> entitys = sysDeptService.getEntitys(deptIds.toArray(new Serializable[deptIds.size()]));
            for (SysDept entity : entitys) {
                deptMap.put(entity.getId(), entity);
            }
        }
        Map<Integer, CmsCategory> categoryMap = new HashMap<>();
        if (null != pksMap.get("categoryIds")) {
            List<Serializable> categoryIds = pksMap.get("categoryIds");
            List<CmsCategory> entitys = categoryService.getEntitys(categoryIds.toArray(new Serializable[categoryIds.size()]));
            for (CmsCategory entity : entitys) {
                categoryMap.put(entity.getId(), entity);
            }
        }
        Map<String, CmsModel> modelMap = modelComponent.getModelMap(site);
        Map<Long, CmsContentAttribute> contentAttributeMap = new HashMap<>();
        if (null != pksMap.get("contentIds")) {
            List<Serializable> contentIds = pksMap.get("contentIds");
            List<CmsContentAttribute> entitys = attributeService
                    .getEntitys(contentIds.toArray(new Serializable[contentIds.size()]));
            for (CmsContentAttribute entity : entitys) {
                contentAttributeMap.put(entity.getContentId(), entity);
            }
        }

        ExcelView view = new ExcelView(workbook -> {
            List<SysExtendField> categoryExtendList = null;
            if (CommonUtils.notEmpty(queryEntity.getCategoryId())) {
                CmsCategory category = categoryService.getEntity(queryEntity.getCategoryId());
                if (null != category && null != category.getExtendId()) {
                    categoryExtendList = extendFieldService.getList(category.getExtendId(), null, null);
                }
            }
            List<SysExtendField> modelExtendList = null;
            if (CommonUtils.notEmpty(queryEntity.getModelIds()) && 1 == queryEntity.getModelIds().length) {
                CmsModel cmsModel = modelComponent.getModelMap(site).get(queryEntity.getModelIds()[0]);
                if (null != cmsModel) {
                    modelExtendList = cmsModel.getExtendList();
                }
            }

            Sheet sheet = workbook.createSheet(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(), "page.content"));
            int i = 0, j = 0;
            Row row = sheet.createRow(i++);

            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.id"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.title"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.url"));
            row.createCell(j++).setCellValue(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.promulgator"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.dept"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.category"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.model"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.score"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.comments"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.clicks"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.publish_date"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.create_date"));
            row.createCell(j++).setCellValue(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.top_level"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.status"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.inspector"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.source"));
            row.createCell(j++).setCellValue(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.source_url"));

            if (CommonUtils.notEmpty(categoryExtendList)) {
                for (SysExtendField extend : categoryExtendList) {
                    row.createCell(j++).setCellValue(extend.getName());
                }
            }
            if (CommonUtils.notEmpty(modelExtendList)) {
                for (SysExtendField extend : modelExtendList) {
                    row.createCell(j++).setCellValue(extend.getName());
                }
            }
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.text"));

            SysUser user;
            SysDept dept;
            CmsCategory category;
            CmsModel cmsModel;
            CmsContentAttribute attribute;
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.FULL_DATE_FORMAT_STRING);
            for (CmsContent entity : entityList) {
                row = sheet.createRow(i++);
                j = 0;
                row.createCell(j++).setCellValue(entity.getId().toString());
                row.createCell(j++).setCellValue(entity.getTitle());
                row.createCell(j++).setCellValue(entity.getUrl());
                user = userMap.get(entity.getUserId());
                dept = deptMap.get(entity.getDeptId());
                row.createCell(j++).setCellValue(null == user ? null : user.getNickname());
                row.createCell(j++).setCellValue(null == dept ? null : dept.getName());
                category = categoryMap.get(entity.getCategoryId());
                row.createCell(j++).setCellValue(null == category ? null : category.getName());
                cmsModel = modelMap.get(entity.getModelId());
                row.createCell(j++).setCellValue(null == cmsModel ? null : cmsModel.getName());
                row.createCell(j++).setCellValue(String.valueOf(entity.getScores()));
                row.createCell(j++).setCellValue(String.valueOf(entity.getComments()));
                row.createCell(j++).setCellValue(String.valueOf(entity.getClicks()));
                row.createCell(j++).setCellValue(dateFormat.format(entity.getPublishDate()));
                row.createCell(j++).setCellValue(dateFormat.format(entity.getCreateDate()));
                row.createCell(j++).setCellValue(String.valueOf(entity.getSort()));
                row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        "page.status.content." + entity.getStatus()));
                user = userMap.get(entity.getCheckUserId());
                row.createCell(j++).setCellValue(null == user ? null : user.getNickname());

                attribute = contentAttributeMap.get(entity.getId());
                row.createCell(j++).setCellValue(null == attribute ? null : attribute.getSource());
                row.createCell(j++).setCellValue(null == attribute ? null : attribute.getSourceUrl());

                Map<String, String> map = ExtendUtils.getExtendMap(null == attribute ? null : attribute.getData());
                if (CommonUtils.notEmpty(categoryExtendList) && entity.getCategoryId() == queryEntity.getCategoryId()) {
                    for (SysExtendField extend : categoryExtendList) {
                        row.createCell(j++).setCellValue(map.get(extend.getId().getCode()));
                    }
                }
                if (CommonUtils.notEmpty(modelExtendList)) {
                    for (SysExtendField extend : modelExtendList) {
                        row.createCell(j++).setCellValue(map.get(extend.getId().getCode()));
                    }
                }

                row.createCell(j++).setCellValue(null == attribute ? null : StringUtils.substring(attribute.getText(), 0, 32767));
                if (null != attribute && null != attribute.getText() && attribute.getText().length() > 32767) {
                    long length = attribute.getText().length();
                    int m = 0;
                    while ((length = length - 32767) > 0) {
                        m++;
                        row.createCell(j++).setCellValue(StringUtils.substring(attribute.getText(), m * 32767, (m + 1) * 32767));
                    }
                }
            }
        });
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.SHORT_DATE_FORMAT_STRING);
        view.setFilename(LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(), "page.content") + "_"
                + dateFormat.format(new Date()));
        return view;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request,
            ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            Set<Integer> categoryIdSet = new HashSet<>();
            for (CmsContent entity : service.delete(site.getId(), admin, ids)) {
                categoryIdSet.add(entity.getCategoryId());
                if (entity.isHasStatic()) {
                    String filepath = siteComponent.getWebFilePath(site, entity.getUrl());
                    if (CmsFileUtils.exists(filepath)) {
                        String backupFilePath = siteComponent.getWebBackupFilePath(site, filepath);
                        CmsFileUtils.moveFile(filepath, backupFilePath);
                    }
                }
            }
            if (!categoryIdSet.isEmpty()) {
                Integer[] categoryIds = categoryIdSet.toArray(new Integer[categoryIdSet.size()]);
                try {
                    for (CmsCategory entity : categoryService.getEntitys(categoryIds)) {
                        templateComponent.createCategoryFile(site, entity, null, null);
                    }
                } catch (IOException | TemplateException e) {
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                    log.error(e.getMessage(), e);
                    return CommonConstants.TEMPLATE_ERROR;
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param categoryIds
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("distribute")
    @Csrf
    public String distribute(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, Integer[] categoryIds,
            HttpServletRequest request, ModelMap model) {
        if (CommonUtils.notEmpty(categoryIds)) {
            CmsContent entity = service.getEntity(id);
            List<CmsCategory> categoryList = categoryService.getEntitys(categoryIds);
            if (null != categoryList) {
                for (CmsCategory category : categoryList) {

                    Map<String, String> config = configComponent.getConfigData(category.getSiteId(),
                            SiteConfigComponent.CONFIG_CODE_SITE);
                    int status = ConfigComponent.getInt(config.get(SiteConfigComponent.CONFIG_DEFAULT_CONTENT_STATUS),
                            CmsContentService.STATUS_PEND);
                    long userId;
                    if (category.getSiteId() == site.getId()) {
                        userId = entity.getUserId();
                    } else {
                        userId = ConfigComponent.getLong(config.get(SiteConfigComponent.CONFIG_DEFAULT_CONTENT_USER), 0);
                    }

                    if (0 != userId) {
                        TemplateComponent.initContentUrl(site, entity);
                        CmsContent content = service.copy(site, entity, category, status, userId);
                        if (null != content) {
                            try {
                                templateComponent.createContentFile(site, service.getEntity(content.getId()), category, null);
                                templateComponent.createCategoryFile(siteService.getEntity(category.getSiteId()), category, null,
                                        null);
                            } catch (IOException | TemplateException e) {
                                model.addAttribute(CommonConstants.ERROR, e.getMessage());
                                log.error(e.getMessage(), e);
                                return CommonConstants.TEMPLATE_ERROR;
                            }
                        }
                    }
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "copy.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(categoryIds, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("recycle")
    @Csrf
    public String recycle(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request,
            ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            Set<Integer> categoryIdSet = new HashSet<>();
            try {
                for (CmsContent entity : service.recycle(site.getId(), ids)) {
                    categoryIdSet.add(entity.getCategoryId());
                    templateComponent.createContentFile(site, entity, null, null);
                }
                if (!categoryIdSet.isEmpty()) {
                    Integer[] categoryIds = categoryIdSet.toArray(new Integer[categoryIdSet.size()]);
                    for (CmsCategory entity : categoryService.getEntitys(categoryIds)) {
                        templateComponent.createCategoryFile(site, entity, null, null);
                    }
                }
            } catch (IOException | TemplateException e) {
                model.addAttribute(CommonConstants.ERROR, e.getMessage());
                log.error(e.getMessage(), e);
                return CommonConstants.TEMPLATE_ERROR;
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "recycle.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @return view name
     */
    @RequestMapping("realDelete")
    @Csrf
    public String realDelete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(ids)) {
            service.realDelete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "realDelete.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

}