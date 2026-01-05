package com.publiccms.controller.admin.cms;

import java.io.IOException;
import java.util.List;

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
import com.publiccms.common.tools.CmsLangUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryLang;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryLangService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.model.CmsCategoryLangListParameters;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * CmsCategoryLangAdminController
 *
 */
@Controller
@RequestMapping("cmsCategoryLang")
public class CmsCategoryLangAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private CmsCategoryLangService service;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private ModelComponent modelComponent;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    protected LogOperateService logOperateService;

    /**
     * @param site
     * @param admin
     * @param categoryId
     * @param categoryLangListParameters
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Integer categoryId,
            @ModelAttribute CmsCategoryLangListParameters categoryLangListParameters, HttpServletRequest request,
            ModelMap model) {
        CmsCategory category = categoryService.getEntity(categoryId);
        if (null != category) {
            if (ControllerUtils.errorNotEquals("siteId", site.getId(), category.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            List<CmsCategoryLang> entityList = service.save(site.getId(), site.getSitePath(), categoryId, admin.getId(),
                    modelComponent.getCategoryType(site.getId(), category.getTypeId()), categoryLangListParameters);
            try {
                for (CmsCategoryLang lang : entityList) {
                    templateComponent.createCategoryFile(site, CmsLangUtils.initLang(category, lang), false, null, null);
                }
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
                model.put(CommonConstants.ERROR, e.getMessage());
                return CommonConstants.TEMPLATE_ERROR;
            }
        }
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "save.categoryLang", RequestUtils.getIpAddress(request), CommonUtils.now(),
                JsonUtils.getString(categoryLangListParameters)));

        return CommonConstants.TEMPLATE_DONE;
    }
}