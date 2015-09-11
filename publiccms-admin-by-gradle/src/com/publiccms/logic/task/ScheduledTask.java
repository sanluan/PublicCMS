package com.publiccms.logic.task;

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

import com.publiccms.entities.system.SystemTask;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.service.log.LogTaskService;
import com.publiccms.logic.service.system.SystemTaskService;
import com.sanluan.common.base.Base;
import com.sanluan.common.servlet.TaskAfterInitServlet;

@Component
public class ScheduledTask extends Base implements TaskAfterInitServlet {
	public static final String ID = "id";
	public static final int TASK_STATUS_READY = 0, TASK_STATUS_RUNNING = 1, TASK_STATUS_PAUSE = 2, TASK_STATUS_ERROR = 3;

	@Autowired
	private SystemTaskService systemTaskService;
	@Autowired
	private LogTaskService logTaskService;
	@Autowired
	private FileComponent fileComponent;
	@Autowired
	private Scheduler scheduler;

	@Override
	public void exec() {
		ScheduledJob.logTaskService = logTaskService;
		ScheduledJob.systemTaskService = systemTaskService;
		ScheduledJob.fileComponent = fileComponent;

		@SuppressWarnings("unchecked")
		List<SystemTask> systemTaskList = (List<SystemTask>) systemTaskService.getPage(TASK_STATUS_READY, null, null).getList();
		for (SystemTask systemTask : systemTaskList) {
			create(systemTask.getId(), systemTask.getCronExpression());
		}
	}

	public void create(Integer id, String cronExpression) {
		if (notEmpty(id) && notEmpty(cronExpression)) {
			String taskName = getTaskName(id);
			TriggerKey triggerKey = TriggerKey.triggerKey(taskName);
			CronTrigger trigger = getCronTrigger(triggerKey);
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			try {
				if (null == trigger) {
					JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class).withIdentity(taskName).build();
					jobDetail.getJobDataMap().put(ID, id);
					trigger = TriggerBuilder.newTrigger().withIdentity(taskName).withSchedule(scheduleBuilder).build();
					scheduler.scheduleJob(jobDetail, trigger);
				} else {
					trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
					scheduler.rescheduleJob(triggerKey, trigger);
				}
			} catch (SchedulerException e) {
				systemTaskService.updateStatus(id, TASK_STATUS_ERROR);
			}
		}
	}

	public void runOnce(Integer id) {
		try {
			if (notEmpty(id))
				scheduler.triggerJob(JobKey.jobKey(getTaskName(id)));
		} catch (SchedulerException e) {
			systemTaskService.updateStatus(id, TASK_STATUS_ERROR);
		}
	}

	public void pause(Integer id) {
		try {
			if (notEmpty(id))
				scheduler.pauseJob(JobKey.jobKey(getTaskName(id)));
		} catch (SchedulerException e) {
			systemTaskService.updateStatus(id, TASK_STATUS_ERROR);
		}
	}

	public void resume(Integer id) {
		try {
			if (notEmpty(id))
				scheduler.resumeJob(JobKey.jobKey(getTaskName(id)));
		} catch (SchedulerException e) {
			systemTaskService.updateStatus(id, TASK_STATUS_ERROR);
		}
	}

	public void delete(Integer id) {
		if (notEmpty(id)) {
			try {
				scheduler.deleteJob(JobKey.jobKey(getTaskName(id)));
			} catch (SchedulerException e) {
			}
		}
	}

	public String getTaskName(Integer id) {
		return "task-" + id;
	}

	private CronTrigger getCronTrigger(TriggerKey triggerKey) {
		try {
			Trigger trigger = scheduler.getTrigger(triggerKey);
			if (null != trigger)
				return (CronTrigger) trigger;
		} catch (SchedulerException e) {
		}
		return null;
	}
}
