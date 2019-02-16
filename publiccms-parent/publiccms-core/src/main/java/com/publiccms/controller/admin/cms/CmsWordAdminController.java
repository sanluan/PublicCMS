package com.publiccms.controller.admin.cms;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsWord;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.cms.CmsWordService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;

/**
 *
 * CmsWordAdminController
 * 
 */
@Controller
@RequestMapping("cmsWord")
public class CmsWordAdminController {
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsWord entity,
            HttpServletRequest request, ModelMap model) {

        if (null != entity.getId()) {
            CmsWord oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(entity.getSiteId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.word", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.word",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
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
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, HttpServletRequest request,
            ModelMap model) {
        CmsWord entity = service.getEntity(id);
        if (null != entity) {
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.delete(id);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "delete.word",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
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
    @RequestMapping("hidden")
    @Csrf
    public String hidden(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, HttpServletRequest request,
            ModelMap model) {
        CmsWord entity = service.getEntity(id);
        if (null != entity) {
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.updateStatus(id, true);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "hidden.word",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
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
    @RequestMapping("show")
    @Csrf
    public String show(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, HttpServletRequest request,
            ModelMap model) {
        CmsWord entity = service.getEntity(id);
        if (null != entity) {
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.updateStatus(id, false);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "show.word",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private CmsWordService service;
}