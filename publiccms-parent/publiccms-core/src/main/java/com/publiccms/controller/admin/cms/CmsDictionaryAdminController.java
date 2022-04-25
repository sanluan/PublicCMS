package com.publiccms.controller.admin.cms;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.entities.cms.CmsDictionaryId;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
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
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param oldId
     * @param parentValue
     * @param dictionaryParameters
     * @param request
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsDictionary entity, String oldId,
            String parentValue, @ModelAttribute CmsDictionaryParameters dictionaryParameters, HttpServletRequest request) {
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
     * @param ids
     * @param request
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String[] ids,
            HttpServletRequest request) {
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