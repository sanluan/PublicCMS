package com.publiccms.admin.views.controller.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.FileComponent.FileInfo;
import com.publiccms.logic.service.log.LogOperateService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;

@Controller
@RequestMapping("cmsTemplate")
public class CmsTemplateController extends BaseController {
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private LogOperateService logOperateService;

    @RequestMapping(SAVE)
    public String save(String path, String content, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (virifyCustom("template.save", !fileComponent.saveContent(path, content), model)) {
            return TEMPLATE_ERROR;
        }
        if (notEmpty(path)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "update.template", RequestUtils
                    .getIp(request), getDate(), path));
        }
        return staticPage(path, request, session);
    }

    @RequestMapping(value = { "create" })
    public String create(String path, String content, HttpServletRequest request, HttpSession session, ModelMap model) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keywords", request.getParameter("keywords"));
        map.put("description", request.getParameter("description"));
        map.put("alias", request.getParameter("alias"));
        map.put("path", request.getParameter("targetpath"));
        if (virifyCustom("template.save", !fileComponent.createPage(path, content), model)) {
            return TEMPLATE_ERROR;
        }
        fileComponent.updateMetadata(path, map);
        if (notEmpty(path)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "save.template", RequestUtils
                    .getIp(request), getDate(), path));
        }
        return staticPage(path, request, session);
    }

    @RequestMapping(DELETE)
    public String delete(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (virifyCustom("template.save", !fileComponent.deletePage(path), model)) {
            return TEMPLATE_ERROR;
        }
        if (notEmpty(path)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "delete.template", RequestUtils
                    .getIp(request), getDate(), path));
        }
        return staticPage(path, request, session);
    }

    @RequestMapping(value = { "update" })
    public String update(String path, HttpServletRequest request, HttpSession session, ModelMap model) {
        Map<String, Object> map = fileComponent.getTemplateMetadata(path);
        map.put("keywords", request.getParameter("keywords"));
        map.put("description", request.getParameter("description"));
        if (null != request.getParameter("alias")) {
            map.put("alias", request.getParameter("alias"));
        }
        if (null != request.getParameter("targetpath")) {
            map.put("path", request.getParameter("targetpath"));
        }
        fileComponent.updateMetadata(path, map);
        if (notEmpty(path)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "update.template.meta",
                    RequestUtils.getIp(request), getDate(), path));
        }
        return staticPage(path, request, session);
    }

    @RequestMapping(value = { "staticPlace" })
    public String staticPlace(String path, HttpServletRequest request, HttpSession session) {
        fileComponent.staticPlace(path);
        if (notEmpty(path)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "static", RequestUtils
                    .getIp(request), getDate(), path));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = { "staticPage" })
    public String staticPage(String path, HttpServletRequest request, HttpSession session) {
        List<FileInfo> list = fileComponent.getFileList(path, true);
        for (FileInfo fileInfo : list) {
            fileComponent.staticPlace(FileComponent.INCLUDE_DIR + path + "/" + fileInfo.getFileName());
        }
        fileComponent.staticPage(path);

        if (notEmpty(path)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "static", RequestUtils
                    .getIp(request), getDate(), path));
        }
        return TEMPLATE_DONE;
    }
}
