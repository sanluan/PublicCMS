package com.publiccms.controller.admin.cms;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.DiyComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.diy.CmsLayout;
import com.publiccms.views.pojo.diy.CmsModule;
import com.publiccms.views.pojo.diy.CmsRegion;
import com.publiccms.views.pojo.diy.CmsRegionData;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * CmsModelController
 *
 */
@Controller
@RequestMapping("cmsDiy")
public class CmsDiyAdminController {
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private DiyComponent diyComponent;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param admin
     * @param categoryId
     * @param diydata
     * @param request
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Integer categoryId, String diydata,
            HttpServletRequest request) {
        CmsRegionData entity = diyComponent.getRegionData(site, categoryService.getEntity(categoryId), diydata);
        if (null != entity) {
            diyComponent.updateRegionData(site, entity);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.diydata", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param entity
     * @param request
     * @return view name
     */
    @RequestMapping("saveRegion")
    @Csrf
    public String saveRegion(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, @ModelAttribute CmsRegion entity,
            HttpServletRequest request) {
        diyComponent.updateRegion(site, entity);
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "save.region", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param entity
     * @param request
     * @return view name
     */
    @RequestMapping("saveLayout")
    @Csrf
    public String saveLayout(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, @ModelAttribute CmsLayout entity,
            HttpServletRequest request) {
        entity.setTemplate(new String(VerificationUtils.base64Decode(entity.getTemplate()), CommonConstants.DEFAULT_CHARSET));
        diyComponent.updateLayout(site, entity);
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "save.layout", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param entity
     * @param request
     * @return view name
     */
    @RequestMapping("saveModule")
    @Csrf
    public String saveModule(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, @ModelAttribute CmsModule entity,
            HttpServletRequest request) {
        diyComponent.updateModule(site, entity);
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "save.module", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param itemType
     * @param id
     * @param request
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String itemType, String id,
            HttpServletRequest request) {
        if ("region".equalsIgnoreCase(itemType)) {
            CmsRegion entity = diyComponent.deleteRegion(site, id);
            if (null != entity) {
                diyComponent.deleteRegionData(site, id);
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "delete.region", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else if ("layout".equalsIgnoreCase(itemType)) {
            CmsLayout entity = diyComponent.deleteLayout(site, id);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "delete.layout", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else if ("module".equalsIgnoreCase(itemType)) {
            CmsModule entity = diyComponent.deleteModule(site, id);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "delete.diymodule", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

}