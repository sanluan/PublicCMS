package org.publiccms.controller.admin.log;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.lang3.StringUtils.join;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.log.LogTaskService;
import org.publiccms.logic.service.log.LogUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * LogAdminController
 * 
 */
@Controller
public class LogAdminController extends AbstractController {
    
    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private LogTaskService logTaskService;
    @Autowired
    private LogUploadService logUploadService;

    /**
     * @param ids
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("logLogin/delete")
    public String logLoginDelete(Long[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(ids)) {
            logLoginService.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.logLogin", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("logOperate/delete")
    public String logOperateDelete(Long[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(ids)) {
            logOperateService.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.logOperate", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("logTask/delete")
    public String logTaskDelete(Long[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(ids)) {
            logTaskService.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.logTask", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("logUpload/delete")
    public String logUploadDelete(Long[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(ids)) {
            logUploadService.delete(site.getId(), ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.logUpload", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }
}