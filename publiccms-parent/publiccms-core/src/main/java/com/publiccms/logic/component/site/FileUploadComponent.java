package com.publiccms.logic.component.site;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.api.FileUploader;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.sys.SysSite;
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

    private List<FileUploader> uploaderList;

    @Autowired(required = false)
    public void initUploaderList(List<FileUploader> uploaderList) {
        this.uploaderList = uploaderList;
    }

    public String getPrivateFileUrl(SysSite site, int expiryMinutes, String filepath, String privatekey, String filename) {
        if (CommonUtils.notEmpty(uploaderList)) {
            for (FileUploader fileUploader : uploaderList) {
                if (fileUploader.enablePrefix(site.getId(), true)) {
                    return fileUploader.getPrivateFileUrl(site.getId(), expiryMinutes, filepath);
                }
            }
        }
        long expiry = System.currentTimeMillis() + expiryMinutes * 60 * 1000;
        String string = CmsFileUtils.getPrivateFileSignString(expiry, filepath);
        String sign = VerificationUtils.base64Encode(VerificationUtils.encryptAES(string, privatekey));
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
            return CmsFileUtils.getFileSize(filepath, fileName, suffix);
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
            return CmsFileUtils.getFileSize(filepath, fileName, suffix);
        } else {
            throw new IOException(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "verify.custom.file.unsafe"));
        }
    }
}