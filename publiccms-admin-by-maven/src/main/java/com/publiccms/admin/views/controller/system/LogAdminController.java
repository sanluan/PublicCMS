package com.publiccms.admin.views.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.logic.service.log.LogEmailCheckService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogTaskService;
import com.sanluan.common.base.BaseController;

@Controller
public class LogAdminController extends BaseController {
    @Autowired
    private LogEmailCheckService logEmailCheckService;
    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private LogOperateService logOperateService;
    @Autowired
    private LogTaskService logTaskService;

    @RequestMapping(value = { "logEmailCheck/delete" })
    public String logEmailCheckDelete(Integer[] ids) {
        if (notEmpty(ids)) {
            for (Integer id : ids) {
                logEmailCheckService.delete(id);
            }
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = { "logLogin/delete" })
    public String logLoginDelete(Integer[] ids) {
        if (notEmpty(ids)) {
            for (Integer id : ids) {
                logLoginService.delete(id);
            }
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = { "logOperate/delete" })
    public String logOperateDelete(Integer[] ids) {
        if (notEmpty(ids)) {
            for (Integer id : ids) {
                logOperateService.delete(id);
            }
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = { "logTask/delete" })
    public String logTaskDelete(Integer[] ids) {
        if (notEmpty(ids)) {
            for (Integer id : ids) {
                logTaskService.delete(id);
            }
        }
        return TEMPLATE_DONE;
    }
}