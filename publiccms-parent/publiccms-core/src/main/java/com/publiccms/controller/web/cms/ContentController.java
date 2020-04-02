package com.publiccms.controller.web.cms;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.controller.admin.cms.CmsContentAdminController;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.entities.CmsContentStatistics;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.model.CmsContentParameters;

/**
 * 
 * ContentController 内容
 *
 */
@Controller
@RequestMapping("content")
public class ContentController {
    @Autowired
    private CmsContentService service;
    @Autowired
    private StatisticsComponent statisticsComponent;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private ModelComponent modelComponent;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    protected ConfigComponent configComponent;

    /**
     * 保存内容
     * 
     * @param site
     * 
     * @param entity
     * @param user
     * @param draft
     * @param attribute
     * @param contentParameters
     * @param returnUrl
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @Csrf
    public String save(@RequestAttribute SysSite site, CmsContent entity, @SessionAttribute SysUser user, Boolean draft,
            CmsContentAttribute attribute, @ModelAttribute CmsContentParameters contentParameters, String returnUrl,
            HttpServletRequest request, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (ControllerUtils.verifyNotEmpty("categoryModel", categoryModel, model)
                || ControllerUtils.verifyCustom("contribute", null == user, model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (null != category && (site.getId() != category.getSiteId() || !category.isAllowContribute())) {
            category = null;
        }
        CmsModel cmsModel = modelComponent.getMap(site).get(entity.getModelId());
        if (ControllerUtils.verifyNotEmpty("category", category, model)
                || ControllerUtils.verifyNotEmpty("model", cmsModel, model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
        CmsContentAdminController.initContent(entity, cmsModel, draft, false, attribute, false, CommonUtils.getDate());
        if (null != entity.getId()) {
            CmsContent oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
            }
            entity = service.update(entity.getId(), entity, entity.isOnlyUrl() ? CmsContentAdminController.ignoreProperties
                    : CmsContentAdminController.ignorePropertiesWithUrl);
            if (null != entity.getId()) {
                logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "update.content",
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            entity.setContribute(true);
            entity.setSiteId(site.getId());
            entity.setUserId(user.getId());
            service.save(entity);
            if (CommonUtils.notEmpty(entity.getParentId())) {
                service.updateChilds(entity.getParentId(), 1);
            }
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "save.content",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        service.saveTagAndAttribute(site.getId(), user.getId(), entity.getId(), contentParameters, cmsModel,
                category.getExtendId(), attribute);
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    /**
     * 内容链接重定向并计数
     * 
     * @param site
     * @param id
     * @param request
     * @return view name
     */
    @RequestMapping("redirect")
    public String contentRedirect(@RequestAttribute SysSite site, Long id, HttpServletRequest request) {
        CmsContentStatistics contentStatistics = statisticsComponent.contentClicks(site, id);
        if (null != contentStatistics && null != contentStatistics.getUrl()
                && site.getId().equals(contentStatistics.getSiteId())) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + contentStatistics.getUrl();
        } else {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + site.getDynamicPath();
        }
    }

}
