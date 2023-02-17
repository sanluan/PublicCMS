package com.publiccms.logic.component.exchange;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;

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
     * @param siteId
     * @param userId
     * @param overwrite
     * @param dataFileSuffix
     * @param exchangeComponent
     * @param file
     * @param model
     * @return
     */
    public static <E, D> String importData(short siteId, long userId, boolean overwrite, String dataFileSuffix,
            AbstractExchange<E, D> exchangeComponent, MultipartFile file, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            if (originalName.endsWith(dataFileSuffix)) {
                String suffix = CmsFileUtils.getSuffix(originalName);
                try {
                    File dest = File.createTempFile("temp_import_", suffix);
                    file.transferTo(dest);
                    try (ZipFile zipFile = new ZipFile(dest, CommonConstants.DEFAULT_CHARSET_NAME)) {
                        exchangeComponent.importData(siteId, userId, overwrite, zipFile);
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
     * @param siteId
     * @param zipOutputStream
     */
    public void exportAll(short siteId, ZipOutputStream zipOutputStream) {
        for (AbstractExchange<?, ?> exchange : exchangeList) {
            exchange.exportAll(siteId, exchange.getDirectory(), zipOutputStream);
        }
    }

    /**
     * @param siteId
     * @param userId
     * @param overwrite
     * @param dataFileSuffix
     * @param file
     * @param fileName
     * @param model
     * @return
     */
    public String importData(short siteId, long userId, boolean overwrite, String dataFileSuffix, MultipartFile file,
            String fileName, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            if (originalName.endsWith(dataFileSuffix)) {
                String suffix = CmsFileUtils.getSuffix(originalName);
                try {
                    File dest = File.createTempFile("temp_import_", suffix);
                    file.transferTo(dest);
                    importDate(siteId, userId, overwrite, dest);
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
                importDate(siteId, userId, overwrite, dest);
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

    private void importDate(short siteId, long userId, boolean overwrite, File file) throws IOException {
        try (ZipFile zipFile = new ZipFile(file, CommonConstants.DEFAULT_CHARSET_NAME)) {
            {
                String filepath = siteComponent.getTemplateFilePath(siteId, CommonConstants.SEPARATOR);
                ZipUtils.unzip(zipFile, "template", filepath, overwrite);
                templateComponent.clearTemplateCache();
            }
            {
                String filepath = siteComponent.getWebFilePath(siteId, CommonConstants.SEPARATOR);
                ZipUtils.unzip(zipFile, "web", filepath, overwrite);
            }
            {
                String filepath = siteComponent.getTaskTemplateFilePath(siteId, CommonConstants.SEPARATOR);
                ZipUtils.unzip(zipFile, "tasktemplate", filepath, overwrite);
                templateComponent.clearTaskTemplateCache();
            }
            exchangeList.sort((a, b) -> b.importOrder() - a.importOrder());
            for (AbstractExchange<?, ?> exchange : exchangeList) {
                exchange.importData(siteId, userId, exchange.getDirectory(), overwrite, zipFile);
            }
        }
    }
}
