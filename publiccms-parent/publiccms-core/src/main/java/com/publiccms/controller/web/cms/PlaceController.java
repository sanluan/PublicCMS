package com.publiccms.controller.web.cms;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
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
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.site.LockComponent;
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
    @Resource
    private StatisticsComponent statisticsComponent;
    @Resource
    private CmsPlaceAttributeService attributeService;
    @Resource
    private MetadataComponent metadataComponent;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected SafeConfigComponent safeConfigComponent;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private LockComponent lockComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId", "type", "path", "createDate", "userId", "disabled" };

    /**
     * @param site
     * @param entity
     * @param returnUrl
     * @param _csrf
     * @param captcha
     * @param placeParameters
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping(value = "save")
    public String save(@RequestAttribute SysSite site, CmsPlace entity, String returnUrl, String _csrf, String captcha,
            @ModelAttribute ExtendDataParameters placeParameters, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        if (null != entity) {
            if (CommonUtils.notEmpty(captcha)
                    || safeConfigComponent.enableCaptcha(site.getId(), SafeConfigComponent.CAPTCHA_MODULE_PLACE_CONTRIBUTE)) {
                String sessionCaptcha = (String) request.getSession().getAttribute("captcha");
                request.getSession().removeAttribute("captcha");
                if (ControllerUtils.errorCustom("captcha.error",
                        null == sessionCaptcha || !sessionCaptcha.equalsIgnoreCase(captcha), model)) {
                    return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
                }
            }
            if (!entity.getPath().startsWith(CommonConstants.SEPARATOR)) {
                entity.setPath(CommonUtils.joinString(CommonConstants.SEPARATOR, entity.getPath()));
            }
            entity.setPath(entity.getPath().replace("//", CommonConstants.SEPARATOR));
            entity.setStatus(CmsPlaceService.STATUS_PEND);
            String filepath = siteComponent.getTemplateFilePath(site.getId(),
                    CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, entity.getPath()));
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
            SysUser user = ControllerUtils.getUserFromSession(session);
            if (ControllerUtils.errorCustom("contribute",
                    null == metadata || !metadata.isAllowContribute() || 0 >= metadata.getSize(), model)
                    || ControllerUtils.errorCustom("anonymousContribute", null == user && !metadata.isAllowAnonymous(), model)) {
                return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
            }
            String ip = RequestUtils.getIpAddress(request);
            if (metadata.isAllowAnonymous()) {
                boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_CONTRIBUTE, ip, null);
                if (ControllerUtils.errorCustom("locked.ip", locked, model)) {
                    lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_CONTRIBUTE, ip, null, true);
                    return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
                }
            } else {
                if (ControllerUtils.errorNotEquals("_csrf", ControllerUtils.getWebToken(request), _csrf, model)) {
                    return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
                } else {
                    boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_CONTRIBUTE,
                            String.valueOf(user.getId()), null);
                    if (ControllerUtils.errorCustom("locked.user", locked, model)) {
                        lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_CONTRIBUTE, String.valueOf(user.getId()), null,
                                true);
                        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
                    }
                }
            }

            if (null != entity.getId()) {
                CmsPlace oldEntity = service.getEntity(entity.getId());
                if (null == oldEntity || CommonUtils.empty(oldEntity.getUserId()) || null == user
                        || ControllerUtils.errorNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)
                        || ControllerUtils.errorNotEquals("siteId", user.getId(), oldEntity.getUserId(), model)) {
                    return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
                }
                entity = service.update(entity.getId(), entity, ignoreProperties);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), null == user ? null : user.getDeptId(),
                        LogLoginService.CHANNEL_WEB, "update.place", ip, CommonUtils.getDate(), entity.getPath()));
            } else {
                entity.setPublishDate(CommonUtils.getDate());
                entity.setPublishDate(CommonUtils.getDate());
                entity.setSiteId(site.getId());
                Long userId = null;
                if (null != user) {
                    userId = user.getId();
                    entity.setUserId(user.getId());
                }
                entity.setDisabled(false);
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), userId, null == user ? null : user.getDeptId(),
                        LogLoginService.CHANNEL_WEB, "save.place", ip, CommonUtils.getDate(), entity.getPath()));
            }
            lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_CONTRIBUTE,
                    metadata.isAllowAnonymous() ? ip : String.valueOf(user.getId()), null, true);
            String extentString = ExtendUtils.getExtendString(placeParameters.getExtendData(),
                    metadataComponent.getPlaceMetadata(filepath).getExtendList());
            attributeService.updateAttribute(entity.getId(), extentString);
            model.addAttribute("id", entity.getId());
        }
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
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
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        CmsPlace entity = service.getEntity(id);
        if (null != entity) {
            String placePath = CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, entity.getPath());
            String filepath = siteComponent.getTemplateFilePath(site.getId(), placePath);
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
            if (ControllerUtils.errorCustom("manage",
                    CommonUtils.empty(metadata.getAdminIds()) || !ArrayUtils.contains(metadata.getAdminIds(), user.getId()),
                    model) || ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            } else {
                service.delete(id);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                        "delete.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), id.toString()));
                if (site.isUseSsi() || CmsFileUtils.exists(siteComponent.getWebFilePath(site.getId(), placePath))) {
                    try {
                        CmsPageData data = metadataComponent.getTemplateData(filepath);
                        templateComponent.staticPlace(site, entity.getPath(), metadata, data);
                    } catch (IOException | TemplateException e) {
                        model.addAttribute(CommonConstants.ERROR, e.getMessage());
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
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
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        CmsPlace entity = service.getEntity(id);
        if (null != entity) {
            String placePath = CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, entity.getPath());
            String filepath = siteComponent.getTemplateFilePath(site.getId(), placePath);
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
            if (ControllerUtils.errorCustom("manage",
                    CommonUtils.empty(metadata.getAdminIds()) || !ArrayUtils.contains(metadata.getAdminIds(), user.getId()),
                    model) || ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            } else {
                service.check(id, user.getId());
                logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                        "check.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), id.toString()));
                if (site.isUseSsi() || CmsFileUtils.exists(siteComponent.getWebFilePath(site.getId(), placePath))) {
                    try {
                        CmsPageData data = metadataComponent.getTemplateData(filepath);
                        templateComponent.staticPlace(site, entity.getPath(), metadata, data);
                    } catch (IOException | TemplateException e) {
                        model.addAttribute(CommonConstants.ERROR, e.getMessage());
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
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
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        CmsPlace entity = service.getEntity(id);
        if (null != entity) {
            String placePath = CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, entity.getPath());
            String filepath = siteComponent.getTemplateFilePath(site.getId(), placePath);
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
            if (ControllerUtils.errorCustom("manage",
                    null == entity || null == user || CommonUtils.empty(metadata.getAdminIds())
                            || !ArrayUtils.contains(metadata.getAdminIds(), user.getId()),
                    model) || ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            } else {
                service.uncheck(id);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                        "check.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), id.toString()));
                if (site.isUseSsi() || CmsFileUtils.exists(siteComponent.getWebFilePath(site.getId(), placePath))) {
                    try {
                        CmsPageData data = metadataComponent.getTemplateData(filepath);
                        templateComponent.staticPlace(site, entity.getPath(), metadata, data);
                    } catch (IOException | TemplateException e) {
                        model.addAttribute(CommonConstants.ERROR, e.getMessage());
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
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
     * @return
     */
    @RequestMapping("redirect")
    public ResponseEntity<?> redirect(@RequestAttribute SysSite site, Long id) {
        ClickStatistics clickStatistics = statisticsComponent.placeClicks(site.getId(), id);
        if (null != clickStatistics && CommonUtils.notEmpty(clickStatistics.getUrl())
                && site.getId().equals(clickStatistics.getSiteId())) {
            return ControllerUtils.redirectPermanently(clickStatistics.getUrl());
        } else {
            return ControllerUtils.redirectPermanently(site.getDynamicPath());
        }
    }

    @Resource
    private CmsPlaceService service;
}
