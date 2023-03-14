package com.publiccms.controller.admin.cms;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipOutputStream;
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
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.entities.cms.CmsDictionaryId;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.exchange.DictionaryExchangeComponent;
import com.publiccms.logic.component.exchange.SiteExchangeComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeService;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeValueService;
import com.publiccms.logic.service.cms.CmsDictionaryService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.model.CmsDictionaryParameters;

/**
 *
 * CmsDictionaryAdminController
 *
 */
@Controller
@RequestMapping("cmsDictionary")
public class CmsDictionaryAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected DictionaryExchangeComponent exchangeComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param oldId
     * @param parentValue
     * @param dictionaryParameters
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsDictionary entity, String oldId,
            String parentValue, @ModelAttribute CmsDictionaryParameters dictionaryParameters, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.errorCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null != entity && null != entity.getId()) {
            entity.getId().setSiteId(site.getId());
            if (CommonUtils.notEmpty(parentValue)) {
                dataService.update(site.getId(), entity.getId().getId(), dictionaryParameters.getDataList(), parentValue);
            } else if (CommonUtils.notEmpty(oldId)) {
                if (entity.getId().getId().equals(oldId)) {
                    entity = service.update(entity.getId(), entity, ignoreProperties);
                    dataService.update(site.getId(), entity.getId().getId(), dictionaryParameters.getDataList(), parentValue);
                } else {
                    CmsDictionaryId id = new CmsDictionaryId(oldId, site.getId());
                    service.delete(id);
                    dataService.delete(site.getId(), new String[] { oldId });
                    service.save(entity);
                    dataService.save(site.getId(), entity.getId().getId(), dictionaryParameters.getDataList());
                }
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.cmsDictionary", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            } else {
                service.save(entity);
                dataService.save(site.getId(), entity.getId().getId(), dictionaryParameters.getDataList());
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "save.cmsDictionary", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param entity
     * @param oldId
     * @return view name
     */
    @RequestMapping("virify")
    @ResponseBody
    public boolean virify(@RequestAttribute SysSite site, CmsDictionary entity, String oldId) {
        if (null != entity && null != entity.getId() && CommonUtils.notEmpty(entity.getId().getId())) {
            entity.getId().setSiteId(site.getId());
            if (CommonUtils.notEmpty(oldId) && !entity.getId().getId().equals(oldId) && null != service.getEntity(entity.getId())
                    || CommonUtils.empty(oldId) && null != service.getEntity(entity.getId())) {
                return false;
            }
        }
        return true;
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
        if (ControllerUtils.errorCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null != file) {
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "import.cmsDictionary", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), file.getOriginalFilename()));
        }
        return SiteExchangeComponent.importData(site, admin.getId(), overwrite, "-dictionary.zip", exchangeComponent, file,
                model);
    }

    /**
     * @param site
     * @param id
     * @return response entity
     */
    @RequestMapping("export")
    @Csrf
    public ResponseEntity<StreamingResponseBody> export(@RequestAttribute SysSite site, String id) {
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(CommonUtils.joinString(site.getName(), dateFormat.format(new Date()), "-dictionary.zip"),
                        StandardCharsets.UTF_8)
                .build());
        StreamingResponseBody body = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                    zipOutputStream.setEncoding(Constants.DEFAULT_CHARSET_NAME);
                    if (CommonUtils.empty(id)) {
                        exchangeComponent.exportAll(site, zipOutputStream);
                    } else {
                        exchangeComponent.exportEntity(site, service.getEntity(new CmsDictionaryId(id, site.getId())),
                                zipOutputStream);
                    }
                }
            }
        };
        return ResponseEntity.ok().headers(headers).body(body);
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String[] ids,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.errorCustom("noright", null != site.getParentId(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(ids)) {
            CmsDictionaryId[] entityIds = new CmsDictionaryId[ids.length];
            for (int i = 0; i < ids.length; i++) {
                entityIds[i] = new CmsDictionaryId(ids[i], site.getId());
            }
            service.delete(entityIds);
            dataService.delete(site.getId(), ids);
            excludeService.delete(site.getId(), ids);
            excludeValueService.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.cmsDictionary", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Resource
    private CmsDictionaryService service;
    @Resource
    private CmsDictionaryDataService dataService;
    @Resource
    private CmsDictionaryExcludeService excludeService;
    @Resource
    private CmsDictionaryExcludeValueService excludeValueService;
}