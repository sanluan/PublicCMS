package com.publiccms.views.controller.admin.cms;

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.cms.CmsModel;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysExtend;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsModelService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.CmsModelParamters;

/**
 * 
 * CmsModelController
 *
 */
@Controller
@RequestMapping("cmsModel")
public class CmsModelAdminController extends AbstractController {
    @Autowired
    private CmsModelService service;
    @Autowired
    private SysExtendService extendService;
    @Autowired
    private SysExtendFieldService extendFieldService;

    /**
     * @param entity
     * @param modelParamters
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(CmsModel entity, @ModelAttribute CmsModelParamters modelParamters, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (notEmpty(entity.getId())) {
            CmsModel oldEntity = service.getEntity(entity.getId());
            if (empty(oldEntity) || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, new String[] { "id", "siteId", "disabled", "extendId" });
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(entity.getSiteId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.model", getIpAddress(request), getDate(), entity.getId()
                                + ":" + entity.getName()));
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.model", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
        }
        if (empty(extendService.getEntity(entity.getExtendId()))) {
            entity = service.updateExtendId(entity.getId(), (Integer) extendService.save(new SysExtend("model", entity.getId())));
        }
        extendFieldService.update(entity.getExtendId(), modelParamters.getContentExtends());
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        CmsModel entity = service.getEntity(id);
        if (notEmpty(entity)) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.delete(id);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.model", getIpAddress(request), getDate(), id + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }
}