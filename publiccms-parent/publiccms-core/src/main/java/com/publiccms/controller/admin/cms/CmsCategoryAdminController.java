package com.publiccms.controller.admin.cms;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.exchange.CategoryExchangeComponent;
import com.publiccms.logic.component.exchange.SiteExchangeComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.model.CmsCategoryListParameters;
import com.publiccms.views.pojo.model.CmsCategoryParameters;
import com.publiccms.views.pojo.model.CmsCategorySEOParameters;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * CmsCategoryController
 *
 */
@Controller
@RequestMapping("cmsCategory")
public class CmsCategoryAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private CmsCategoryService service;
    @Resource
    private CmsContentService contentService;
    @Resource
    private CmsCategoryAttributeService attributeService;
    @Resource
    private CmsCategoryModelService categoryModelService;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private ModelComponent modelComponent;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected CategoryExchangeComponent exchangeComponent;

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
        service.saveTagAndAttribute(site.getId(), site.getSitePath(), entity.getId(), admin.getId(), attribute,
                modelComponent.getCategoryType(site.getId(), entity.getTypeId()), categoryParameters);
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
     * @param id
     * @param categoryListParameters
     * @param request
     * @return view name
     */
    @RequestMapping("batchSave")
    @Csrf
    public String batchSave(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Integer id,
            @ModelAttribute CmsCategoryListParameters categoryListParameters, HttpServletRequest request) {
        CmsCategory copy = service.getEntity(id);
        if (null != copy && site.getId() == copy.getSiteId() && CommonUtils.notEmpty(categoryListParameters.getCategoryList())) {
            for (CmsCategory entity : categoryListParameters.getCategoryList()) {
                service.copy(site.getId(), entity, copy);
                try {
                    templateComponent.createCategoryFile(site, entity, null, null);
                } catch (IOException | TemplateException e) {
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.category.batch", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(categoryListParameters.getCategoryList())));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param seoParameters
     * @param request
     * @return view name
     */
    @RequestMapping("saveSeo")
    @Csrf
    public String saveSeo(@RequestAttribute SysSite site, @SessionAttribute SysUser admin,
            @ModelAttribute CmsCategorySEOParameters seoParameters, HttpServletRequest request) {
        attributeService.updateSeo(seoParameters.getAttributeList());
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "save.category.seo", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                JsonUtils.getString(seoParameters.getAttributeList())));
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
                    CommonUtils.getDate(), CommonUtils.joinString(StringUtils.join(ids, Constants.COMMA), " to ", parentId)));
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
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "static.category", RequestUtils.getIpAddress(request), CommonUtils.getDate(), CommonUtils.joinString(
                                    StringUtils.join(ids, Constants.COMMA), ",pageSize:", CommonUtils.empty(max) ? 1 : max)));
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
                    CommonUtils.getDate(), CommonUtils.joinString(id, " to ", typeId)));
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
                if (entity.isHasStatic() && CommonUtils.notEmpty(entity.getUrl())) {
                    String filepath = siteComponent.getWebFilePath(site.getId(), entity.getUrl());
                    if (entity.getUrl().endsWith(Constants.SEPARATOR)) {
                        filepath = CommonUtils.joinString(filepath, CommonConstants.getDefaultPage());
                    }
                    if (CmsFileUtils.isFile(filepath)) {
                        String backupFilePath = siteComponent.getWebBackupFilePath(site.getId(), entity.getUrl());
                        CmsFileUtils.moveFile(filepath, backupFilePath);
                    }
                }
            }
            contentService.deleteByCategoryIds(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.category", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, Constants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param overwrite
     * @param file
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("doImport")
    @Csrf
    public String doImport(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file, boolean overwrite,
            HttpServletRequest request, ModelMap model) {
        if (null != file) {
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "import.category", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), file.getOriginalFilename()));
        }
        return SiteExchangeComponent.importData(site, admin.getId(), overwrite, "-category.zip", exchangeComponent, file, model);
    }

    /**
     * @param site
     * @param id
     * @return response entity
     */
    @RequestMapping("export")
    @Csrf
    public ResponseEntity<StreamingResponseBody> export(@RequestAttribute SysSite site, Integer id) {
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(CommonUtils.joinString(site.getName(), dateFormat.format(new Date()), "-category.zip"),
                        Constants.DEFAULT_CHARSET)
                .build());
        StreamingResponseBody body = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                try (ArchiveOutputStream archiveOutputStream = new ZipArchiveOutputStream(outputStream)) {
                    if (null == id) {
                        exchangeComponent.exportAll(site, archiveOutputStream);
                    } else {
                        exchangeComponent.exportEntity(site, service.getEntity(id), archiveOutputStream);
                    }
                }
            }
        };
        return ResponseEntity.ok().headers(headers).body(body);
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
                                    log.info(CommonUtils.joinString("publish for category : ", category.getName(), " batch ", i,
                                            " size : ", list.size()));
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
    @RequestMapping("rebuildChildIds")
    @Csrf
    public String rebuildChildIds(@RequestAttribute SysSite site) {
        service.generateChildIds(site.getId(), null);
        return CommonConstants.TEMPLATE_DONE;
    }
}