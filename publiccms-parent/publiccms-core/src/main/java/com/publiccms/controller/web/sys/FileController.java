package com.publiccms.controller.web.sys;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.views.pojo.entities.FileUploadResult;

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
    private ConfigDataComponent configDataComponent;
    @Resource
    protected FileUploadComponent fileUploadComponent;
    @Resource
    private LocaleResolver localeResolver;

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
    @PostMapping("doUpload")
    @Csrf
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser user, boolean privatefile,
            String captcha, MultipartFile file, String base64File, String originalFilename, HttpServletRequest request) {
        ModelMap result = new ModelMap();
        result.put("success", false);
        if (CommonUtils.notEmpty(captcha)
                || safeConfigComponent.enableCaptcha(site.getId(), SafeConfigComponent.CAPTCHA_MODULE_UPLOAD)) {
            String sessionCaptcha = (String) request.getSession().getAttribute("captcha");
            request.getSession().removeAttribute("captcha");
            if (ControllerUtils.errorCustom("captcha.error", null == sessionCaptcha || !sessionCaptcha.equalsIgnoreCase(captcha),
                    result)) {
                return result;
            }
        }
        boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()),
                null);
        boolean sizeLocked = lockComponent.isLocked(site.getId(),
                privatefile ? LockComponent.ITEM_TYPE_FILEUPLOAD_PRIVATE_SIZE : LockComponent.ITEM_TYPE_FILEUPLOAD_SIZE,
                String.valueOf(user.getId()), null);
        if (ControllerUtils.errorCustom("locked.user", locked, result)) {
            lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()), null, true);
            return result;
        } else if (ControllerUtils.errorCustom("locked.user", sizeLocked, result)) {
            return result;
        }
        lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()), null, true);
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
                    FileUploadResult uploadResult = null;
                    if (CommonUtils.notEmpty(base64File)) {
                        uploadResult = fileUploadComponent.upload(site.getId(), VerificationUtils.base64Decode(base64File),
                                privatefile, suffix, localeResolver.resolveLocale(request));
                    } else {
                        uploadResult = fileUploadComponent.upload(site.getId(), file, privatefile, suffix, localeResolver.resolveLocale(request));
                    }
                    lockComponent.lock(site.getId(),
                            privatefile ? LockComponent.ITEM_TYPE_FILEUPLOAD_PRIVATE_SIZE
                                    : LockComponent.ITEM_TYPE_FILEUPLOAD_SIZE,
                            String.valueOf(user.getId()), null, (int) uploadResult.getFileSize() / 1024);
                    result.put("success", true);
                    result.put("fileName", uploadResult.getFilename());
                    String fileType = CmsFileUtils.getFileType(suffix);
                    result.put("fileType", fileType);
                    result.put("fileSize", uploadResult.getFileSize());
                    logUploadService.save(new LogUpload(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, originalName,
                            privatefile, fileType, uploadResult.getFileSize(), uploadResult.getWidth(), uploadResult.getHeight(),
                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), uploadResult.getFilename()));
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    result.put(CommonConstants.ERROR, e.getMessage());
                }
            } else {
                result.put(CommonConstants.ERROR, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                        localeResolver.resolveLocale(request), "verify.custom.fileType"));
            }
        } else {
            result.put(CommonConstants.ERROR,
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, localeResolver.resolveLocale(request), "verify.notEmpty.file"));
        }
        return result;
    }

    /**
     * @param site
     * @param expiry
     * @param sign
     * @param filePath
     * @param filename
     * @param request
     * @return response entity
     */
    @RequestMapping("private")
    public ResponseEntity<StreamingResponseBody> privatefile(@RequestAttribute SysSite site, long expiry, String sign,
            String filePath, String filename, HttpServletRequest request) {
        if (CommonUtils.notEmpty(sign) && expiry > System.currentTimeMillis()) {
            String signKey = safeConfigComponent.getSignKey(site.getId());
            String string = CmsFileUtils.getPrivateFileSignString(expiry, filePath);
            if (string.equalsIgnoreCase(VerificationUtils.decryptAES(VerificationUtils.base64Decode(sign), signKey))) {
                HttpHeaders headers = new HttpHeaders();
                if (CommonUtils.notEmpty(filename)) {
                    headers.setContentDisposition(
                            ContentDisposition.attachment().filename(filename, Constants.DEFAULT_CHARSET).build());
                }
                String sendfile = request.getHeader(CmsFileUtils.HEADERS_SEND_CTRL);
                if (CmsFileUtils.HEADERS_SEND_NGINX.equalsIgnoreCase(sendfile)) {
                    headers.set(CmsFileUtils.HEADERS_SEND_NGINX,
                            CommonUtils.joinString(CmsFileUtils.NGINX_PRIVATEFILE_PREFIX, filePath));
                    return ResponseEntity.ok().headers(headers).body(null);
                } else if (CmsFileUtils.HEADERS_SEND_APACHE.equalsIgnoreCase(sendfile)) {
                    headers.set(CmsFileUtils.HEADERS_SEND_APACHE,
                            SiteComponent.getFullFileName(site.getId(), filePath).substring(1));
                    return ResponseEntity.ok().headers(headers).body(null);
                } else {
                    String privatefilePath = siteComponent.getPrivateFilePath(site.getId(), filePath);
                    if (CmsFileUtils.isFile(privatefilePath)) {
                        StreamingResponseBody body = new StreamingResponseBody() {
                            @Override
                            public void writeTo(OutputStream outputStream) throws IOException {
                                CmsFileUtils.copyFileToOutputStream(privatefilePath, outputStream);
                            }
                        };
                        return ResponseEntity.ok().headers(headers).body(body);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
