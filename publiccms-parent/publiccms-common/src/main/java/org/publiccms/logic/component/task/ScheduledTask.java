package org.publiccms.logic.component.task;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.logging.LogFactory.getLog;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.publiccms.entities.log.LogTask;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysTask;
import org.publiccms.logic.component.template.TemplateComponent;
import org.publiccms.logic.service.log.LogTaskService;
import org.publiccms.logic.service.sys.SysSiteService;
import org.publiccms.logic.service.sys.SysTaskService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.Base;

/**
 * 
 * ScheduledTask 任务计划操作类
 *
 */
@Component
public class ScheduledTask implements Base {
    protected final Log log = getLog(getClass());
    /**
     * 
     */
    public static final String ID = "id";
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

    @Autowired
    private SysTaskService sysTaskService;
    @Autowired(required = false)
    private Scheduler scheduler;
    @Autowired
    private LogTaskService logTaskService;
    @Autowired
    private SysSiteService siteService;
    @Autowired
    private TemplateComponent templateComponent;

    /**
     * @param startDate
     */
    public void init(Date startDate) {
        if (null != scheduler) {
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
            ScheduledJob.setSysTaskService(sysTaskService);
            ScheduledJob.setLogTaskService(logTaskService);
            ScheduledJob.setSiteService(siteService);
            ScheduledJob.setScheduledTask(this);
            ScheduledJob.setTemplateComponent(templateComponent);
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
        if (notEmpty(id) && notEmpty(cronExpression)) {
            Date startTime = getDate();
            String taskName = getTaskName(id);
            TriggerKey triggerKey = TriggerKey.triggerKey(taskName);
            try {
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                        .cronSchedule(site.getId() % 60 + BLANK_SPACE + cronExpression);
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
            } catch (SchedulerException e) {
                sysTaskService.updateStatus(id, TASK_STATUS_ERROR);
                logTaskService.save(new LogTask(site.getId(), id, startTime, getDate(), false, e.getMessage()));
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
     * @param site
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
     * @param site
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
}
