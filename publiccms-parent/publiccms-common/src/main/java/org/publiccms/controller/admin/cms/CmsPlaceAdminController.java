package org.publiccms.controller.admin.cms;
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.lang3.StringUtils.join;
import static org.publiccms.common.tools.ExtendUtils.getExtendString;
import static org.publiccms.common.tools.ExtendUtils.getExtentDataMap;
import static org.publiccms.logic.component.template.TemplateComponent.INCLUDE_DIRECTORY;
import static org.publiccms.logic.service.cms.CmsPlaceService.ITEM_TYPE_CUSTOM;
import static org.publiccms.logic.service.cms.CmsPlaceService.STATUS_NORMAL;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.cms.CmsPlace;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.template.MetadataComponent;
import org.publiccms.logic.service.cms.CmsPlaceAttributeService;
import org.publiccms.logic.service.cms.CmsPlaceService;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.views.pojo.CmsPlaceParamters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    private String[] ignoreProperties = new String[] { "id", "siteId", "status", "userId", "type", "clicks", "path", "createDate",
            "disabled" };

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
        if (null != entity && notEmpty(entity.getPath())) {
            entity.setPath(entity.getPath().replace("//", SEPARATOR));
            SysSite site = getSite(request);
            Long userId = getAdminFromSession(session).getId();
            if (empty(entity.getItemType()) || empty(entity.getItemId())) {
                entity.setItemType(ITEM_TYPE_CUSTOM);
                entity.setItemId(null);
            }
            if (null != entity.getId()) {
                CmsPlace oldEntity = service.getEntity(entity.getId());
                if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                    return TEMPLATE_ERROR;
                }
                entity = service.update(entity.getId(), entity, ignoreProperties);
                if (null != entity) {
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
