package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractExchange;
import com.publiccms.entities.sys.SysConfigData;
import com.publiccms.entities.sys.SysConfigDataId;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.sys.SysConfigDataService;

import jakarta.annotation.Resource;

/**
 * ConfigDataExchangeComponent 站点配置导出组件
 * 
 */
@Component
public class ConfigDataExchangeComponent extends AbstractExchange<SysConfigData, SysConfigData> {
    @Resource
    private ConfigComponent configComponent;
    @Resource
    private SysConfigDataService service;
    @Resource
    private SiteComponent siteComponent;

    @Override
    public void exportAll(short siteId, String directory, ByteArrayOutputStream outputStream, ZipOutputStream zipOutputStream) {
        Set<String> configCodeSet = configComponent.getExportableConfigCodeList(siteId);
        for (String code : configCodeSet) {
            SysConfigData entity = service.getEntity(new SysConfigDataId(siteId, code));
            if (null != entity) {
                exportEntity(siteId, directory, entity, outputStream, zipOutputStream);
            }
        }
    }

    @Override
    public void exportEntity(short siteId, String directory, SysConfigData entity, ByteArrayOutputStream outputStream,
            ZipOutputStream zipOutputStream) {
        export(directory, outputStream, zipOutputStream, entity, entity.getId().getCode() + ".json");
    }

    public void save(short siteId, long userId, boolean overwrite, SysConfigData data) {
        if (null != data && null != data.getId()) {
            data.getId().setSiteId(siteId);
            SysConfigData oldEntity = service.getEntity(data.getId());
            if (overwrite || null == oldEntity) {
                service.saveOrUpdate(data);
            }
        }
    }

    @Override
    public String getDirectory() {
        return "config";
    }
}
