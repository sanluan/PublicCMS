package com.publiccms.logic.component.exchange;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.AbstractExchange;
import com.publiccms.common.constants.CommonConstants;
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
    protected final static Log log = LogFactory.getLog(SiteExchangeComponent.class);

    @Resource
    private List<AbstractExchange<?, ?>> exchangeList;
    @Resource
    private SiteComponent siteComponent;
    @Resource
    private TemplateComponent templateComponent;

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
            AbstractExchange<E, D> exchangeComponent, MultipartFile file, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            if (originalName.endsWith(dataFileSuffix)) {
                String suffix = CmsFileUtils.getSuffix(originalName);
                try {
                    File dest = File.createTempFile("temp_import_", suffix);
                    file.transferTo(dest);
                    try (ZipFile zipFile = new ZipFile(dest, CommonConstants.DEFAULT_CHARSET_NAME)) {
                        exchangeComponent.importData(site, userId, overwrite, zipFile);
                    }
                    dest.delete();
                    return CommonConstants.TEMPLATE_DONE;
                } catch (IOException e) {
                    log.error(e.getMessage());
                    model.addAttribute("error", e.getMessage());
                }
            } else {
                model.addAttribute("error", "verify.custom.fileType");
            }
        } else {
            model.addAttribute("error", "verify.notEmpty.file");
        }
        return CommonConstants.TEMPLATE_ERROR;
    }

    /**
     * @param site
     * @param zipOutputStream
     */
    public void exportAll(SysSite site, ZipOutputStream zipOutputStream) {
        for (AbstractExchange<?, ?> exchange : exchangeList) {
            exchange.exportAll(site, exchange.getDirectory(), zipOutputStream);
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
            if (originalName.endsWith(dataFileSuffix)) {
                String suffix = CmsFileUtils.getSuffix(originalName);
                try {
                    File dest = File.createTempFile("temp_import_", suffix);
                    file.transferTo(dest);
                    importDate(site, userId, overwrite, dest);
                    dest.delete();
                    return CommonConstants.TEMPLATE_DONE;
                } catch (IOException e) {
                    log.error(e.getMessage());
                    model.addAttribute("error", e.getMessage());
                }
            } else {
                model.addAttribute("error", "verify.custom.fileType");
            }
        } else if (CommonUtils.notEmpty(fileName)) {
            File dest = new File(siteComponent.getSiteFilePath(), fileName);
            try {
                importDate(site, userId, overwrite, dest);
            } catch (IOException e) {
                log.error(e.getMessage());
                model.addAttribute("error", e.getMessage());
            }
            return CommonConstants.TEMPLATE_DONE;
        } else {
            model.addAttribute("error", "verify.notEmpty.file");
        }
        return CommonConstants.TEMPLATE_ERROR;
    }

    public static <T> boolean mergeMap(String filepath, Class<T> clazz, ZipFile zipFile, ZipEntry zipEntry) {
        File file = new File(filepath);
        Map<String, T> map = null;
        try {
            map = CommonConstants.objectMapper.readValue(file,
                    CommonConstants.objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, clazz));
        } catch (IOException e) {
            return true;
        }
        try {
            Map<String, T> newMap = CommonConstants.objectMapper.readValue(zipFile.getInputStream(zipEntry),
                    CommonConstants.objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, clazz));
            map.putAll(newMap);
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                CommonConstants.objectMapper.writeValue(outputStream, map);
            }
        } catch (IOException e) {
        }
        return false;
    }

    public static boolean mergeDataFile(String filepath, SysSite site, ZipFile zipFile, ZipEntry zipEntry) {
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
        try (ZipFile zipFile = new ZipFile(file, CommonConstants.DEFAULT_CHARSET_NAME)) {
            {
                String filepath = siteComponent.getTemplateFilePath(site.getId(), CommonConstants.SEPARATOR);
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
            }
            {
                String filepath = siteComponent.getWebFilePath(site.getId(), CommonConstants.SEPARATOR);
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
                String filepath = siteComponent.getTaskTemplateFilePath(site.getId(), CommonConstants.SEPARATOR);
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
            exchangeList.sort((a, b) -> b.importOrder() - a.importOrder());
            for (AbstractExchange<?, ?> exchange : exchangeList) {
                exchange.importData(site, userId, exchange.getDirectory(), overwrite, zipFile);
            }
        }
    }
}
