package com.publiccms.logic.component.site;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.api.FileUploader;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ImageUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.views.pojo.entities.FileUploadResult;

import jakarta.annotation.Resource;

/**
 *
 * FileUploadComponent
 * 
 */
@Component
public class FileUploadComponent {
    @Resource
    private SiteComponent siteComponent;
    @Resource
    private ConfigDataComponent configDataComponent;
    @Resource
    private SafeConfigComponent safeConfigComponent;

    private List<FileUploader> uploaderList;

    @Autowired(required = false)
    public void initUploaderList(List<FileUploader> uploaderList) {
        this.uploaderList = uploaderList;
    }

    public String getPrivateFileUrl(SysSite site, Integer expiryMinutes, String filepath, String filename) {
        if (null == expiryMinutes) {
            Map<String, String> config = configDataComponent.getConfigData(site.getId(), SafeConfigComponent.CONFIG_CODE);
            expiryMinutes = ConfigDataComponent.getInt(config.get(SafeConfigComponent.CONFIG_EXPIRY_MINUTES_SIGN),
                    SafeConfigComponent.DEFAULT_EXPIRY_MINUTES_SIGN);
        }
        if (CommonUtils.notEmpty(uploaderList)) {
            for (FileUploader fileUploader : uploaderList) {
                if (fileUploader.enablePrefix(site.getId(), true)) {
                    return fileUploader.getPrivateFileUrl(site.getId(), expiryMinutes, filepath);
                }
            }
        }
        long expiry = System.currentTimeMillis() + expiryMinutes * 60 * 1000;
        String string = CmsFileUtils.getPrivateFileSignString(expiry, filepath);
        String signKey = safeConfigComponent.getSignKey(site.getId());
        String sign = VerificationUtils.base64Encode(VerificationUtils.encryptAES(string, signKey));
        if (CommonUtils.notEmpty(filename)) {
            return CommonUtils.joinString(site.getDynamicPath(), "file/private?expiry=", expiry, "&sign=",
                    CommonUtils.encodeURI(sign), "&filePath=", CommonUtils.encodeURI(filepath), "&filename=",
                    CommonUtils.encodeURI(filename));
        } else {
            return CommonUtils.joinString(site.getDynamicPath(), "file/private?expiry=", expiry, "&sign=",
                    CommonUtils.encodeURI(sign), "&filePath=", CommonUtils.encodeURI(filepath));
        }
    }

    public void initContentCover(SysSite site, CmsContent entity) {
        entity.setCover(CmsUrlUtils.getUrl(getPrefix(site), entity.getCover()));
    }

    public void initPlaceCover(SysSite site, CmsPlace entity) {
        entity.setCover(CmsUrlUtils.getUrl(getPrefix(site), entity.getCover()));
    }

    public String getPrefix(SysSite site) {
        return getPrefix(site, false);
    }

    public String getPrefix(SysSite site, Boolean privatefile) {
        if (CommonUtils.notEmpty(uploaderList)) {
            for (FileUploader fileUploader : uploaderList) {
                if (fileUploader.enablePrefix(site.getId(), null != privatefile && privatefile)) {
                    return fileUploader.getPrefix(site.getId(), null != privatefile && privatefile);
                }
            }
        }
        return site.getSitePath();
    }

    private FileUploadResult thumb(short siteId, FileUploadResult fileSize, String filepath, String suffix) {
        if (fileSize.isImage() && null != fileSize.getWidth() && null != fileSize.getHeight()) {
            Map<String, String> config = configDataComponent.getConfigData(siteId, SiteConfigComponent.CONFIG_CODE);
            Integer maxImageWidth = ConfigDataComponent.getInt(config.get(SiteConfigComponent.CONFIG_MAX_IMAGE_WIDTH));
            if (null != maxImageWidth && maxImageWidth > fileSize.getWidth()) {
                int height = fileSize.getHeight() * maxImageWidth / fileSize.getWidth();
                try {
                    ImageUtils.thumb(filepath, filepath, maxImageWidth, height, suffix);
                    fileSize.setWidth(maxImageWidth);
                    fileSize.setHeight(height);
                } catch (IOException e) {
                }
            }
        }
        return fileSize;
    }

    public FileUploadResult upload(short siteId, MultipartFile file, boolean privatefile, String suffix, Locale locale)
            throws IOException {
        String fileName = CmsFileUtils.getUploadFileName(suffix);
        if (CommonUtils.notEmpty(uploaderList)) {
            for (FileUploader fileUploader : uploaderList) {
                if (fileUploader.enableUpload(siteId, privatefile)) {
                    return fileUploader.upload(siteId, file, privatefile, fileName, locale);
                }
            }
        }
        String filepath = privatefile ? siteComponent.getPrivateFilePath(siteId, fileName)
                : siteComponent.getWebFilePath(siteId, fileName);
        Path path = CmsFileUtils.upload(file, filepath);
        if (CmsFileUtils.isSafe(filepath, suffix)) {
            FileUploadResult fileSize = CmsFileUtils.getFileSize(filepath, fileName, suffix);
            return thumb(siteId, fileSize, filepath, suffix);
        } else {
            Files.delete(path);
            throw new IOException(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "verify.custom.file.unsafe"));
        }
    }

    public FileUploadResult upload(short siteId, byte[] file, boolean privatefile, String suffix, Locale locale)
            throws IOException {
        String fileName = CmsFileUtils.getUploadFileName(suffix);
        if (CommonUtils.notEmpty(uploaderList)) {
            for (FileUploader fileUploader : uploaderList) {
                if (fileUploader.enableUpload(siteId, privatefile)) {
                    return fileUploader.upload(siteId, file, privatefile, fileName, locale);
                }
            }
        }
        String filepath = privatefile ? siteComponent.getPrivateFilePath(siteId, fileName)
                : siteComponent.getWebFilePath(siteId, fileName);
        CmsFileUtils.upload(file, filepath);
        if (CmsFileUtils.isSafe(filepath, suffix)) {
            FileUploadResult fileSize = CmsFileUtils.getFileSize(filepath, fileName, suffix);
            return thumb(siteId, fileSize, filepath, suffix);
        } else {
            throw new IOException(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "verify.custom.file.unsafe"));
        }
    }

    public void clearCache(short siteId) {
        if (CommonUtils.notEmpty(uploaderList)) {
            for (FileUploader fileUploader : uploaderList) {
                fileUploader.clear(siteId);
            }
        }
    }

    public Set<String> getCacheCodes() {
        Set<String> result = null;
        if (CommonUtils.notEmpty(uploaderList)) {
            result = new HashSet<>();
            for (FileUploader fileUploader : uploaderList) {
                result.add(fileUploader.getCacheCode());
            }
        }
        return result;
    }
}