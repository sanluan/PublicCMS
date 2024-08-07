package com.publiccms.logic.component.task;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.log.LogTask;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.service.log.LogTaskService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.sys.SysTaskService;

/**
 * 
 * ScheduledTask 任务计划操作类
 *
 */
@Component
public class ScheduledTask {
    protected final Log log = LogFactory.getLog(getClass());
    /**
     * 
     */
    public static final String ID = "id";
    /**
     * 
     */
    public static final String RUNONCE_FLAG = "RUNONCE";
    /**
     * 
     */
    public static final int TASK_STATUS_READY = 0;
    /**
     * 
     */
    public static final int TASK_STATUS_RUNNING = 1;
    /**
     * 
     */
    public static final int TASK_STATUS_PAUSE = 2;
    /**
     * 
     */
    public static final int TASK_STATUS_ERROR = 3;

    @Resource
    private SysTaskService service;
    @Autowired(required = false)
    private Scheduler scheduler;
    @Resource
    private LogTaskService logTaskService;
    @Resource
    private SysSiteService siteService;

    /**
     * @param startDate
     */
    public void init(Date startDate) {
        if (null != scheduler) {
            @SuppressWarnings("unchecked")
            List<SysTask> sysTaskList = (List<SysTask>) service.getPage(null, null, startDate, null, null).getList();
            for (SysTask sysTask : sysTaskList) {
                SysSite site = siteService.getEntity(sysTask.getSiteId());
                create(site, sysTask.getId(), sysTask.getCronExpression());
                if (TASK_STATUS_PAUSE == sysTask.getStatus()) {
                    pause(site, sysTask.getId());
                }
            }
        }
    }

    // 结束一天以前执行但是没有结果的任务，可以误杀,误杀任务后续会被修复
    public void dealNotEndTask(Date startDate) {
        List<LogTask> runingTaskList = logTaskService.getNotEndList(null, startDate);
        for (LogTask logTask : runingTaskList) {
            SysTask task = service.getEntity(logTask.getTaskId());
            if (null != task && TASK_STATUS_RUNNING == task.getStatus()) {
                service.updateStatus(task.getId(), TASK_STATUS_READY);
                logTask.setEndtime(new Date());
                logTask.setSuccess(false);
                logTaskService.update(logTask.getId(), logTask, ScheduledJob.ignoreProperties);
            }
        }
    }

    /**
     * 创建任务计划
     * 
     * @param site
     * @param id
     * @param cronExpression
     */
    public void create(SysSite site, Integer id, String cronExpression) {
        if (CommonUtils.notEmpty(id) && CommonUtils.notEmpty(cronExpression)) {
            Date startTime = CommonUtils.getDate();
            String taskName = getTaskName(id);
            TriggerKey triggerKey = TriggerKey.triggerKey(taskName);
            try {
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(
                        new CronExpression(CommonUtils.joinString(site.getId() % 60, Constants.BLANK_SPACE, cronExpression)));
                if (null == trigger) {
                    JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class).withIdentity(taskName).build();
                    jobDetail.getJobDataMap().put(ID, id);
                    trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).startNow()
                            .build();
                    scheduler.scheduleJob(jobDetail, trigger);
                } else {
                    trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).startNow()
                            .build();
                    scheduler.rescheduleJob(triggerKey, trigger);
                }
                SysTask sysTask = service.getEntity(id);
                if (null != sysTask && TASK_STATUS_ERROR == sysTask.getStatus()) {
                    service.updateStatus(sysTask.getId(), TASK_STATUS_READY);
                }
            } catch (SchedulerException | ParseException e) {
                if (service.updateStatus(id, TASK_STATUS_ERROR)) {
                    logTaskService.save(new LogTask(site.getId(), id, startTime, CommonUtils.getDate(), false, e.getMessage()));
                }
            }
        }
    }

    /**
     * 执行任务计划
     * 
     * @param site
     * @param id
     */
    public void runOnce(SysSite site, Integer id) {
        if (CommonUtils.notEmpty(id)) {
            Date startTime = CommonUtils.getDate();
            try {
                JobKey jobKey = JobKey.jobKey(getTaskName(id));
                JobDetail job = scheduler.getJobDetail(jobKey);
                if (null == job) {
                    SysTask task = service.getEntity(id);
                    if (null != task) {
                        create(site, id, task.getCronExpression());
                    }
                    job = scheduler.getJobDetail(jobKey);
                }
                if (null != job) {
                    job.getJobDataMap().put(RUNONCE_FLAG, true);
                    scheduler.triggerJob(jobKey, job.getJobDataMap());
                }
            } catch (SchedulerException e) {
                service.updateStatus(id, TASK_STATUS_ERROR);
                logTaskService.save(new LogTask(site.getId(), id, startTime, CommonUtils.getDate(), false, e.getMessage()));
            }
        }
    }

    /**
     * 暂停任务计划
     * 
     * @param site
     * @param id
     */
    public void pause(SysSite site, Integer id) {
        if (CommonUtils.notEmpty(id)) {
            Date startTime = CommonUtils.getDate();
            try {
                scheduler.pauseJob(JobKey.jobKey(getTaskName(id)));
            } catch (SchedulerException e) {
                service.updateStatus(id, TASK_STATUS_ERROR);
                logTaskService.save(new LogTask(site.getId(), id, startTime, CommonUtils.getDate(), false, e.getMessage()));
            }
        }
    }

    /**
     * 打断任务计划
     * 
     * @param site
     * @param id
     */
    public void interrupt(SysSite site, Integer id) {
        if (CommonUtils.notEmpty(id)) {
            Date startTime = CommonUtils.getDate();
            try {
                scheduler.interrupt(JobKey.jobKey(getTaskName(id)));
            } catch (UnableToInterruptJobException e) {
                service.updateStatus(id, TASK_STATUS_RUNNING);
                logTaskService.save(new LogTask(site.getId(), id, startTime, CommonUtils.getDate(), false, e.getMessage()));
            }
        }
    }

    /**
     * 恢复任务计划
     * 
     * @param site
     * @param id
     */
    public void resume(SysSite site, Integer id) {
        if (CommonUtils.notEmpty(id)) {
            Date startTime = CommonUtils.getDate();
            try {
                scheduler.resumeJob(JobKey.jobKey(getTaskName(id)));
            } catch (SchedulerException e) {
                service.updateStatus(id, TASK_STATUS_ERROR);
                logTaskService.save(new LogTask(site.getId(), id, startTime, CommonUtils.getDate(), false, e.getMessage()));
            }
        }
    }

    /**
     * 删除任务计划
     * 
     * @param id
     */
    public void delete(Integer id) {
        if (CommonUtils.notEmpty(id)) {
            try {
                scheduler.deleteJob(JobKey.jobKey(getTaskName(id)));
            } catch (SchedulerException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 任务计划名称
     * 
     * @param id
     * @return task name
     */
    public String getTaskName(Integer id) {
        return CommonUtils.joinString("task-", id);
    }

    @PreDestroy
    public void destroy() {
        try {
            if (scheduler.isStarted()) {
                scheduler.shutdown(true);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
}
