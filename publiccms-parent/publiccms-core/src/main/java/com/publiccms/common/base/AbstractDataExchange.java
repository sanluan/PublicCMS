package com.publiccms.common.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.publiccms.common.api.DataExchange;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ZipUtils;
import com.publiccms.entities.sys.SysSite;

public abstract class AbstractDataExchange<E, D> implements DataExchange<E, D> {
    protected static final Log log = LogFactory.getLog(AbstractDataExchange.class);
    private Class<D> clazz;

    public static final String ATTACHMENT_DIR = "attachment/";

    @Override
    public void exportAll(SysSite site, String directory, ArchiveOutputStream archiveOutputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exportAll(site, directory, outputStream, archiveOutputStream);
    }

    public abstract void exportAll(SysSite site, String directory, ByteArrayOutputStream outputStream,
            ArchiveOutputStream archiveOutputStream);

    public void exportAll(SysSite site, ArchiveOutputStream archiveOutputStream) {
        exportAll(site, null, archiveOutputStream);
    }

    public abstract void exportEntity(SysSite site, String directory, E entity, ByteArrayOutputStream outputStream,
            ArchiveOutputStream archiveOutputStream);

    @Override
    public void exportEntity(SysSite site, E entity, ArchiveOutputStream archiveOutputStream) {
        if (null != entity) {
            exportEntity(site, null, entity, new ByteArrayOutputStream(), archiveOutputStream);
        }
    }
    
    protected void export(String directory, ByteArrayOutputStream outputStream, ArchiveOutputStream archiveOutputStream, D value,
            String path) {
        try {
            outputStream.reset();
            Constants.objectMapper.writeValue(outputStream, value);
            try (InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                ZipUtils.compressFile(inputStream, archiveOutputStream, getPath(directory, path));
            }
        } catch (IOException e) {
        }
    }

    public void importData(SysSite site, long userId, boolean overwrite, ZipFile zipFile) {
        importData(site, userId, null, overwrite, zipFile);
    }

    @Override
    public void importData(SysSite site, long userId, String directory, boolean overwrite, ZipFile zipFile) {
        if (CommonUtils.empty(directory)) {
            Enumeration<ZipArchiveEntry> entryEnum = zipFile.getEntries();
            while (entryEnum.hasMoreElements()) {
                importData(site, userId, overwrite, zipFile, entryEnum.nextElement());
            }
        } else {
            Enumeration<ZipArchiveEntry> entryEnum = zipFile.getEntries();
            if (!directory.endsWith(Constants.SEPARATOR)) {
                directory = CommonUtils.joinString(directory + Constants.SEPARATOR);
            }
            while (entryEnum.hasMoreElements()) {
                ZipArchiveEntry zipEntry = entryEnum.nextElement();
                if (zipEntry.getName().startsWith(directory)) {
                    importData(site, userId, overwrite, zipFile, zipEntry);
                }
            }
        }
    }

    protected boolean needReplace(String source, String sitePath) {
        return CommonUtils.notEmpty(source) && CommonUtils.notEmpty(sitePath)
                && (sitePath.contains("://") || sitePath.startsWith("//"));
    }

    public void importData(SysSite site, long userId, boolean overwrite, ZipFile zipFile, ZipArchiveEntry zipEntry) {
        if (!zipEntry.isDirectory() && zipFile.canReadEntryData(zipEntry)) {
            try (InputStream inputStream = zipFile.getInputStream(zipEntry)) {
                importData(site, userId, overwrite, inputStream);
            } catch (IOException e) {
            }
        }
    }

    protected void importData(SysSite site, long userId, boolean overwrite, InputStream inputStream) {
        try {
            D data = Constants.objectMapper.readValue(inputStream, getDataClass());
            if (null != data) {
                save(site, userId, overwrite, data);
            }
        } catch (IOException e) {
        }
    }

    protected String getPath(String directory, String path) {
        if (CommonUtils.empty(directory)) {
            return path;
        } else {
            return CommonUtils.joinString(directory, Constants.SEPARATOR, path);
        }
    }

    @SuppressWarnings("unchecked")
    protected Class<D> getDataClass() {
        return null == clazz
                ? this.clazz = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]
                : clazz;
    }
}
