package com.publiccms.controller.admin.cms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptPageId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateCacheComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDeptPageService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.model.ExtendDataParameters;

/**
 * 
 * CmsPageController
 *
 */
@Controller
@RequestMapping("cmsPage")
public class CmsPageAdminController {
    @Autowired
    private MetadataComponent metadataComponent;
    @Autowired
    private SysDeptPageService sysDeptPageService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private TemplateCacheComponent templateCacheComponent;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    /**
     * @param path
     * @param extendDataParameters 
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String saveMetadata(String path, @ModelAttribute ExtendDataParameters extendDataParameters,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        SysUser user = ControllerUtils.getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                || ControllerUtils
                        .verifyCustom("noright",
                                !(dept.isOwnsAllPage()
                                        || null != sysDeptPageService.getEntity(new SysDeptPageId(user.getDeptId(), path))),
                                model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            SysSite site = siteComponent.getSite(request.getServerName());
            String filePath = siteComponent.getCurrentSiteWebTemplateFilePath(site, path);
            CmsPageData pageDate = new CmsPageData();
            pageDate.setExtendDataList(extendDataParameters.getExtendDataList());
            metadataComponent.updateTemplateData(filePath, pageDate);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "update.template.data", RequestUtils.getIpAddress(request), CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param path
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("clearCache")
    @Csrf
    public String clearCache(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysUser user = ControllerUtils.getAdminFromSession(session);
        SysDept dept = sysDeptService.getEntity(user.getDeptId());
        if (ControllerUtils.verifyNotEmpty("deptId", user.getDeptId(), model)
                || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                || ControllerUtils
                        .verifyCustom("noright",
                                !(dept.isOwnsAllPage()
                                        || null != sysDeptPageService.getEntity(new SysDeptPageId(user.getDeptId(), path))),
                                model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(path)) {
            SysSite site = siteComponent.getSite(request.getServerName());
            templateCacheComponent.deleteCachedFile(SiteComponent.getFullTemplatePath(site, path));
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "clear.pageCache", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), path));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}
