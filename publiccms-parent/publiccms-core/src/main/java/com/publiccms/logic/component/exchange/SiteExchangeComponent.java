package com.publiccms.logic.component.exchange;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.AbstractDataExchange;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ZipUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsCategoryType;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;
import com.publiccms.views.pojo.entities.SysConfig;

/**
 * SiteExchangeComponent 站点导入导出组件
 * 
 */
@Component
public class SiteExchangeComponent {
    protected static final Log log = LogFactory.getLog(SiteExchangeComponent.class);

    @Resource
    private List<AbstractDataExchange<?, ?>> exchangeList;
    @Resource
    private SiteComponent siteComponent;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private MetadataComponent metadataComponent;

    /**
     * @param <E>
     * @param <D>
     * @param site
     * @param userId
     * @param overwrite
     * @param dataFileSuffix
     * @param exchangeComponent
     * @param file
     * @param model
     * @return
     */
    public static <E, D> String importData(SysSite site, long userId, boolean overwrite, String dataFileSuffix,
            AbstractDataExchange<E, D> exchangeComponent, MultipartFile file, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            if (null != originalName && originalName.endsWith(dataFileSuffix)) {
                String suffix = CmsFileUtils.getSuffix(originalName);
                try {
                    File dest = File.createTempFile("temp_import_", suffix);
                    file.transferTo(dest);
                    try (ZipFile zipFile = new ZipFile(dest, Constants.DEFAULT_CHARSET_NAME)) {
                        exchangeComponent.importData(site, userId, overwrite, zipFile);
                    }
                    Files.delete(dest.toPath());
                    return CommonConstants.TEMPLATE_DONE;
                } catch (IOException e) {
                    log.error(e.getMessage());
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                }
            } else {
                model.addAttribute(CommonConstants.ERROR, "verify.custom.fileType");
            }
        } else {
            model.addAttribute(CommonConstants.ERROR, "verify.notEmpty.file");
        }
        return CommonConstants.TEMPLATE_ERROR;
    }

    /**
     * @param site
     * @param archiveOutputStream
     */
    public void exportAll(SysSite site, ArchiveOutputStream archiveOutputStream) {
        for (AbstractDataExchange<?, ?> exchange : exchangeList) {
            exchange.exportAll(site, exchange.getDirectory(), archiveOutputStream);
        }
    }

    /**
     * @param site
     * @param userId
     * @param overwrite
     * @param dataFileSuffix
     * @param file
     * @param fileName
     * @param model
     * @return
     */
    public String importData(SysSite site, long userId, boolean overwrite, String dataFileSuffix, MultipartFile file,
            String fileName, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            if (null != originalName && originalName.endsWith(dataFileSuffix)) {
                String suffix = CmsFileUtils.getSuffix(originalName);
                try {
                    File dest = File.createTempFile("temp_import_", suffix);
                    file.transferTo(dest);
                    importDate(site, userId, overwrite, dest);
                    Files.delete(dest.toPath());
                    return CommonConstants.TEMPLATE_DONE;
                } catch (IOException e) {
                    log.error(e.getMessage());
                    model.addAttribute(CommonConstants.ERROR, e.getMessage());
                }
            } else {
                model.addAttribute(CommonConstants.ERROR, "verify.custom.fileType");
            }
        } else if (CommonUtils.notEmpty(fileName)) {
            File dest = new File(siteComponent.getSiteFilePath(fileName));
            try {
                importDate(site, userId, overwrite, dest);
            } catch (IOException e) {
                log.error(e.getMessage());
                model.addAttribute(CommonConstants.ERROR, e.getMessage());
            }
            return CommonConstants.TEMPLATE_DONE;
        } else {
            model.addAttribute(CommonConstants.ERROR, "verify.notEmpty.file");
        }
        return CommonConstants.TEMPLATE_ERROR;
    }

    public static <T> boolean mergeMap(String filepath, Class<T> clazz, ZipFile zipFile, ZipArchiveEntry zipEntry) {
        File file = new File(filepath);
        Map<String, T> map = null;
        try {
            map = Constants.objectMapper.readValue(file,
                    Constants.objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, clazz));
        } catch (IOException e) {
            return true;
        }
        try {
            Map<String, T> newMap = Constants.objectMapper.readValue(zipFile.getInputStream(zipEntry),
                    Constants.objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, clazz));
            map.putAll(newMap);
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                Constants.objectMapper.writeValue(outputStream, map);
            }
        } catch (IOException e) {
        }
        return false;
    }

    public static boolean mergeDataFile(String filepath, SysSite site, ZipFile zipFile, ZipArchiveEntry zipEntry) {
        String filename = CmsFileUtils.getFileName(zipEntry.getName());
        if (SiteComponent.MODEL_FILE.equalsIgnoreCase(filename)) {
            if (null == site.getParentId()) {
                return mergeMap(filepath, CmsModel.class, zipFile, zipEntry);
            }
        } else if (SiteComponent.CATEGORY_TYPE_FILE.equalsIgnoreCase(filename)) {
            return mergeMap(filepath, CmsCategoryType.class, zipFile, zipEntry);
        } else if (SiteComponent.CONFIG_FILE.equalsIgnoreCase(filename)) {
            return mergeMap(filepath, SysConfig.class, zipFile, zipEntry);
        } else if (MetadataComponent.METADATA_FILE.equalsIgnoreCase(filename)) {
            if (zipEntry.getName().toLowerCase().contains("include/")) {
                return mergeMap(filepath, CmsPlaceMetadata.class, zipFile, zipEntry);
            } else {
                return mergeMap(filepath, CmsPageMetadata.class, zipFile, zipEntry);
            }
        } else if (MetadataComponent.DATA_FILE.equalsIgnoreCase(filename)) {
            return mergeMap(filepath, CmsPageData.class, zipFile, zipEntry);
        }
        return true;
    }

    private void importDate(SysSite site, long userId, boolean overwrite, File file) throws IOException {
        try (ZipFile zipFile = new ZipFile(file, Constants.DEFAULT_CHARSET_NAME)) {
            {
                String filepath = siteComponent.getTemplateFilePath(site.getId(), Constants.SEPARATOR);
                ZipUtils.unzip(zipFile, "template", filepath, overwrite, (f, e) -> {
                    if (e.getName().toLowerCase().endsWith(".data")) {
                        String datafile = siteComponent.getTemplateFilePath(site.getId(),
                                StringUtils.removeStart(e.getName(), "template/"));
                        return mergeDataFile(datafile, site, f, e);
                    } else {
                        String historyFilePath = siteComponent.getTemplateHistoryFilePath(site.getId(),
                                StringUtils.removeStart(e.getName(), "template/"), true);
                        try {
                            CmsFileUtils.copyInputStreamToFile(f.getInputStream(e), historyFilePath);
                        } catch (IOException e1) {
                        }
                        return true;
                    }
                });
                templateComponent.clearTemplateCache();
                metadataComponent.clear();
            }
            {
                String filepath = siteComponent.getWebFilePath(site.getId(), Constants.SEPARATOR);
                ZipUtils.unzip(zipFile, "web", filepath, overwrite, (f, e) -> {
                    String historyFilePath = siteComponent.getWebHistoryFilePath(site.getId(),
                            StringUtils.removeStart(e.getName(), "web/"), true);
                    try {
                        CmsFileUtils.copyInputStreamToFile(f.getInputStream(e), historyFilePath);
                    } catch (IOException e1) {
                    }
                    return true;
                });
            }
            {
                String filepath = siteComponent.getTaskTemplateFilePath(site.getId(), Constants.SEPARATOR);
                ZipUtils.unzip(zipFile, "tasktemplate", filepath, overwrite, (f, e) -> {
                    String historyFilePath = siteComponent.getTaskTemplateHistoryFilePath(site.getId(),
                            StringUtils.removeStart(e.getName(), "tasktemplate/"), true);
                    try {
                        CmsFileUtils.copyInputStreamToFile(f.getInputStream(e), historyFilePath);
                    } catch (IOException e1) {
                    }
                    return true;
                });
                templateComponent.clearTaskTemplateCache();
            }
            for (AbstractDataExchange<?, ?> exchange : exchangeList) {
                exchange.importData(site, userId, exchange.getDirectory(), overwrite, zipFile);
            }
        }
    }
}
