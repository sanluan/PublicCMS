package com.publiccms.common.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.DataExchange;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.HtmlUtils;
import com.publiccms.common.tools.ZipUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;

import jakarta.annotation.Resource;

public abstract class AbstractDataExchange<E, D> implements DataExchange<E, D> {
    protected static final Log log = LogFactory.getLog(AbstractDataExchange.class);
    private Class<D> clazz;

    public static final String ATTACHMENT_DIR = "attachment/";
    public static final String PRIVATE_DIR = "privatefile/";
    @Resource
    protected SiteComponent siteComponent;

    @Override
    public void exportAll(SysSite site, String directory, ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exportAll(site, directory, outputStream, archiveOutputStream);
    }

    public abstract void exportAll(SysSite site, String directory, ByteArrayOutputStream outputStream,
            ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream);

    public void exportAll(SysSite site, ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream) {
        exportAll(site, null, archiveOutputStream);
    }

    public abstract void exportEntity(SysSite site, String directory, E entity, ByteArrayOutputStream outputStream,
            ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream);

    @Override
    public void exportEntity(SysSite site, E entity, ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream) {
        if (null != entity) {
            exportEntity(site, null, entity, new ByteArrayOutputStream(), archiveOutputStream);
        }
    }

    protected void export(String directory, ByteArrayOutputStream outputStream,
            ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream, D value, String path) {
        try {
            outputStream.reset();
            Constants.objectMapper.writeValue(outputStream, value);
            try (InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                ZipUtils.compressFile(inputStream, archiveOutputStream, getPath(directory, path));
            }
        } catch (IOException e) {
        }
    }

    protected void exportAttachment(SysSite site, Set<String> webfileList, Set<String> privateFileList,
            ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream) {
        if (null != webfileList && !webfileList.isEmpty()) {
            for (String file : webfileList) {
                String fullName;
                if (file.startsWith(site.getSitePath())) {
                    fullName = StringUtils.removeStart(file, site.getSitePath());
                } else {
                    fullName = file;
                }
                if (fullName.contains(Constants.DOT) && !fullName.contains(".htm")) {
                    String filepath = siteComponent.getWebFilePath(site.getId(), fullName);
                    try {
                        ZipUtils.compressFile(new File(filepath), archiveOutputStream,
                                CommonUtils.joinString(ATTACHMENT_DIR, fullName));
                    } catch (IOException e) {
                    }
                }
            }
        }
        if (null != privateFileList && !privateFileList.isEmpty()) {
            for (String file : privateFileList) {
                if (file.contains(Constants.DOT)) {
                    String filepath = siteComponent.getPrivateFilePath(site.getId(), file);
                    try {
                        ZipUtils.compressFile(new File(filepath), archiveOutputStream, CommonUtils.joinString(PRIVATE_DIR, file));
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    protected void exportFileList(Map<String, String> extendMap, List<SysExtendField> extendList, Set<String> webfileList,
            Set<String> privateFileList) {
        for (SysExtendField extendField : extendList) {
            if (ArrayUtils.contains(Config.INPUT_TYPE_EDITORS, extendField.getInputType())) {
                String value = extendMap.get(extendField.getId().getCode());
                if (CommonUtils.notEmpty(value)) {
                    HtmlUtils.getFileList(extendMap.get(extendField.getId().getCode()), webfileList);
                }
            } else if (ArrayUtils.contains(Config.INPUT_TYPE_FILES, extendField.getInputType())) {
                String value = extendMap.get(extendField.getId().getCode());
                if (CommonUtils.notEmpty(value)) {
                    webfileList.add(extendMap.get(extendField.getId().getCode()));
                }
            } else if (ArrayUtils.contains(Config.INPUT_TYPE_PRIVATE_FILES, extendField.getInputType())) {
                String value = extendMap.get(extendField.getId().getCode());
                if (CommonUtils.notEmpty(value)) {
                    privateFileList.add(extendMap.get(extendField.getId().getCode()));
                }
            }
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
