package com.publiccms.controller.admin.sys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.usermodel.PictureType;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DocToHtmlUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.views.pojo.entities.FileSize;

import fr.opensagres.poi.xwpf.converter.core.ImageManager;

/**
 * FileAdminController
 */
@Controller
@RequestMapping("file")
public class FileAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    protected LogUploadService logUploadService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected SiteConfigComponent siteConfigComponent;

    /**
     * @param site
     * @param admin
     * @param file
     * @param base64File
     * @param originalFilename
     * @param field
     * @param originalField
     * @param request
     * @return view name
     */
    @RequestMapping(value = "doUpload", method = RequestMethod.POST)
    @Csrf
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file,
            String base64File, String originalFilename, String field, String originalField, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        if (null != file && !file.isEmpty() || CommonUtils.notEmpty(base64File)) {
            String originalName;
            String suffix;
            if (null != file && !file.isEmpty()) {
                originalName = file.getOriginalFilename();
            } else {
                originalName = originalFilename;
            }
            suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(siteConfigComponent.getSafeSuffix(site), suffix)) {
                String fileName = CmsFileUtils.getUploadFileName(suffix);
                String filepath = siteComponent.getWebFilePath(site, fileName);
                try {
                    if (CommonUtils.notEmpty(base64File)) {
                        CmsFileUtils.upload(VerificationUtils.base64Decode(base64File), filepath);
                    } else {
                        CmsFileUtils.upload(file, filepath);
                    }
                    result.put("field", field);
                    result.put(field, fileName);
                    String fileType = CmsFileUtils.getFileType(suffix);
                    result.put("fileType", fileType);
                    FileSize fileSize = CmsFileUtils.getFileSize(filepath, suffix);
                    result.put("width", fileSize.getWidth());
                    result.put("height", fileSize.getHeight());
                    result.put("fileSize", fileSize.getFileSize());
                    if (CommonUtils.notEmpty(originalField)) {
                        result.put("originalField", originalField);
                        result.put(originalField, originalName);
                    }
                    logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            originalName, fileType, fileSize.getFileSize(), fileSize.getWidth(), fileSize.getHeight(),
                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                    return result;
                } catch (IllegalStateException | IOException e) {
                    log.error(e.getMessage(), e);
                    result.put("statusCode", 300);
                    result.put("message", e.getMessage());
                    result.put(field, "");
                    if (CommonUtils.notEmpty(originalField)) {
                        result.put(originalField, null);
                    }
                }
            } else {
                result.put("statusCode", 300);
                result.put("message", LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                        "verify.custom.fileType"));
                result.put(field, "");
                if (CommonUtils.notEmpty(originalField)) {
                    result.put(originalField, null);
                }
            }
        } else {
            result.put("statusCode", 300);
            result.put("message",
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(), "verify.notEmpty.file"));
            result.put(field, "");
            if (CommonUtils.notEmpty(originalField)) {
                result.put(originalField, null);
            }
        }
        return result;
    }

    /**
     * @param site
     * @param admin
     * @param file
     * @param field 
     * @param titleField 
     * @param request
     * @return view name
     */
    @RequestMapping(value = "doImport", method = RequestMethod.POST)
    @Csrf
    @ResponseBody
    public Map<String, Object> doImport(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file,
            String field, String titleField, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(siteConfigComponent.getSafeSuffix(site), suffix)) {
                try {
                    int index = originalName.lastIndexOf(CommonConstants.DOT);
                    if (0 < index) {
                        originalName = originalName.substring(0, index);
                    }
                    result.put(titleField, originalName);
                    File dest = File.createTempFile("temp_", suffix);
                    file.transferTo(dest);
                    if (".docx".equalsIgnoreCase(suffix) || ".xlsx".equalsIgnoreCase(suffix) || ".xls".equalsIgnoreCase(suffix)) {
                        ImageManager imageManager = new ImageManager(new File(""), "") {
                            private String fileName;

                            @Override
                            public void extract(String imagePath, byte[] imageData) throws IOException {
                                String imagesuffix = CmsFileUtils.getSuffix(imagePath);
                                fileName = CmsFileUtils.getUploadFileName(imagesuffix);
                                String filepath = siteComponent.getWebFilePath(site, fileName);
                                try {
                                    CmsFileUtils.upload(imageData, filepath);
                                    String fileType = CmsFileUtils.getFileType(imagesuffix);
                                    FileSize fileSize = CmsFileUtils.getFileSize(filepath, imagesuffix);
                                    logUploadService.save(new LogUpload(site.getId(), admin.getId(),
                                            LogLoginService.CHANNEL_WEB_MANAGER, new File(imagePath).getName(), fileType,
                                            imageData.length, fileSize.getWidth(), fileSize.getHeight(),
                                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                                } catch (IllegalStateException | IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public String resolve(String uri) {
                                return site.getSitePath() + fileName;
                            }
                        };
                        if (".docx".equalsIgnoreCase(suffix)) {
                            result.put(field, DocToHtmlUtils.docxToHtml(dest, imageManager));
                        } else {
                            result.put(field, DocToHtmlUtils.excelToHtml(dest, imageManager));
                        }
                    } else if (".doc".equalsIgnoreCase(suffix)) {
                        result.put(field, DocToHtmlUtils.docToHtml(dest, new PicturesManager() {
                            @Override
                            public String savePicture(byte[] content, PictureType pictureType, String suggestedName,
                                    float widthInches, float heightInches) {
                                String imagesuffix = pictureType.getExtension();
                                if (!suffix.contains(CommonConstants.DOT)) {
                                    imagesuffix = CommonConstants.DOT + imagesuffix;
                                }
                                String fileName = CmsFileUtils.getUploadFileName(imagesuffix);
                                String filepath = siteComponent.getWebFilePath(site, fileName);
                                try {
                                    CmsFileUtils.upload(content, filepath);
                                    String fileType = CmsFileUtils.getFileType(imagesuffix);
                                    FileSize fileSize = CmsFileUtils.getFileSize(filepath, imagesuffix);
                                    logUploadService.save(new LogUpload(site.getId(), admin.getId(),
                                            LogLoginService.CHANNEL_WEB_MANAGER, suggestedName, fileType, content.length,
                                            fileSize.getWidth(), fileSize.getHeight(), RequestUtils.getIpAddress(request),
                                            CommonUtils.getDate(), fileName));
                                    return site.getSitePath() + fileName;
                                } catch (IllegalStateException | IOException e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            }

                        }));
                    }
                    dest.delete();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    result.put(field, e.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * @param site
     * @param admin
     * @param files
     * @param field
     * @param originalField
     * @param request
     * @return view name
     */
    @RequestMapping(value = "doBatchUpload", method = RequestMethod.POST)
    @Csrf
    @ResponseBody
    public List<Map<String, Object>> batchUpload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin,
            MultipartFile[] files, String field, String originalField, HttpServletRequest request) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (CommonUtils.notEmpty(files)) {
            for (MultipartFile file : files) {
                Map<String, Object> result = new HashMap<>();
                String originalName = file.getOriginalFilename();
                String suffix = CmsFileUtils.getSuffix(originalName);
                if (ArrayUtils.contains(siteConfigComponent.getSafeSuffix(site), suffix)) {
                    String fileName = CmsFileUtils.getUploadFileName(suffix);
                    String filepath = siteComponent.getWebFilePath(site, fileName);
                    try {
                        CmsFileUtils.upload(file, filepath);
                        result.put("field", field);
                        result.put(field, fileName);
                        String fileType = CmsFileUtils.getFileType(suffix);
                        result.put("fileType", fileType);
                        result.put("fileSize", file.getSize());
                        FileSize fileSize = CmsFileUtils.getFileSize(filepath, suffix);
                        result.put("width", fileSize.getWidth());
                        result.put("height", fileSize.getHeight());
                        if (CommonUtils.notEmpty(originalField)) {
                            result.put("originalField", originalField);
                            result.put(originalField, originalName);
                        }
                        resultList.add(result);
                        logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                originalName, fileType, file.getSize(), fileSize.getWidth(), fileSize.getHeight(),
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                    } catch (IllegalStateException | IOException e) {
                        log.error(e.getMessage(), e);
                    }
                } else {
                    result.put("statusCode", 300);
                    result.put("message", LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                            "verify.custom.fileType"));
                    result.put(field, "");
                    if (CommonUtils.notEmpty(originalField)) {
                        result.put(originalField, null);
                    }
                    resultList.add(result);
                }
            }
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("statusCode", 300);
            result.put("message",
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(), "verify.notEmpty.file"));
            result.put(field, "");
            if (CommonUtils.notEmpty(originalField)) {
                result.put(originalField, null);
            }
            resultList.add(result);
        }

        return resultList;
    }
}
