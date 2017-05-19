package com.publiccms.controller.web.cms;

import static com.publiccms.common.tools.ExtendUtils.getExtendString;
import static com.publiccms.common.tools.ExtendUtils.getExtentDataMap;
import static com.publiccms.logic.component.template.TemplateComponent.INCLUDE_DIRECTORY;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.lang3.ArrayUtils.contains;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.views.pojo.CmsPlaceMetadata;
import com.publiccms.views.pojo.CmsPlaceParamters;
import com.publiccms.views.pojo.CmsPlaceStatistics;

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
     * @param callback
     * @param placeParamters
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "save")
    public void save(CmsPlace entity, String returnUrl, @ModelAttribute CmsPlaceParamters placeParamters,
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
                redirect(response, returnUrl);
                return;
            }
            if (null != entity.getId()) {
                CmsPlace oldEntity = service.getEntity(entity.getId());
                if (null == oldEntity || empty(oldEntity.getUserId()) || null == user
                        || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)
                        || verifyNotEquals("siteId", user.getId(), oldEntity.getUserId(), model)) {
                    redirect(response, returnUrl);
                    return;
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
        redirect(response, returnUrl);
    }

    /**
     * @param id
     * @param callback
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("delete")
    public void delete(Long id, String returnUrl, HttpServletRequest request, HttpSession session, HttpServletResponse response,
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
            redirect(response, returnUrl);
        } else {
            service.delete(id);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "delete.place",
                    getIpAddress(request), getDate(), id.toString()));
            redirect(response, returnUrl);
        }
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return
     * @return
     */
    @RequestMapping("check")
    public void check(Long id, String returnUrl, HttpServletRequest request, HttpSession session, HttpServletResponse response,
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
            redirect(response, returnUrl);
        } else {
            service.check(id);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "check.place",
                    getIpAddress(request), getDate(), id.toString()));
            redirect(response, returnUrl);
        }
    }

    /**
     * 推荐位链接重定向并计数
     * 
     * @param id
     * @return
     */
    @RequestMapping("redirect")
    public void clicks(Long id, HttpServletRequest request, HttpServletResponse response) {
        SysSite site = getSite(request);
        CmsPlaceStatistics placeStatistics = statisticsComponent.placeClicks(id);
        if (null != placeStatistics.getEntity() && site.getId() == placeStatistics.getEntity().getSiteId()) {
            redirectPermanently(response, placeStatistics.getEntity().getUrl());
        } else {
            redirectPermanently(response, site.getSitePath());
        }
    }
}
