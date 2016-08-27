package com.publiccms.views.controller.admin.cms;

import static com.publiccms.common.tools.ExtendUtils.getExtendString;
import static com.publiccms.common.tools.ExtendUtils.getExtentDataMap;
import static com.publiccms.logic.component.TemplateComponent.INCLUDE_DIRECTORY;
import static com.publiccms.logic.service.cms.CmsPlaceService.ITEM_TYPE_CUSTOM;
import static com.publiccms.logic.service.cms.CmsPlaceService.STATUS_NORMAL;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.lang3.StringUtils.join;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.publiccms.logic.component.MetadataComponent;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.views.pojo.CmsPlaceParamters;

/**
 * 
 * cmsPlaceController
 *
 */
@Controller
@RequestMapping("cmsPlace")
public class CmsPlaceAdminController extends AbstractController {
    @Autowired
    private CmsPlaceService service;
    @Autowired
    private CmsPlaceAttributeService attributeService;
    @Autowired
    private MetadataComponent metadataComponent;

    /**
     * @param entity
     * @param placeParamters
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(CmsPlace entity, @ModelAttribute CmsPlaceParamters placeParamters, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        if (notEmpty(entity) && notEmpty(entity.getPath())) {
            entity.setPath(entity.getPath().replace("//", SEPARATOR));
            SysSite site = getSite(request);
            Long userId = getAdminFromSession(session).getId();
            if (empty(entity.getItemType()) || empty(entity.getItemId())) {
                entity.setItemType(ITEM_TYPE_CUSTOM);
                entity.setItemId(null);
            }
            if (notEmpty(entity.getId())) {
                CmsPlace oldEntity = service.getEntity(entity.getId());
                if (empty(oldEntity) || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                    return TEMPLATE_ERROR;
                }
                entity = service.update(entity.getId(), entity,
                        new String[] { "id", "siteId", "status", "userId", "type", "clicks", "path", "createDate", "disabled" });
                if (notEmpty(entity)) {
                    logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER,
                            "update.place", getIpAddress(request), getDate(), entity.getPath()));
                }
            } else {
                entity.setUserId(userId);
                entity.setSiteId(site.getId());
                entity.setStatus(STATUS_NORMAL);
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "save.place",
                        getIpAddress(request), getDate(), entity.getPath()));
            }
            String filePath = siteComponent.getWebTemplateFilePath(site, INCLUDE_DIRECTORY + entity.getPath());
            Map<String, String> map = getExtentDataMap(placeParamters.getExtendDataList(),
                    metadataComponent.getPlaceMetadata(filePath).getExtendList());
            String extentString = getExtendString(map);
            attributeService.updateAttribute(entity.getId(), extentString);
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("refresh")
    public String refresh(Long[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(ids)) {
            SysSite site = getSite(request);
            service.refresh(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "refresh.place", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("check")
    public String check(Long[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(ids)) {
            SysSite site = getSite(request);
            service.check(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "check.place", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param type
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("clear")
    public String clear(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(path)) {
            SysSite site = getSite(request);
            service.delete(site.getId(), path);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "clear.place", getIpAddress(request), getDate(), path));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("delete")
    public String delete(Long[] ids, HttpServletRequest request, HttpSession session) {
        if (notEmpty(ids)) {
            SysSite site = getSite(request);
            service.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.place", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }
}
