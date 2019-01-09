package com.publiccms.controller.admin.cms;

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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.DateFormatUtils;
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
import com.publiccms.entities.sys.SysDeptCategoryId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDeptCategoryService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.model.CmsContentParameters;
import com.publiccms.views.pojo.query.CmsContentQuery;

/**
 *
 * CmsContentController
 *
 */
@Controller
@RequestMapping("cmsContent")
public class CmsContentAdminController {
    @Autowired
    private CmsContentService service;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDeptCategoryService sysDeptCategoryService;
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

    public static final String[] ignoreProperties = new String[] { "siteId", "userId", "categoryId", "tagIds", "sort",
            "createDate", "updateDate", "clicks", "comments", "scores", "childs", "checkUserId" };

    public static final String[] ignorePropertiesWithUrl = ArrayUtils.addAll(ignoreProperties, new String[] { "url" });

    /**
     * 保存内容
     *
     * @param entity
     * @param attribute
     * @param contentParameters
     * @param draft
     * @param checked
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    public String save(CmsContent entity, CmsContentAttribute attribute, @ModelAttribute CmsContentParameters contentParameters,
            Boolean draft, Boolean checked, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = siteComponent.getSite(request.getServerName());
        SysUser user = ControllerUtils.getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                || ControllerUtils.verifyCustom("noright", !(dept.isOwnsAllCategory() || null != sysDeptCategoryService
                        .getEntity(new SysDeptCategoryId(user.getDeptId(), entity.getCategoryId()))), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (ControllerUtils.verifyNotEmpty("categoryModel", categoryModel, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (null != category && site.getId() != category.getSiteId()) {
            category = null;
        }

        CmsModel cmsModel = modelComponent.getMap(site).get(entity.getModelId());
        if (ControllerUtils.verifyNotEmpty("category", category, model)
                || ControllerUtils.verifyNotEmpty("model", cmsModel, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        Date now = CommonUtils.getDate();
        initContent(entity, cmsModel, draft, checked, attribute, now);
        if (null != entity.getId()) {
            CmsContent oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)
                    || ControllerUtils.verifyCustom("noright",
                            !(user.isOwnsAllContent() || oldEntity.getUserId() == user.getId()), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity.setUpdateDate(now);
            entity = service.update(entity.getId(), entity, entity.isOnlyUrl() ? ignoreProperties : ignorePropertiesWithUrl);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.content", RequestUtils.getIpAddress(request), now, JsonUtils.getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            entity.setUserId(user.getId());
            service.save(entity);
            if (CommonUtils.notEmpty(entity.getParentId())) {
                service.updateChilds(entity.getParentId(), 1);
            }
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.content",
                    RequestUtils.getIpAddress(request), now, JsonUtils.getString(entity)));
        }
        service.saveTagAndAttribute(site.getId(), user.getId(), entity.getId(), contentParameters, cmsModel, category, attribute);
        if (null != checked && checked) {
            service.check(site.getId(), user, new Long[] { entity.getId() });
            if (CommonUtils.notEmpty(entity.getParentId())) {
                CmsContent parent = service.getEntity(entity.getParentId());
                if (null != parent) {
                    publish(site, parent, user);
                }
            }
            templateComponent.createCategoryFile(site, category, null, null);
        }
        if (!entity.isOnlyUrl()) {
            templateComponent.createContentFile(site, entity, category, categoryModel);// 静态化
            if (null == entity.getParentId() && !entity.isOnlyUrl()) {
                service.quote(site.getId(), entity, contentParameters.getContentIds());
                Set<Integer> categoryIdsList = contentParameters.getCategoryIds();
                if (CommonUtils.notEmpty(categoryIdsList)) {
                    if (categoryIdsList.contains(entity.getCategoryId())) {
                        categoryIdsList.remove(entity.getCategoryId());
                    }
                    entity = service.getEntity(entity.getId());
                    for (Integer categoryId : categoryIdsList) {
                        CmsContent quote = new CmsContent(entity.getSiteId(), entity.getTitle(), entity.getUserId(), categoryId,
                                entity.getModelId(), entity.isCopied(), true, entity.isHasImages(), entity.isHasFiles(), false, 0,
                                0, 0, 0, entity.getPublishDate(), entity.getCreateDate(), 0, entity.getStatus(), false);
                        quote.setQuoteContentId(entity.getId());
                        quote.setDescription(entity.getDescription());
                        quote.setAuthor(entity.getAuthor());
                        quote.setCover(entity.getCover());
                        quote.setEditor(entity.getEditor());
                        quote.setExpiryDate(entity.getExpiryDate());
                        service.save(quote);
                    }
                }
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    public static void initContent(CmsContent entity, CmsModel cmsModel, Boolean draft, Boolean checked,
            CmsContentAttribute attribute, Date now) {
        entity.setHasFiles(cmsModel.isHasFiles());
        entity.setHasImages(cmsModel.isHasImages());
        entity.setOnlyUrl(cmsModel.isOnlyUrl());
        if ((null == checked || !checked) && null != draft && draft) {
            entity.setStatus(CmsContentService.STATUS_DRAFT);
        } else {
            entity.setStatus(CmsContentService.STATUS_PEND);
        }
        if (null == entity.getPublishDate()) {
            entity.setPublishDate(now);
        }

        if (null != attribute.getText()) {
            attribute.setText(new String(VerificationUtils.base64Decode(attribute.getText()), CommonConstants.DEFAULT_CHARSET));
            String text = HtmlUtils.removeHtmlTag(attribute.getText());
            attribute.setWordCount(text.length());
            if (CommonUtils.empty(entity.getDescription())) {
                entity.setDescription(StringUtils.substring(text, 0, 300));
            }
        }
    }

    /**
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("check")
    public String check(Long[] ids, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        return checkOrUncheck(false, ids, _csrf, request, session, model);
    }

    /**
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("uncheck")
    public String uncheck(Long[] ids, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        return checkOrUncheck(true, ids, _csrf, request, session, model);
    }

    private String checkOrUncheck(boolean uncheck, Long[] ids, String _csrf, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = siteComponent.getSite(request.getServerName());
            SysUser user = ControllerUtils.getAdminFromSession(session);
            List<CmsContent> entityList;
            if (uncheck) {
                entityList = service.uncheck(site.getId(), user, ids);
            } else {
                entityList = service.check(site.getId(), user, ids);
            }
            Set<Integer> categoryIdSet = new HashSet<>();
            Set<Long> parentIdSet = new HashSet<>();
            for (CmsContent entity : entityList) {
                if (null != entity && site.getId() == entity.getSiteId()) {
                    if (CommonUtils.notEmpty(entity.getParentId())) {
                        parentIdSet.add(entity.getParentId());
                    }
                    publish(site, entity, user);
                    categoryIdSet.add(entity.getCategoryId());
                }
            }
            for (CmsContent parent : service.getEntitys(parentIdSet.toArray(new Integer[parentIdSet.size()]))) {
                publish(site, parent, user);
            }
            for (CmsCategory category : categoryService.getEntitys(categoryIdSet.toArray(new Integer[categoryIdSet.size()]))) {
                templateComponent.createCategoryFile(site, category, null, null);
            }
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    uncheck ? "uncheck.content" : "check.content", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("refresh")
    public String refresh(Long[] ids, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = siteComponent.getSite(request.getServerName());
            SysUser user = ControllerUtils.getAdminFromSession(session);
            Set<Integer> categoryIdSet = new HashSet<>();
            for (CmsContent entity : service.refresh(site.getId(), user, ids)) {
                categoryIdSet.add(entity.getCategoryId());
            }
            if (!categoryIdSet.isEmpty()) {
                Integer[] categoryIds = categoryIdSet.toArray(new Integer[categoryIdSet.size()]);
                for (CmsCategory entity : categoryService.getEntitys(categoryIds)) {
                    templateComponent.createCategoryFile(site, entity, null, null);
                }
            }
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "refresh.content", RequestUtils.getIpAddress(request), CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param entity
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("related")
    public String related(CmsContentRelated entity, String _csrf, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        CmsContent content = service.getEntity(entity.getContentId());
        CmsContent related = service.getEntity(entity.getRelatedContentId());
        SysSite site = siteComponent.getSite(request.getServerName());
        SysUser user = ControllerUtils.getAdminFromSession(session);
        if (null != content && null != related) {
            if (null == entity || ControllerUtils.verifyNotEquals("siteId", site.getId(), content.getSiteId(), model)
                    || ControllerUtils.verifyNotEquals("siteId", site.getId(), related.getSiteId(), model) || ControllerUtils
                            .verifyCustom("noright", !(user.isOwnsAllContent() || content.getUserId() == user.getId()), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (CommonUtils.empty(entity.getTitle())) {
                entity.setTitle(entity.getTitle());
            }
            if (CommonUtils.empty(entity.getDescription())) {
                entity.setDescription(entity.getDescription());
            }
            entity.setUserId(user.getId());
            cmsContentRelatedService.save(entity);
            publish(site, content, user);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "related.content", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(related)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("unrelated")
    public String unrelated(Long id, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        CmsContentRelated entity = cmsContentRelatedService.getEntity(id);
        SysSite site = siteComponent.getSite(request.getServerName());
        SysUser user = ControllerUtils.getAdminFromSession(session);
        if (null != entity) {
            if (null == entity || ControllerUtils.verifyCustom("noright",
                    !(user.isOwnsAllContent() || entity.getUserId() == user.getId()), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            CmsContent content = service.getEntity(entity.getContentId());
            if (null == content || site.getId() == content.getSiteId()) {
                cmsContentRelatedService.delete(id);
                publish(site, content, user);
                logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "unrelated.content", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param categoryId
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("move")
    public String move(Long[] ids, Integer categoryId, String _csrf, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = siteComponent.getSite(request.getServerName());
        SysUser user = ControllerUtils.getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        CmsCategory category = categoryService.getEntity(categoryId);
        if (ControllerUtils.verifyNotEquals("siteId", site.getId(), category.getSiteId(), model)
                || ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                || ControllerUtils.verifyCustom("noright", !(dept.isOwnsAllCategory()
                        || null != sysDeptCategoryService.getEntity(new SysDeptCategoryId(user.getDeptId(), category.getId()))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids) && null != category && site.getId() == category.getSiteId()) {
            StringBuilder sb = new StringBuilder();
            Set<Integer> categoryIdSet = new HashSet<>();
            for (CmsContent entity : service.getEntitys(ids)) {
                if (null != entity && entity.getCategoryId() != categoryId && site.getId() == entity.getSiteId()
                        && null == entity.getParentId() && (user.isOwnsAllContent() || entity.getUserId() == user.getId())
                        && move(site, entity, categoryId)) {
                    categoryIdSet.add(entity.getCategoryId());
                } else {
                    sb.append(entity.getId()).append(CommonConstants.COMMA_DELIMITED);
                }
            }
            if (!categoryIdSet.isEmpty()) {
                categoryIdSet.add(categoryId);
                Integer[] categoryIds = categoryIdSet.toArray(new Integer[categoryIdSet.size()]);
                for (CmsCategory entity : categoryService.getEntitys(categoryIds)) {
                    templateComponent.createCategoryFile(site, entity, null, null);
                }
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
                model.addAttribute("message", LanguagesUtils.getMessage(CommonConstants.applicationContext,
                        RequestContextUtils.getLocale(request), "message.content_move_fail", sb.toString()));
            }
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "move.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder(StringUtils.join(ids, ',')).append(" to ").append(category.getId())
                            .append(":").append(category.getName()).toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param siteId
     * @param entity
     * @param categoryId
     */
    private boolean move(SysSite site, CmsContent entity, Integer categoryId) {
        CmsCategoryModel categoryModel = categoryModelService.getEntity(new CmsCategoryModelId(categoryId, entity.getModelId()));
        if (null != categoryModel) {
            service.updateCategoryId(entity.getSiteId(), entity.getId(), categoryId);
            templateComponent.createContentFile(site, entity, null, categoryModel);
            return true;
        }
        return false;
    }

