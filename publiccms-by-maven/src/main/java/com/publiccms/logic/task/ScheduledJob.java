package com.publiccms.logic.task;

import java.io.IOException;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.publiccms.entities.log.LogTask;
import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.service.log.LogTaskService;
import com.publiccms.logic.service.sys.SysTaskService;

import freemarker.template.TemplateException;

/**
 * 
 * ScheduledJob 任务计划实现类
 *
 */
public class ScheduledJob implements Job {

    public static SysTaskService sysTaskService;
    public static LogTaskService logTaskService;
    public static FileComponent fileComponent;

    /* (non-Javadoc)
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date startTime = new Date();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        SysTask task = sysTaskService.getEntity((Integer) dataMap.get(ScheduledTask.ID));
        if (null != task) {
            sysTaskService.updateStatus(task.getId(), ScheduledTask.TASK_STATUS_RUNNING);
            String result;
            try {
                result = "success:" + fileComponent.dealStringTemplate(task.getContent(), null);
            } catch (IOException | TemplateException e) {
                result = "error:" + e.toString();
            }
            sysTaskService.updateStatus(task.getId(), ScheduledTask.TASK_STATUS_READY);
            LogTask entity = new LogTask(task.getId(), startTime, new Date(), result);
            logTaskService.save(entity);
        }
    }
}