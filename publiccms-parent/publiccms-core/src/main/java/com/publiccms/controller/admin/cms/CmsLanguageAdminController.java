package com.publiccms.controller.admin.cms;

import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsLanguage;
import com.publiccms.entities.cms.CmsLanguageId;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.cms.CmsLanguageService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;

/**
 *
 * CmsLanguageAdminController
 * 
 */
@Controller
@RequestMapping("cmsLanguage")
public class CmsLanguageAdminController {
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param code
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsLanguage entity, String code,
            HttpServletRequest request, ModelMap model) {
        if (null != entity.getId()) {
            entity.getId().setSiteId(site.getId());
            CmsLanguage oldEntity = service.getEntity(new CmsLanguageId(code, site.getId()));
            if (null == oldEntity) {
                service.save(entity);
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "save.language", RequestUtils.getIpAddress(request),
                        CommonUtils.now(), JsonUtils.getString(entity)));
            } else {
                if (ControllerUtils.errorNotEquals("siteId", site.getId(), oldEntity.getId().getSiteId(), model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
                if (CommonUtils.notEmpty(code) && code.equals(entity.getId().getCode())) {
                    entity = service.update(oldEntity.getId(), entity, ignoreProperties);
                } else {
                    service.delete(oldEntity.getId());
                    service.save(entity);
                }
                if (null != entity) {
                    logOperateService.save(new LogOperate(entity.getId().getSiteId(), admin.getId(), admin.getDeptId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, "update.language", RequestUtils.getIpAddress(request),
                            CommonUtils.now(), JsonUtils.getString(entity)));
                }
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param entity
     * @param oldCode
     * @return view name
     */
    @RequestMapping("virify")
    @ResponseBody
    public boolean virify(@RequestAttribute SysSite site, CmsLanguage entity, String oldCode) {
        if (null != entity.getId() && CommonUtils.notEmpty(entity.getId().getCode())) {
            entity.getId().setSiteId(site.getId());
            if (CommonUtils.notEmpty(oldCode) && !entity.getId().getCode().equals(oldCode)
                    && null != service.getEntity(entity.getId())
                    || CommonUtils.empty(oldCode) && null != service.getEntity(entity.getId())) {
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
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String[] ids,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(ids)) {
            CmsLanguageId[] languageIds = Stream.of(ids).map(e -> new CmsLanguageId(e, site.getId()))
                    .toArray(CmsLanguageId[]::new);
            service.delete(languageIds);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.language", RequestUtils.getIpAddress(request),
                    CommonUtils.now(), StringUtils.join(ids, Constants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Resource
    private CmsLanguageService service;
}