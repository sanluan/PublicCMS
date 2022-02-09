package com.publiccms.controller.admin.sys;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

// Generated 2021-8-2 11:31:34 by com.publiccms.common.generator.SourceGenerator

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.DatabaseUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDatasource;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysSiteDatasource;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.DatasourceComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDatasourceService;
import com.publiccms.logic.service.sys.SysSiteDatasourceService;

/**
 *
 * SysDatasourceAdminController
 * 
 */
@Controller
@RequestMapping("sysDatasource")
public class SysDatasourceAdminController {
    private String[] ignoreProperties = new String[] { "name" };
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    protected DatasourceComponent datasourceComponent;
    @Autowired
    private SysSiteDatasourceService siteDatasourceService;

    /**
     * @param site
     * @param admin
     * @param entity
     * @param url
     * @param driverClassName
     * @param username
     * @param password
     * @param minPoolSize
     * @param maxPoolSize
     * @param initDatabase
     * @param siteIds
     * @param request
     * @param model
     * @return operate result
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysDatasource entity, String url,
            String driverClassName, String username, String password, Integer minPoolSize, Integer maxPoolSize,
            boolean initDatabase, Short[] siteIds, HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.errorCustom("noright", !siteComponent.isMaster(site.getId()), model)
                || ControllerUtils.errorCustom("needAuthorizationEdition", !CmsVersion.isAuthorizationEdition(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        try (Connection connection = DatabaseUtils.getConnection(driverClassName, url, username, password)) {
            Properties dbconfig = PropertiesLoaderUtils.loadAllProperties(CmsDataSource.DATABASE_CONFIG_TEMPLATE);
            dbconfig.setProperty("hikariCP.minPoolSize", minPoolSize.toString());
            dbconfig.setProperty("hikariCP.maxPoolSize", maxPoolSize.toString());
            dbconfig.setProperty("jdbc.url", url);
            dbconfig.setProperty("jdbc.driverClassName", driverClassName);
            dbconfig.setProperty("jdbc.username", username);
            dbconfig.setProperty("jdbc.encryptPassword",
                    VerificationUtils.base64Encode(VerificationUtils.encrypt(password, CommonConstants.ENCRYPT_KEY)));
            StringWriter writer = new StringWriter();
            dbconfig.store(writer, null);
            entity.setConfig(writer.toString());
            if (null != service.getEntity(entity.getName())) {
                entity.setDisabled(false);
                entity.setUpdateDate(CommonUtils.getDate());
                service.update(entity.getName(), entity, ignoreProperties);
                logOperateService.save(
                        new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER, "update.datasource",
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            } else {
                service.save(entity);
                logOperateService
                        .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.datasource",
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
            siteDatasourceService.dealSiteDataSources(siteIds, entity.getName());
            datasourceComponent.sync(initDatabase, connection, entity, siteIds);
            datasourceComponent.clear(site.getId());
            return CommonConstants.TEMPLATE_DONE;
        } catch (ClassNotFoundException | SQLException | IOException | PropertyVetoException e) {
            model.addAttribute(CommonConstants.ERROR, e.getMessage());
            return CommonConstants.TEMPLATE_ERROR;
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
    @RequestMapping(value = "sync", method = RequestMethod.POST)
    @Csrf
    public String sync(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String id, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.errorCustom("noright", !siteComponent.isMaster(site.getId()), model)
                || ControllerUtils.errorCustom("needAuthorizationEdition", !CmsVersion.isAuthorizationEdition(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysDatasource entity = service.getEntity(id);
        if (null != entity) {
            List<SysSiteDatasource> list = siteDatasourceService.getList(null, id);
            List<Short> siteIdList = list.stream().map(s -> s.getId().getSiteId()).collect(Collectors.toList());
            try {
                datasourceComponent.sync(false, null, entity, siteIdList.toArray(new Short[siteIdList.size()]));
                logOperateService
                        .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER, "sync.datasource",
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            } catch (IOException | PropertyVetoException e) {
                model.addAttribute(CommonConstants.ERROR, e.getMessage());
                return CommonConstants.TEMPLATE_ERROR;
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "disable", method = RequestMethod.POST)
    @Csrf
    public String disable(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String id, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.errorCustom("noright", !siteComponent.isMaster(site.getId()), model)
                || ControllerUtils.errorCustom("needAuthorizationEdition", !CmsVersion.isAuthorizationEdition(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysDatasource entity = service.getEntity(id);
        if (null != entity) {
            service.disabled(entity.getName());
            siteDatasourceService.deleteByDatasource(entity.getName());
            datasourceComponent.removeDatasource(site.getId(), entity.getName());
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER, "disable.datasource",
                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private SysDatasourceService service;
    @Autowired
    protected LogOperateService logOperateService;
}