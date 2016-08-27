package com.publiccms.logic.component.task;

import static com.publiccms.logic.component.SiteComponent.expose;
import static com.publiccms.logic.component.SiteComponent.getFullFileName;
import static com.sanluan.common.tools.FreeMarkerUtils.makeStringByFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.log.LogTask;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.component.SiteComponent;
import com.publiccms.logic.component.TemplateComponent;
import com.publiccms.logic.service.log.LogTaskService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.sys.SysTaskService;
import com.sanluan.common.base.Base;

import freemarker.template.TemplateException;

/**
 * 
 * ScheduledJob 任务计划实现类
 *
 */
@Component
public class ScheduledJob extends Base implements Job {

	public static SysTaskService sysTaskService;
	public static LogTaskService logTaskService;
	public static SysSiteService siteService;
	public static ScheduledTask scheduledTask;
	public static TemplateComponent templateComponent;
	public static SiteComponent siteComponent;

	@Autowired
	public void init(LogTaskService logTaskService, SysSiteService siteService, SysTaskService sysTaskService,
			SiteComponent siteComponent, ScheduledTask scheduledTask, TemplateComponent templateComponent) {
		ScheduledJob.logTaskService = logTaskService;
		ScheduledJob.sysTaskService = sysTaskService;
		ScheduledJob.siteService = siteService;
		ScheduledJob.templateComponent = templateComponent;
		ScheduledJob.siteComponent = siteComponent;
		ScheduledJob.scheduledTask = scheduledTask;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Integer taskId = (Integer) context.getJobDetail().getJobDataMap().get(ScheduledTask.ID);
		SysTask task = sysTaskService.getEntity(taskId);
		if (notEmpty(task)) {
			if (ScheduledTask.TASK_STATUS_READY == task.getStatus()
					&& sysTaskService.updateStatusToRunning(task.getId())) {
				LogTask entity = new LogTask(task.getSiteId(), task.getId(), getDate(), false);
				logTaskService.save(entity);
				boolean success = false;
				String result;
				try {
					success = true;
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("task", task);
					SysSite site = siteService.getEntity(task.getSiteId());
					expose(map, site);
					result = makeStringByFile(getFullFileName(site, task.getFilePath()),
							templateComponent.getTaskConfiguration(), map);
				} catch (IOException | TemplateException e) {
					result = e.getMessage();
				}
				entity.setEndtime(getDate());
				entity.setSuccess(success);
				entity.setResult(result);
				logTaskService.update(entity.getId(), entity, new String[] { "id", "begintime", "taskId", "siteId" });
				sysTaskService.updateStatus(task.getId(), ScheduledTask.TASK_STATUS_READY);

			}
		} else {
			scheduledTask.delete(taskId);
		}
	}
}