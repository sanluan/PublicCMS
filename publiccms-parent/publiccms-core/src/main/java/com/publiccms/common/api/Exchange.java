package com.publiccms.common.api;

import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public interface Exchange<E, D> {
    public String getDirectory();
    
    public int importOrder();

    public void exportAll(short siteId, String directory, ZipOutputStream zipOutputStream);

    public void exportEntity(short siteId, E entity, ZipOutputStream zipOutputStream);

    public void importData(short siteId, long userId, String directory, boolean overwrite, ZipFile zipFile);
    
    public void save(short siteId, long userId, boolean overwrite, D data);
}
