package com.publiccms.controller.admin.log;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogTaskService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysUserService;

/**
 *
 * LogAdminController
 * 
 */
@Controller
public class LogAdminController {

    @Resource
    private LogLoginService logLoginService;
    @Resource
    private LogTaskService logTaskService;
    @Resource
    private LogUploadService logUploadService;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysDeptService sysDeptService;

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @return view name
     */
    @RequestMapping("logLogin/delete")
    @Csrf
    public String logLoginDelete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(ids)) {
            logLoginService.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.logLogin", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @return view name
     */
    @RequestMapping("logTask/delete")
    @Csrf
    public String logTaskDelete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(ids)) {
            logTaskService.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.logTask", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param ids
     * @param request
     * @return view name
     */
    @RequestMapping("logUpload/delete")
    @Csrf
    public String logUploadDelete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long[] ids,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(ids)) {
            logUploadService.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.logUpload", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), StringUtils.join(ids, CommonConstants.COMMA)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}