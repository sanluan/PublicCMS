package com.publiccms.controller.admin.cms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param dictionaryParameters
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsDictionary entity,
            CmsDictionaryParameters dictionaryParameters, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (null != entity.getId()) {
            entity.getId().setSiteId(site.getId());
            if (null != service.getEntity(entity.getId())) {
                entity = service.update(entity.getId(), entity, ignoreProperties);
                dataService.update(site.getId(), entity.getId().getId(), dictionaryParameters.getDataList());
                logOperateService.save(
                        new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "update.cmsDictionary",
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            } else {
                entity.getId().setSiteId(site.getId());
                service.save(entity);
                dataService.save(site.getId(), entity.getId().getId(), dictionaryParameters.getDataList());
                logOperateService.save(
                        new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.cmsDictionary",
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param request
     * @param id
     * @param oldId
     * @return view name
     */
    @RequestMapping("virify")
    @ResponseBody
    public boolean virify(@RequestAttribute SysSite site, HttpServletRequest request, String id, String oldId) {
        if (CommonUtils.notEmpty(id)) {
            CmsDictionaryId entityId = new CmsDictionaryId(id, site.getId());
            if (CommonUtils.notEmpty(oldId) && !id.equals(oldId) && null != service.getEntity(entityId)
                    || CommonUtils.empty(oldId) && null != service.getEntity(entityId)) {
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
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String[] ids,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(ids)) {
            CmsDictionaryId[] entityIds = new CmsDictionaryId[ids.length];
            for (int i = 0; i < ids.length; i++) {
                entityIds[i] = new CmsDictionaryId(ids[i], site.getId());
            }
            service.delete(entityIds);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "delete.cmsDictionary", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private CmsDictionaryService service;
    @Autowired
    private CmsDictionaryDataService dataService;
}