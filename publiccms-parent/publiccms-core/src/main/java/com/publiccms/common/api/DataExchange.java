package com.publiccms.common.api;

import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import com.publiccms.entities.sys.SysSite;

public interface DataExchange<E, D> {
    public String getDirectory();
    
    public int importOrder();

    public void exportAll(SysSite site, String directory, ZipOutputStream zipOutputStream);

    public void exportEntity(SysSite site, E entity, ZipOutputStream zipOutputStream);

    public void importData(SysSite site, long userId, String directory, boolean overwrite, ZipFile zipFile);
    
    public void save(SysSite site, long userId, boolean overwrite, D data);
}
