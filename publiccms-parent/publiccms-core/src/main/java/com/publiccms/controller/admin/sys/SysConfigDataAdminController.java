package com.publiccms.controller.admin.sys;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

// Generated 2016-7-16 11:54:16 by com.publiccms.common.generator.SourceGenerator

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysConfigData;
import com.publiccms.entities.sys.SysConfigDataId;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptItemId;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.CorsConfigComponent;
import com.publiccms.logic.component.exchange.ConfigDataExchangeComponent;
import com.publiccms.logic.component.exchange.SiteExchangeComponent;
import com.publiccms.logic.component.site.EmailComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.cms.CmsEditorHistoryService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysConfigDataService;
import com.publiccms.logic.service.sys.SysDeptItemService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.views.pojo.model.ExtendDataParameters;

/**
 *
 * SysConfigDataAdminController
 * 
 */
@Controller
@RequestMapping("sysConfigData")
public class SysConfigDataAdminController {
    @Resource
    private SysDeptItemService sysDeptItemService;
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private ConfigDataComponent configDataComponent;
    @Resource
    private ConfigComponent configComponent;
    @Resource
    private CorsConfigComponent corsConfigComponent;
    @Resource
    private EmailComponent emailComponent;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    private CmsEditorHistoryService editorHistoryService;
    @Resource
    private ConfigDataExchangeComponent exchangeComponent;

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param extendDataParameters
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysConfigData entity,
            @ModelAttribute ExtendDataParameters extendDataParameters, HttpServletRequest request, ModelMap model) {
        if (null != entity.getId()) {
            SysDept dept = sysDeptService.getEntity(admin.getDeptId());
            if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                    || ControllerUtils.errorNotEmpty("deptId", dept, model)
                    || ControllerUtils.errorCustom("noright", !(dept.isOwnsAllConfig() || null != sysDeptItemService.getEntity(
                            new SysDeptItemId(admin.getDeptId(), SysDeptItemService.ITEM_TYPE_CONFIG, entity.getId().getCode()))),
                            model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity.getId().setSiteId(site.getId());
            SysConfigData oldEntity = service.getEntity(entity.getId());
            if (null != oldEntity
                    && ControllerUtils.errorNotEquals("siteId", site.getId(), oldEntity.getId().getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            List<SysExtendField> fieldList = configComponent.getFieldList(site, entity.getId().getCode(), null,
                    RequestContextUtils.getLocale(request));
            Map<String, String> map = extendDataParameters.getExtendData();
            entity.setData(ExtendUtils.getExtendString(map, site.getSitePath(), fieldList));
            if (null != oldEntity) {
                entity = service.update(oldEntity.getId(), entity, ignoreProperties);
                if (null != entity) {
                    logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "update.configData", RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), CommonUtils.joinString(entity.getId().getCode(), ":", entity.getData())));
                }

                if (CommonUtils.notEmpty(oldEntity.getData()) && CommonUtils.notEmpty(fieldList)) {
                    Map<String, String> oldMap = ExtendUtils.getExtendMap(oldEntity.getData());
                    editorHistoryService.saveHistory(site.getId(), admin.getId(), CmsEditorHistoryService.ITEM_TYPE_CONFIG_DATA,
                            entity.getId().getCode(), oldMap, map, fieldList);
                }

            } else {
                entity.getId().setSiteId(site.getId());
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "save.configData", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), CommonUtils.joinString(entity.getId().getCode(), ":", entity.getData())));
            }

            configDataComponent.removeCache(site.getId(), entity.getId().getCode());
            if (emailComponent.getCode(site.getId()).equals(entity.getId().getCode())) {
                emailComponent.clear(site.getId());
            } else if (corsConfigComponent.getCode(site.getId()).equals(entity.getId().getCode())) {
                corsConfigComponent.clear(site.getId());
            }

        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param code
     * @param model
     * @return
     */
    @RequestMapping("export")
    @Csrf
    public ResponseEntity<StreamingResponseBody> export(@RequestAttribute SysSite site, @SessionAttribute SysUser admin,
            String code, ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils.errorCustom("noright",
                        !(dept.isOwnsAllConfig() || null != sysDeptItemService
                                .getEntity(new SysDeptItemId(admin.getDeptId(), SysDeptItemService.ITEM_TYPE_CONFIG, code))),
                        model)) {
        } else {
            SysConfigData entity = service.getEntity(new SysConfigDataId(site.getId(), code));
            if (null != entity) {
                DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(ContentDisposition.attachment()
                        .filename(CommonUtils.joinString(site.getName(), dateFormat.format(new Date()), "-config.zip"),
                                CommonConstants.DEFAULT_CHARSET)
                        .build());
                StreamingResponseBody body = new StreamingResponseBody() {
                    @Override
                    public void writeTo(OutputStream outputStream) throws IOException {
                        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                            zipOutputStream.setEncoding(Constants.DEFAULT_CHARSET_NAME);
                            exchangeComponent.exportEntity(site, entity, zipOutputStream);
                        }
                    }
                };
                return ResponseEntity.ok().headers(headers).body(body);
            }
        }
        return ResponseEntity.notFound().build();
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
                    LogLoginService.CHANNEL_WEB_MANAGER, "import.configData", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), file.getOriginalFilename()));
        }
        return SiteExchangeComponent.importData(site, admin.getId(), overwrite, "-config.zip", exchangeComponent, file, model);
    }

    /**
     * @param site
     * @param admin
     * @param code
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String code, HttpServletRequest request,
            ModelMap model) {
        SysDept dept = sysDeptService.getEntity(admin.getDeptId());
        if (ControllerUtils.errorNotEmpty("deptId", admin.getDeptId(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils.errorCustom("noright",
                        !(dept.isOwnsAllConfig() || null != sysDeptItemService
                                .getEntity(new SysDeptItemId(admin.getDeptId(), SysDeptItemService.ITEM_TYPE_CONFIG, code))),
                        model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysConfigData entity = service.getEntity(new SysConfigDataId(site.getId(), code));
        if (null != entity) {
            service.delete(entity.getId());
            sysDeptItemService.delete(null, SysDeptItemService.ITEM_TYPE_CONFIG, code);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.configData", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), CommonUtils.joinString(entity.getId().getCode(), ":", entity.getData())));
            configDataComponent.removeCache(site.getId(), entity.getId().getCode());
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Resource
    private SysConfigDataService service;
}