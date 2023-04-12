package com.publiccms.controller.admin.sys;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.ZipUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.exchange.SiteExchangeComponent;
import com.publiccms.logic.component.site.ScriptComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDomainService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.tools.HqlService;
import com.publiccms.logic.service.tools.SqlService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * SysSiteAdminController
 *
 */
@Controller
@RequestMapping("sysSite")
public class SysSiteAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private SysSiteService service;
    @Resource
    private SysDomainService domainService;
    @Resource
    private SysUserService userService;
    @Resource
    private SqlService sqlService;
    @Resource
    private HqlService hqlService;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected ScriptComponent scriptComponent;
    @Resource
    protected SiteExchangeComponent siteExchangeComponent;

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param fileName
     * @param domain
     * @param wild
     * @param multiple
     * @param roleName
     * @param deptName
     * @param userName
     * @param password
     * @param encoding
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysSite entity, String fileName,
            String domain, Boolean wild, Boolean multiple, String roleName, String deptName, String userName, String password,
            String encoding, HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.errorCustom("noright", !siteComponent.isMaster(site.getId()), model)
                || ControllerUtils.errorCustom("needAuthorizationEdition", !CmsVersion.isAuthorizationEdition(), model)
                || (null != domain
                        && ControllerUtils.errorCustom("unauthorizedDomain", !CmsVersion.verifyDomain(domain), model))) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null == entity.getDynamicPath()) {
            entity.setDynamicPath(Constants.SEPARATOR);
        } else if (!entity.getDynamicPath().endsWith(Constants.SEPARATOR)) {
            entity.setDynamicPath(CommonUtils.joinString(entity.getDynamicPath(), Constants.SEPARATOR));
        }
        if (null == entity.getSitePath()) {
            entity.setSitePath(Constants.SEPARATOR);
        } else if (!entity.getSitePath().endsWith(Constants.SEPARATOR)) {
            entity.setSitePath(CommonUtils.joinString(entity.getSitePath(), Constants.SEPARATOR));
        }
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.site", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            if (ControllerUtils.errorNotEmpty("userName", userName, model)
                    || ControllerUtils.errorNotEmpty("password", password, model)
                    || ControllerUtils.errorHasExist("domain", domainService.getEntity(domain), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            SysUser user = service.save(entity, domain, null != wild && wild, null != multiple && multiple, roleName, deptName,
                    userName, password, encoding);
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.site", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            if (CommonUtils.notEmpty(fileName)) {
                siteExchangeComponent.importData(entity, user.getId(), true, "-site.zip", null, fileName, model);
            }
        }
        siteComponent.clear();
        if (!siteComponent.getSite(request.getServerName(), null).getId().equals(site.getId()) || site.getId()
                .equals(entity.getId())
                && (!site.getSitePath().equals(entity.getSitePath()) || !site.getDynamicPath().equals(entity.getDynamicPath()))) {
            return CommonConstants.TEMPLATE_DONEANDREFRESH;
        } else {
            return CommonConstants.TEMPLATE_DONE;
        }
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Short id, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.errorCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite entity = service.getEntity(id);
        if (null != entity) {
            service.delete(id);
            domainService.deleteBySiteId(entity.getId());
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.site", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param id
     * @return response entity
     */
    @RequestMapping("export")
    @Csrf
    public ResponseEntity<StreamingResponseBody> export(Short id) {
        SysSite site = service.getEntity(id);
        if (null != site) {
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename(CommonUtils.joinString(site.getName(), dateFormat.format(new Date()), "-site.zip"),
                            Constants.DEFAULT_CHARSET)
                    .build());
            StreamingResponseBody body = new StreamingResponseBody() {
                @Override
                public void writeTo(OutputStream outputStream) throws IOException {
                    try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                        zipOutputStream.setEncoding(Constants.DEFAULT_CHARSET_NAME);
                        {
                            String filepath = siteComponent.getTemplateFilePath(site.getId(), Constants.SEPARATOR);
                            ZipUtils.compress(Paths.get(filepath), zipOutputStream, "template");
                        }
                        {
                            String filepath = siteComponent.getWebFilePath(site.getId(), Constants.SEPARATOR);
                            ZipUtils.compress(Paths.get(filepath), zipOutputStream, "web");
                        }
                        {
                            String filepath = siteComponent.getTaskTemplateFilePath(site.getId(), Constants.SEPARATOR);
                            ZipUtils.compress(Paths.get(filepath), zipOutputStream, "tasktemplate");
                        }
                        siteExchangeComponent.exportAll(site, zipOutputStream);
                    }
                }
            };
            return ResponseEntity.ok().headers(headers).body(body);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * @param site
     * @param admin
     * @param file
     * @param overwrite
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("doUploadSitefile")
    @Csrf
    public String doUploadSitefile(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file,
            boolean overwrite, HttpServletRequest request, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            if (null != originalName && originalName.endsWith("-site.zip")) {
                try {
                    String filepath = siteComponent.getSiteFilePath(originalName);
                    if (overwrite || !CmsFileUtils.exists(filepath)) {
                        CmsFileUtils.upload(file, originalName);
                        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                                LogLoginService.CHANNEL_WEB_MANAGER, "upload.sitefile", RequestUtils.getIpAddress(request),
                                CommonUtils.getDate(), originalName));
                    }
                    return CommonConstants.TEMPLATE_DONE;
                } catch (IOException e) {
                    log.error(e.getMessage());
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                }
            } else {
                model.addAttribute(CommonConstants.ERROR, "verify.custom.fileType");
            }
        } else {
            model.addAttribute(CommonConstants.ERROR, "verify.notEmpty.file");
        }
        return CommonConstants.TEMPLATE_ERROR;
    }

    /**
     * @param site
     * @param admin
     * @param command
     * @param parameters
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("execSql")
    @Csrf
    public String execSql(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String command, String[] parameters,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.errorCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if ("update_url".contains(command) && (null != parameters && 3 == parameters.length)) {
            try {
                short siteId = Short.parseShort(parameters[0]);
                String oldurl = parameters[1];
                String newurl = parameters[2];
                int i = sqlService.updateContentAttribute(siteId, oldurl, newurl);
                i += sqlService.updateContentRelated(siteId, oldurl, newurl);
                i += sqlService.updatePlace(siteId, oldurl, newurl);
                i += sqlService.updatePlaceAttribute(siteId, oldurl, newurl);
                i += sqlService.updateCategoryAttribute(siteId, oldurl, newurl);
                i += sqlService.updateConfigData(siteId, oldurl, newurl);
                String filepath = siteComponent.getTemplateFilePath(site.getId(), Constants.SEPARATOR);
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(filepath))) {
                    for (Path entry : stream) {
                        File file = entry.toFile();
                        if (file.isFile() && MetadataComponent.DATA_FILE.equalsIgnoreCase(file.getName())) {
                            String content = StringUtils.replace(FileUtils.readFileToString(file, Constants.DEFAULT_CHARSET),
                                    oldurl, newurl);
                            FileUtils.write(file, content, Constants.DEFAULT_CHARSET);
                            i += 1;
                        }
                    }
                }
                model.addAttribute("result", i);
            } catch (IOException | NumberFormatException e) {
                model.addAttribute(CommonConstants.ERROR, e.getMessage());
            }

        }
        model.addAttribute("sqlcommand", command);
        model.addAttribute("sqlparameters", parameters);
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "execsql.site", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(model)));
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @author Qicz
     *
     * @param site
     * @param admin
     * @param command
     * @param parameters
     * @param request
     * @param model
     * @return
     * @since 2021/6/4 13:59
     */
    @RequestMapping(value = "execScript")
    @Csrf
    public String execScript(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String command, String[] parameters,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.errorCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        String message = null;
        try {
            message = scriptComponent.execute(command, parameters, 1);
        } catch (IOException | InterruptedException e) {
            message = e.getMessage();
            Thread.currentThread().interrupt();
        }
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "execscript.site", RequestUtils.getIpAddress(request), CommonUtils.getDate(), message));
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param model
     * @return view name
     */
    @RequestMapping({ "sitefile.html", "sitefile" })
    public String sitefile(ModelMap model) {
        model.addAttribute("list",
                CmsFileUtils.getFileList(siteComponent.getSiteFilePath(Constants.BLANK), CmsFileUtils.ORDERFIELD_MODIFIEDDATE));
        return "sysSite/sitefile";
    }

    /**
     * @param model
     * @return view name
     */
    @RequestMapping({ "lookupSitefile.html", "lookupSitefile" })
    public String lookupSitefile(ModelMap model) {
        model.addAttribute("list",
                CmsFileUtils.getFileList(siteComponent.getSiteFilePath(Constants.BLANK), CmsFileUtils.ORDERFIELD_MODIFIEDDATE));
        return "sysSite/lookupSitefile";
    }

    /**
     * @param site
     * @param file
     * @param model
     * @return view name
     */
    @PostMapping("doUploadLicense")
    @Csrf
    public String upload(@RequestAttribute SysSite site, MultipartFile file, ModelMap model) {
        if (ControllerUtils.errorCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null != file && !file.isEmpty()) {
            try {
                CmsFileUtils.upload(file, CommonUtils.joinString(siteComponent.getRootPath(), CommonConstants.LICENSE_FILENAME));
                return CommonConstants.TEMPLATE_DONE;
            } catch (IllegalStateException | IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return CommonConstants.TEMPLATE_ERROR;
    }

    /**
     * @param site
     * @param admin
     * @param request
     * @return view name
     */
    @RequestMapping("reCreateIndex")
    @Csrf
    public String reCreateIndex(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, HttpServletRequest request) {
        hqlService.reCreateIndex();
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "reCreateIndex", RequestUtils.getIpAddress(request), CommonUtils.getDate(), Constants.BLANK));
        return CommonConstants.TEMPLATE_DONE;
    }
}