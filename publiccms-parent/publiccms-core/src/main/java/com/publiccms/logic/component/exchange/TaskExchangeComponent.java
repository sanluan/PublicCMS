package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractExchange;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.component.task.ScheduledTask;
import com.publiccms.logic.service.sys.SysTaskService;

import jakarta.annotation.Resource;

/**
 * TaskExchangeComponent 任务计划导出组件
 *
 */
@Component
public class TaskExchangeComponent extends AbstractExchange<SysTask, SysTask> {
    @Resource
    private SysTaskService service;
    @Resource
    private ScheduledTask scheduledTask;

    @Override
    public void exportAll(SysSite site, String directory, ByteArrayOutputStream outputStream, ZipOutputStream zipOutputStream) {
        PageHandler page = service.getPage(site.getId(), null, null, null, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<SysTask> list = (List<SysTask>) page.getList();
        if (0 < page.getTotalCount()) {
            for (SysTask entity : list) {
                exportEntity(site, directory, entity, outputStream, zipOutputStream);
            }
        }
    }

    @Override
    public void exportEntity(SysSite site, String directory, SysTask task, ByteArrayOutputStream outputStream,
            ZipOutputStream zipOutputStream) {
        int id = task.getId();
        task.setId(null);
        export(directory, outputStream, zipOutputStream, task, CommonUtils.joinString(id, ".json"));
    }

    public void save(SysSite site, long userId, boolean overwrite, SysTask data) {
        if (null != data) {
            data.setSiteId(site.getId());
            data.setUpdateDate(CommonUtils.getDate());
            service.save(data);
            scheduledTask.create(site, data.getId(), data.getCronExpression());
        }
    }

    @Override
    public String getDirectory() {
        return "task";
    }
}
