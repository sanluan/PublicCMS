package com.publiccms.controller.web.cms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.base.AbstractController;
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
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.views.pojo.entities.CmsContentRelatedStatistics;
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
public class ContentController extends AbstractController {
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

    private String[] ignoreProperties = new String[] { "siteId", "userId", "categoryId", "tagIds", "createDate", "clicks",
            "comments", "scores", "childs", "checkUserId" };

    private String[] ignorePropertiesWithUrl = ArrayUtils.addAll(ignoreProperties, new String[] { "url" });

    /**
     * 保存内容
     * 
     * @param entity
     * @param draft 
     * @param attribute
     * @param contentParameters
     * @param returnUrl
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(CmsContent entity, Boolean draft, CmsContentAttribute attribute,
            @ModelAttribute CmsContentParameters contentParameters, String returnUrl, String _csrf, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (isUnSafeUrl(returnUrl, site, request)) {
            returnUrl = site.getDynamicPath();
        }
        SysUser user = ControllerUtils.getUserFromSession(session);
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getWebToken(request), _csrf, model)
                || ControllerUtils.verifyNotEmpty("categoryModel", categoryModel, model)
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
        CmsContentAdminController.initContent(entity, cmsModel, draft, false, attribute, CommonUtils.getDate());
        if (null != entity.getId()) {
            CmsContent oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
            }
            entity = service.update(entity.getId(), entity, entity.isOnlyUrl() ? ignoreProperties : ignorePropertiesWithUrl);
            if (null != entity.getId()) {
                logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "update.content",
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            entity.setUserId(user.getId());
            service.save(entity);
            if (CommonUtils.notEmpty(entity.getParentId())) {
                service.updateChilds(entity.getParentId(), 1);
            }
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "save.content",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        service.saveTagAndAttribute(site.getId(), user.getId(), entity.getId(), contentParameters, cmsModel, category, attribute);
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    /**
     * 内容推荐重定向并计数
     * 
     * @param id
     * @param request
     * @return view name
     */
    @RequestMapping("related/redirect")
    public String relatedRedirect(Long id, HttpServletRequest request) {
        CmsContentRelatedStatistics contentRelatedStatistics = statisticsComponent.relatedClicks(id);
        SysSite site = getSite(request);
        if (null != contentRelatedStatistics && null != contentRelatedStatistics.getEntity()) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + contentRelatedStatistics.getEntity().getUrl();
        } else {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + site.getDynamicPath();
        }
    }

    /**
     * 内容链接重定向并计数
     * 
     * @param id
     * @param request
     * @return view name
     */
    @RequestMapping("redirect")
    public String contentRedirect(Long id, HttpServletRequest request) {
        CmsContentStatistics contentStatistics = statisticsComponent.contentClicks(id);
        SysSite site = getSite(request);
        if (null != contentStatistics && null != contentStatistics.getEntity()
                && site.getId() == contentStatistics.getEntity().getSiteId()) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + contentStatistics.getEntity().getUrl();
        } else {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + site.getDynamicPath();
        }
    }

    /**
     * 内容点击
     * 
     * @param id
     * @return click
     */
    @RequestMapping("click")
    @ResponseBody
    public int click(Long id) {
        CmsContentStatistics contentStatistics = statisticsComponent.contentClicks(id);
        if (null != contentStatistics && null != contentStatistics.getEntity()) {
            return contentStatistics.getEntity().getClicks() + contentStatistics.getClicks();
        }
        return 0;
    }

}
