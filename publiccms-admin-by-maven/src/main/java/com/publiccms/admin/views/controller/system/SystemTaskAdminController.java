package com.publiccms.admin.views.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.system.SystemTask;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.system.SystemTaskService;
import com.publiccms.logic.task.ScheduledTask;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;

@Controller
@RequestMapping("systemTask")
public class SystemTaskAdminController extends BaseController {
	@Autowired
	private SystemTaskService service;
	@Autowired
	private ScheduledTask scheduledTask;
	@Autowired
	private LogOperateService logOperateService;

	@RequestMapping("save")
	public String save(SystemTask entity, HttpServletRequest request, ModelMap model) {
		if (notEmpty(entity.getId())) {
			entity = service.update(entity.getId(), entity, new String[] { "id" });
			if (notEmpty(entity)) {
				logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(request).getId(), "update.task", RequestUtils
						.getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
			}
		} else {
			entity = service.save(entity);
			if (notEmpty(entity)) {
				logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(request).getId(), "save.task", RequestUtils
						.getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
			}

		}
		scheduledTask.create(entity.getId(), entity.getCronExpression());
		return "common/ajaxDone";
	}

	@RequestMapping(value = { "runOnce" })
	public String runOnce(Integer id, HttpServletRequest request) {
		SystemTask entity = service.getEntity(id);
		if (notEmpty(entity)) {
			scheduledTask.runOnce(id);
			logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(request).getId(), "runOnce.task", RequestUtils
					.getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
		}
		return "common/ajaxDone";
	}

	@RequestMapping(value = { "pause" })
	public String pause(Integer id, HttpServletRequest request) {
		SystemTask entity = service.updateStatus(id, ScheduledTask.TASK_STATUS_PAUSE);
		if (notEmpty(entity)) {
			scheduledTask.pause(id);
			logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(request).getId(), "pause.task", RequestUtils
					.getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
		}
		return "common/ajaxDone";
	}

	@RequestMapping(value = { "resume" })
	public String resume(Integer id, HttpServletRequest request) {
		SystemTask entity = service.updateStatus(id, ScheduledTask.TASK_STATUS_READY);
		if (notEmpty(entity)) {
			scheduledTask.resume(id);
			logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(request).getId(), "resume.task", RequestUtils
					.getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
		}
		return "common/ajaxDone";
	}

	@RequestMapping("delete")
	public String delete(Integer id, HttpServletRequest request) {
		SystemTask entity = service.delete(id);
		if (notEmpty(entity)) {
			scheduledTask.delete(id);
			logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(request).getId(), "delete.task", RequestUtils
					.getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
		}
		return "common/ajaxDone";
	}
}