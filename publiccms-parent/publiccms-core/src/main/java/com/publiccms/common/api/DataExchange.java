package com.publiccms.common.api;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import com.publiccms.entities.sys.SysSite;

public interface DataExchange<E, D> {
    public String getDirectory();

    public void exportAll(SysSite site, String directory, ArchiveOutputStream archiveOutputStream);

    public void exportEntity(SysSite site, E entity, ArchiveOutputStream archiveOutputStream);

    public void importData(SysSite site, long userId, String directory, boolean overwrite, ZipFile zipFile);
    
    public void save(SysSite site, long userId, boolean overwrite, D data);
}
