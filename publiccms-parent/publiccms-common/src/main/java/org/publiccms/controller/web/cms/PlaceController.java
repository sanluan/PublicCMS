package org.publiccms.controller.web.cms;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.redirectPermanently;
import static com.publiccms.common.tools.ControllerUtils.verifyCustom;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.publiccms.common.tools.ExtendUtils.getExtendString;
import static org.publiccms.common.tools.ExtendUtils.getExtentDataMap;
import static org.publiccms.logic.component.template.TemplateComponent.INCLUDE_DIRECTORY;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.cms.CmsPlace;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.component.site.StatisticsComponent;
import org.publiccms.logic.component.template.MetadataComponent;
import org.publiccms.logic.service.cms.CmsPlaceAttributeService;
import org.publiccms.logic.service.cms.CmsPlaceService;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.views.pojo.CmsPlaceMetadata;
import org.publiccms.views.pojo.CmsPlaceParamters;
import org.publiccms.views.pojo.CmsPlaceStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * @return 
     */
    @RequestMapping(value = "save")
    public String save(CmsPlace entity, String returnUrl, @ModelAttribute CmsPlaceParamters placeParamters,
            HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        if (empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        if (null != entity && notEmpty(entity.getPath())) {
            entity.setPath(entity.getPath().replace("//", SEPARATOR));
            String filePath = siteComponent.getWebTemplateFilePath(site, INCLUDE_DIRECTORY + entity.getPath());
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
            SysUser user = getUserFromSession(session);
            if (verifyCustom("contribute", null == metadata || !metadata.isAllowContribute() || 0 >= metadata.getSize()
                    || (null == user && !metadata.isAllowAnonymous()), model)) {
                return REDIRECT + returnUrl;
            }
            if (null != entity.getId()) {
                CmsPlace oldEntity = service.getEntity(entity.getId());
                if (null == oldEntity || empty(oldEntity.getUserId()) || null == user
                        || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)
                        || verifyNotEquals("siteId", user.getId(), oldEntity.getUserId(), model)) {
                    return REDIRECT + returnUrl;
                }
                entity = service.update(entity.getId(), entity, ignoreProperties);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "update.place",
                        getIpAddress(request), getDate(), entity.getPath()));
            } else {
                entity.setSiteId(site.getId());
                Long userId = null;
                if (null != user) {
                    userId = user.getId();
                    entity.setUserId(user.getId());
                }
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB, "save.place",
                        getIpAddress(request), getDate(), entity.getPath()));
            }
            Map<String, String> map = getExtentDataMap(placeParamters.getExtendDataList(),
                    metadataComponent.getPlaceMetadata(filePath).getExtendList());
            String extentString = getExtendString(map);
            attributeService.updateAttribute(entity.getId(), extentString);
        }
        return REDIRECT + returnUrl;
    }

    /**
     * @param id
     * @param returnUrl
     * @param request
     * @param session
     * @param response
     * @param model
     * @return 
     */
    @RequestMapping("delete")
    public String delete(Long id, String returnUrl, HttpServletRequest request, HttpSession session, HttpServletResponse response,
            ModelMap model) {
        SysSite site = getSite(request);
        if (empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        CmsPlace entity = service.getEntity(id);
        SysUser user = getUserFromSession(session);
        String filePath = siteComponent.getWebTemplateFilePath(site, INCLUDE_DIRECTORY + entity.getPath());
        CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
        if (verifyCustom("manage",
                null == entity || null == user || empty(metadata.getAdminIds())
                        || !contains(metadata.getAdminIds(), user.getId()),
                model) || verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            return REDIRECT + returnUrl;
        } else {
            service.delete(id);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "delete.place",
                    getIpAddress(request), getDate(), id.toString()));
            return REDIRECT + returnUrl;
        }
    }

    /**
     * @param id
     * @param returnUrl
     * @param request
     * @param session
     * @param response
     * @param model
     * @return 
     */
    @RequestMapping("check")
    public String check(Long id, String returnUrl, HttpServletRequest request, HttpSession session, HttpServletResponse response,
            ModelMap model) {
        SysSite site = getSite(request);
        if (empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        CmsPlace entity = service.getEntity(id);
        SysUser user = getUserFromSession(session);
        String filePath = siteComponent.getWebTemplateFilePath(site, INCLUDE_DIRECTORY + entity.getPath());
        CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
        if (verifyCustom("manage",
                null == entity || null == user || empty(metadata.getAdminIds())
                        || !contains(metadata.getAdminIds(), user.getId()),
                model) || verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            return REDIRECT + returnUrl;
        } else {
            service.check(id);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "check.place",
                    getIpAddress(request), getDate(), id.toString()));
            return REDIRECT + returnUrl;
        }
    }

    /**
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("redirect")
    public void clicks(Long id, HttpServletRequest request, HttpServletResponse response) {
        SysSite site = getSite(request);
        CmsPlaceStatistics placeStatistics = statisticsComponent.placeClicks(id);
        if (null != placeStatistics && null != placeStatistics.getEntity()
                && site.getId() == placeStatistics.getEntity().getSiteId() && notEmpty(placeStatistics.getEntity().getUrl())) {
            redirectPermanently(response, placeStatistics.getEntity().getUrl());
        } else {
            redirectPermanently(response, site.getDynamicPath());
        }
    }
}
