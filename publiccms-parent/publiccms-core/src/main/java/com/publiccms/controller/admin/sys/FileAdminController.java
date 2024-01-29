package com.publiccms.controller.admin.sys;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.fit.pdfdom.resource.HtmlResource;
import org.fit.pdfdom.resource.HtmlResourceHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DocToHtmlUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;

import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.views.pojo.entities.FileUploadResult;

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
    protected FileUploadComponent fileUploadComponent;
    @Resource
    private ConfigDataComponent configDataComponent;
    @Resource
    protected SafeConfigComponent safeConfigComponent;

    /**
     * @param site
     * @param admin
     * @param privatefile
     * @param file
     * @param base64File
     * @param originalFilename
     * @param field
     * @param originalField
     * @param request
     * @return view name
     */
    @PostMapping("doUpload")
    @Csrf
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, boolean privatefile,
            MultipartFile file, String base64File, String originalFilename, String field, String originalField,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        if (null != file && !file.isEmpty() || CommonUtils.notEmpty(base64File)) {
            String originalName;
            if (null != file && !file.isEmpty()) {
                originalName = file.getOriginalFilename();
            } else {
                originalName = originalFilename;
            }
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(safeConfigComponent.getSafeSuffix(site), suffix)) {
                try {
                    FileUploadResult uploadResult = null;
                    if (CommonUtils.notEmpty(base64File)) {
                        uploadResult = fileUploadComponent.upload(site.getId(), VerificationUtils.base64Decode(base64File),
                                privatefile, suffix, request.getLocale());
                    } else {
                        uploadResult = fileUploadComponent.upload(site.getId(), file, privatefile, suffix, request.getLocale());
                    }
                    result.put("field", field);
                    result.put(field, uploadResult.getFilename());
                    String fileType = CmsFileUtils.getFileType(suffix);
                    result.put("fileType", fileType);
                    result.put("width", uploadResult.getWidth());
                    result.put("height", uploadResult.getHeight());
                    result.put("fileSize", uploadResult.getFileSize());
                    if (CommonUtils.notEmpty(originalField)) {
                        result.put("originalField", originalField);
                        result.put(originalField, originalName);
                    }
                    logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            originalName, privatefile, fileType, uploadResult.getFileSize(), uploadResult.getWidth(),
                            uploadResult.getHeight(), RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                            uploadResult.getFilename()));
                    return result;
                } catch (IOException e) {
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
     * @param overrideTitle
     * @param useIframe
     * @param width
     * @param height
     * @param defaultFontFamily
     * @param field
     * @param titleField
     * @param request
     * @return view name
     */
    @PostMapping("doImport")
    @Csrf
    @ResponseBody
    public Map<String, Object> doImport(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file,
            boolean overrideTitle, boolean useIframe, String width, String height, String defaultFontFamily, String field,
            String titleField, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(safeConfigComponent.getSafeSuffix(site), suffix)) {
                try {

                    if (overrideTitle) {
                        int index = originalName.lastIndexOf(Constants.DOT);
                        if (0 < index) {
                            originalName = originalName.substring(0, index);
                        }
                        result.put(titleField, originalName);
                    }
                    if (".docx".equalsIgnoreCase(suffix) || ".xlsx".equalsIgnoreCase(suffix) || ".ppt".equalsIgnoreCase(suffix)
                            || ".pptx".equalsIgnoreCase(suffix) || ".xls".equalsIgnoreCase(suffix)) {
                        File dest = File.createTempFile("temp_", suffix);
                        file.transferTo(dest);
                        ImageManager imageManager = new ImageManager(new File(""), "") {
                            private String fileName;

                            @Override
                            public void extract(String imagePath, byte[] imageData) throws IOException {
                                String imagesuffix = CmsFileUtils.getSuffix(imagePath);
                                fileName = CmsFileUtils.getUploadFileName(imagesuffix);
                                String filepath = siteComponent.getWebFilePath(site.getId(), fileName);
                                try {
                                    CmsFileUtils.upload(imageData, filepath);
                                    String fileType = CmsFileUtils.getFileType(imagesuffix);
                                    FileUploadResult uploadResult = CmsFileUtils.getFileSize(filepath, fileName, imagesuffix);
                                    logUploadService.save(new LogUpload(site.getId(), admin.getId(),
                                            LogLoginService.CHANNEL_WEB_MANAGER, CmsFileUtils.getFileName(filepath), false,
                                            fileType, imageData.length, uploadResult.getWidth(), uploadResult.getHeight(),
                                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                                } catch (IllegalStateException | IOException e) {
                                    log.error(e.getMessage());
                                }
                            }

                            @Override
                            public String resolve(String uri) {
                                return CommonUtils.joinString(site.getSitePath(), fileName);
                            }
                        };
                        if (".docx".equalsIgnoreCase(suffix)) {
                            result.put(field, DocToHtmlUtils.docxToHtml(dest, imageManager));
                        } else if (".ppt".equalsIgnoreCase(suffix) || ".pptx".equalsIgnoreCase(suffix)) {
                            result.put(field, DocToHtmlUtils.pptToHtml(dest, defaultFontFamily, imageManager));
                        } else {
                            result.put(field, DocToHtmlUtils.excelToHtml(dest, imageManager));
                        }
                        Files.delete(dest.toPath());
                    } else if (".doc".equalsIgnoreCase(suffix)) {
                        File dest = File.createTempFile("temp_", suffix);
                        file.transferTo(dest);
                        result.put(field, DocToHtmlUtils.docToHtml(dest, new PicturesManager() {
                            @Override
                            public String savePicture(byte[] content, PictureType pictureType, String suggestedName,
                                    float widthInches, float heightInches) {
                                String imagesuffix = pictureType.getExtension();
                                if (!suffix.contains(Constants.DOT)) {
                                    imagesuffix = CommonUtils.joinString(Constants.DOT, imagesuffix);
                                }
                                String fileName = CmsFileUtils.getUploadFileName(imagesuffix);
                                String filepath = siteComponent.getWebFilePath(site.getId(), fileName);
                                try {
                                    CmsFileUtils.upload(content, filepath);
                                    String fileType = CmsFileUtils.getFileType(imagesuffix);
                                    FileUploadResult uploadResult = CmsFileUtils.getFileSize(filepath, fileName, imagesuffix);
                                    logUploadService.save(new LogUpload(site.getId(), admin.getId(),
                                            LogLoginService.CHANNEL_WEB_MANAGER, suggestedName, false, fileType, content.length,
                                            uploadResult.getWidth(), uploadResult.getHeight(), RequestUtils.getIpAddress(request),
                                            CommonUtils.getDate(), fileName));
                                    return CommonUtils.joinString(site.getSitePath(), fileName);
                                } catch (IllegalStateException | IOException e) {
                                    log.error(e.getMessage());
                                    return null;
                                }
                            }
                        }));
                        Files.delete(dest.toPath());
                    } else if (".pdf".equalsIgnoreCase(suffix)) {
                        if (useIframe) {
                            String fileName = CmsFileUtils.getUploadFileName(suffix);
                            String filepath = siteComponent.getWebFilePath(site.getId(), fileName);
                            try {
                                CmsFileUtils.upload(file, filepath);
                                String fileType = CmsFileUtils.getFileType(suffix);
                                FileUploadResult uploadResult = CmsFileUtils.getFileSize(filepath, fileName, suffix);
                                logUploadService.save(new LogUpload(site.getId(), admin.getId(),
                                        LogLoginService.CHANNEL_WEB_MANAGER, originalName, false, fileType,
                                        uploadResult.getFileSize(), uploadResult.getWidth(), uploadResult.getHeight(),
                                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));

                                Map<String, String> config = configDataComponent.getConfigData(site.getId(),
                                        SiteConfigComponent.CONFIG_CODE);
                                String pdfViewerUrl = config.get(SiteConfigComponent.CONFIG_PDFVIEWER_URL);
                                if (CommonUtils.empty(pdfViewerUrl)) {
                                    pdfViewerUrl = CommonUtils.joinString(site.getDynamicPath(),
                                            "resource/plugins/pdfjs/viewer.html?file=");
                                }
                                result.put(field,
                                        DocToHtmlUtils.pdfToHtml(
                                                CommonUtils.joinString(pdfViewerUrl,
                                                        CommonUtils
                                                                .encodeURI(CommonUtils.joinString(site.getSitePath(), fileName))),
                                                width, height));
                            } catch (IllegalStateException | IOException e) {
                                log.error(e.getMessage());
                            }
                        } else {
                            File dest = File.createTempFile("temp_", suffix);
                            file.transferTo(dest);
                            result.put(field, DocToHtmlUtils.pdfToHtml(dest, new HtmlResourceHandler() {
                                @Override
                                public String handleResource(HtmlResource resource) throws IOException {
                                    String imagesuffix = resource.getFileEnding();
                                    if (!suffix.contains(Constants.DOT)) {
                                        imagesuffix = CommonUtils.joinString(Constants.DOT, imagesuffix);
                                    }
                                    String fileName = CmsFileUtils.getUploadFileName(imagesuffix);
                                    String filepath = siteComponent.getWebFilePath(site.getId(), fileName);
                                    try {
                                        CmsFileUtils.upload(resource.getData(), filepath);
                                        String fileType = CmsFileUtils.getFileType(imagesuffix);
                                        FileUploadResult uploadResult = CmsFileUtils.getFileSize(filepath, fileName, imagesuffix);
                                        logUploadService.save(new LogUpload(site.getId(), admin.getId(),
                                                LogLoginService.CHANNEL_WEB_MANAGER, resource.getName(), false, fileType,
                                                resource.getData().length, uploadResult.getWidth(), uploadResult.getHeight(),
                                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                                        return CommonUtils.joinString(site.getSitePath(), fileName);
                                    } catch (IllegalStateException | IOException e) {
                                        log.error(e.getMessage());
                                        return null;
                                    }
                                }

                            }));
                            Files.delete(dest.toPath());
                        }
                    }
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
    @PostMapping("doBatchUpload")
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
                if (ArrayUtils.contains(safeConfigComponent.getSafeSuffix(site), suffix)) {
                    try {
                        FileUploadResult uploadResult = fileUploadComponent.upload(site.getId(), file, false, suffix,
                                request.getLocale());
                        result.put("field", field);
                        result.put(field, uploadResult.getFilename());
                        String fileType = CmsFileUtils.getFileType(suffix);
                        result.put("fileType", fileType);
                        result.put("fileSize", uploadResult.getFileSize());
                        result.put("width", uploadResult.getWidth());
                        result.put("height", uploadResult.getHeight());
                        if (CommonUtils.notEmpty(originalField)) {
                            result.put("originalField", originalField);
                            result.put(originalField, originalName);
                        }
                        resultList.add(result);
                        logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                originalName, false, fileType, file.getSize(), uploadResult.getWidth(), uploadResult.getHeight(),
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), uploadResult.getFilename()));
                    } catch (IllegalStateException | IOException e) {
                        log.error(e.getMessage(), e);
                        result.put("statusCode", 300);
                        result.put("message", e.getMessage());
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
