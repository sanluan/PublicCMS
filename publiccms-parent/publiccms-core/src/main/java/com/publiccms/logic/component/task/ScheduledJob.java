package com.publiccms.logic.component.task;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.entities.log.LogTask;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.component.BeanComponent;
import com.publiccms.logic.component.site.SiteComponent;

import freemarker.template.TemplateException;

/**
 *
 * ScheduledJob 任务计划实现类
 *
 */
public class ScheduledJob extends QuartzJobBean implements InterruptableJob {
    public static String[] ignoreProperties = new String[] { "id", "begintime", "taskId", "siteId" };
    private Thread thread;

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Integer taskId = (Integer) context.getMergedJobDataMap().get(ScheduledTask.ID);
        SysTask task = BeanComponent.getSysTaskService().getEntity(taskId);
        if (null != task) {
            thread = Thread.currentThread();
            Object flag = context.getMergedJobDataMap().get(ScheduledTask.RUNONCE);
            if (ScheduledTask.TASK_STATUS_READY == task.getStatus() && CmsVersion.isMaster() || null != flag) {
                if (null != flag) {
                    context.getMergedJobDataMap().remove(ScheduledTask.RUNONCE);
                }
                BeanComponent.getSysTaskService().updateStatus(task.getId(), ScheduledTask.TASK_STATUS_RUNNING);
                LogTask entity = new LogTask(task.getSiteId(), task.getId(), new Date(), false);
                BeanComponent.getLogTaskService().save(entity);
                boolean success = false;
                String result;
                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("object", task);
                    SysSite site = BeanComponent.getSiteService().getEntity(task.getSiteId());
                    AbstractFreemarkerView.exposeSite(map, site);
                    String templatePath = SiteComponent.getFullTemplatePath(site.getId(), task.getFilePath());
                    result = FreeMarkerUtils.generateStringByFile(templatePath,
                            BeanComponent.getTemplateComponent().getTaskConfiguration(), map);
                    success = true;
                } catch (IOException | TemplateException e) {
                    result = e.getMessage();
                }
                entity.setEndtime(new Date());
                entity.setSuccess(success);
                entity.setResult(result);
                BeanComponent.getLogTaskService().update(entity.getId(), entity, ignoreProperties);
                BeanComponent.getSysTaskService().updateStatus(task.getId(), ScheduledTask.TASK_STATUS_READY);

            }
        } else {
            BeanComponent.getScheduledTask().delete(taskId);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        if (null != thread) {
            thread.interrupt();
        }
    }
}