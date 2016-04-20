package com.publiccms.views.controller.web.page;

import static com.publiccms.common.tools.ExtendUtils.getExtendString;
import static com.publiccms.logic.component.TemplateComponent.INCLUDE_DIRECTORY;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.lang3.ArrayUtils.contains;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.MetadataComponent;
import com.publiccms.logic.component.StatisticsComponent;
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

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(CmsPlace entity, String returnUrl, @ModelAttribute CmsPlaceParamters placeParamters,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(entity) && notEmpty(entity.getPath())) {
            SysSite site = getSite(request);
            entity.setPath(entity.getPath().replace("//", SEPARATOR));
            String placePath = INCLUDE_DIRECTORY + entity.getPath();
            String filePath = siteComponent.getWebTemplateFilePath(site, placePath);
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
            if (empty(getUserFromSession(session))
                    || virifyCustom("contribute", empty(metadata) || !metadata.isAllowContribute() || !(metadata.getSize() > 0),
                            model)) {
                return REDIRECT + returnUrl;
            }
            if (notEmpty(entity.getId())) {
                CmsPlace oldEntity = service.getEntity(entity.getId());
                if (empty(oldEntity) || virifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)
                        || virifyNotEquals("siteId", getUserFromSession(session).getId(), oldEntity.getUserId(), model)) {
                    return REDIRECT + returnUrl;
                }
                entity = service.update(entity.getId(), entity, new String[] { "id", "siteId", "type", "path", "createDate",
                        "disabled" });
                logOperateService.save(new LogOperate(site.getId(), getUserFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB, "update.place", getIpAddress(request), getDate(), entity.getPath()));
            } else {
                entity.setSiteId(site.getId());
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), getUserFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB, "save.place", getIpAddress(request), getDate(), entity.getPath()));
            }
            String extentString = getExtendString(metadataComponent.getPlaceExtendDataMap(filePath,
                    placeParamters.getExtendDataList()));
            attributeService.updateAttribute(entity.getId(), extentString);
        }
        return REDIRECT + returnUrl;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("delete")
    public void delete(Integer id, HttpServletRequest request, HttpSession session) {
        CmsPlace entity = service.getEntity(id);
        SysSite site = getSite(request);
        SysUser user = getUserFromSession(session);
        if (notEmpty(entity) && notEmpty(user) && site.getId() == entity.getSiteId()) {
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(INCLUDE_DIRECTORY + entity.getPath());
            if (notEmpty(metadata.getAdminIds()) && contains(metadata.getAdminIds(), user.getId())) {
                service.delete(id);
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB, "delete.place", getIpAddress(request), getDate(), id.toString()));
            }
        }
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("check")
    public void check(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        CmsPlace entity = service.getEntity(id);
        SysSite site = getSite(request);
        SysUser user = getUserFromSession(session);
        if (notEmpty(entity) && notEmpty(user) && site.getId() == entity.getSiteId()) {
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(INCLUDE_DIRECTORY + entity.getPath());
            if (notEmpty(metadata.getAdminIds()) && contains(metadata.getAdminIds(), user.getId())) {
                service.check(id);
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB, "check.place", getIpAddress(request), getDate(), id.toString()));
            }
        }
    }

    /**
     * 推荐位链接重定向并计数
     * 
     * @param id
     * @return
     */
    @RequestMapping("redirect")
    public void clicks(Integer id, HttpServletRequest request, HttpServletResponse response) {
        SysSite site = getSite(request);
        CmsPlaceStatistics placeStatistics = statisticsComponent.placeClicks(id);
        if (notEmpty(placeStatistics.getEntity()) && site.getId() == placeStatistics.getEntity().getSiteId()) {
            redirectPermanently(response, placeStatistics.getEntity().getUrl());
        } else {
            redirectPermanently(response, site.getSitePath());
        }
    }
}
