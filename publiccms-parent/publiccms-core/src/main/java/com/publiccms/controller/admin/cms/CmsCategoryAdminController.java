package com.publiccms.controller.admin.cms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.ExportComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.model.CmsCategoryParameters;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

import freemarker.template.TemplateException;

/**
 * 
 * CmsCategoryController
 *
 */
@Controller
@RequestMapping("cmsCategory")
public class CmsCategoryAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private CmsCategoryService service;
    @Autowired
    private CmsContentService contentService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private ModelComponent modelComponent;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    protected ExportComponent exportComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId", "childIds", "tagTypeIds", "url", "disabled", "extendId",
            "hasStatic", "typeId" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param attribute
     * @param categoryParameters
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsCategory entity,
            CmsCategoryAttribute attribute, @ModelAttribute CmsCategoryParameters categoryParameters, HttpServletRequest request,
            ModelMap model) {
        if (null != entity.getId()) {
            CmsCategory oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.errorNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                if (null != oldEntity.getParentId() && !oldEntity.getParentId().equals(entity.getParentId())) {
                    service.generateChildIds(site.getId(), oldEntity.getParentId());
                    service.generateChildIds(site.getId(), entity.getParentId());
                } else if (null != entity.getParentId() && null == oldEntity.getParentId()) {
                    service.generateChildIds(site.getId(), entity.getParentId());
                }
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.category", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.category", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        service.saveTagAndAttribute(site.getId(), entity.getId(), admin.getId(), attribute,
                modelComponent.getCategoryTypeMap(site).get(entity.getTypeId()), categoryParameters);
        try {
            publish(site, entity.getId(), null);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
            model.put(CommonConstants.ERROR, e.getMessage());
            return CommonConstants.TEMPLATE_ERROR;
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param parentId
     * @param request
     * @return view name
     */
    @RequestMapping("move")
    @Csrf
    public String move(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Integer[] ids, Integer parentId,
            HttpServletRequest request) {
        CmsCategory parent = service.getEntity(parentId);
        if (CommonUtils.notEmpty(ids) && (null == parent || site.getId() == parent.getSiteId())) {
            for (Integer id : ids) {
                move(site, id, parentId);
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "move.category", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(),
                    new StringBuilder(StringUtils.join(ids, CommonConstants.COMMA)).append(" to ").append(parentId).toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param id
     * @param parentId
     */
    private void move(SysSite site, Integer id, Integer parentId) {
        CmsCategory entity = service.getEntity(id);
        if (null != entity && site.getId() == entity.getSiteId()) {
            service.updateParentId(site.getId(), id, parentId);
            service.generateChildIds(site.getId(), entity.getParentId());
            if (null != parentId) {
                service.generateChildIds(site.getId(), parentId);
            }
        }
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param max
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("publish")
    @Csrf
    public String publish(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Integer[] ids, Integer max,
            HttpServletRequest request, ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            try {
                for (Integer id : ids) {
                    publish(site, id, max);
                }
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
                model.put(CommonConstants.ERROR, e.getMessage());
                return CommonConstants.TEMPLATE_ERROR;
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "static.category", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder(StringUtils.join(ids, CommonConstants.COMMA)).append(",pageSize:")
                            .append((CommonUtils.empty(max) ? 1 : max)).toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param typeId
     * @param request
     * @return view name
     */
    @RequestMapping("changeType")
    @Csrf
    public String changeType(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Integer id, String typeId,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(id)) {
            service.changeType(id, typeId);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "changeType.category", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), new StringBuilder(id).append(" to ").append(typeId).toString()));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param id
     * @param max
     * @throws IOException
     * @throws TemplateException
     */
    private void publish(SysSite site, Integer id, Integer max) throws IOException, TemplateException {
        CmsCategory entity = service.getEntity(id);
        if (null != site && null != entity && site.getId() == entity.getSiteId()) {
            templateComponent.createCategoryFile(site, entity, null, max);
        }
    }

    /**
     * @param siteId
     * @param parentId
     * @param model
     * @return view name
     */
    @RequestMapping("lookupBySiteId")
    public String lookupBySiteId(short siteId, Integer parentId, ModelMap model) {
        CmsCategoryQuery query = new CmsCategoryQuery();
        query.setSiteId(siteId);
        query.setParentId(parentId);
        query.setDisabled(false);
        model.addAttribute("page", service.getPage(query, 1, 500));
        return "cmsCategory/lookupBySiteId";
    }

    /**
     * @param site
     * @param code
     * @param oldCode
     * @return view name
     */
    @RequestMapping("virify")
    @ResponseBody
    public boolean virify(@RequestAttribute SysSite site, String code, String oldCode) {
        if (CommonUtils.notEmpty(code)) {
            if (CommonUtils.notEmpty(oldCode) && !code.equals(oldCode) && null != service.getEntityByCode(site.getId(), code)
                    || CommonUtils.empty(oldCode) && null != service.getEntityByCode(site.getId(), code)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Integer[] ids,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(ids)) {
            for (CmsCategory entity : service.delete(site.getId(), ids)) {
                if (entity.isHasStatic()) {
                    String filepath = siteComponent.getWebFilePath(site, entity.getUrl());
                    if (CmsFileUtils.exists(filepath)) {
                        String backupFilePath = siteComponent.getWebBackupFilePath(site, filepath);
                        CmsFileUtils.moveFile(filepath, backupFilePath);
                    }
                }
            }
            contentService.deleteByCategoryIds(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.category", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param response
     */
    @RequestMapping("export")
    @Csrf
    public void export(@RequestAttribute SysSite site, HttpServletResponse response) {
        try {
            response.setHeader("content-disposition",
                    "attachment;fileName=" + URLEncoder.encode(site.getName() + "_category.zip", "utf-8"));
        } catch (UnsupportedEncodingException e1) {
        }
        try (ServletOutputStream outputStream = response.getOutputStream();
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            zipOutputStream.setEncoding(Constants.DEFAULT_CHARSET_NAME);
            exportComponent.exportCategory(site.getId(), zipOutputStream);
        } catch (IOException e) {
        }
    }

    /**
     * @param site
     * @param ids
     * @return view name
     */
    @RequestMapping("batchPublish")
    @Csrf
    public String batchPublish(@RequestAttribute SysSite site, Integer[] ids) {
        if (CommonUtils.notEmpty(ids)) {
            log.info("begin batch publish");
            List<CmsCategory> categoryList = service.getEntitys(ids);
            for (CmsCategory category : categoryList) {
                if (category.getSiteId() == site.getId()) {
                    List<CmsCategoryModel> categoryModelList = categoryModelService.getList(site.getId(), null, category.getId());
                    for (CmsCategoryModel categoryModel : categoryModelList) {
                        contentService.batchWorkId(site.getId(), category.getId(), categoryModel.getId().getModelId(),
                                (list, i) -> {
                                    templateComponent.createContentFile(site, list, category, categoryModel);
                                    log.info("batch " + i + " publish size : " + list.size());
                                }, PageHandler.MAX_PAGE_SIZE);
                    }
                }
            }
            log.info("complete batch publish");
        }
        return CommonConstants.TEMPLATE_DONE;

    }

    /**
     * @param site
     * @return view name
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("rebuildChildIds")
    @Csrf
    public String rebuildChildIds(@RequestAttribute SysSite site) {
        CmsCategoryQuery query = new CmsCategoryQuery();
        query.setSiteId(site.getId());
        query.setQueryAll(true);
        PageHandler page = service.getPage(query, null, null);
        for (CmsCategory category : (List<CmsCategory>) page.getList()) {
            service.generateChildIds(category.getSiteId(), category.getId());
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}