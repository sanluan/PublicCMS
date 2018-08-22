package com.publiccms.controller.admin.cms;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.HtmlUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
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
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDeptCategoryService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.model.CmsContentParameters;

/**
 *
 * CmsContentController
 *
 */
@Controller
@RequestMapping("cmsContent")
public class CmsContentAdminController extends AbstractController {
    @Autowired
    private CmsContentService service;
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

    private String[] ignoreProperties = new String[] { "siteId", "userId", "categoryId", "tagIds", "sort", "createDate", "clicks",
            "comments", "scores", "childs", "checkUserId" };

    private String[] ignorePropertiesWithUrl = ArrayUtils.addAll(ignoreProperties, new String[] { "url" });

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
        SysSite site = getSite(request);
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
        templateComponent.createContentFile(site, entity, category, categoryModel);// 静态化
        if (null != checked && checked) {
            service.check(site.getId(), user, new Long[] { entity.getId() });
            if (CommonUtils.notEmpty(entity.getParentId())) {
                publish(new Long[] { entity.getParentId() }, _csrf, request, session, model);
            }
            templateComponent.createCategoryFile(site, category, null, null);
        }
        return CommonConstants.TEMPLATE_DONE;
    }
    
    public static void initContent(CmsContent entity, CmsModel cmsModel, Boolean draft, Boolean checked, CmsContentAttribute attribute,
            Date now) {
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
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            SysUser user = ControllerUtils.getAdminFromSession(session);
            List<CmsContent> entityList = service.check(site.getId(), user, ids);
            Set<Integer> categoryIdSet = new HashSet<>();
            for (CmsContent entity : entityList) {
                if (null != entity && site.getId() == entity.getSiteId()) {
                    if (CommonUtils.notEmpty(entity.getParentId())) {
                        publish(new Long[] { entity.getParentId() }, _csrf, request, session, model);
                    }
                    publish(new Long[] { entity.getId() }, _csrf, request, session, model);
                    categoryIdSet.add(entity.getCategoryId());
                }
            }
            for (CmsCategory category : categoryService.getEntitys(categoryIdSet.toArray(new Integer[categoryIdSet.size()]))) {
                templateComponent.createCategoryFile(site, category, null, null);
            }
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "check.content", RequestUtils.getIpAddress(request), CommonUtils.getDate(), StringUtils.join(ids, ',')));
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
    @RequestMapping("uncheck")
    public String uncheck(Long[] ids, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            SysUser user = ControllerUtils.getAdminFromSession(session);
            List<CmsContent> entityList = service.uncheck(site.getId(), user, ids);
            Set<Integer> categoryIdSet = new HashSet<>();
            for (CmsContent entity : entityList) {
                if (null != entity && site.getId() == entity.getSiteId()) {
                    if (CommonUtils.notEmpty(entity.getParentId())) {
                        publish(new Long[] { entity.getParentId() }, _csrf, request, session, model);
                    }
                    publish(new Long[] { entity.getId() }, _csrf, request, session, model);
                    categoryIdSet.add(entity.getCategoryId());
                }
            }
            for (CmsCategory category : categoryService.getEntitys(categoryIdSet.toArray(new Integer[categoryIdSet.size()]))) {
                templateComponent.createCategoryFile(site, category, null, null);
            }
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "uncheck.content", RequestUtils.getIpAddress(request), CommonUtils.getDate(), StringUtils.join(ids, ',')));
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
            SysSite site = getSite(request);
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
        SysSite site = getSite(request);
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
            publish(new Long[] { entity.getContentId() }, _csrf, request, session, model);
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
        SysSite site = getSite(request);
        SysUser user = ControllerUtils.getAdminFromSession(session);
        if (null != entity) {
            if (null == entity || ControllerUtils.verifyCustom("noright",
                    !(user.isOwnsAllContent() || entity.getUserId() == user.getId()), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            CmsContent content = service.getEntity(entity.getContentId());
            if (null == content || site.getId() == content.getSiteId()) {
                cmsContentRelatedService.delete(id);
                publish(new Long[] { entity.getContentId() }, _csrf, request, session, model);
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
        SysSite site = getSite(request);
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
        SysSite site = getSite(request);
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
        SysSite site = getSite(request);
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
        SysSite site = getSite(request);
        SysUser user = ControllerUtils.getAdminFromSession(session);
        if (CommonUtils.notEmpty(ids)) {
            for (CmsContent entity : service.getEntitys(ids)) {
                CmsCategoryModel categoryModel = categoryModelService
                        .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
                if (null != categoryModel && (user.isOwnsAllContent() || entity.getUserId() == user.getId())
                        && !entity.isOnlyUrl() && !templateComponent.createContentFile(site, entity, null, categoryModel)) {
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
        SysSite site = getSite(request);
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
        SysSite site = getSite(request);
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
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(ids)) {
            service.realDelete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "realDelete.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

}