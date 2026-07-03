package com.publiccms.controller.admin.cms;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptItemId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryLangService;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentLangService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDeptItemService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.model.CmsContentLangListParameters;

import freemarker.template.TemplateException;

/**
 *
 * CmsContentLangAdminController
 *
 */
@Controller
@RequestMapping("cmsContentLang")
public class CmsContentLangAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private CmsContentLangService service;
    @Resource
    private CmsCategoryLangService categoryLangService;
    @Resource
    private CmsContentService contentService;
    @Resource
    private SysDeptItemService sysDeptItemService;
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private CmsCategoryModelService categoryModelService;
    @Resource
    private ModelComponent modelComponent;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    protected LogOperateService logOperateService;

    /**
     * 保存内容
     *
     * @param site
     * @param admin
     * @param contentId
     * @param contentLangListParameters
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long contentId,
            CmsContentLangListParameters contentLangListParameters, HttpServletRequest request, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        CmsContent content = contentService.getEntity(contentId);
        if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils.errorCustom("noright",
                        !(dept.isOwnsAllCategory() || null != sysDeptItemService.getEntity(new SysDeptItemId(admin.getDeptId(),
                                SysDeptItemService.ITEM_TYPE_CATEGORY, String.valueOf(content.getCategoryId())))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        CmsCategory category = categoryService.getEntity(content.getCategoryId());
        if (null != category && site.getId() != category.getSiteId()) {
            category = null;
        }

        CmsModel cmsModel = modelComponent.getModel(site, content.getModelId());
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(content.getCategoryId(), content.getModelId()));

        if (ControllerUtils.errorNotEmpty("category", category, model) || ControllerUtils.errorNotEmpty("model", cmsModel, model)
                || ControllerUtils.errorNotEmpty("categoryModel", categoryModel, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }

        service.save(site, admin.getId(), content, contentLangListParameters, cmsModel, category.getExtendId());

        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "save.contentLang", RequestUtils.getIpAddress(request), CommonUtils.now(),
                JsonUtils.getString(contentLangListParameters)));

        try {
            if (CmsContentService.STATUS_NORMAL == content.getStatus()) {
                templateComponent.publish(site, content, category, categoryModel); // 静态化
                templateComponent.publish(site, category, null, null);
            }
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
            model.put(CommonConstants.ERROR, e.getMessage());
            return CommonConstants.TEMPLATE_ERROR;
        }
        return CommonConstants.TEMPLATE_DONE;
    }

}