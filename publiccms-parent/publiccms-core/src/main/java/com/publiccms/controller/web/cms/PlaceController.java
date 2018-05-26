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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;
import com.publiccms.views.pojo.entities.CmsPlaceStatistics;
import com.publiccms.views.pojo.model.CmsPlaceParamters;

/**
 *
 * PlaceController
 * 
 */
@Controller
@RequestMapping("place")
public class PlaceController extends AbstractController {
    @Autowired
    private CmsPlaceService service;
    @Autowired
    private StatisticsComponent statisticsComponent;
    @Autowired
    private CmsPlaceAttributeService attributeService;
    @Autowired
    private MetadataComponent metadataComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId", "type", "path", "createDate", "userId", "disabled" };

    /**
     * @param entity
     * @param returnUrl
     * @param placeParamters
     * @param request
     * @param session
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "save")
    public String save(CmsPlace entity, String returnUrl, @ModelAttribute CmsPlaceParamters placeParamters,
            HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        if (null != entity && CommonUtils.notEmpty(entity.getPath())) {
            entity.setPath(entity.getPath().replace("//", CommonConstants.SEPARATOR));
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
            SysUser user = ControllerUtils.getUserFromSession(session);
            if (ControllerUtils.verifyCustom("contribute", null == metadata || !metadata.isAllowContribute()
                    || 0 >= metadata.getSize() || (null == user && !metadata.isAllowAnonymous()), model)) {
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
            Map<String, String> map = ExtendUtils.getExtentDataMap(placeParamters.getExtendDataList(),
                    metadataComponent.getPlaceMetadata(filePath).getExtendList());
            String extentString = ExtendUtils.getExtendString(map);
            attributeService.updateAttribute(entity.getId(), extentString);
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    /**
     * @param id
     * @param returnUrl
     * @param request
     * @param session
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(Long id, String returnUrl, HttpServletRequest request, HttpSession session, HttpServletResponse response,
            ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        CmsPlace entity = service.getEntity(id);
        SysUser user = ControllerUtils.getUserFromSession(session);
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
     * @param id
     * @param returnUrl
     * @param request
     * @param session
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping("check")
    public String check(Long id, String returnUrl, HttpServletRequest request, HttpSession session, HttpServletResponse response,
            ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        CmsPlace entity = service.getEntity(id);
        SysUser user = ControllerUtils.getUserFromSession(session);
        String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
        CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
        if (ControllerUtils.verifyCustom("manage",
                null == entity || null == user || CommonUtils.empty(metadata.getAdminIds())
                        || !ArrayUtils.contains(metadata.getAdminIds(), user.getId()),
                model) || ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        } else {
            service.check(id);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "check.place",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), id.toString()));
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
    }

    /**
     * @param id
     * @param request
     * @param response
     * @return view name
     */
    @RequestMapping("click")
    public String click(Long id, HttpServletRequest request, HttpServletResponse response) {
        SysSite site = getSite(request);
        CmsPlaceStatistics placeStatistics = statisticsComponent.placeClicks(id);
        if (null != placeStatistics && null != placeStatistics.getEntity()
                && site.getId() == placeStatistics.getEntity().getSiteId()
                && CommonUtils.notEmpty(placeStatistics.getEntity().getUrl())) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + placeStatistics.getEntity().getUrl();
        } else {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + site.getDynamicPath();
        }
    }

    /**
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("redirect")
    public void redirect(Long id, HttpServletRequest request, HttpServletResponse response) {
        SysSite site = getSite(request);
        CmsPlaceStatistics placeStatistics = statisticsComponent.placeClicks(id);
        if (null != placeStatistics && null != placeStatistics.getEntity()
                && site.getId() == placeStatistics.getEntity().getSiteId()
                && CommonUtils.notEmpty(placeStatistics.getEntity().getUrl())) {
            ControllerUtils.redirectPermanently(response, placeStatistics.getEntity().getUrl());
        } else {
            ControllerUtils.redirectPermanently(response, site.getDynamicPath());
        }
    }
}
