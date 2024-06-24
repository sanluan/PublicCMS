package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractDataExchange;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.cms.CmsDictionaryExclude;
import com.publiccms.entities.cms.CmsDictionaryExcludeValue;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeService;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeValueService;
import com.publiccms.logic.service.cms.CmsDictionaryService;
import com.publiccms.views.pojo.exchange.Dictionary;

import jakarta.annotation.Resource;
import jakarta.annotation.Priority;

/**
 * DictionaryExchangeComponent 数据字典导入导出组件
 * 
 */
@Component
@Priority(3)
public class DictionaryExchangeComponent extends AbstractDataExchange<CmsDictionary, Dictionary> {
    @Resource
    private CmsDictionaryService service;
    @Resource
    private CmsDictionaryDataService dataService;
    @Resource
    private CmsDictionaryExcludeService excludeService;
    @Resource
    private CmsDictionaryExcludeValueService excludeValueService;

    public void exportAll(SysSite site, String directory, ByteArrayOutputStream outputStream, ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        service.batchWork(site.getId(), (list, i) -> {
            for (CmsDictionary entity : list) {
                exportEntity(site, directory, entity, out, archiveOutputStream);
            }
        }, PageHandler.MAX_PAGE_SIZE);
    }

    public void exportEntity(SysSite site, String directory, CmsDictionary entity, ByteArrayOutputStream outputStream,
            ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream) {
        Dictionary data = new Dictionary();
        data.setEntity(entity);
        data.setDataList(dataService.getList(site.getId(), entity.getId().getId()));
        data.setExcludeList(excludeService.getList(site.getId(), entity.getId().getId(), null));
        data.setExcludeValueList(excludeValueService.getList(site.getId(), entity.getId().getId(), null));
        export(directory, outputStream, archiveOutputStream, data, CommonUtils.joinString(entity.getId().getId(), ".json"));
    }

    public void save(SysSite site, long userId, boolean overwrite, Dictionary data) {
        CmsDictionary entity = data.getEntity();
        entity.getId().setSiteId(site.getId());
        CmsDictionary oldentity = service.getEntity(entity.getId());
        if (null == oldentity || overwrite) {
            if (null == oldentity) {
                service.save(entity);
            } else {
                service.update(entity.getId(), entity);
            }
            if (null != data.getDataList()) {
                for (CmsDictionaryData temp : data.getDataList()) {
                    temp.getId().setSiteId(site.getId());
                    if (null == dataService.update(temp.getId(), temp)) {
                        dataService.save(temp);
                    }
                }
            }
            if (null != data.getExcludeList()) {
                for (CmsDictionaryExclude temp : data.getExcludeList()) {
                    temp.getId().setSiteId(site.getId());
                    if (null == excludeService.update(temp.getId(), temp)) {
                        excludeService.save(temp);
                    }
                }
            }
            if (null != data.getExcludeValueList()) {
                for (CmsDictionaryExcludeValue temp : data.getExcludeValueList()) {
                    temp.getId().setSiteId(site.getId());
                    if (null == excludeValueService.update(temp.getId(), temp)) {
                        excludeValueService.save(temp);
                    }
                }
            }
        }
    }

    @Override
    public String getDirectory() {
        return "dictionary";
    }
}
