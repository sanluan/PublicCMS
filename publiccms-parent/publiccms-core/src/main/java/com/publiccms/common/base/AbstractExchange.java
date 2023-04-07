package com.publiccms.common.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import com.publiccms.common.api.Exchange;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ZipUtils;
import com.publiccms.entities.sys.SysSite;

public abstract class AbstractExchange<E, D> implements Exchange<E, D> {
    protected static final Log log = LogFactory.getLog(AbstractExchange.class);
    private Class<D> clazz;

    public static final String ATTACHMENT_DIR = "attachment/";

    @Override
    public void exportAll(SysSite site, String directory, ZipOutputStream zipOutputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exportAll(site, directory, outputStream, zipOutputStream);
    }

    public abstract void exportAll(SysSite site, String directory, ByteArrayOutputStream outputStream,
            ZipOutputStream zipOutputStream);

    public void exportAll(SysSite site, ZipOutputStream zipOutputStream) {
        exportAll(site, null, zipOutputStream);
    }

    public abstract void exportEntity(SysSite site, String directory, E entity, ByteArrayOutputStream outputStream,
            ZipOutputStream zipOutputStream);

    @Override
    public void exportEntity(SysSite site, E entity, ZipOutputStream zipOutputStream) {
        if (null != entity) {
            exportEntity(site, null, entity, new ByteArrayOutputStream(), zipOutputStream);
        }
    }
    
    protected void export(String directory, ByteArrayOutputStream outputStream, ZipOutputStream zipOutputStream, D value,
            String path) {
        try {
            outputStream.reset();
            Constants.objectMapper.writeValue(outputStream, value);
            try (InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                ZipUtils.compressFile(inputStream, zipOutputStream, getPath(directory, path));
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
            Enumeration<ZipEntry> entryEnum = zipFile.getEntries();
            while (entryEnum.hasMoreElements()) {
                importData(site, userId, overwrite, zipFile, entryEnum.nextElement());
            }
        } else {
            Enumeration<? extends ZipEntry> entryEnum = zipFile.getEntries();
            if (!directory.endsWith(Constants.SEPARATOR)) {
                directory = CommonUtils.joinString(directory + Constants.SEPARATOR);
            }
            while (entryEnum.hasMoreElements()) {
                ZipEntry zipEntry = entryEnum.nextElement();
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

    public void importData(SysSite site, long userId, boolean overwrite, ZipFile zipFile, ZipEntry zipEntry) {
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

    @Override
    public int importOrder() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    protected Class<D> getDataClass() {
        return null == clazz
                ? this.clazz = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]
                : clazz;
    }
}
