package com.publiccms.controller.admin.cms;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsSurvey;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsSurveyService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;

import jakarta.annotation.Resource;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * CmsSurveyAdminController
 * 
 */
@Controller
@RequestMapping("cmsSurvey")
public class CmsSurveyAdminController {

    private String[] ignoreProperties = new String[] { "id", "siteId", "votes", "createDate", "disabled" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param request
     * @return operate result
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, CmsSurvey entity,
            HttpServletRequest request) {
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.cmsSurvey", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            entity.setSiteId(site.getId());
            entity.setUserId(admin.getId());
            entity.setVotes(0);
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.cmsSurvey", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param site
     * @param admin
     * @return operate result
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request) {
        if (CommonUtils.notEmpty(ids)) {
            service.delete(ids);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.cmsSurvey", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, Constants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Resource
    private CmsSurveyService service;
    @Resource
    protected LogOperateService logOperateService;
}