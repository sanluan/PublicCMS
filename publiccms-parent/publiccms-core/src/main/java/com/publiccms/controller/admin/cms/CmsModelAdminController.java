package com.publiccms.controller.admin.cms;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.entities.CmsModel;

/**
 * 
 * CmsModelController
 *
 */
@Controller
@RequestMapping("cmsModel")
public class CmsModelAdminController {
    @Autowired
    private ModelComponent modelComponent;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    /**
     * @param entity
     * @param modelId
     * @param request
     * @param session
     * @param model 
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@ModelAttribute CmsModel entity, String modelId, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        SysSite site = siteComponent.getSite(request.getServerName());
        if (ControllerUtils.verifyCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        modelComponent.clear(site.getId());
        if (CommonUtils.notEmpty(modelId)) {
            Map<String, CmsModel> modelMap = modelComponent.getMap(site);
            modelMap.remove(modelId);
            modelMap.put(entity.getId(), entity);
            modelComponent.save(site, modelMap);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.model", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            Map<String, CmsModel> modelMap = modelComponent.getMap(site);
            modelMap.put(entity.getId(), entity);
            modelComponent.save(site, modelMap);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.model", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(String id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = siteComponent.getSite(request.getServerName());
        if (ControllerUtils.verifyCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        Map<String, CmsModel> modelMap = modelComponent.getMap(site);
        CmsModel entity = modelMap.remove(id);
        if (null != entity) {
            List<CmsModel> modelList = modelComponent.getList(site, entity.getId(), null, null, null, null);
            for (CmsModel m : modelList) {
                m.setParentId(null);
                modelMap.put(m.getId(), m);
            }
            modelComponent.save(site, modelMap);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.model", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}