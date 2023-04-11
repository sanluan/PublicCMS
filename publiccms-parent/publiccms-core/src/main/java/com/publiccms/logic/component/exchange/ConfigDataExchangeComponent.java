package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.annotation.Priority;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractDataExchange;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysConfigData;
import com.publiccms.entities.sys.SysConfigDataId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.service.sys.SysConfigDataService;

/**
 * ConfigDataExchangeComponent 站点配置导出组件
 * 
 */
@Component
@Priority(2)
public class ConfigDataExchangeComponent extends AbstractDataExchange<SysConfigData, SysConfigData> {
    @Resource
    private ConfigComponent configComponent;
    @Resource
    private ConfigDataComponent configDataComponent;
    @Resource
    private SysConfigDataService service;

    @Override
    public void exportAll(SysSite site, String directory, ByteArrayOutputStream outputStream, ZipOutputStream zipOutputStream) {
        Set<String> configCodeSet = configComponent.getExportableConfigCodeList(site.getId());
        for (String code : configCodeSet) {
            SysConfigData entity = service.getEntity(new SysConfigDataId(site.getId(), code));
            if (null != entity) {
                exportEntity(site, directory, entity, outputStream, zipOutputStream);
            }
        }
    }

    @Override
    public void importData(SysSite site, long userId, String directory, boolean overwrite, ZipFile zipFile) {
        super.importData(site, userId, directory, overwrite, zipFile);
        configDataComponent.clear(site.getId());
    }

    @Override
    public void exportEntity(SysSite site, String directory, SysConfigData entity, ByteArrayOutputStream outputStream,
            ZipOutputStream zipOutputStream) {
        if (needReplace(entity.getData(), site.getDynamicPath())) {
            entity.setData(StringUtils.replace(entity.getData(), site.getDynamicPath(), "#DYNAMICPATH#"));
        }
        if (needReplace(entity.getData(), site.getSitePath())) {
            entity.setData(StringUtils.replace(entity.getData(), site.getSitePath(), "#SITEPATH#"));
        }
        export(directory, outputStream, zipOutputStream, entity, CommonUtils.joinString(entity.getId().getCode(), ".json"));
    }

    @Override
    public void save(SysSite site, long userId, boolean overwrite, SysConfigData data) {
        if (null != data && null != data.getId()) {
            data.getId().setSiteId(site.getId());
            SysConfigData oldEntity = service.getEntity(data.getId());
            if (overwrite || null == oldEntity) {
                if (CommonUtils.notEmpty(data.getData())) {
                    data.setData(StringUtils.replace(data.getData(), "#DYNAMICPATH#", site.getDynamicPath()));
                }
                if (CommonUtils.notEmpty(data.getData())) {
                    data.setData(StringUtils.replace(data.getData(), "#SITEPATH#", site.getSitePath()));
                }
                service.saveOrUpdate(data);
            }
        }
    }

    @Override
    public String getDirectory() {
        return "config";
    }
}
