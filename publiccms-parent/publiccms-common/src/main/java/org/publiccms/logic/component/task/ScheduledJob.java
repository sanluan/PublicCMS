package org.publiccms.logic.component.task;

import static com.publiccms.common.tools.FreeMarkerUtils.generateStringByFile;
import static org.publiccms.common.base.AbstractFreemarkerView.exposeSite;
import static org.publiccms.logic.component.site.SiteComponent.getFullFileName;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.publiccms.entities.log.LogTask;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysTask;
import org.publiccms.logic.component.template.TemplateComponent;
import org.publiccms.logic.service.log.LogTaskService;
import org.publiccms.logic.service.sys.SysSiteService;
import org.publiccms.logic.service.sys.SysTaskService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import freemarker.template.TemplateException;

/**
 * 
 * ScheduledJob 任务计划实现类
 *
 */
public class ScheduledJob extends QuartzJobBean {
    private static String[] ignoreProperties = new String[] { "id", "begintime", "taskId", "siteId" };

    private static SysTaskService sysTaskService;
    private static LogTaskService logTaskService;
    private static SysSiteService siteService;
    private static ScheduledTask scheduledTask;
    private static TemplateComponent templateComponent;

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Integer taskId = (Integer) context.getJobDetail().getJobDataMap().get(ScheduledTask.ID);
        SysTask task = sysTaskService.getEntity(taskId);
        if (null != task) {
            if (ScheduledTask.TASK_STATUS_READY == task.getStatus() && sysTaskService.updateStatusToRunning(task.getId())) {
                LogTask entity = new LogTask(task.getSiteId(), task.getId(), new Date(), false);
                logTaskService.save(entity);
                boolean success = false;
                String result;
                try {
                    success = true;
                    Map<String, Object> map = new HashMap<>();
                    map.put("task", task);
                    SysSite site = siteService.getEntity(task.getSiteId());
                    exposeSite(map, site);
                    result = generateStringByFile(getFullFileName(site, task.getFilePath()),
                            templateComponent.getTaskConfiguration(), map);
                } catch (IOException | TemplateException e) {
                    result = e.getMessage();
                }
                entity.setEndtime(new Date());
                entity.setSuccess(success);
                entity.setResult(result);
                logTaskService.update(entity.getId(), entity, ignoreProperties);
                sysTaskService.updateStatus(task.getId(), ScheduledTask.TASK_STATUS_READY);

            }
        } else {
            scheduledTask.delete(taskId);
        }
    }

    /**
     * @param sysTaskService
     */
    public static void setSysTaskService(SysTaskService sysTaskService) {
        ScheduledJob.sysTaskService = sysTaskService;
    }

    /**
     * @param logTaskService
     */
    public static void setLogTaskService(LogTaskService logTaskService) {
        ScheduledJob.logTaskService = logTaskService;
    }

    /**
     * @param siteService
     */
    public static void setSiteService(SysSiteService siteService) {
        ScheduledJob.siteService = siteService;
    }

    /**
     * @param scheduledTask
     */
    public static void setScheduledTask(ScheduledTask scheduledTask) {
        ScheduledJob.scheduledTask = scheduledTask;
    }

    /**
     * @param templateComponent
     */
    public static void setTemplateComponent(TemplateComponent templateComponent) {
        ScheduledJob.templateComponent = templateComponent;
    }

}