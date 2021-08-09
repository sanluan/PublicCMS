package com.publiccms.controller.admin.sys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
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
    @Autowired
    protected LogUploadService logUploadService;
    @Autowired
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param admin
     * @param file
     * @param base64File
     * @param originalFilename
     * @param field
     * @param originalField
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doUpload", method = RequestMethod.POST)
    @Csrf
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file,
            String base64File, String originalFilename, String field, String originalField, HttpServletRequest request,
            ModelMap model) {
        Map<String, Object> result = new HashMap<>();
        if (null != file && !file.isEmpty() || CommonUtils.notEmpty(base64File)) {
            String originalName;
            String suffix;
            if (null != file) {
                originalName = file.getOriginalFilename();
            } else {
                originalName = originalFilename;
            }
            suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(UeditorAdminController.ALLOW_FILES, suffix)) {
                String fileName = CmsFileUtils.getUploadFileName(suffix);
                String filePath = siteComponent.getWebFilePath(site, fileName);
                try {
                    byte[] fileData = VerificationUtils.base64Decode(base64File);
                    if (null != file) {
                        CmsFileUtils.upload(file, filePath);
                        result.put("fileSize", file.getSize());
                    } else {
                        CmsFileUtils.upload(fileData, filePath);
                        result.put("fileSize", fileData.length);
                    }
                    result.put("field", field);
                    result.put(field, fileName);
                    String fileType = CmsFileUtils.getFileType(suffix);
                    result.put("fileType", fileType);
                    FileSize fileSize = CmsFileUtils.getFileSize(filePath, suffix);
                    result.put("width", fileSize.getWidth());
                    result.put("height", fileSize.getHeight());
                    if (CommonUtils.notEmpty(originalField)) {
                        result.put("originalField", originalField);
                        result.put(originalField, originalName);
                    }
                    logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            originalName, fileType, file.getSize(), fileSize.getWidth(), fileSize.getHeight(),
                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                } catch (IllegalStateException | IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return result;
    }

    /**
     * @param site
     * @param admin
     * @param file
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doImport", method = RequestMethod.POST)
    @Csrf
    @ResponseBody
    public Map<String, Object> doImport(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(UeditorAdminController.ALLOW_FILES, suffix)) {
                try {
                    int index = originalName.lastIndexOf(CommonConstants.DOT);
                    if (0 < index) {
                        originalName = originalName.substring(0, index);
                    }
                    result.put("title", originalName);
                    File dest = File.createTempFile("temp_", suffix);
                    file.transferTo(dest);
                    if (".docx".equalsIgnoreCase(suffix) || ".xlsx".equalsIgnoreCase(suffix) || ".xls".equalsIgnoreCase(suffix)) {
                        ImageManager imageManager = new ImageManager(new File(""), "") {
                            private String fileName;

                            @Override
                            public void extract(String imagePath, byte[] imageData) throws IOException {
                                String imagesuffix = CmsFileUtils.getSuffix(imagePath);
                                fileName = CmsFileUtils.getUploadFileName(imagesuffix);
                                String filePath = siteComponent.getWebFilePath(site, fileName);
                                try {
                                    CmsFileUtils.upload(imageData, filePath);
                                    String fileType = CmsFileUtils.getFileType(imagesuffix);
                                    FileSize fileSize = CmsFileUtils.getFileSize(filePath, imagesuffix);
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
                            result.put("text", DocToHtmlUtils.docxToHtml(dest, imageManager));
                        } else {
                            result.put("text", DocToHtmlUtils.excelToHtml(dest, imageManager));
                        }
                    } else if (".doc".equalsIgnoreCase(suffix)) {
                        result.put("text", DocToHtmlUtils.docToHtml(dest, new PicturesManager() {
                            @Override
                            public String savePicture(byte[] content, PictureType pictureType, String suggestedName,
                                    float widthInches, float heightInches) {
                                String imagesuffix = pictureType.getExtension();
                                if (!suffix.contains(CommonConstants.DOT)) {
                                    imagesuffix = CommonConstants.DOT + imagesuffix;
                                }
                                String fileName = CmsFileUtils.getUploadFileName(imagesuffix);
                                String filePath = siteComponent.getWebFilePath(site, fileName);
                                try {
                                    CmsFileUtils.upload(content, filePath);
                                    String fileType = CmsFileUtils.getFileType(imagesuffix);
                                    FileSize fileSize = CmsFileUtils.getFileSize(filePath, imagesuffix);
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
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    result.put("text", e.getMessage());
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
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doBatchUpload", method = RequestMethod.POST)
    @Csrf
    @ResponseBody
    public List<Map<String, Object>> batchUpload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin,
            MultipartFile[] files, String field, String originalField, HttpServletRequest request, ModelMap model) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (CommonUtils.notEmpty(files)) {
            for (MultipartFile file : files) {
                Map<String, Object> result = new HashMap<>();
                String originalName = file.getOriginalFilename();
                String suffix = CmsFileUtils.getSuffix(originalName);
                if (ArrayUtils.contains(UeditorAdminController.ALLOW_FILES, suffix)) {
                    String fileName = CmsFileUtils.getUploadFileName(suffix);
                    String filePath = siteComponent.getWebFilePath(site, fileName);
                    try {
                        CmsFileUtils.upload(file, filePath);
                        result.put("field", field);
                        result.put(field, fileName);
                        String fileType = CmsFileUtils.getFileType(suffix);
                        result.put("fileType", fileType);
                        result.put("fileSize", file.getSize());
                        FileSize fileSize = CmsFileUtils.getFileSize(filePath, suffix);
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
                }
            }
        }
        return resultList;
    }
}
