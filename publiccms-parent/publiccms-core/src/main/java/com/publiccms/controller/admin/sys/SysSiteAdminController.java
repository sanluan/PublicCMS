package com.publiccms.controller.admin.sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.logic.service.sys.SysDomainService;
import com.publiccms.logic.service.sys.SysSiteDatasourceService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.tools.HqlService;
import com.publiccms.logic.service.tools.SqlService;

/**
 *
 * SysSiteAdminController
 *
 */
@Controller
@RequestMapping("sysSite")
public class SysSiteAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private SysSiteService service;
    @Autowired
    private SysDomainService domainService;
    @Autowired
    private SqlService sqlService;
    @Autowired
    private HqlService hqlService;
    @Autowired
    protected LogUploadService logUploadService;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    private SysSiteDatasourceService siteDatasourceService;
    @Autowired
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param site
     * @param admin
     * @param entity
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
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysSite entity, String domain,
            Boolean wild, Boolean multiple, String roleName, String deptName, String userName, String password, String encoding,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.errorCustom("noright", !siteComponent.isMaster(site.getId()), model)
                || ControllerUtils.errorCustom("needAuthorizationEdition", !CmsVersion.isAuthorizationEdition(), model)
                || (null != domain
                        && ControllerUtils.errorCustom("unauthorizedDomain", !CmsVersion.verifyDomain(domain), model))) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null == entity.getDynamicPath()) {
            entity.setDynamicPath(CommonConstants.SEPARATOR);
        } else if (!entity.getDynamicPath().endsWith(CommonConstants.SEPARATOR)) {
            entity.setDynamicPath(entity.getDynamicPath() + CommonConstants.SEPARATOR);
        }
        if (null == entity.getSitePath()) {
            entity.setSitePath(CommonConstants.SEPARATOR);
        } else if (!entity.getSitePath().endsWith(CommonConstants.SEPARATOR)) {
            entity.setSitePath(entity.getSitePath() + CommonConstants.SEPARATOR);
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
            service.save(entity, domain, null == wild ? false : wild, null == multiple ? false : multiple, roleName, deptName,
                    userName, password, encoding);
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.site", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
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
            siteDatasourceService.deleteBySiteId(entity.getId());
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.site", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;

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
        if ("update_url".contains(command)) {
            if (null != parameters && 2 == parameters.length) {
                try {
                    String oldurl = parameters[0];
                    String newurl = parameters[1];
                    int i = sqlService.updateContentAttribute(oldurl, newurl);
                    i += sqlService.updateContentRelated(oldurl, newurl);
                    i += sqlService.updatePlace(oldurl, newurl);
                    i += sqlService.updatePlaceAttribute(oldurl, newurl);
                    i += sqlService.updateCategoryAttribute(oldurl, newurl);
                    model.addAttribute("result", i);
                } catch (Exception e) {
                    model.addAttribute("error", e.getMessage());
                }
            }
        }
        model.addAttribute("sqlcommand", command);
        model.addAttribute("sqlparameters", parameters);
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "execsql.site", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(model)));
        return CommonConstants.TEMPLATE_DONE;
    }

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_\\-\\.]{1,191}$");

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
        String log = null;
        if ("sync.bat".equalsIgnoreCase(command) || "sync.sh".equalsIgnoreCase(command)
                || "backupDB.bat".equalsIgnoreCase(command) || "backupDB.sh".equalsIgnoreCase(command)) {
            try {
                String dir = CommonConstants.CMS_FILEPATH + "/script";
                String[] cmdarray;
                if ("backupDB.bat".equalsIgnoreCase(command) || "backupDB.sh".equalsIgnoreCase(command)) {
                    String databaseConfiFile = CommonConstants.CMS_FILEPATH + CmsDataSource.DATABASE_CONFIG_FILENAME;
                    Properties dbconfigProperties = CmsDataSource.loadDatabaseConfig(databaseConfiFile);
                    String userName = dbconfigProperties.getProperty("jdbc.username");
                    String database = dbconfigProperties.getProperty("database", "publiccms");
                    String password = dbconfigProperties.getProperty("jdbc.password");
                    String encryptPassword = dbconfigProperties.getProperty("jdbc.encryptPassword");
                    if (null != encryptPassword) {
                        password = VerificationUtils.decrypt(VerificationUtils.base64Decode(encryptPassword),
                                CommonConstants.ENCRYPT_KEY);
                    }
                    cmdarray = new String[] { database, userName, password };
                } else {
                    cmdarray = new String[parameters.length];
                    if (null != parameters) {
                        int i = 0;
                        for (String c : parameters) {
                            if (!PARAMETER_PATTERN.matcher(c).matches()) {
                                cmdarray[i] = "";
                            } else {
                                cmdarray[i] = c;
                            }
                            i++;
                        }
                    }
                }
                String filepath = String.format("%s/%s", dir, command);
                File script = new File(filepath);
                if (!script.exists()) {
                    FileUtils.copyInputStreamToFile(this.getClass().getResourceAsStream(String.format("/script/%s", command)),
                            script);
                }
                if (command.toLowerCase().endsWith(".sh")) {
                    cmdarray = ArrayUtils.insert(0, cmdarray, filepath);
                    cmdarray = ArrayUtils.insert(0, cmdarray, "sh");
                } else {
                    cmdarray = ArrayUtils.insert(0, cmdarray, filepath);
                }
                Process ps = Runtime.getRuntime().exec(cmdarray, null, new File(dir));
                ps.waitFor();
                BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                log = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                log = e.toString();
            } finally {
                logOperateService
                        .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "execscript.site", RequestUtils.getIpAddress(request), CommonUtils.getDate(), log));
            }
        }
        return CommonConstants.TEMPLATE_DONE;

    }

    /**
     * @param site
     * @param admin
     * @param file
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doUploadLicense", method = RequestMethod.POST)
    @Csrf
    public String upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.errorCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null != file && !file.isEmpty()) {
            try {
                CmsFileUtils.upload(file, siteComponent.getRootPath() + CommonConstants.LICENSE_FILENAME);
                logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "license.dat", CmsFileUtils.FILE_TYPE_OTHER, file.getSize(), null, null,
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), CommonConstants.LICENSE_FILENAME));
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
                "reCreateIndex", RequestUtils.getIpAddress(request), CommonUtils.getDate(), CommonConstants.BLANK));
        return CommonConstants.TEMPLATE_DONE;
    }
}