package com.publiccms.controller.admin.sys;

import java.util.List;

// Generated 2016-7-16 11:54:16 by com.publiccms.common.generator.SourceGenerator

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsEditorHistory;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysConfigData;
import com.publiccms.entities.sys.SysConfigDataId;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptItemId;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.CorsConfigComponent;
import com.publiccms.logic.component.site.EmailComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.cms.CmsEditorHistoryService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysConfigDataService;
import com.publiccms.logic.service.sys.SysDeptItemService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.views.pojo.model.SysConfigParameters;

/**
 *
 * SysConfigDataAdminController
 * 
 */
@Controller
@RequestMapping("sysConfigData")
public class SysConfigDataAdminController {
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    private CmsEditorHistoryService editorHistoryService;

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param sysConfigParameters
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysConfigData entity,
            @ModelAttribute SysConfigParameters sysConfigParameters, HttpServletRequest request, ModelMap model) {
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
            Map<String, String> map = ExtendUtils.getExtentDataMap(sysConfigParameters.getExtendDataList(), fieldList);
            entity.setData(ExtendUtils.getExtendString(map));
            if (null != oldEntity) {
                entity = service.update(oldEntity.getId(), entity, ignoreProperties);
                if (null != entity) {
                    logOperateService.save(
                            new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                    "update.configData", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                                    new StringBuilder(entity.getId().getCode()).append(":").append(entity.getData()).toString()));
                }

                if (CommonUtils.notEmpty(oldEntity.getData()) && CommonUtils.notEmpty(fieldList)) {
                    Map<String, String> oldMap = ExtendUtils.getExtendMap(oldEntity.getData());
                    for (SysExtendField extendField : fieldList) {
                        if (ArrayUtils.contains(Config.INPUT_TYPE_EDITORS, extendField.getInputType())) {
                            if (CommonUtils.notEmpty(oldMap) && CommonUtils.notEmpty(oldMap.get(extendField.getId().getCode()))
                                    && (CommonUtils.notEmpty(map) || !oldMap.get(extendField.getId().getCode())
                                            .equals(map.get(extendField.getId().getCode())))) {
                                CmsEditorHistory history = new CmsEditorHistory(site.getId(),
                                        CmsEditorHistoryService.ITEM_TYPE_CONFIG_DATA, entity.getId().getCode(),
                                        extendField.getId().getCode(), CommonUtils.getDate(), admin.getId(),
                                        map.get(extendField.getId().getCode()));
                                editorHistoryService.save(history);
                            }
                        }
                    }
                }

            } else {
                entity.getId().setSiteId(site.getId());
                service.save(entity);
                logOperateService
                        .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "save.configData", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                                new StringBuilder(entity.getId().getCode()).append(":").append(entity.getData()).toString()));
            }

            configComponent.removeCache(site.getId(), entity.getId().getCode());
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
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "delete.configData", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                            new StringBuilder(entity.getId().getCode()).append(":").append(entity.getData()).toString()));
            configComponent.removeCache(site.getId(), entity.getId().getCode());
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Resource
    private SysDeptItemService sysDeptItemService;
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private ConfigComponent configComponent;
    @Resource
    private CorsConfigComponent corsConfigComponent;
    @Resource
    private EmailComponent emailComponent;
    @Resource
    private SysConfigDataService service;
}