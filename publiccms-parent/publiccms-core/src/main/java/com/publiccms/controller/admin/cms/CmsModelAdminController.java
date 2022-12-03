package com.publiccms.controller.admin.cms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.publiccms.entities.cms.CmsContent;
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

import freemarker.template.TemplateException;

/**
 * 
 * CmsModelController
 *
 */
@Controller
@RequestMapping("cmsModel")
public class CmsModelAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private ModelComponent modelComponent;
    @Autowired
    protected CmsContentService contentService;
    @Autowired
    private SysExtendFieldService extendFieldService;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private CmsCategoryModelService categoryModelService;

    /**
     * @param site
     * @param admin
     * @param entity
     * @param modelId
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, @ModelAttribute CmsModel entity,
            String modelId, HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.errorCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        modelComponent.clear(site.getId());
        if (!(CommonUtils.notEmpty(entity.getFieldList()) && entity.getFieldList().contains("content"))) {
            entity.setSearchable(false);
        }
        if (CommonUtils.notEmpty(entity.getExtendList())) {
            entity.getExtendList().sort((e1, e2) -> e1.getSort() - e2.getSort());
        }
        if (CommonUtils.notEmpty(entity.getRelatedList())) {
            List<Integer> templist = new ArrayList<>();
            Set<String> dictionarySet = new HashSet<String>();
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
            modelMap.remove(modelId);
            modelMap.put(entity.getId(), entity);
            List<CmsModel> modelList = modelComponent.getModelList(site, modelId, false, null, null, null, null);
            for (CmsModel m : modelList) {
                m.setParentId(entity.getId());
                modelMap.put(m.getId(), m);
            }
            modelComponent.saveModel(site, modelMap);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.model", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            Map<String, CmsModel> modelMap = modelComponent.getModelMap(site);
            modelMap.put(entity.getId(), entity);
            modelComponent.saveModel(site, modelMap);
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
            modelComponent.saveModel(site, modelMap);
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
        Map<String, CmsModel> modelMap = modelComponent.getModelMap(site);
        CmsModel entity = modelMap.get(id);
        if (null != entity) {
            log.info("begin batch publish");
            contentService.batchWork(site.getId(), null, new String[] { id }, (list, i) -> {
                for (CmsContent content : list) {
                    try {
                        templateComponent.createContentFile(site, content, null, null);
                    } catch (IOException | TemplateException e) {
                        log.error(e.getMessage());
                    }
                }
                log.info("batch " + i + " publish size : " + list.size());
            }, PageHandler.MAX_PAGE_SIZE);
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
        Map<String, CmsModel> modelMap = modelComponent.getModelMap(site);
        CmsModel entity = modelMap.get(id);
        if (null != entity) {
            CmsCategoryQuery query = new CmsCategoryQuery();
            query.setSiteId(site.getId());
            query.setQueryAll(true);
            for (CmsCategoryModel categoryModel : categoryModelService.getList(id, null)) {
                CmsCategory category = categoryService.getEntity(categoryModel.getId().getCategoryId());
                if (null != category) {
                    log.info("begin rebuild search text for category : " + category.getId());
                    contentService.batchWork(site.getId(), new Integer[] { category.getId() }, new String[] { id }, (list, i) -> {
                        List<SysExtendField> categoryExtendList = null;
                        if (null != category.getExtendId()) {
                            categoryExtendList = extendFieldService.getList(category.getExtendId(), null, true);
                        }
                        contentService.rebuildSearchText(site.getId(), entity, categoryExtendList, list);
                        log.info("rebuild search text for category : " + category.getId() + " batch " + i + " size : "
                                + list.size());
                    }, PageHandler.MAX_PAGE_SIZE);
                    log.info("complete rebuild search text for category : " + category.getId());
                }
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}