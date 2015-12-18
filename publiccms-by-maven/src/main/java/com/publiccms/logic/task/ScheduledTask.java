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

import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.service.log.LogTaskService;
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
    public static final int TASK_STATUS_READY = 0, TASK_STATUS_RUNNING = 1, TASK_STATUS_PAUSE = 2, TASK_STATUS_ERROR = 3;

    @Autowired
    private SysTaskService sysTaskService;
    @Autowired
    private Scheduler scheduler;

    @Autowired
    public void init(SysTaskService sysTaskService, LogTaskService logTaskService, FileComponent fileComponent) {
        ScheduledJob.logTaskService = logTaskService;
        ScheduledJob.sysTaskService = sysTaskService;
        ScheduledJob.fileComponent = fileComponent;

        @SuppressWarnings("unchecked")
        List<SysTask> sysTaskList = (List<SysTask>) sysTaskService.getPage(TASK_STATUS_READY, null, null).getList();
        for (SysTask sysTask : sysTaskList) {
            create(sysTask.getId(), sysTask.getCronExpression());
        }
    }

    /**
     * 创建任务计划
     * 
     * @param id
     * @param cronExpression
     */
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
                    trigger = TriggerBuilder.newTrigger().withIdentity(taskName).withSchedule(scheduleBuilder).startNow().build();
                    scheduler.scheduleJob(jobDetail, trigger);
                } else {
                    trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).startNow()
                            .build();
                    scheduler.rescheduleJob(triggerKey, trigger);
                }
            } catch (SchedulerException e) {
                log.error(e.getMessage());
                sysTaskService.updateStatus(id, TASK_STATUS_ERROR);
            }
        }
    }

    /**
     * 执行任务计划
     * 
     * @param id
     */
    public void runOnce(Integer id) {
        try {
            if (notEmpty(id)) {
                scheduler.triggerJob(JobKey.jobKey(getTaskName(id)));
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            sysTaskService.updateStatus(id, TASK_STATUS_ERROR);
        }
    }

    /**
     * 暂停任务计划
     * 
     * @param id
     */
    public void pause(Integer id) {
        try {
            if (notEmpty(id)) {
                scheduler.pauseJob(JobKey.jobKey(getTaskName(id)));
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            sysTaskService.updateStatus(id, TASK_STATUS_ERROR);
        }
    }

    /**
     * 恢复任务计划
     * 
     * @param id
     */
    public void resume(Integer id) {
        try {
            if (notEmpty(id)) {
                scheduler.resumeJob(JobKey.jobKey(getTaskName(id)));
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            sysTaskService.updateStatus(id, TASK_STATUS_ERROR);
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

    private CronTrigger getCronTrigger(TriggerKey triggerKey) {
        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (null != trigger) {
                return (CronTrigger) trigger;
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
