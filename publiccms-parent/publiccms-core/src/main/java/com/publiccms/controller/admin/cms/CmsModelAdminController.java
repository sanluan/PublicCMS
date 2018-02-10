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

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.views.pojo.entities.CmsModel;

/**
 * 
 * CmsModelController
 *
 */
@Controller
@RequestMapping("cmsModel")
public class CmsModelAdminController extends AbstractController {
    @Autowired
    private ModelComponent modelComponent;

    /**
     * @param entity
     * @param modelId
     * @param request
     * @param session
     * @return view name
     */
    @RequestMapping("save")
    public String save(@ModelAttribute CmsModel entity, String modelId, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        modelComponent.clear(site.getId());
        if (CommonUtils.notEmpty(modelId)) {
            Map<String, CmsModel> modelMap = modelComponent.getMap(site);
            modelMap.remove(modelId);
            modelMap.put(entity.getId(), entity);
            modelComponent.save(site, modelMap);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.model", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            Map<String, CmsModel> modelMap = modelComponent.getMap(site);
            modelMap.put(entity.getId(), entity);
            modelComponent.save(site, modelMap);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.model", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(String id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        Map<String, CmsModel> modelMap = modelComponent.getMap(site);
        CmsModel entity = modelMap.remove(id);
        if (null != entity) {
            List<CmsModel> modelList = modelComponent.getList(site, entity.getId(), null, null, null, null);
            for (CmsModel m : modelList) {
                m.setParentId(null);
                modelMap.put(m.getId(), m);
            }
            modelComponent.save(site, modelMap);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.model", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return TEMPLATE_DONE;
    }
}