package com.publiccms.controller.admin.cms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.entities.ContentRelated;

/**
 * 
 * CmsModelController
 *
 */
@Controller
@RequestMapping("cmsModel")
public class CmsModelAdminController {
    @Resource
    private ModelComponent modelComponent;
    @Resource
    protected CmsContentService contentService;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;

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
    @RequestMapping("rebuildSearchText")
    @Csrf
    public String rebuildSearchText(@RequestAttribute SysSite site, String id) {
        Map<String, CmsModel> modelMap = modelComponent.getModelMap(site);
        CmsModel entity = modelMap.get(id);
        if (null != entity) {
            contentService.rebuildSearchText(site.getId(), entity);
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}