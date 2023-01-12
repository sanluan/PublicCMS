package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractExchange;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.sys.SysTaskService;

/**
 * TaskExchangeComponent 任务计划导出组件
 * 
 */
@Component
public class TaskExchangeComponent extends AbstractExchange<SysTask, SysTask> {
    @Resource
    private SysTaskService service;
    @Resource
    private SiteComponent siteComponent;

    @Override
    public void exportAll(short siteId, String directory, ByteArrayOutputStream outputStream, ZipOutputStream zipOutputStream) {
        PageHandler page = service.getPage(siteId, null, null, null, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<SysTask> list = (List<SysTask>) page.getList();
        if (0 < page.getTotalCount()) {
            for (SysTask entity : list) {
                exportEntity(siteId, directory, entity, outputStream, zipOutputStream);
            }
        }
    }

    @Override
    public void exportEntity(short siteId, String directory, SysTask task, ByteArrayOutputStream outputStream,
            ZipOutputStream zipOutputStream) {
        int id = task.getId();
        task.setId(null);
        export(directory, outputStream, zipOutputStream, task, id + ".json");
    }

    public void save(short siteId, long userId, boolean overwrite, SysTask data) {
        if (null != data) {
            data.setSiteId(siteId);
            service.save(data);
        }
    }

    @Override
    public String getDirectory() {
        return "task";
    }
}
