package com.publiccms.admin.views.controller.cms;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.service.log.LogOperateService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;

@Controller
@RequestMapping("templateData")
public class CmsTemplateDataController extends BaseController {
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private LogOperateService logOperateService;

    @RequestMapping(value = SAVE, method = RequestMethod.POST)
    public String save(String path, Long createDate, HttpServletRequest request, HttpSession session)
            throws IllegalStateException, IOException {
        Map<String, Object> data = getData(request);
        if (null == createDate) {
            synchronized (this) {
                data.put("createDate", System.currentTimeMillis());
            }
            fileComponent.saveData(path, data);
            if (notEmpty(path)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "save.template.data",
                        RequestUtils.getIp(request), getDate(), path));
            }
        } else {
            data.put("createDate", createDate);
            fileComponent.updateData(path, createDate, data);
            if (notEmpty(path)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "update.template.data",
                        RequestUtils.getIp(request), getDate(), path));
            }
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(String path, Long createDate, HttpServletRequest request, HttpSession session)
            throws IllegalStateException, IOException {
        Map<String, Object> data = getData(request);
        data.put("createDate", System.currentTimeMillis());
        fileComponent.saveMapData(path, data);
        if (notEmpty(path)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "update.template.data",
                    RequestUtils.getIp(request), getDate(), path));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(DELETE)
    public String delete(String path, Long createDate, HttpServletRequest request, HttpSession session) {
        try {
            fileComponent.deleteData(path, createDate);
            if (notEmpty(path)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "delete.template.data",
                        RequestUtils.getIp(request), getDate(), path));
            }
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return TEMPLATE_DONE;
    }

    private Map<String, Object> getData(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        Enumeration<String> parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String paramterName = parameters.nextElement();
            data.put(paramterName, request.getParameter(paramterName));
        }
        data.remove("path");
        data.remove("callbackType");
        return data;
    }
}
