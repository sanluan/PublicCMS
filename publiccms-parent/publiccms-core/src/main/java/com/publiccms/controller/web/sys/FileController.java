package com.publiccms.controller.web.sys;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.views.pojo.entities.FileSize;

/**
 *
 * FileAdminController
 *
 */
@Controller
@RequestMapping("file")
public class FileController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private LogUploadService logUploadService;
    @Resource
    private SiteComponent siteComponent;
    @Resource
    private LockComponent lockComponent;
    @Resource
    private SafeConfigComponent safeConfigComponent;
    @Resource
    private ConfigComponent configComponent;

    /**
     * @param site
     * @param user
     * @param privatefile
     * @param captcha
     * @param file
     * @param base64File
     * @param originalFilename
     * @param request
     * @return view name
     */
    @RequestMapping(value = "doUpload", method = RequestMethod.POST)
    @Csrf
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser user, boolean privatefile,
            String captcha, MultipartFile file, String base64File, String originalFilename, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()),
                null);
        if (ControllerUtils.errorCustom("locked.user", locked, result)) {
            lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()), null, true);
            return result;
        }
        if (CommonUtils.notEmpty(captcha)
                || safeConfigComponent.enableCaptcha(site.getId(), SafeConfigComponent.CAPTCHA_MODULE_UPLOAD)) {
            String sessionCaptcha = (String) request.getSession().getAttribute("captcha");
            request.getSession().removeAttribute("captcha");
            if (ControllerUtils.errorCustom("captcha.error", null == sessionCaptcha || !sessionCaptcha.equalsIgnoreCase(captcha),
                    result)) {
                return result;
            }
        }

        if (null != file && !file.isEmpty() || CommonUtils.notEmpty(base64File)) {
            String originalName;
            if (null != file && !file.isEmpty()) {
                originalName = file.getOriginalFilename();
            } else {
                originalName = originalFilename;
            }
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(privatefile ? CmsFileUtils.IMAGE_FILE_SUFFIXS : safeConfigComponent.getSafeSuffix(site),
                    suffix)) {
                try {
                    String fileName = CmsFileUtils.getUploadFileName(suffix);
                    String filepath;
                    if (privatefile) {
                        fileName = CmsFileUtils.getUserPrivateFileName(user.getId(), fileName);
                        filepath = siteComponent.getPrivateFilePath(site.getId(), fileName);
                        if (CommonUtils.notEmpty(base64File)) {
                            CmsFileUtils.upload(VerificationUtils.base64Decode(base64File), filepath);
                        } else {
                            CmsFileUtils.upload(file, filepath);
                        }
                    } else {
                        filepath = siteComponent.getWebFilePath(site.getId(), fileName);
                        String metadataPath = siteComponent.getPrivateFilePath(site.getId(), fileName);
                        if (CommonUtils.notEmpty(base64File)) {
                            CmsFileUtils.upload(VerificationUtils.base64Decode(base64File), filepath, originalName, metadataPath);
                        } else {
                            CmsFileUtils.upload(file, filepath, originalName, metadataPath);
                        }
                    }
                    if (CmsFileUtils.isSafe(filepath, suffix)) {
                        lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()), null,
                                true);
                        result.put("success", true);
                        result.put("fileName", fileName);
                        String fileType = CmsFileUtils.getFileType(suffix);
                        result.put("fileType", fileType);
                        result.put("fileSize", file.getSize());
                        FileSize fileSize = CmsFileUtils.getFileSize(filepath, suffix);
                        logUploadService.save(new LogUpload(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, originalName,
                                privatefile, fileType, file.getSize(), fileSize.getWidth(), fileSize.getHeight(),
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                    } else {
                        result.put("error", LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                                "verify.custom.file.unsafe"));
                    }
                } catch (IllegalStateException | IOException e) {
                    log.error(e.getMessage(), e);
                    result.put("error", e.getMessage());
                }
            } else {
                result.put("error", LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                        "verify.custom.fileType"));
            }
        } else {
            result.put("error",
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(), "verify.notEmpty.file"));
        }
        return result;
    }

    /**
     * @param site
     * @param filePath
     * @param request
     * @param response
     */
    @RequestMapping("download")
    public void download(@RequestAttribute SysSite site, String filePath, HttpServletRequest request,
            HttpServletResponse response) {
        Matcher matcher = CmsFileUtils.UPLOAD_FILE_PATTERN.matcher(filePath);
        if (matcher.matches()) {
            String absolutePath = matcher.group(1);
            String metadataPath = siteComponent.getPrivateFilePath(site.getId(), CmsFileUtils.getMetadataFileName(absolutePath));
            response.setHeader("content-type", "application/octet-stream");
            if (CmsFileUtils.isFile(metadataPath)) {
                try {
                    response.setHeader("content-disposition", "attachment;fileName="
                            + URLEncoder.encode(CmsFileUtils.getFileContent(metadataPath), CommonConstants.DEFAULT_CHARSET_NAME));
                } catch (UnsupportedEncodingException e) {
                }
            } else {
                try {
                    response.setHeader("content-disposition", "attachment;fileName="
                            + URLEncoder.encode(CmsFileUtils.getFileName(absolutePath), CommonConstants.DEFAULT_CHARSET_NAME));
                } catch (UnsupportedEncodingException e) {
                }
            }
            String sendfile = request.getHeader("Sendfile");
            if ("X-Accel-Redirect".equalsIgnoreCase(sendfile)) {
                response.setHeader("X-Accel-Redirect", "/downloadfile/" + absolutePath);
            } else if ("X-Sendfile".equalsIgnoreCase(sendfile)) {
                response.setHeader("X-Sendfile", "/downloadfile/" + absolutePath);
            } else {
                String webfilePath = siteComponent.getWebFilePath(site.getId(), absolutePath);
                if (CmsFileUtils.isFile(webfilePath)) {
                    try (OutputStream outputStream = response.getOutputStream()) {
                        CmsFileUtils.copyFileToOutputStream(webfilePath, outputStream);
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    /**
     * @param site
     * @param expiry
     * @param sign
     * @param filePath
     * @param request
     * @param response
     */
    @RequestMapping("private")
    public void privatefile(@RequestAttribute SysSite site, long expiry, String sign, String filePath, HttpServletRequest request,
            HttpServletResponse response) {
        if (CommonUtils.notEmpty(sign) && expiry > System.currentTimeMillis()) {
            Map<String, String> config = configComponent.getConfigData(site.getId(), SafeConfigComponent.CONFIG_CODE);
            String signKey = config.get(SafeConfigComponent.CONFIG_EXPIRY_MINUTES_SIGN);
            if (null == signKey) {
                signKey = CmsVersion.getClusterId();
            }
            String string = CmsFileUtils.getPrivateFileSignString(expiry, filePath);
            if (string.equalsIgnoreCase(VerificationUtils.decryptAES(VerificationUtils.base64Decode(sign), signKey))) {
                String sendfile = request.getHeader("Sendfile");
                if ("X-Accel-Redirect".equalsIgnoreCase(sendfile)) {
                    response.setHeader("X-Accel-Redirect", "/privatefile/" + filePath);
                } else if ("X-Sendfile".equalsIgnoreCase(sendfile)) {
                    response.setHeader("X-Sendfile", "/privatefile/" + filePath);
                } else {
                    String privatefilePath = siteComponent.getPrivateFilePath(site.getId(), filePath);
                    if (CmsFileUtils.isFile(privatefilePath)) {
                        try (OutputStream outputStream = response.getOutputStream()) {
                            CmsFileUtils.copyFileToOutputStream(privatefilePath, outputStream);
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
    }
}
