package com.publiccms.controller.admin.cms;

import java.util.Comparator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.entities.CmsCategoryType;

/**
 * 
 * CmsCategoryTypeController
 *
 */
@Controller
@RequestMapping("cmsCategoryType")
public class CmsCategoryTypeAdminController {
    @Resource
    private ModelComponent modelComponent;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param admin
     * @param entity
     * @param categoryTypeId
     * @param request
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, @ModelAttribute CmsCategoryType entity,
            String categoryTypeId, HttpServletRequest request) {
        if (CommonUtils.notEmpty(entity.getExtendList())) {
            entity.getExtendList().sort(Comparator.comparing(SysExtendField::getSort));
            entity.getExtendList().forEach(e -> {
                if (CommonUtils.empty(e.getName())) {
                    e.setName(e.getId().getCode());
                }
            });
        }
        modelComponent.clear(site.getId());
        if (CommonUtils.notEmpty(categoryTypeId)) {
            Map<String, CmsCategoryType> categoryTypeMap = modelComponent.getCategoryTypeMap(site.getId());
            categoryTypeMap.remove(categoryTypeId);
            categoryTypeMap.put(entity.getId(), entity);
            modelComponent.saveCategoryType(site.getId(), categoryTypeMap);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.categoryType", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            Map<String, CmsCategoryType> categoryTypeMap = modelComponent.getCategoryTypeMap(site.getId());
            categoryTypeMap.put(entity.getId(), entity);
            modelComponent.saveCategoryType(site.getId(), categoryTypeMap);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.categoryType", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param request
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String id, HttpServletRequest request) {
        Map<String, CmsCategoryType> categoryTypeMap = modelComponent.getCategoryTypeMap(site.getId());
        CmsCategoryType entity = categoryTypeMap.remove(id);
        if (null != entity) {
            modelComponent.saveCategoryType(site.getId(), categoryTypeMap);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.categoryType", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}