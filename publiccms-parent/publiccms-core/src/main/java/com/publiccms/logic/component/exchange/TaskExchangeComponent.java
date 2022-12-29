package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.sys.SysTask;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.sys.SysTaskService;

import jakarta.annotation.Resource;

/**
 * TaskExchangeComponent 任务计划导出组件
 * 
 */
@Component
public class TaskExchangeComponent extends Exchange<SysTask, SysTask> {
    @Resource
    private SysTaskService service;
    @Resource
    private SiteComponent siteComponent;

    @Override
    public void exportAll(short siteId, String directory, ZipOutputStream zipOutputStream) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PageHandler page = service.getPage(siteId, null, null, null, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<SysTask> list = (List<SysTask>) page.getList();
        if (0 < page.getTotalCount()) {
            for (SysTask entity : list) {
                exportEntity(siteId, directory, entity, out, zipOutputStream);
            }
        }
    }

    @Override
    public void exportEntity(short siteId, String directory, SysTask task, ByteArrayOutputStream out,
            ZipOutputStream zipOutputStream) {
        PageHandler page = service.getPage(siteId, null, null, null, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<SysTask> list = (List<SysTask>) page.getList();
        if (0 < page.getTotalCount()) {
            for (SysTask entity : list) {
                int id = entity.getId();
                entity.setId(null);
                export(directory, out, zipOutputStream, entity, id + ".json");
            }
        }
    }

    public void save(short siteId, long userId, boolean overwrite, SysTask data) {
        if (null != data) {
            data.setSiteId(siteId);
            service.save(data);
        }
    }
}
