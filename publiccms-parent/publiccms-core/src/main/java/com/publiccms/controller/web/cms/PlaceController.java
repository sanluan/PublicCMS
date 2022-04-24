package com.publiccms.controller.web.cms;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.entities.ClickStatistics;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;
import com.publiccms.views.pojo.model.ExtendDataParameters;

import freemarker.template.TemplateException;

/**
 *
 * PlaceController
 * 
 */
@Controller
@RequestMapping("place")
public class PlaceController {
    protected final Log log = LogFactory.getLog(getClass());
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
    protected SiteConfigComponent siteConfigComponent;
    @Autowired
    private TemplateComponent templateComponent;

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
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        if (null != entity && CommonUtils.notEmpty(entity.getPath())) {
            if (!entity.getPath().startsWith(CommonConstants.SEPARATOR)) {
                entity.setPath(CommonConstants.SEPARATOR + entity.getPath());
            }
            entity.setPath(entity.getPath().replace("//", CommonConstants.SEPARATOR));
            entity.setStatus(CmsPlaceService.STATUS_PEND);
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
            SysUser user = ControllerUtils.getUserFromSession(session);
            if (ControllerUtils.errorCustom("contribute",
                    null == metadata || !metadata.isAllowContribute() || 0 >= metadata.getSize(), model)
                    || ControllerUtils.errorCustom("anonymousContribute", null == user && !metadata.isAllowAnonymous(), model)) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
            }
            if (!metadata.isAllowAnonymous()
                    && ControllerUtils.errorNotEquals("_csrf", ControllerUtils.getWebToken(request), _csrf, model)) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
            }
            if (null != entity.getId()) {
                CmsPlace oldEntity = service.getEntity(entity.getId());
                if (null == oldEntity || CommonUtils.empty(oldEntity.getUserId()) || null == user
                        || ControllerUtils.errorNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)
                        || ControllerUtils.errorNotEquals("siteId", user.getId(), oldEntity.getUserId(), model)) {
                    return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
                }
                entity = service.update(entity.getId(), entity, ignoreProperties);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                        "update.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), entity.getPath()));
            } else {
                entity.setSiteId(site.getId());
                Long userId = null;
                if (null != user) {
                    userId = user.getId();
                    entity.setUserId(user.getId());
                }
                entity.setDisabled(false);
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), userId, user.getDeptId(), LogLoginService.CHANNEL_WEB,
                        "save.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), entity.getPath()));
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
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        CmsPlace entity = service.getEntity(id);
        if (null != entity) {
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
            if (ControllerUtils.errorCustom("manage",
                    CommonUtils.empty(metadata.getAdminIds()) || !ArrayUtils.contains(metadata.getAdminIds(), user.getId()),
                    model) || ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            } else {
                service.delete(id);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                        "delete.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), id.toString()));
                if (site.isUseSsi() || CmsFileUtils
                        .exists(siteComponent.getWebFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath()))) {
                    try {
                        CmsPageData data = metadataComponent.getTemplateData(filePath);
                        templateComponent.staticPlace(site, entity.getPath(), metadata, data);
                    } catch (IOException | TemplateException e) {
                        model.addAttribute(CommonConstants.ERROR, e.getMessage());
                        log.error(e.getMessage(), e);
                    }
                }
            }
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
    @RequestMapping("check")
    @Csrf
    public String check(@RequestAttribute SysSite site, Long id, @SessionAttribute SysUser user, String returnUrl,
            HttpServletRequest request, ModelMap model) {
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        CmsPlace entity = service.getEntity(id);
        if (null != entity) {
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
            if (ControllerUtils.errorCustom("manage",
                    CommonUtils.empty(metadata.getAdminIds()) || !ArrayUtils.contains(metadata.getAdminIds(), user.getId()),
                    model) || ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            } else {
                service.check(id, user.getId());
                logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                        "check.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), id.toString()));
                if (site.isUseSsi() || CmsFileUtils
                        .exists(siteComponent.getWebFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath()))) {
                    try {
                        CmsPageData data = metadataComponent.getTemplateData(filePath);
                        templateComponent.staticPlace(site, entity.getPath(), metadata, data);
                    } catch (IOException | TemplateException e) {
                        model.addAttribute(CommonConstants.ERROR, e.getMessage());
                        log.error(e.getMessage(), e);
                    }
                }
            }
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
    @RequestMapping("uncheck")
    @Csrf
    public String uncheck(@RequestAttribute SysSite site, Long id, @SessionAttribute SysUser user, String returnUrl,
            HttpServletRequest request, ModelMap model) {
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        CmsPlace entity = service.getEntity(id);
        if (null != entity) {
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
            if (ControllerUtils.errorCustom("manage",
                    null == entity || null == user || CommonUtils.empty(metadata.getAdminIds())
                            || !ArrayUtils.contains(metadata.getAdminIds(), user.getId()),
                    model) || ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            } else {
                service.uncheck(id);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                        "check.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), id.toString()));
                if (site.isUseSsi() || CmsFileUtils
                        .exists(siteComponent.getWebFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath()))) {
                    try {
                        CmsPageData data = metadataComponent.getTemplateData(filePath);
                        templateComponent.staticPlace(site, entity.getPath(), metadata, data);
                    } catch (IOException | TemplateException e) {
                        model.addAttribute(CommonConstants.ERROR, e.getMessage());
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    /**
     * @param site
     * @param id
     */
    @RequestMapping("click")
    public void click(@RequestAttribute SysSite site, Long id) {
        statisticsComponent.placeClicks(site.getId(), id);
    }

    /**
     * @param site
     * @param id
     * @param response
     */
    @RequestMapping("redirect")
    public void redirect(@RequestAttribute SysSite site, Long id, HttpServletResponse response) {
        ClickStatistics clickStatistics = statisticsComponent.placeClicks(site.getId(), id);
        if (null != clickStatistics && CommonUtils.notEmpty(clickStatistics.getUrl())
                && site.getId().equals(clickStatistics.getSiteId())) {
            ControllerUtils.redirectPermanently(response, clickStatistics.getUrl());
        } else {
            ControllerUtils.redirectPermanently(response, site.getDynamicPath());
        }
    }
}
