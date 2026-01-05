package com.publiccms.controller.admin.cms;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ContentConfigComponent;
import com.publiccms.logic.component.site.DirectiveComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.model.CmsContentParameters;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * CmsContentPreviewAdminController
 *
 */
@Controller
@RequestMapping("cmsContent")
public class CmsContentPreviewAdminController {
    @Resource
    private CmsContentService service;
    @Resource
    private CmsContentAttributeService attributeService;
    @Resource
    protected ContentConfigComponent contentConfigComponent;
    @Resource
    protected DirectiveComponent directiveComponent;
    @Resource
    private MetadataComponent metadataComponent;
    @Resource
    private TemplateComponent templateComponent;

    /**
     * @param site
     * @param id
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("preview")
    public void preview(@RequestAttribute SysSite site, Long id, HttpServletRequest request, HttpServletResponse response,
            ModelMap model) {
        CmsContent entity = service.getEntity(id);
        CmsContentAttribute attribute = attributeService.getEntity(id);
        if (null != entity && site.getId() == entity.getSiteId()) {
            try {
                AbstractFreemarkerView.exposeAttribute(model, request);
                response.setContentType("text/html; charset=UTF-8");
                templateComponent.previewContent(site, entity, attribute, response.getWriter(), model);
            } catch (IOException e) {
            }
        }
    }

    /**
     * @param site
     * @param admin
     * @param entity
     * @param attribute
     * @param contentParameters
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("previewBeforeSave")
    @Csrf
    public void previewBeforeSave(@RequestAttribute SysSite site, CmsContent entity, CmsContentAttribute attribute,
            @ModelAttribute CmsContentParameters contentParameters, HttpServletRequest request, HttpServletResponse response,
            ModelMap model) {
        try {
            entity.setId(0L);
            entity.setPublishDate(CommonUtils.now());
            AbstractFreemarkerView.exposeAttribute(model, request);
            templateComponent.initPreviewContentModel(site, entity, attribute, contentParameters, model);
            response.setContentType("text/html; charset=UTF-8");
            templateComponent.previewContent(site, entity, attribute, response.getWriter(), model);
        } catch (IOException e) {
        }
    }
}
