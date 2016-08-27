package com.publiccms.logic.component.task;

import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.log.LogTask;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.service.log.LogTaskService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.sys.SysTaskService;
import com.sanluan.common.base.Base;

/**
 * 
 * ScheduledTask 任务计划操作类
 *
 */
@Component
public class ScheduledTask extends Base {
	public static final String ID = "id";
	public static final int TASK_STATUS_READY = 0, TASK_STATUS_RUNNING = 1, TASK_STATUS_PAUSE = 2,
			TASK_STATUS_ERROR = 3;

	@Autowired
	private SysTaskService sysTaskService;
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private LogTaskService logTaskService;
	@Autowired
	private SysSiteService siteService;
	private Date startDate = null;

	public void init() {
		@SuppressWarnings("unchecked")
		List<SysTask> sysTaskList = (List<SysTask>) sysTaskService.getPage(null, null, startDate, null, null).getList();
		for (SysTask sysTask : sysTaskList) {
			SysSite site = siteService.getEntity(sysTask.getSiteId());
			if (TASK_STATUS_ERROR == sysTask.getStatus()) {
				sysTaskService.updateStatus(sysTask.getId(), TASK_STATUS_READY);
			}
			create(site, sysTask.getId(), sysTask.getCronExpression());
			if (TASK_STATUS_PAUSE == sysTask.getStatus()) {
				pause(site, sysTask.getId());
			}
		}
	}

	/**
	 * 创建任务计划
	 * 
	 * @param id
	 * @param cronExpression
	 */
	public void create(SysSite site, Integer id, String cronExpression) {
		if (notEmpty(id) && notEmpty(cronExpression)) {
			Date startTime = getDate();
			String taskName = getTaskName(id);
			TriggerKey triggerKey = TriggerKey.triggerKey(taskName);
			try {
				CronTrigger trigger = getCronTrigger(triggerKey);
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
						.cronSchedule(site.getId() % 60 + BLANK_SPACE + cronExpression);
				if (empty(trigger)) {
					JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class).withIdentity(taskName).build();
					jobDetail.getJobDataMap().put(ID, id);
					trigger = TriggerBuilder.newTrigger().withIdentity(taskName).withSchedule(scheduleBuilder)
							.startNow().build();
					scheduler.scheduleJob(jobDetail, trigger);
				} else {
					trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder)
							.startNow().build();
					scheduler.rescheduleJob(triggerKey, trigger);
				}
			} catch (SchedulerException e) {
				sysTaskService.updateStatus(id, TASK_STATUS_ERROR);
				logTaskService.save(new LogTask(site.getId(), id, startTime, getDate(), false, e.getMessage()));
			}
		}
	}

	/**
	 * 执行任务计划
	 * 
	 * @param id
	 */
	public void runOnce(SysSite site, Integer id) {
		if (notEmpty(id)) {
			Date startTime = getDate();
			try {
				scheduler.triggerJob(JobKey.jobKey(getTaskName(id)));
			} catch (SchedulerException e) {
				sysTaskService.updateStatus(id, TASK_STATUS_ERROR);
				logTaskService.save(new LogTask(site.getId(), id, startTime, getDate(), false, e.getMessage()));
			}
		}
	}

	/**
	 * 暂停任务计划
	 * 
	 * @param id
	 */
	public void pause(SysSite site, Integer id) {
		if (notEmpty(id)) {
			Date startTime = getDate();
			try {
				scheduler.pauseJob(JobKey.jobKey(getTaskName(id)));
			} catch (SchedulerException e) {
				sysTaskService.updateStatus(id, TASK_STATUS_ERROR);
				logTaskService.save(new LogTask(site.getId(), id, startTime, getDate(), false, e.getMessage()));
			}
		}
	}

	/**
	 * 恢复任务计划
	 * 
	 * @param id
	 */
	public void resume(SysSite site, Integer id) {
		if (notEmpty(id)) {
			Date startTime = getDate();
			try {
				scheduler.resumeJob(JobKey.jobKey(getTaskName(id)));
			} catch (SchedulerException e) {
				sysTaskService.updateStatus(id, TASK_STATUS_ERROR);
				logTaskService.save(new LogTask(site.getId(), id, startTime, getDate(), false, e.getMessage()));
			}
		}
	}

	/**
	 * 删除任务计划
	 * 
	 * @param id
	 */
	public void delete(Integer id) {
		if (notEmpty(id)) {
			try {
				scheduler.deleteJob(JobKey.jobKey(getTaskName(id)));
			} catch (SchedulerException e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * 任务计划名称
	 * 
	 * @param id
	 * @return
	 */
	public String getTaskName(Integer id) {
		return "task-" + id;
	}

	private CronTrigger getCronTrigger(TriggerKey triggerKey) throws SchedulerException {
		Trigger trigger = scheduler.getTrigger(triggerKey);
		if (notEmpty(trigger)) {
			return (CronTrigger) trigger;
		}
		return null;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
