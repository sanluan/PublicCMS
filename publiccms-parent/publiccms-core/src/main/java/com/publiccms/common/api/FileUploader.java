package com.publiccms.common.api;

import java.io.IOException;
import java.util.Locale;

import org.springframework.web.multipart.MultipartFile;

import com.publiccms.views.pojo.entities.FileUploadResult;

public interface FileUploader {
    public boolean enable(short siteId, boolean privatefile);

    public int getOrder();

    public String getPrefix(short siteId, boolean privatefile);

    public String getPrivateFileUrl(short siteId, int expiryMinutes, String filepath);

    public FileUploadResult upload(short siteId, MultipartFile file, boolean privatefile, String filepath, Locale locale)
            throws IOException;

    public FileUploadResult upload(short siteId, byte[] file, boolean privatefile, String filepath, Locale locale)
            throws IOException;
}
