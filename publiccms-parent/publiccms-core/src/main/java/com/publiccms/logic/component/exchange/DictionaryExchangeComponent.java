package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;

import org.apache.tools.zip.ZipOutputStream;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.cms.CmsDictionaryExclude;
import com.publiccms.entities.cms.CmsDictionaryExcludeValue;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeService;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeValueService;
import com.publiccms.logic.service.cms.CmsDictionaryService;
import com.publiccms.views.pojo.exchange.Dictionary;

/**
 * DictionaryInteractionComponent 数据字典导入导出组件
 * 
 */
@Component
public class DictionaryExchangeComponent extends Exchange<CmsDictionary, Dictionary> {
    @Resource
    private CmsDictionaryService service;
    @Resource
    private CmsDictionaryDataService dataService;
    @Resource
    private CmsDictionaryExcludeService excludeService;
    @Resource
    private CmsDictionaryExcludeValueService excludeValueService;

    public void exportAll(short siteId, String directory, ZipOutputStream zipOutputStream) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        service.batchWork(siteId, (list, i) -> {
            for (CmsDictionary entity : list) {
                exportEntity(siteId, directory, entity, out, zipOutputStream);
            }
        }, PageHandler.MAX_PAGE_SIZE);
    }

    public void exportEntity(short siteId, String directory, CmsDictionary entity, ByteArrayOutputStream out,
            ZipOutputStream zipOutputStream) {
        Dictionary data = new Dictionary();
        data.setEntity(entity);
        data.setDataList(dataService.getList(siteId, entity.getId().getId()));
        data.setExcludeList(excludeService.getList(siteId, entity.getId().getId(), null));
        data.setExcludeValueList(excludeValueService.getList(siteId, entity.getId().getId(), null));
        export(directory, out, zipOutputStream, data, entity.getId().getId() + ".json");
    }

    public void save(short siteId, long userId, boolean overwrite, Dictionary data) {
        CmsDictionary entity = data.getEntity();
        entity.getId().setSiteId(siteId);
        CmsDictionary oldentity = service.getEntity(entity.getId());
        if (null == oldentity || overwrite) {
            service.saveOrUpdate(entity);
            if (null != data.getDataList()) {
                for (CmsDictionaryData temp : data.getDataList()) {
                    temp.getId().setSiteId(siteId);
                }
                dataService.saveOrUpdate(data.getDataList());
            }
            if (null != data.getExcludeList()) {
                for (CmsDictionaryExclude temp : data.getExcludeList()) {
                    temp.getId().setSiteId(siteId);
                }
                excludeService.saveOrUpdate(data.getExcludeList());
            }
            if (null != data.getExcludeValueList()) {
                for (CmsDictionaryExcludeValue temp : data.getExcludeValueList()) {
                    temp.getId().setSiteId(siteId);
                }
                excludeValueService.saveOrUpdate(data.getExcludeValueList());
            }
        }
    }
}
