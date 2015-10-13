package com.publiccms.admin.views.controller.cms;

import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.join;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsModel;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.system.SystemUser;
import com.publiccms.logic.component.ExtendComponent;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.FileComponent.StaticResult;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsContentTagService;
import com.publiccms.logic.service.cms.CmsModelService;
import com.publiccms.logic.service.cms.CmsTagService;
import com.publiccms.logic.service.log.LogOperateService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;

@Controller
@RequestMapping("cmsContent")
public class CmsContentController extends BaseController {
    @Autowired
    private CmsContentService service;
    @Autowired
    private CmsTagService tagService;
    @Autowired
    private CmsContentTagService contentTagService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private CmsModelService modelService;
    @Autowired
    private CmsContentAttributeService attributeService;
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private ExtendComponent extendComponent;
    @Autowired
    private LogOperateService logOperateService;

    @RequestMapping(SAVE)
    public String save(CmsContent entity, Integer[] tagIds, String[] tagTitles, String txt, Boolean timing, Boolean draft,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(draft) && draft) {
            entity.setStatus(CmsContentService.STATUS_DRAFT);
        } else {
            entity.setStatus(CmsContentService.STATUS_PEND);
        }
        Date now = getDate();
        if (null == entity.getPublishDate()) {
            entity.setPublishDate(now);
        }
        if (notEmpty(timing) && timing && entity.getPublishDate().before(now)) {
            entity.setPublishDate(now);
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        CmsModel cmsModel = modelService.getEntity(entity.getModelId());
        Integer[] newTagIds = null;
        if (notEmpty(entity.getId())) {
            String url = entity.getUrl();
            entity = service.update(entity.getId(), entity, new String[] { "userId", "url", "tags", "createDate", "clicks",
                    "comments", "scores", "childs" });
            if (cmsModel.isIsUrl()) {
                entity = service.updateUrl(entity.getId(), url);
            }
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "update.content",
                        RequestUtils.getIp(request), getDate(), entity.getId() + ":" + entity.getTitle()));
            }
            contentTagService.delete(null, entity.getId());
            newTagIds = tagService.saveTags(entity.getId(), tagTitles);
        } else {
            SystemUser user = UserUtils.getAdminFromSession(session);
            entity.setUserId(user.getId());
            entity.setDeptId(user.getDeptId());
            entity = service.save(entity);
            if (notEmpty(entity.getParentId())) {
                service.updateChilds(entity.getParentId(), 1);
            } else {
                categoryService.updateContents(entity.getCategoryId(), 1);
            }
            logOperateService.save(new LogOperate(user.getId(), "save.content", RequestUtils.getIp(request), getDate(), entity
                    .getId() + ":" + entity.getTitle()));
            newTagIds = tagService.saveTags(entity.getId(), tagTitles);
        }
        service.updateTags(entity.getId(), arrayToCommaDelimitedString(addAll(tagIds, newTagIds)));
        if (cmsModel.isIsPart()) {
            txt = extendComponent.contentExtent(attributeService.getEntity(entity.getId()), parameterMap);
        } else if (cmsModel.isIsImages()) {
            txt = extendComponent.contentImages(attributeService.getEntity(entity.getId()), parameterMap);
        }
        String data = extendComponent.dealExtent(ExtendComponent.EXTEND_TYPE_CONTENT, entity.getCategoryId(),
                entity.getModelId(), parameterMap);
        attributeService.dealAttribute(entity.getId(), txt, data);
        publish(new Integer[] { entity.getId() }, request, session, model);
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = { "static" })
    public String publish(Integer[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(ids)) {
            for (Integer id : ids) {
                CmsContent entity = service.getEntity(id);
                if (notEmpty(entity)) {
                    CmsCategoryModel categoryModel = categoryModelService.getEntity(entity.getModelId(), entity.getCategoryId());
                    CmsModel cmsModel = modelService.getEntity(entity.getModelId());
                    CmsCategory category = categoryService.getEntity(entity.getCategoryId());
                    if (notEmpty(cmsModel) && notEmpty(categoryModel) && notEmpty(category) && !cmsModel.isIsUrl()) {
                        StaticResult result = fileComponent.createContentHtml(entity, category, cmsModel,
                                categoryModel.getTemplatePath());
                        if (virifyCustom("static", !result.getResult(), model)) {
                            return TEMPLATE_ERROR;
                        } else {
                            entity = service.updateUrl(id, result.getFilePath());
                        }
                    }
                }
            }
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "static", RequestUtils
                    .getIp(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = { "check" })
    public String check(Integer[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(ids)) {
            for (Integer id : ids) {
                CmsContent entity = service.check(id);
                if (notEmpty(entity)) {
                    contentTagService.updateContentTags(id, entity.getTags());
                    if (notEmpty(entity.getParentId())) {
                        publish(new Integer[] { entity.getParentId() }, request, session, model);
                    }
                    CmsCategory category = categoryService.getEntity(entity.getCategoryId());
                    if (isNotBlank(category.getPath())) {
                        fileComponent.createCategoryHtml(category, category.getTemplatePath(), category.getPath());
                    }
                    logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "check.content",
                            RequestUtils.getIp(request), getDate(), join(ids, ',')));
                }
            }
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(DELETE)
    public String delete(Integer[] ids, HttpServletRequest request, HttpSession session) {
        if (notEmpty(ids)) {
            for (Integer id : ids) {
                CmsContent entity = service.delete(id);
                contentTagService.delete(null, id);
                if (notEmpty(entity.getParentId())) {
                    service.updateChilds(entity.getParentId(), -1);
                } else {
                    categoryService.updateContents(entity.getCategoryId(), -1);
                }
            }
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "delete.content", RequestUtils
                    .getIp(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }
}