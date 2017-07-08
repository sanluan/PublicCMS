package org.publiccms.controller.admin.cms;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyCustom;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.HtmlUtils.removeHtmlTag;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.LanguagesUtils.getMessage;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.ArrayUtils.removeElements;
import static org.apache.commons.lang3.StringUtils.join;
import static org.publiccms.common.constants.CommonConstants.webApplicationContext;
import static org.publiccms.common.tools.ExtendUtils.getExtendString;
import static org.publiccms.common.tools.ExtendUtils.getExtentDataMap;
import static org.publiccms.common.tools.ExtendUtils.getSysExtentDataMap;
import static org.springframework.util.StringUtils.arrayToDelimitedString;
import static org.springframework.web.servlet.support.RequestContextUtils.getLocale;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.cms.CmsCategory;
import org.publiccms.entities.cms.CmsCategoryModel;
import org.publiccms.entities.cms.CmsCategoryModelId;
import org.publiccms.entities.cms.CmsContent;
import org.publiccms.entities.cms.CmsContentAttribute;
import org.publiccms.entities.cms.CmsContentRelated;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysDept;
import org.publiccms.entities.sys.SysDeptCategoryId;
import org.publiccms.entities.sys.SysExtendField;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.component.template.ModelComponent;
import org.publiccms.logic.component.template.TemplateComponent;
import org.publiccms.logic.service.cms.CmsCategoryModelService;
import org.publiccms.logic.service.cms.CmsCategoryService;
import org.publiccms.logic.service.cms.CmsContentAttributeService;
import org.publiccms.logic.service.cms.CmsContentFileService;
import org.publiccms.logic.service.cms.CmsContentRelatedService;
import org.publiccms.logic.service.cms.CmsContentService;
import org.publiccms.logic.service.cms.CmsTagService;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysDeptCategoryService;
import org.publiccms.logic.service.sys.SysDeptService;
import org.publiccms.logic.service.sys.SysExtendFieldService;
import org.publiccms.logic.service.sys.SysExtendService;
import org.publiccms.views.pojo.CmsContentParamters;
import org.publiccms.views.pojo.CmsModel;
import org.publiccms.views.pojo.ExtendField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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

    private String[] ignorePropertiesWithUrl = addAll(ignoreProperties, new String[] { "url" });

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
     * @return
     */
    @RequestMapping("save")
    public String save(CmsContent entity, CmsContentAttribute attribute, @ModelAttribute CmsContentParamters contentParamters,
            Boolean draft, Boolean checked, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysUser user = getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        if (verifyNotEmpty(
                "deptId", user
                        .getDeptId(),
                model) && verifyNotEmpty("deptId", dept, model)
                && verifyCustom("noright", !(dept.isOwnsAllCategory() || null != sysDeptCategoryService
                        .getEntity(new SysDeptCategoryId(user.getDeptId(), entity.getCategoryId()))), model)) {
            return TEMPLATE_ERROR;
        }
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (verifyNotEmpty("categoryModel", categoryModel, model)) {
            return TEMPLATE_ERROR;
        }
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (null != category && site.getId() != category.getSiteId()) {
            category = null;
        }
        CmsModel cmsModel = modelComponent.getMap(site).get(entity.getModelId());

        if (verifyNotEmpty("category", category, model) || verifyNotEmpty("model", cmsModel, model)) {
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
        Date now = getDate();
        if (empty(entity.getPublishDate())) {
            entity.setPublishDate(now);
        }
        if (null != entity.getId()) {
            CmsContent oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, entity.isOnlyUrl() ? ignoreProperties : ignorePropertiesWithUrl);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.content", getIpAddress(request), now, getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            entity.setUserId(user.getId());
            service.save(entity);
            if (notEmpty(entity.getParentId())) {
                service.updateChilds(entity.getParentId(), 1);
            } else {
                categoryService.updateContents(entity.getCategoryId(), 1);
            }
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.content",
                    getIpAddress(request), now, getString(entity)));
        }
        Long[] tagIds = tagService.update(site.getId(), contentParamters.getTags());
        service.updateTagIds(entity.getId(), arrayToDelimitedString(tagIds, BLANK_SPACE));// 更新保存标签
        if (entity.isHasImages() || entity.isHasFiles()) {
            contentFileService.update(entity.getId(), user.getId(), entity.isHasFiles() ? contentParamters.getFiles() : null,
                    entity.isHasImages() ? contentParamters.getImages() : null);// 更新保存图集，附件
        }
        if (null != attribute.getText()) {
            attribute.setWordCount(removeHtmlTag(attribute.getText()).length());
        }

        List<ExtendField> modelExtendList = cmsModel.getExtendList();
        Map<String, String> map = getExtentDataMap(contentParamters.getModelExtendDataList(), modelExtendList);
        if (null != category && null != extendService.getEntity(category.getExtendId())) {
            List<SysExtendField> categoryExtendList = extendFieldService.getList(category.getExtendId());
            Map<String, String> categoryMap = getSysExtentDataMap(contentParamters.getCategoryExtendDataList(),
                    categoryExtendList);
            if (notEmpty(map)) {
                map.putAll(categoryMap);
            } else {
                map = categoryMap;
            }
        }

        if (notEmpty(map)) {
            attribute.setData(getExtendString(map));
        } else {
            attribute.setData(null);
        }

        attributeService.updateAttribute(entity.getId(), attribute);// 更新保存扩展字段，文本字段

        cmsContentRelatedService.update(entity.getId(), user.getId(), contentParamters.getContentRelateds());// 更新保存推荐内容
        templateComponent.createContentFile(site, entity, category, categoryModel);// 静态化
        if (null != checked && checked) {
            service.check(site.getId(), user.getId(), new Long[] { entity.getId() });
            if (notEmpty(entity.getParentId())) {
                publish(new Long[] { entity.getParentId() }, request, session, model);
            }
            templateComponent.createCategoryFile(site, category, null, null);
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("check")
    public String check(Long[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(ids)) {
            SysSite site = getSite(request);
            Long userId = getAdminFromSession(session).getId();
            List<CmsContent> entityList = service.check(site.getId(), userId, ids);
            Set<Integer> categoryIdSet = new HashSet<Integer>();
            for (CmsContent entity : entityList) {
                if (null != entity && site.getId() == entity.getSiteId()) {
                    if (notEmpty(entity.getParentId())) {
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
                    getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("refresh")
    public String refresh(Long[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(ids)) {
            SysSite site = getSite(request);
            service.refresh(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "refresh.content", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param entity
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("related")
    public String related(CmsContentRelated entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        CmsContent content = service.getEntity(entity.getContentId());
        CmsContent related = service.getEntity(entity.getRelatedContentId());
        SysSite site = getSite(request);
        if (null != content && null != related && site.getId() == content.getSiteId() && site.getId() == related.getSiteId()) {
            if (empty(entity.getTitle())) {
                entity.setTitle(entity.getTitle());
            }
            if (empty(entity.getDescription())) {
                entity.setDescription(entity.getDescription());
            }
            SysUser user = getAdminFromSession(session);
            entity.setUserId(user.getId());
            cmsContentRelatedService.save(entity);
            publish(new Long[] { entity.getContentId() }, request, session, model);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "related.content", getIpAddress(request), getDate(), getString(related)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param categoryId
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("move")
    public String move(Long[] ids, Integer categoryId, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        CmsCategory category = categoryService.getEntity(categoryId);
        if (notEmpty(ids) && null != category && site.getId() == category.getSiteId()) {
            StringBuilder sb = new StringBuilder();
            for (CmsContent entity : service.getEntitys(ids)) {
                if (!move(site, entity, categoryId)) {
                    sb.append(getMessage(webApplicationContext, getLocale(request), "message.content.categoryModel.empty",
                            new StringBuilder().append(entity.getId()).append(":").append(entity.getTitle()).toString(),
                            new StringBuilder().append(categoryId).append(":").append(category.getName()).toString()))
                            .append(COMMA_DELIMITED);
                }
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            model.put("message", sb.toString());
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "move.content", getIpAddress(request), getDate(), new StringBuilder(join(ids, ',')).append(" to ")
                                    .append(category.getId()).append(":").append(category.getName()).toString()));
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
                    .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
            if (null != categoryModel) {
                categoryService.updateContents(categoryId, 1);
                if (notEmpty(entity.getParentId())) {
                    service.updateChilds(entity.getParentId(), -1);
                } else {
                    categoryService.updateContents(entity.getCategoryId(), -1);
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
     * @return
     */
    @RequestMapping("sort")
    public String sort(Long id, int sort, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (notEmpty(id)) {
            CmsContent entity = service.sort(site.getId(), id, sort);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "sort.content", getIpAddress(request), getDate(), new StringBuilder().append(entity.getId())
                                    .append(":").append(entity.getTitle()).append(" to ").append(sort).toString()));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("publish")
    public String publish(Long[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (notEmpty(ids)) {
            for (CmsContent entity : service.getEntitys(ids)) {
                CmsCategoryModel categoryModel = categoryModelService
                        .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
                if (null != categoryModel && !entity.isOnlyUrl()
                        && !templateComponent.createContentFile(site, entity, null, categoryModel)) {
                    verifyCustom("static", true, model);
                    return TEMPLATE_ERROR;
                }
            }
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "static.content", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("delete")
    public String delete(Long[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(ids)) {
            for (CmsContent entity : service.getEntitys(ids)) {
                if (!entity.isDisabled() && site.getId() == entity.getSiteId()) {
                    if (notEmpty(entity.getParentId())) {
                        service.updateChilds(entity.getParentId(), -1);
                    } else {
                        categoryService.updateContents(entity.getCategoryId(), -1);
                    }
                } else {
                    removeElements(ids, entity.getId());
                }
            }
            service.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.content", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("recycle")
    public String recycle(Long[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(ids)) {
            for (CmsContent entity : service.getEntitys(ids)) {
                if (entity.isDisabled() && site.getId() == entity.getSiteId()) {
                    if (notEmpty(entity.getParentId())) {
                        service.updateChilds(entity.getParentId(), 1);
                    } else {
                        categoryService.updateContents(entity.getCategoryId(), 1);
                    }
                }
            }
            service.recycle(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "recycle.content", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("realDelete")
    public String realDelete(Long[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(ids)) {
            service.realDelete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "realDelete.content", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

}