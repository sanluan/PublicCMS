package com.publiccms.controller.admin.cms;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryType;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysExtend;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsCategoryTypeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsTagTypeService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.model.CmsCategoryModelParamters;
import com.publiccms.views.pojo.model.CmsCategoryParamters;

import freemarker.template.TemplateException;

/**
 * 
 * CmsCategoryController
 *
 */
@Controller
@RequestMapping("cmsCategory")
public class CmsCategoryAdminController extends AbstractController {
    @Autowired
    private CmsCategoryService service;
    @Autowired
    private CmsTagTypeService tagTypeService;
    @Autowired
    private CmsContentService contentService;
    @Autowired
    private CmsCategoryAttributeService attributeService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private CmsCategoryTypeService categoryTypeService;
    @Autowired
    private SysExtendService extendService;
    @Autowired
    private SysExtendFieldService extendFieldService;
    @Autowired
    private TemplateComponent templateComponent;

    private String[] ignoreProperties = new String[] { "siteId", "childIds", "tagTypeIds", "url", "disabled", "extendId",
            "contents", "typeId" };

    /**
     * @param entity
     * @param attribute
     * @param categoryParamters
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    public String save(CmsCategory entity, CmsCategoryAttribute attribute, @ModelAttribute CmsCategoryParamters categoryParamters,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            CmsCategory oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                if (null != oldEntity.getParentId() && oldEntity.getParentId() != entity.getParentId()) {
                    service.generateChildIds(site.getId(), oldEntity.getParentId());
                    service.generateChildIds(site.getId(), entity.getParentId());
                } else if (null != entity.getParentId() && null == oldEntity.getParentId()) {
                    service.generateChildIds(site.getId(), entity.getParentId());
                }
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.category", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            if (entity.isOnlyUrl()) {
                entity.setUrl(entity.getPath());
            }
            entity.setSiteId(site.getId());
            service.save(entity);
            service.addChildIds(entity.getParentId(), entity.getId());
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.category", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        if (null == extendService.getEntity(entity.getExtendId())) {
            entity = service.updateExtendId(entity.getId(),
                    (Integer) extendService.save(new SysExtend("category", entity.getId())));
        }

        Integer[] tagTypeIds = tagTypeService.update(site.getId(), categoryParamters.getTagTypes());
        service.updateTagTypeIds(entity.getId(), arrayToCommaDelimitedString(tagTypeIds));// 更新保存标签分类

        List<CmsCategoryModelParamters> categoryModelList = categoryParamters.getCategoryModelList();
        if (CommonUtils.notEmpty(categoryModelList)) {
            for (CmsCategoryModelParamters cmsCategoryModelParamters : categoryModelList) {
                if (null != cmsCategoryModelParamters.getCategoryModel()) {
                    cmsCategoryModelParamters.getCategoryModel().getId().setCategoryId(entity.getId());
                    if (cmsCategoryModelParamters.isUse()) {
                        categoryModelService.updateCategoryModel(cmsCategoryModelParamters.getCategoryModel());
                    } else {
                        categoryModelService.delete(cmsCategoryModelParamters.getCategoryModel().getId());
                    }
                }
            }
        }
        extendFieldService.update(entity.getExtendId(), categoryParamters.getContentExtends());// 修改或增加内容扩展字段

        CmsCategoryType categoryType = categoryTypeService.getEntity(entity.getTypeId());
        if (null != categoryType && CommonUtils.notEmpty(categoryType.getExtendId())) {
            List<SysExtendField> categoryTypeExtendList = extendFieldService.getList(categoryType.getExtendId());
            Map<String, String> map = ExtendUtils.getSysExtentDataMap(categoryParamters.getExtendDataList(),
                    categoryTypeExtendList);
            attribute.setData(ExtendUtils.getExtendString(map));
        } else {
            attribute.setData(null);
        }
        attributeService.updateAttribute(entity.getId(), attribute);
        try {
            publish(site, entity.getId(), null);
        } catch (IOException | TemplateException e) {
            ControllerUtils.verifyCustom("static", true, model);
            log.error(e.getMessage(), e);
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param parentId
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("move")
    public String move(Integer[] ids, Integer parentId, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        CmsCategory parent = service.getEntity(parentId);
        if (CommonUtils.notEmpty(ids) && (null == parent || null != parent && site.getId() == parent.getSiteId())) {
            for (Integer id : ids) {
                move(site, id, parentId);
            }
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "move.category", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                            new StringBuilder(StringUtils.join(ids, ',')).append(" to ").append(parentId).toString()));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param siteId
     * @param id
     * @param parentId
     */
    private void move(SysSite site, Integer id, Integer parentId) {
        CmsCategory entity = service.getEntity(id);
        if (null != entity && site.getId() == entity.getSiteId()) {
            service.updateParentId(site.getId(), id, parentId);
            service.generateChildIds(site.getId(), entity.getParentId());
            if (null != parentId) {
                service.generateChildIds(site.getId(), parentId);
            }
        }
    }

    /**
     * @param ids
     * @param max
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("publish")
    public String publish(Integer[] ids, Integer max, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(ids)) {
            try {
                for (Integer id : ids) {
                    publish(site, id, max);
                }
            } catch (IOException | TemplateException e) {
                ControllerUtils.verifyCustom("static", true, model);
                log.error(e.getMessage(), e);
                return TEMPLATE_ERROR;
            }
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "static.category", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder(StringUtils.join(ids, ',')).append(",pageSize:")
                            .append((CommonUtils.empty(max) ? 1 : max)).toString()));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param typeId
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("changeType")
    public String changeType(Integer id, Integer typeId, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(id)) {
            service.changeType(id, typeId);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "changeType.category", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder(id).append(" to ").append(typeId).toString()));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param siteId
     * @param id
     * @param max
     * @throws IOException
     * @throws TemplateException
     */
    private void publish(SysSite site, Integer id, Integer max) throws IOException, TemplateException {
        CmsCategory entity = service.getEntity(id);
        if (null != site && null != entity && site.getId() == entity.getSiteId()) {
            templateComponent.createCategoryFile(site, entity, null, max);
        }
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(Integer[] ids, HttpServletRequest request, HttpSession session) {
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            service.delete(site.getId(), ids);
            contentService.deleteByCategoryIds(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.category", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }
}