    /**
     * @param id
     * @param modelId
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("changeModel")
    public String changeModel(Long id, String modelId, String _csrf, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = siteComponent.getSite(request.getServerName());
        SysUser user = ControllerUtils.getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        CmsContent content = service.getEntity(id);
        if (null != content && CommonUtils.notEmpty(modelId)) {
            if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                    || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                    || ControllerUtils.verifyCustom("noright",
                            !(dept.isOwnsAllCategory() || null != sysDeptCategoryService
                                    .getEntity(new SysDeptCategoryId(user.getDeptId(), content.getCategoryId()))),
                            model)
                    || ControllerUtils.verifyCustom("noright", !(user.isOwnsAllContent() || content.getUserId() == user.getId()),
                            model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.changeModel(id, modelId);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "changeModel.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder().append(id).append(" to ").append(modelId).toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param sort
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("sort")
    public String sort(Long id, int sort, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = siteComponent.getSite(request.getServerName());
        SysUser user = ControllerUtils.getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        CmsContent content = service.getEntity(id);
        if (null != content) {
            if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                    || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                    || ControllerUtils.verifyCustom("noright",
                            !(dept.isOwnsAllCategory() || null != sysDeptCategoryService
                                    .getEntity(new SysDeptCategoryId(user.getDeptId(), content.getCategoryId()))),
                            model)
                    || ControllerUtils.verifyCustom("noright", !(user.isOwnsAllContent() || content.getUserId() == user.getId()),
                            model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            CmsContent entity = service.sort(site.getId(), id, sort);
            CmsCategory category = categoryService.getEntity(entity.getCategoryId());
            if (null != category) {
                templateComponent.createCategoryFile(site, category, null, null);
            }
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "sort.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder().append(entity.getId()).append(":").append(entity.getTitle())
                            .append(" to ").append(sort).toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("publish")
    public String publish(Long[] ids, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = siteComponent.getSite(request.getServerName());
        SysUser user = ControllerUtils.getAdminFromSession(session);
        if (CommonUtils.notEmpty(ids)) {
            for (CmsContent entity : service.getEntitys(ids)) {
                if (!publish(site, entity, user)) {
                    ControllerUtils.verifyCustom("static", true, model);
                    return CommonConstants.TEMPLATE_ERROR;
                }
            }
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "static.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    private boolean publish(SysSite site, CmsContent entity, SysUser user) {
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (null != categoryModel && (user.isOwnsAllContent() || entity.getUserId() == user.getId())
                && (!entity.isOnlyUrl() || null != entity.getQuoteContentId())) {
            return templateComponent.createContentFile(site, entity, null, categoryModel);
        }
        return false;
    }

    /**
     * @param queryEntity
     * @param orderField
     * @param orderType
     * @param _csrf
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("export")
    public ExcelView export(CmsContentQuery queryEntity, String orderField, String orderType, String _csrf,
            HttpServletRequest request, ModelMap model) {
        ExcelView view = new ExcelView();
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            List<String> list = new ArrayList<>();
            list.add((String) model.get(CommonConstants.ERROR));
            view.getDataList().add(list);
            return view;
        }
        SysSite site = siteComponent.getSite(request.getServerName());
        queryEntity.setSiteId(site.getId());
        queryEntity.setDisabled(false);
        queryEntity.setEmptyParent(true);
        List<String> list = new ArrayList<>();
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        Locale locale = request.getLocale();
        if (null != localeResolver) {
            locale = localeResolver.resolveLocale(request);
        }
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.id"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.title"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.url"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.promulgator"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.category"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.model"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.score"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.comments"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.clicks"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.publish_date"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.create_date"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.top_level"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.status"));
        list.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.inspector"));
        view.getDataList().add(list);
        PageHandler page = service.getPage(queryEntity, null, orderField, orderType, 1, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<CmsContent> entityList = (List<CmsContent>) page.getList();
        Map<String, List<Serializable>> pksMap = new HashMap<>();
        for (CmsContent entity : entityList) {
            List<Serializable> userIds = pksMap.get("userIds");
            if (null == userIds) {
                userIds = new ArrayList<>();
                pksMap.put("userIds", userIds);
            }
            userIds.add(entity.getUserId());
            userIds.add(entity.getCheckUserId());
            List<Serializable> categoryIds = pksMap.get("categoryIds");
            if (null == categoryIds) {
                categoryIds = new ArrayList<>();
                pksMap.put("categoryIds", categoryIds);
            }
            categoryIds.add(entity.getCategoryId());
            List<Serializable> modelIds = pksMap.get("modelIds");
            if (null == modelIds) {
                modelIds = new ArrayList<>();
                pksMap.put("modelIds", modelIds);
            }
            modelIds.add(entity.getModelId());
        }
        Map<Long, SysUser> userMap = new HashMap<>();
        if (null != pksMap.get("userIds")) {
            List<Serializable> userIds = pksMap.get("userIds");
            List<SysUser> entitys = sysUserService.getEntitys(userIds.toArray(new Serializable[userIds.size()]));
            for (SysUser entity : entitys) {
                userMap.put(entity.getId(), entity);
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

        Map<String, CmsModel> modelMap = modelComponent.getMap(site);
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.FULL_DATE_FORMAT_STRING);
        SysUser user;
        CmsCategory category;
        CmsModel cmsModel;
        for (CmsContent entity : entityList) {
            List<String> cellList = new ArrayList<>();
            cellList.add(entity.getId().toString());
            cellList.add(entity.getTitle());
            cellList.add(entity.getUrl());
            user = userMap.get(entity.getUserId());
            cellList.add(null == user ? null : user.getNickName());
            category = categoryMap.get(entity.getCategoryId());
            cellList.add(null == category ? null : category.getName());
            cmsModel = modelMap.get(entity.getModelId());
            cellList.add(null == cmsModel ? null : cmsModel.getName());
            cellList.add(String.valueOf(entity.getScores()));
            cellList.add(String.valueOf(entity.getComments()));
            cellList.add(String.valueOf(entity.getClicks()));
            cellList.add(dateFormat.format(entity.getPublishDate()));
            cellList.add(dateFormat.format(entity.getCreateDate()));
            cellList.add(String.valueOf(entity.getSort()));
            cellList.add(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                    "page.status.content." + entity.getStatus()));
            user = userMap.get(entity.getCheckUserId());
            cellList.add(null == user ? null : user.getNickName());
            view.getDataList().add(cellList);
        }
        return view;
    }

    /**
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(Long[] ids, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = siteComponent.getSite(request.getServerName());
        SysUser user = ControllerUtils.getAdminFromSession(session);
        if (CommonUtils.notEmpty(ids)) {
            Set<Integer> categoryIdSet = new HashSet<>();
            for (CmsContent entity : service.delete(site.getId(), user, ids)) {
                categoryIdSet.add(entity.getCategoryId());
            }
            if (!categoryIdSet.isEmpty()) {
                Integer[] categoryIds = categoryIdSet.toArray(new Integer[categoryIdSet.size()]);
                for (CmsCategory entity : categoryService.getEntitys(categoryIds)) {
                    templateComponent.createCategoryFile(site, entity, null, null);
                }
            }
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("recycle")
    public String recycle(Long[] ids, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = siteComponent.getSite(request.getServerName());
        if (CommonUtils.notEmpty(ids)) {
            Set<Integer> categoryIdSet = new HashSet<>();
            for (CmsContent entity : service.getEntitys(ids)) {
                if (entity.isDisabled() && site.getId() == entity.getSiteId()) {
                    if (CommonUtils.notEmpty(entity.getParentId())) {
                        service.updateChilds(entity.getParentId(), 1);
                    }
                    categoryIdSet.add(entity.getCategoryId());
                }
            }
            service.recycle(site.getId(), ids);
            if (!categoryIdSet.isEmpty()) {
                Integer[] categoryIds = categoryIdSet.toArray(new Integer[categoryIdSet.size()]);
                for (CmsCategory entity : categoryService.getEntitys(categoryIds)) {
                    templateComponent.createCategoryFile(site, entity, null, null);
                }
            }
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "recycle.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("realDelete")
    public String realDelete(Long[] ids, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = siteComponent.getSite(request.getServerName());
        if (CommonUtils.notEmpty(ids)) {
            service.realDelete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "realDelete.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

}