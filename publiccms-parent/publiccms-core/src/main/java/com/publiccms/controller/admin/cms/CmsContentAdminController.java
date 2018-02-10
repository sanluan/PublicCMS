package com.publiccms.controller.admin.cms;

import static org.springframework.util.StringUtils.arrayToDelimitedString;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
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
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentFileService;
import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsTagService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDeptCategoryService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.entities.ExtendField;
import com.publiccms.views.pojo.model.CmsContentParamters;

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
    private CmsTagService tagService;
    @Autowired
    private CmsContentFileService contentFileService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private ModelComponent modelComponent;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private CmsContentAttributeService attributeService;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private SysExtendService extendService;
    @Autowired
    private SysExtendFieldService extendFieldService;

    private String[] ignoreProperties = new String[] { "siteId", "userId", "categoryId", "tagIds", "sort", "createDate", "clicks",
            "comments", "scores", "childs", "checkUserId" };

    private String[] ignorePropertiesWithUrl = ArrayUtils.addAll(ignoreProperties, new String[] { "url" });

    /**
     * 保存内容
     *
     * @param entity
     * @param attribute
     * @param contentParamters
     * @param draft
     * @param checked
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    public String save(CmsContent entity, CmsContentAttribute attribute, @ModelAttribute CmsContentParamters contentParamters,
            Boolean draft, Boolean checked, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysUser user = getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                && ControllerUtils.verifyNotEmpty("deptId", dept, model)
                && ControllerUtils.verifyCustom("noright", !(dept.isOwnsAllCategory() || null != sysDeptCategoryService
                        .getEntity(new SysDeptCategoryId(user.getDeptId(), entity.getCategoryId()))), model)) {
            return TEMPLATE_ERROR;
        }
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (ControllerUtils.verifyNotEmpty("categoryModel", categoryModel, model)) {
            return TEMPLATE_ERROR;
        }
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (null != category && site.getId() != category.getSiteId()) {
            category = null;
        }
        CmsModel cmsModel = modelComponent.getMap(site).get(entity.getModelId());

        if (ControllerUtils.verifyNotEmpty("category", category, model)
                || ControllerUtils.verifyNotEmpty("model", cmsModel, model)) {
            return TEMPLATE_ERROR;
        }
        entity.setHasFiles(cmsModel.isHasFiles());
        entity.setHasImages(cmsModel.isHasImages());
        entity.setOnlyUrl(cmsModel.isOnlyUrl());
        if ((null == checked || !checked) && null != draft && draft) {
            entity.setStatus(CmsContentService.STATUS_DRAFT);
        } else {
            entity.setStatus(CmsContentService.STATUS_PEND);
        }
        Date now = CommonUtils.getDate();
        if (null == entity.getPublishDate()) {
            entity.setPublishDate(now);
        }

		if (null != attribute.getText()) {
			String text = HtmlUtils.removeHtmlTag(attribute.getText());
            attribute.setWordCount(text.length());
			if(CommonUtils.empty(entity.getDescription())){
				entity.setDescription(StringUtils.substring(text,0,300));
			}
        }
        if (null != entity.getId()) {
            CmsContent oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
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
        Long[] tagIds = tagService.update(site.getId(), contentParamters.getTags());
        service.updateTagIds(entity.getId(), arrayToDelimitedString(tagIds, BLANK_SPACE));// 更新保存标签
        if (entity.isHasImages() || entity.isHasFiles()) {
            contentFileService.update(entity.getId(), user.getId(), entity.isHasFiles() ? contentParamters.getFiles() : null,
                    entity.isHasImages() ? contentParamters.getImages() : null);// 更新保存图集，附件
        }

        List<ExtendField> modelExtendList = cmsModel.getExtendList();
        Map<String, String> map = ExtendUtils.getExtentDataMap(contentParamters.getModelExtendDataList(), modelExtendList);
        if (null != category && null != extendService.getEntity(category.getExtendId())) {
            List<SysExtendField> categoryExtendList = extendFieldService.getList(category.getExtendId());
            Map<String, String> categoryMap = ExtendUtils.getSysExtentDataMap(contentParamters.getCategoryExtendDataList(),
                    categoryExtendList);
            if (CommonUtils.notEmpty(map)) {
                map.putAll(categoryMap);
            } else {
                map = categoryMap;
            }
        }

        if (CommonUtils.notEmpty(map)) {
            attribute.setData(ExtendUtils.getExtendString(map));
        } else {
            attribute.setData(null);
        }

        attributeService.updateAttribute(entity.getId(), attribute);// 更新保存扩展字段，文本字段

        cmsContentRelatedService.update(entity.getId(), user.getId(), contentParamters.getContentRelateds());// 更新保存推荐内容
        templateComponent.createContentFile(site, entity, category, categoryModel);// 静态化
        if (null != checked && checked) {
            service.check(site.getId(), user.getId(), new Long[] { entity.getId() }, false);
            if (CommonUtils.notEmpty(entity.getParentId())) {
                publish(new Long[] { entity.getParentId() }, request, session, model);
            }
            templateComponent.createCategoryFile(site, category, null, null);
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param refresh
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("check")
    public String check(Long[] ids, Boolean refresh, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            Long userId = getAdminFromSession(session).getId();
            List<CmsContent> entityList = service.check(site.getId(), userId, ids, refresh);
            Set<Integer> categoryIdSet = new HashSet<>();
            for (CmsContent entity : entityList) {
                if (null != entity && site.getId() == entity.getSiteId()) {
                    if (CommonUtils.notEmpty(entity.getParentId())) {
                        publish(new Long[] { entity.getParentId() }, request, session, model);
                    }
                    publish(new Long[] { entity.getId() }, request, session, model);
                    categoryIdSet.add(entity.getCategoryId());
                }
            }
            for (CmsCategory category : categoryService.getEntitys(categoryIdSet.toArray(new Integer[categoryIdSet.size()]))) {
                templateComponent.createCategoryFile(site, category, null, null);
            }
            logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "check.content",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("uncheck")
    public String uncheck(Long[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            Long userId = getAdminFromSession(session).getId();
            List<CmsContent> entityList = service.uncheck(site.getId(), userId, ids);
            Set<Integer> categoryIdSet = new HashSet<>();
            for (CmsContent entity : entityList) {
                if (null != entity && site.getId() == entity.getSiteId()) {
                    if (CommonUtils.notEmpty(entity.getParentId())) {
                        publish(new Long[] { entity.getParentId() }, request, session, model);
                    }
                    publish(new Long[] { entity.getId() }, request, session, model);
                    categoryIdSet.add(entity.getCategoryId());
                }
            }
            for (CmsCategory category : categoryService.getEntitys(categoryIdSet.toArray(new Integer[categoryIdSet.size()]))) {
                templateComponent.createCategoryFile(site, category, null, null);
            }
            logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "uncheck.content",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("refresh")
    public String refresh(Long[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            service.refresh(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "refresh.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param entity
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("related")
    public String related(CmsContentRelated entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        CmsContent content = service.getEntity(entity.getContentId());
        CmsContent related = service.getEntity(entity.getRelatedContentId());
        SysSite site = getSite(request);
        if (null != content && null != related && site.getId() == content.getSiteId() && site.getId() == related.getSiteId()) {
            if (CommonUtils.empty(entity.getTitle())) {
                entity.setTitle(entity.getTitle());
            }
            if (CommonUtils.empty(entity.getDescription())) {
                entity.setDescription(entity.getDescription());
            }
            SysUser user = getAdminFromSession(session);
            entity.setUserId(user.getId());
            cmsContentRelatedService.save(entity);
            publish(new Long[] { entity.getContentId() }, request, session, model);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "related.content", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(related)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param categoryId
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("move")
    public String move(Long[] ids, Integer categoryId, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        CmsCategory category = categoryService.getEntity(categoryId);
        if (CommonUtils.notEmpty(ids) && null != category && site.getId() == category.getSiteId()) {
            StringBuilder sb = new StringBuilder();
            for (CmsContent entity : service.getEntitys(ids)) {
                if (!move(site, entity, categoryId)) {
                    sb.append(LanguagesUtils.getMessage(CommonConstants.applicationContext,
                            RequestContextUtils.getLocale(request), "message.content.categoryModel.empty",
                            new StringBuilder().append(entity.getId()).append(":").append(entity.getTitle()).toString(),
                            new StringBuilder().append(categoryId).append(":").append(category.getName()).toString()))
                            .append(COMMA_DELIMITED);
                }
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            model.addAttribute("message", sb.toString());
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "move.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder(StringUtils.join(ids, ',')).append(" to ").append(category.getId())
                            .append(":").append(category.getName()).toString()));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param siteId
     * @param entity
     * @param categoryId
     */
    private boolean move(SysSite site, CmsContent entity, Integer categoryId) {
        if (null != entity && site.getId() == entity.getSiteId()) {
            CmsCategoryModel categoryModel = categoryModelService
                    .getEntity(new CmsCategoryModelId(categoryId, entity.getModelId()));
            if (null != categoryModel) {
                if (CommonUtils.notEmpty(entity.getParentId())) {
                    service.updateChilds(entity.getParentId(), -1);
                }
                service.updateCategoryId(entity.getSiteId(), entity.getId(), categoryId);
                templateComponent.createContentFile(site, entity, null, categoryModel);
                return true;
            }
        }
        return false;
    }

    /**
     * @param id
     * @param sort
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("sort")
    public String sort(Long id, int sort, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(id)) {
            CmsContent entity = service.sort(site.getId(), id, sort);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "sort.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder().append(entity.getId()).append(":").append(entity.getTitle())
                            .append(" to ").append(sort).toString()));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("publish")
    public String publish(Long[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(ids)) {
            for (CmsContent entity : service.getEntitys(ids)) {
                CmsCategoryModel categoryModel = categoryModelService
                        .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
                if (null != categoryModel && !entity.isOnlyUrl()
                        && !templateComponent.createContentFile(site, entity, null, categoryModel)) {
                    ControllerUtils.verifyCustom("static", true, model);
                    return TEMPLATE_ERROR;
                }
            }
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "static.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(Long[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(ids)) {
            for (CmsContent entity : service.getEntitys(ids)) {
                if (!entity.isDisabled() && site.getId() == entity.getSiteId()) {
                    if (CommonUtils.notEmpty(entity.getParentId())) {
                        service.updateChilds(entity.getParentId(), -1);
                    }
                } else {
                    ArrayUtils.removeElements(ids, entity.getId());
                }
            }
            service.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return view name
     */
    @RequestMapping("recycle")
    public String recycle(Long[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(ids)) {
            for (CmsContent entity : service.getEntitys(ids)) {
                if (entity.isDisabled() && site.getId() == entity.getSiteId()) {
                    if (CommonUtils.notEmpty(entity.getParentId())) {
                        service.updateChilds(entity.getParentId(), 1);
                    }
                }
            }
            service.recycle(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "recycle.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return view name
     */
    @RequestMapping("realDelete")
    public String realDelete(Long[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(ids)) {
            service.realDelete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "realDelete.content", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

}