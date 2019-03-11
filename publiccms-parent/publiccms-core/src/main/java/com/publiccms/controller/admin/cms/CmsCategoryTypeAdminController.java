package com.publiccms.controller.admin.cms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.publiccms.entities.cms.CmsCategoryType;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysExtend;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsCategoryTypeService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.model.CmsCategoryTypeParameters;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

/**
 * 
 * CmsCategoryTypeController
 *
 */
@Controller
@RequestMapping("cmsCategoryType")
public class CmsCategoryTypeAdminController {
    @Autowired
    private CmsCategoryTypeService service;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private SysExtendService extendService;
    @Autowired
    private SysExtendFieldService extendFieldService;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId", "extendId" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param categoryTypeParameters
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsCategoryType entity,
            @ModelAttribute CmsCategoryTypeParameters categoryTypeParameters, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (null != entity.getId()) {
            CmsCategoryType oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(
                        new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "update.categoryType",
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "save.categoryType", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        if (CommonUtils.notEmpty(categoryTypeParameters.getCategoryExtends()) || CommonUtils.notEmpty(entity.getExtendId())) {
            if (null == extendService.getEntity(entity.getExtendId())) {
                entity = service.updateExtendId(entity.getId(),
                        (Integer) extendService.save(new SysExtend("categoryType", entity.getId())));
            }
            extendFieldService.update(entity.getExtendId(), categoryTypeParameters.getCategoryExtends());// 修改或增加分类类型扩展字段
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
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Integer id, HttpServletRequest request,
            ModelMap model) {
        CmsCategoryType entity = service.getEntity(id);
        if (null != entity) {
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)
                    || ControllerUtils.verifyNotGreaterThen("category",
                            categoryService
                                    .getPage(new CmsCategoryQuery(site.getId(), null, true, id, null, null, false), null, null)
                                    .getTotalCount(),
                            1, model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.delete(id);
            extendService.delete(entity.getExtendId());
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "delete.categoryType", RequestUtils.getIpAddress(request), CommonUtils.getDate(), id.toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}