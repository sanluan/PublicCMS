package com.publiccms.controller.admin.cms;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.views.pojo.model.CmsPlaceParamters;

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
     * @return view name
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(CmsPlace entity, @ModelAttribute CmsPlaceParamters placeParamters, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        if (null != entity && CommonUtils.notEmpty(entity.getPath())) {
            entity.setPath(entity.getPath().replace("//", SEPARATOR));
            SysSite site = getSite(request);
            Long userId = getAdminFromSession(session).getId();
            if (CommonUtils.empty(entity.getItemType()) || CommonUtils.empty(entity.getItemId())) {
                entity.setItemType(CmsPlaceService.ITEM_TYPE_CUSTOM);
                entity.setItemId(null);
            }
            if (null != entity.getId()) {
                CmsPlace oldEntity = service.getEntity(entity.getId());
                if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                    return TEMPLATE_ERROR;
                }
                entity = service.update(entity.getId(), entity, ignoreProperties);
                if (null != entity) {
                    logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER,
                            "update.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), entity.getPath()));
                }
            } else {
                entity.setUserId(userId);
                entity.setSiteId(site.getId());
                entity.setStatus(CmsPlaceService.STATUS_NORMAL);
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "save.place",
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), entity.getPath()));
            }
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
            Map<String, String> map = ExtendUtils.getExtentDataMap(placeParamters.getExtendDataList(),
                    metadataComponent.getPlaceMetadata(filePath).getExtendList());
            String extentString = ExtendUtils.getExtendString(map);
            attributeService.updateAttribute(entity.getId(), extentString);
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("refresh")
    public String refresh(Long[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            service.refresh(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "refresh.place", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("check")
    public String check(Long[] ids, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            service.check(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "check.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    StringUtils.join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("clear")
    public String clear(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(request);
            service.delete(site.getId(), path);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "clear.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(Long[] ids, HttpServletRequest request, HttpSession session) {
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            service.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.place", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }
}
