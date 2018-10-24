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
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptPageId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDeptPageService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.views.pojo.model.ExtendDataParameters;

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
    @Autowired
    private SysDeptPageService sysDeptPageService;
    @Autowired
    private SysDeptService sysDeptService;

    private String[] ignoreProperties = new String[] { "id", "siteId", "status", "userId", "type", "clicks", "path", "createDate",
            "disabled" };

    /**
     * @param entity
     * @param extendDataParameters 
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(CmsPlace entity, @ModelAttribute ExtendDataParameters extendDataParameters, String _csrf,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null != entity && CommonUtils.notEmpty(entity.getPath())) {
            if (!entity.getPath().startsWith(CommonConstants.SEPARATOR)) {
                entity.setPath(CommonConstants.SEPARATOR + entity.getPath());
            }
            entity.setPath(entity.getPath().replace("//", CommonConstants.SEPARATOR));
            SysSite site = getSite(request);
            SysUser user = ControllerUtils.getAdminFromSession(session);
            SysDept dept = sysDeptService.getEntity(user.getDeptId());
            if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                    || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                    || ControllerUtils.verifyCustom("noright",
                            !(dept.isOwnsAllPage() || null != sysDeptPageService.getEntity(new SysDeptPageId(user.getDeptId(),
                                    CommonConstants.SEPARATOR + TemplateComponent.INCLUDE_DIRECTORY + JsonUtils.getString(entity)))),
                            model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (CommonUtils.empty(entity.getItemType()) || CommonUtils.empty(entity.getItemId())) {
                entity.setItemType(CmsPlaceService.ITEM_TYPE_CUSTOM);
                entity.setItemId(null);
            }
            if (null != entity.getId()) {
                CmsPlace oldEntity = service.getEntity(entity.getId());
                if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
                entity = service.update(entity.getId(), entity, ignoreProperties);
                if (null != entity) {
                    logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "update.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), entity.getPath()));
                }
            } else {
                entity.setUserId(user.getId());
                entity.setSiteId(site.getId());
                entity.setStatus(CmsPlaceService.STATUS_NORMAL);
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "save.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
            String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + entity.getPath());
            Map<String, String> map = ExtendUtils.getExtentDataMap(extendDataParameters.getExtendDataList(),
                    metadataComponent.getPlaceMetadata(filePath).getExtendList());
            String extentString = ExtendUtils.getExtendString(map);
            attributeService.updateAttribute(entity.getId(), extentString);
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("refresh")
    public String refresh(String path, Long[] ids, String _csrf, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysUser user = ControllerUtils.getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                || ControllerUtils.verifyCustom("noright",
                        !(dept.isOwnsAllPage() || null != sysDeptPageService.getEntity(new SysDeptPageId(user.getDeptId(),
                                CommonConstants.SEPARATOR + TemplateComponent.INCLUDE_DIRECTORY + path))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            service.refresh(site.getId(), ids, path);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "refresh.place", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("check")
    public String check(String path, Long[] ids, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysUser user = ControllerUtils.getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                || ControllerUtils.verifyCustom("noright",
                        !(dept.isOwnsAllPage() || null != sysDeptPageService.getEntity(new SysDeptPageId(user.getDeptId(),
                                CommonConstants.SEPARATOR + TemplateComponent.INCLUDE_DIRECTORY + path))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            service.check(site.getId(), ids, path);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "check.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
    
    /**
     * @param path
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("uncheck")
    public String uncheck(String path, Long[] ids, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysUser user = ControllerUtils.getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                || ControllerUtils.verifyCustom("noright",
                        !(dept.isOwnsAllPage() || null != sysDeptPageService.getEntity(new SysDeptPageId(user.getDeptId(),
                                CommonConstants.SEPARATOR + TemplateComponent.INCLUDE_DIRECTORY + path))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            service.uncheck(site.getId(), ids, path);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "check.place", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("clear")
    public String clear(String path, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysUser user = ControllerUtils.getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                || ControllerUtils.verifyCustom("noright",
                        !(dept.isOwnsAllPage() || null != sysDeptPageService.getEntity(new SysDeptPageId(user.getDeptId(),
                                CommonConstants.SEPARATOR + TemplateComponent.INCLUDE_DIRECTORY + path))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(request);
            service.delete(site.getId(), path);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "clear.place",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param path 
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(String path, Long[] ids, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysUser user = ControllerUtils.getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                || ControllerUtils.verifyCustom("noright",
                        !(dept.isOwnsAllPage() || null != sysDeptPageService.getEntity(new SysDeptPageId(user.getDeptId(),
                                CommonConstants.SEPARATOR + TemplateComponent.INCLUDE_DIRECTORY + path))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            SysSite site = getSite(request);
            service.delete(site.getId(), ids, path);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.place", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}
