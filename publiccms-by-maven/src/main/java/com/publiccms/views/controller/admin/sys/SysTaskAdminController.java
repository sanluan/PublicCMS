package com.publiccms.views.controller.admin.sys;

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.component.task.ScheduledTask;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysTaskService;

@Controller
@RequestMapping("sysTask")
public class SysTaskAdminController extends AbstractController {
    @Autowired
    private SysTaskService service;
    @Autowired
    private ScheduledTask scheduledTask;

    @RequestMapping("save")
    public String save(SysTask entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (notEmpty(entity.getId())) {
            SysTask oldEntity = service.getEntity(entity.getId());
            if (empty(oldEntity) || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity.setUpdateDate(getDate());
            entity = service.update(entity.getId(), entity, new String[] { "id", "siteId" });
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.task", getIpAddress(request), getDate(), entity.getId()
                                + ":" + entity.getName()));
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.task", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));

        }
        scheduledTask.create(site, entity.getId(), entity.getCronExpression());
        return TEMPLATE_DONE;
    }

    @RequestMapping("runOnce")
    public String runOnce(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysTask entity = service.getEntity(id);
        if (notEmpty(entity)) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            scheduledTask.runOnce(site, id);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "runOnce.task", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("pause")
    public String pause(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysTask entity = service.getEntity(id);
        if (notEmpty(entity)) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, ScheduledTask.TASK_STATUS_PAUSE);
            scheduledTask.pause(site, id);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "pause.task", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("resume")
    public String resume(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysTask entity = service.getEntity(id);
        if (notEmpty(entity)) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, ScheduledTask.TASK_STATUS_READY);
            scheduledTask.resume(site, id);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "resume.task", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("recreate")
    public String recreate(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysTask entity = service.getEntity(id);
        if (notEmpty(entity)) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, ScheduledTask.TASK_STATUS_READY);
            scheduledTask.create(site, entity.getId(), entity.getCronExpression());
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.task", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("status")
    @ResponseBody
    public Map<String, Integer> status(Integer[] ids, HttpServletRequest request) {
        SysSite site = getSite(request);
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (SysTask entity : service.getEntitys(ids)) {
            if (site.getId() == entity.getSiteId()) {
                map.put(entity.getId().toString(), entity.getStatus());
            }
        }
        return map;
    }

    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        SysTask entity = service.getEntity(id);
        if (notEmpty(entity)) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.delete(id);
            scheduledTask.delete(id);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.task", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }
}