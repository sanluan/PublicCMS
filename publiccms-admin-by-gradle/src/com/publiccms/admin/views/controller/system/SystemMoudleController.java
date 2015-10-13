package com.publiccms.admin.views.controller.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;
import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.system.SystemMoudle;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.system.SystemMoudleService;

@Controller
@RequestMapping("systemMoudle")
public class SystemMoudleController extends BaseController {
    @Autowired
    private SystemMoudleService service;
    @Autowired
    private LogOperateService logOperateService;

    @RequestMapping(SAVE)
    public String save(SystemMoudle entity, HttpServletRequest request, HttpSession session) {
        if (notEmpty(entity.getId())) {
            entity = service.update(entity.getId(), entity, new String[] { ID });
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "update.moudle",
                        RequestUtils.getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
            }
        } else {
            entity = service.save(entity);
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "save.moudle", RequestUtils
                        .getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
            }
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(DELETE)
    public String delete(Integer id, HttpServletRequest request, HttpSession session) {
        SystemMoudle entity = service.delete(id);
        if (notEmpty(entity)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "delete.dept", RequestUtils
                    .getIp(request), getDate(), id + ":" + entity.getName()));
        }
        return TEMPLATE_DONE;
    }
}