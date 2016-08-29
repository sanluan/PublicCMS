package com.publiccms.views.controller.admin.cms;

import static com.publiccms.common.tools.ExtendUtils.getExtendString;
import static com.publiccms.common.tools.ExtendUtils.getSysExtentDataMap;
import static com.sanluan.common.tools.LanguagesUtils.getMessage;
import static com.sanluan.common.tools.HTMLUtils.removeHtmlTag;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.ArrayUtils.removeElements;
import static org.apache.commons.lang3.StringUtils.join;
import static org.springframework.util.StringUtils.arrayToDelimitedString;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.servlet.support.RequestContextUtils.getLocale;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsContentRelated;
import com.publiccms.entities.cms.CmsModel;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentFileService;
import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsContentTagService;
import com.publiccms.logic.service.cms.CmsModelService;
import com.publiccms.logic.service.cms.CmsTagService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDeptCategoryService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.CmsContentParamters;

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
    private CmsContentTagService contentTagService;
    @Autowired
    private CmsContentFileService contentFileService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private CmsModelService modelService;
    @Autowired
    private CmsContentAttributeService attributeService;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private SysExtendService extendService;
    @Autowired
    private SysExtendFieldService extendFieldService;

    /**
     * @param entity
     * @param attribute
     * @param contentParamters
     * @param txt
     * @param timing
     * @param draft
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(CmsContent entity, CmsContentAttribute attribute, @ModelAttribute CmsContentParamters contentParamters,
            Boolean timing, Boolean draft, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysUser user = getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        if (verifyNotEmpty("deptId", user.getDeptId(), model) && verifyNotEmpty("deptId", dept, model)
                && verifyCustom("noright",
                        !(dept.isOwnsAllCategory()
                                || notEmpty(sysDeptCategoryService.getEntity(user.getDeptId(), entity.getCategoryId()))),
                        model)) {
            return TEMPLATE_ERROR;
        }
        CmsCategoryModel categoryModel = categoryModelService.getEntity(entity.getModelId(), entity.getCategoryId());
        if (verifyNotEmpty("categoryModel", categoryModel, model)) {
            return TEMPLATE_ERROR;
        }
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (notEmpty(category) && site.getId() != category.getSiteId()) {
            category = null;
        }
        CmsModel cmsModel = modelService.getEntity(entity.getModelId());
        if (notEmpty(cmsModel) && site.getId() != cmsModel.getSiteId()) {
            cmsModel = null;
        }

        if (verifyNotEmpty("category", category, model) || verifyNotEmpty("model", cmsModel, model)) {
            return TEMPLATE_ERROR;
        }
        entity.setHasFiles(cmsModel.isHasFiles());
        entity.setHasImages(cmsModel.isHasImages());
        if (notEmpty(draft) && draft) {
            entity.setStatus(CmsContentService.STATUS_DRAFT);
        } else {
            entity.setStatus(CmsContentService.STATUS_PEND);
        }
        Date now = getDate();
        if (empty(entity.getPublishDate())) {
            entity.setPublishDate(now);
        } else if (notEmpty(timing) && timing && now.after(entity.getPublishDate())) {
            entity.setPublishDate(now);
        }
        if (notEmpty(entity.getId())) {
            CmsContent oldEntity = service.getEntity(entity.getId());
            if (empty(oldEntity) || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            String[] ignoreProperties = new String[] { "siteId", "userId", "categoryId", "tagIds", "createDate", "clicks",
                    "comments", "scores", "childs", "checkUserId" };
            if (!entity.isOnlyUrl()) {
                ignoreProperties = addAll(ignoreProperties, new String[] { "url" });
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.content", getIpAddress(request), now, entity.getId() + ":" + entity.getTitle()));
            }
            contentTagService.delete(null, new Long[] { entity.getId() });
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
                    getIpAddress(request), now, entity.getId() + ":" + entity.getTitle()));
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

        Map<String, String> map = null;
        if (notEmpty(extendService.getEntity(cmsModel.getExtendId()))) {
            @SuppressWarnings("unchecked")
            List<SysExtendField> modelExtendList = (List<SysExtendField>) extendFieldService
                    .getPage(cmsModel.getExtendId(), null, null).getList();
            map = getSysExtentDataMap(contentParamters.getModelExtendDataList(), modelExtendList);
        }
        if (notEmpty(extendService.getEntity(category.getExtendId()))) {
            @SuppressWarnings("unchecked")
            List<SysExtendField> categoryExtendList = (List<SysExtendField>) extendFieldService
                    .getPage(category.getExtendId(), null, null).getList();
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
                if (notEmpty(entity) && site.getId() == entity.getSiteId()) {
                    contentTagService.update(entity.getId(), entity.getTagIds());
                    if (notEmpty(entity.getParentId())) {
                        publish(new Long[] { entity.getParentId() }, request, session, model);
                    }
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
        if (notEmpty(content) && notEmpty(related) && site.getId() == content.getSiteId()
                && site.getId() == related.getSiteId()) {
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
                    "related.content", getIpAddress(request), getDate(),
                    related.getId() + ":" + related.getTitle() + " to " + content.getId() + ":" + content.getTitle()));
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
        if (notEmpty(ids) && notEmpty(category) && site.getId() == category.getSiteId()) {
            StringBuffer sb = new StringBuffer();
            for (CmsContent entity : service.getEntitys(ids)) {
                if (!move(site, entity, categoryId)) {
                    if (sb.length() > 0) {
                        sb.append(COMMA_DELIMITED);
                    }
                    sb.append(getMessage(getLocale(request), "message.content.categoryModel.empty",
                            entity.getId() + ":" + entity.getTitle(), categoryId + ":" + category.getName()));
                }
            }
            model.put("message", sb.toString());
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "move.content", getIpAddress(request), getDate(),
                    join(ids, ',') + " to " + category.getId() + ":" + category.getName()));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param siteId
     * @param entity
     * @param categoryId
     */
    private boolean move(SysSite site, CmsContent entity, Integer categoryId) {
        if (notEmpty(entity) && site.getId() == entity.getSiteId()) {
            CmsCategoryModel categoryModel = categoryModelService.getEntity(entity.getModelId(), entity.getCategoryId());
            if (notEmpty(categoryModel)) {
                service.updateCategoryId(entity.getSiteId(), entity.getId(), categoryId);
                templateComponent.createContentFile(site, entity, null, categoryModel);
                return true;
            }
        }
        return false;
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
                CmsCategoryModel categoryModel = categoryModelService.getEntity(entity.getModelId(), entity.getCategoryId());
                if (verifyNotEmpty("categoryModel", categoryModel, model)) {
                    return TEMPLATE_ERROR;
                } else if (!templateComponent.createContentFile(site, entity, null, categoryModel)) {
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
            contentTagService.delete(null, ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.content", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

}