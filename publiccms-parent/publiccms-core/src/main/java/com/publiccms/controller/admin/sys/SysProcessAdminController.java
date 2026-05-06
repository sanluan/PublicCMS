package com.publiccms.controller.admin.sys;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysWorkflowProcess;
import com.publiccms.entities.sys.SysWorkflowProcessHistory;
import com.publiccms.logic.component.workflow.ProcessComponent;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysWorkflowProcessHistoryService;
import com.publiccms.logic.service.sys.SysWorkflowProcessService;

import jakarta.annotation.Resource;

/**
 *
 * ProcessController
 * 
 */
@Controller
@RequestMapping("sysWorkflowProcess")
public class SysProcessAdminController {
    @Resource
    private ProcessComponent processComponent;

    /**
     * @param site
     * @param admin
     * @param entity
     * @return operate result
     */
    @RequestMapping("handle")
    @Csrf
    public String handle(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysWorkflowProcessHistory entity) {
        SysWorkflowProcess process = service.handleProcess(site, entity, admin);
        if (process.isClosed()) {
            if (SysWorkflowProcessHistoryService.OPERATE_AGREE.equalsIgnoreCase(entity.getOperate())) {
                processComponent.finishProcess(site, process, admin, entity);
            } else if (SysWorkflowProcessHistoryService.OPERATE_REJECT.equalsIgnoreCase(entity.getOperate())) {
                processComponent.reject(site, process, admin, entity);
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Resource
    private SysWorkflowProcessService service;
    @Resource
    protected LogOperateService logOperateService;
}