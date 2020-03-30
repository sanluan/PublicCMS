package com.publiccms.controller.web.cms;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.entities.ClickStatistics;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;
import com.publiccms.views.pojo.model.ExtendDataParameters;

/**
 *
 * PlaceController
 * 
 */
@Controller
@RequestMapping("place")
public class PlaceController {
    @Autowired
    private CmsPlaceService service;
    @Autowired
    private StatisticsComponent statisticsComponent;
    @Autowired
    private CmsPlaceAttributeService attributeService;
    @Autowired
    private MetadataComponent metadataComponent;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    protected ConfigComponent configComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId", "type", "path", "createDate", "userId", "disabled" };

    /**
     * @param site
     * @param entity
     * @param returnUrl
     * @param _csrf
     * @param placeParameters
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping(value = "save")
    public String save(@RequestAttribute SysSite site, CmsPlace entity, String returnUrl, String _csrf,
            @ModelAttribute ExtendDataParameters placeParameters, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        if (null != entity && CommonUtils.notEmpty(entity.getPath())) {
            if (!entity.getPath().startsWith(CommonConstants.SEPARATOR)) {
                entity.setPath(CommonConstants.SEPARATOR + entity.getPath());
            }
            entity.setPath(entity.getPath().replace("//", CommonConstants.SEPARATOR));
            entity.setStatus(CmsPlaceService.STATUS_PEND);
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
            SysUser user = ControllerUtils.getUserFromSession(session);
            if (ControllerUtils.verifyCustom("contribute",
                    null == metadata || !metadata.isAllowContribute() || 0 >= metadata.getSize(), model)
                    || ControllerUtils.verifyCustom("anonymousContribute", null == user && !metadata.isAllowAnonymous(), model)) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
            }
            if (!metadata.isAllowAnonymous()
                    && ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getWebToken(request), _csrf, model)) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
            }
            if (null != entity.getId()) {
                CmsPlace oldEntity = service.getEntity(entity.getId());
                if (null == oldEntity || CommonUtils.empty(oldEntity.getUserId()) || null == user
                        || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)
                        || ControllerUtils.verifyNotEquals("siteId", user.getId(), oldEntity.getUserId(), model)) {
                    return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
                }
                entity = service.update(entity.getId(), entity, ignoreProperties);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "update.place",
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), entity.getPath()));
            } else {
                entity.setSiteId(site.getId());
                Long userId = null;
                if (null != user) {
                    userId = user.getId();
                    entity.setUserId(user.getId());
                }
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB, "save.place",
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), entity.getPath()));
            }
            Map<String, String> map = ExtendUtils.getExtentDataMap(placeParameters.getExtendDataList(),
                    metadataComponent.getPlaceMetadata(filePath).getExtendList());
            String extentString = ExtendUtils.getExtendString(map);
            attributeService.updateAttribute(entity.getId(), extentString);
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    /**
     * @param site
     * @param id
     * @param user
     * @param returnUrl
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, Long id, @SessionAttribute SysUser user, String returnUrl,
            HttpServletRequest request, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        CmsPlace entity = service.getEntity(id);
        String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
        CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
        if (ControllerUtils.verifyCustom("manage",
                null == entity || null == user || CommonUtils.empty(metadata.getAdminIds())
                        || !ArrayUtils.contains(metadata.getAdminIds(), user.getId()),
                model) || ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        } else {
            service.delete(id);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "delete.place",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), id.toString()));
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
    }

    /**
     * @param site
     * @param id
     * @param user
     * @param returnUrl
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("check")
    @Csrf
    public String check(@RequestAttribute SysSite site, Long id, @SessionAttribute SysUser user, String returnUrl,
            HttpServletRequest request, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        CmsPlace entity = service.getEntity(id);
        String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
        CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
        if (ControllerUtils.verifyCustom("manage",
                null == entity || null == user || CommonUtils.empty(metadata.getAdminIds())
                        || !ArrayUtils.contains(metadata.getAdminIds(), user.getId()),
                model) || ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        } else {
            service.check(id, user.getId());
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "check.place",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), id.toString()));
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
    }

    /**
     * @param site
     * @param id
     * @param user
     * @param returnUrl
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("uncheck")
    @Csrf
    public String uncheck(@RequestAttribute SysSite site, Long id, @SessionAttribute SysUser user, String returnUrl,
            HttpServletRequest request, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        CmsPlace entity = service.getEntity(id);
        String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
        CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
        if (ControllerUtils.verifyCustom("manage",
                null == entity || null == user || CommonUtils.empty(metadata.getAdminIds())
                        || !ArrayUtils.contains(metadata.getAdminIds(), user.getId()),
                model) || ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        } else {
            service.uncheck(id);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "check.place",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), id.toString()));
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
    }

    /**
     * @param site
     * @param id
     * @param request
     * @return view name
     */
    @RequestMapping("click")
    public String click(@RequestAttribute SysSite site, Long id, HttpServletRequest request) {
        ClickStatistics clickStatistics = statisticsComponent.placeClicks(site.getId(), id);
        if (null != clickStatistics && CommonUtils.notEmpty(clickStatistics.getUrl())
                && site.getId().equals(clickStatistics.getSiteId())) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + clickStatistics.getUrl();
        } else {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + site.getDynamicPath();
        }
    }

    /**
     * @param site
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("redirect")
    public void redirect(@RequestAttribute SysSite site, Long id, HttpServletRequest request, HttpServletResponse response) {
        ClickStatistics clickStatistics = statisticsComponent.placeClicks(site.getId(), id);
        if (null != clickStatistics && CommonUtils.notEmpty(clickStatistics.getUrl())
                && site.getId().equals(clickStatistics.getSiteId())) {
            ControllerUtils.redirectPermanently(response, clickStatistics.getUrl());
        } else {
            ControllerUtils.redirectPermanently(response, site.getDynamicPath());
        }
    }
}
