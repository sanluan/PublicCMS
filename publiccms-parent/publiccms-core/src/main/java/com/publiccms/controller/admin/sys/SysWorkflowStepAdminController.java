package com.publiccms.controller.admin.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysWorkflowStep;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysWorkflowStepService;
/**
 *
 * SysWorkflowStepAdminController
 * 
 */
@Controller
@RequestMapping("sysWorkflowStep")
public class SysWorkflowStepAdminController {

    private String[] ignoreProperties = new String[]{ "id" };
    
    /**
     * @param site
     * @param admin
     * @param entity
     * @param request
     * @param model
     * @return operate result
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysWorkflowStep entity, HttpServletRequest request) {
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER, "update.sysWorkflowStep", 
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.sysWorkflowStep", 
                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param site
     * @param admin 
     * @param model
     * @return operate result
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids, HttpServletRequest request) {
        if (CommonUtils.notEmpty(ids)) {
            service.delete(ids);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER, "delete.sysWorkflowStep",
                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), StringUtils.join(ids, Constants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Resource
    private SysWorkflowStepService service;
    @Resource
    protected LogOperateService logOperateService;
}