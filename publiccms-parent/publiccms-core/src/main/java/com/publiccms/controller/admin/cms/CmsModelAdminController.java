package com.publiccms.controller.admin.cms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.entities.ContentRelated;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

/**
 * 
 * CmsModelController
 *
 */
@Controller
@RequestMapping("cmsModel")
public class CmsModelAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private ModelComponent modelComponent;
    @Resource
    protected CmsContentService contentService;
    @Resource
    private SysExtendFieldService extendFieldService;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private CmsCategoryModelService categoryModelService;

    /**
     * @param site
     * @param admin
     * @param entity
     * @param searchableModel
     * @param modelId
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, @ModelAttribute CmsModel entity,
            boolean searchableModel, String modelId, HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.errorCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        modelComponent.clear(site.getId());
        if (!(CommonUtils.notEmpty(entity.getFieldList()) && entity.getFieldList().contains("content"))) {
            entity.setSearchable(false);
        }
        if (CommonUtils.notEmpty(entity.getExtendList())) {
            entity.getExtendList().sort(Comparator.comparing(SysExtendField::getSort));
            entity.getExtendList().forEach(e -> {
                if (CommonUtils.empty(e.getName())) {
                    e.setName(e.getId().getCode());
                }
            });
        }
        entity.setSearchableModel(searchableModel);
        if (CommonUtils.notEmpty(entity.getRelatedList())) {
            List<Integer> templist = new ArrayList<>();
            Set<String> dictionarySet = new HashSet<>();
            int i = 0;
            for (ContentRelated related : entity.getRelatedList()) {
                if (!dictionarySet.add(related.getDictionaryId())) {
                    templist.add(i);
                }
                i++;
            }
            if (!templist.isEmpty()) {
                Collections.reverse(templist);
                for (int index : templist) {
                    entity.getRelatedList().remove(index);
                }
            }
        }
        if (CommonUtils.notEmpty(modelId)) {
            Map<String, CmsModel> modelMap = modelComponent.getModelMap(site);
            CmsModel oldModel = modelMap.remove(modelId);
            modelMap.put(entity.getId(), entity);
            List<CmsModel> modelList = modelComponent.getModelList(site, modelId, false, null, null, null, null);
            for (CmsModel m : modelList) {
                m.setParentId(entity.getId());
                modelMap.put(m.getId(), m);
            }
            modelComponent.saveModel(site.getId(), modelMap);
            if (CommonUtils.notEmpty(entity.getParentId()) && !entity.getId().equals(oldModel.getId())) {
                List<CmsCategoryModel> categoryModelList = categoryModelService.getList(site.getId(), entity.getParentId(), null);
                for (CmsCategoryModel categoryModel : categoryModelList) {
                    CmsCategoryModel cm = new CmsCategoryModel(
                            new CmsCategoryModelId(categoryModel.getId().getCategoryId(), entity.getId()), site.getId(), true);
                    categoryModelService.save(cm);
                }
                categoryModelService.delete(site.getId(), oldModel.getId(), null);
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.model", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            Map<String, CmsModel> modelMap = modelComponent.getModelMap(site);
            modelMap.put(entity.getId(), entity);
            modelComponent.saveModel(site.getId(), modelMap);
            if (CommonUtils.notEmpty(entity.getParentId())) {
                List<CmsCategoryModel> categoryModelList = categoryModelService.getList(site.getId(), entity.getParentId(), null);
                for (CmsCategoryModel categoryModel : categoryModelList) {
                    CmsCategoryModel cm = new CmsCategoryModel(
                            new CmsCategoryModelId(categoryModel.getId().getCategoryId(), entity.getId()), site.getId(), true);
                    categoryModelService.save(cm);
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.model", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
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
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String id, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.errorCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        Map<String, CmsModel> modelMap = modelComponent.getModelMap(site);
        CmsModel entity = modelMap.remove(id);
        if (null != entity) {
            List<CmsModel> modelList = modelComponent.getModelList(site, entity.getId(), false, null, null, null, null);
            for (CmsModel m : modelList) {
                m.setParentId(null);
                modelMap.put(m.getId(), m);
            }
            modelComponent.saveModel(site.getId(), modelMap);
            categoryModelService.delete(site.getId(), id, null);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.model", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param id
     * @return view name
     */
    @RequestMapping("batchPublish")
    @Csrf
    public String batchPublish(@RequestAttribute SysSite site, String id) {
        CmsModel entity = modelComponent.getModel(site, id);
        if (null != entity) {
            log.info("begin batch publish");
            List<CmsCategoryModel> categoryModelList = categoryModelService.getList(site.getId(), id, null);
            for (CmsCategoryModel categoryModel : categoryModelList) {
                CmsCategory category = categoryService.getEntity(categoryModel.getId().getCategoryId());
                if (null != category) {
                    contentService.batchWorkId(site.getId(), category.getId(), id, (list, i) -> {
                        templateComponent.createContentFile(site, list, category, categoryModel);
                        log.info(CommonUtils.joinString("publish for category : ", category.getName(), " batch ", i, " size : ",
                                list.size()));
                    }, PageHandler.MAX_PAGE_SIZE);
                }
            }
            log.info("complete batch publish");
        }
        return CommonConstants.TEMPLATE_DONE;

    }

    /**
     * @param site
     * @param id
     * @return view name
     */
    @RequestMapping("rebuildSearchText")
    @Csrf
    public String rebuildSearchText(@RequestAttribute SysSite site, String id) {
        CmsModel entity = modelComponent.getModel(site, id);
        if (null != entity) {
            CmsCategoryQuery query = new CmsCategoryQuery();
            query.setSiteId(site.getId());
            query.setQueryAll(true);
            for (CmsCategoryModel categoryModel : categoryModelService.getList(site.getId(), id, null)) {
                CmsCategory category = categoryService.getEntity(categoryModel.getId().getCategoryId());
                if (null != category) {
                    log.info(CommonUtils.joinString("begin rebuild search text for category : ", category.getName()));
                    contentService.batchWorkContent(site.getId(), category.getId(), id, (list, i) -> {
                        List<SysExtendField> categoryExtendList = null;
                        if (null != category.getExtendId()) {
                            categoryExtendList = extendFieldService.getList(category.getExtendId(), null, true);
                        }
                        contentService.rebuildSearchText(site, entity, categoryExtendList, list);
                        log.info(CommonUtils.joinString("rebuild search text for category : ", category.getName(), " batch ", i,
                                " size : ", list.size()));
                    }, PageHandler.MAX_PAGE_SIZE);
                    log.info(CommonUtils.joinString("complete rebuild search text for category : ", category.getName()));
                }
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}