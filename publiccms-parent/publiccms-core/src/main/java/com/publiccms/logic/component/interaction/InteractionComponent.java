package com.publiccms.logic.component.interaction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ZipUtils;

public abstract class InteractionComponent<E, D> {
    private Class<D> clazz;

    public abstract void exportAll(short siteId, String directory, ZipOutputStream zipOutputStream);

    public void exportAll(short siteId, ZipOutputStream zipOutputStream) {
        exportAll(siteId, null, zipOutputStream);
    }

    public abstract void exportEntity(short siteId, String directory, E entity, ByteArrayOutputStream out,
            ZipOutputStream zipOutputStream);

    public void exportEntity(short siteId, E entity, ZipOutputStream zipOutputStream) {
        if (null != entity) {
            exportEntity(siteId, null, entity, new ByteArrayOutputStream(), zipOutputStream);
        }
    }

    protected void export(String directory, ByteArrayOutputStream out, ZipOutputStream zipOutputStream, D value, String path) {
        try {
            out.reset();
            Constants.objectMapper.writeValue(out, value);
            ZipUtils.compressFile(new ByteArrayInputStream(out.toByteArray()), zipOutputStream, getPath(directory, path));
        } catch (IOException e) {
        }
    }

    public void importData(short siteId, long userId, boolean overwrite, ZipFile zipFile) {
        importData(siteId, userId, null, overwrite, zipFile);
    }

    public void importData(short siteId, long userId, String directory, boolean overwrite, ZipFile zipFile) {
        if (CommonUtils.empty(directory)) {
            Enumeration<ZipEntry> entryEnum = zipFile.getEntries();
            while (entryEnum.hasMoreElements()) {
                importData(siteId, userId, overwrite, zipFile, entryEnum.nextElement());
            }
        } else {
            Iterable<ZipEntry> list = zipFile.getEntries(directory);
            for (ZipEntry zipEntry : list) {
                importData(siteId, userId, overwrite, zipFile, zipEntry);
            }
        }
    }

    public void importData(short siteId, long userId, boolean overwrite, ZipFile zipFile, ZipEntry zipEntry) {
        if (!zipEntry.isDirectory() && zipFile.canReadEntryData(zipEntry)) {
            try (InputStream inputStream = zipFile.getInputStream(zipEntry)) {
                importData(siteId, userId, overwrite, inputStream);
            } catch (IOException e) {
            }
        }
    }

    protected void importData(short siteId, long userId, boolean overwrite, InputStream inputStream) {
        try {
            D data = Constants.objectMapper.readValue(inputStream, getDataClass());
            if (null != data) {
                save(siteId, userId, overwrite, data);
            }
        } catch (IOException e) {
        }
    }

    public abstract void save(short siteId, long userId, boolean overwrite, D data);

    protected String getPath(String directory, String path) {
        if (CommonUtils.empty(directory)) {
            return path;
        } else {
            return directory + CommonConstants.SEPARATOR + path;
        }
    }

    @SuppressWarnings("unchecked")
    protected Class<D> getDataClass() {
        return null == clazz
                ? this.clazz = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]
                : clazz;
    }
}
