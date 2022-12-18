package com.publiccms.controller.admin.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.views.pojo.entities.FileSize;

/**
 *
 * CkeditorAdminController
 * 
 */
@Controller
@RequestMapping("ckeditor")
public class CkEditorAdminController {
    @Resource
    protected LogUploadService logUploadService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected SiteConfigComponent siteConfigComponent;

    private static final String RESULT_UPLOADED = "uploaded";
    private static final String RESULT_FILENAME = "fileName";
    private static final String RESULT_URL = "url";

    /**
     * @param site
     * @param admin
     * @param upload
     * @param ckCsrfToken
     * @param csrfToken
     * @param request
     * @return view name
     */
    @RequestMapping("upload")
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile upload,
            String ckCsrfToken, @CookieValue("ckCsrfToken") String csrfToken, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        int uploaded = 0;
        if (null != upload && !upload.isEmpty() && csrfToken.equals(ckCsrfToken)) {
            String originalName = upload.getOriginalFilename();
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(siteConfigComponent.getSafeSuffix(site), suffix)) {
                String fileName = CmsFileUtils.getUploadFileName(suffix);
                String filepath = siteComponent.getWebFilePath(site.getId(), fileName);
                try {
                    CmsFileUtils.upload(upload, filepath);
                    if (CmsFileUtils.isSafe(filepath, suffix)) {
                        FileSize fileSize = CmsFileUtils.getFileSize(filepath, suffix);
                        logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                originalName, CmsFileUtils.getFileType(suffix), upload.getSize(), fileSize.getWidth(),
                                fileSize.getHeight(), RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                        map.put(RESULT_FILENAME, originalName);
                        map.put(RESULT_URL, site.getSitePath() + fileName);
                        uploaded++;
                    } else {
                        Map<String, String> messageMap = new HashMap<>();
                        messageMap.put(CommonConstants.MESSAGE, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                                request.getLocale(), "verify.custom.file.unsafe"));
                        map.put(CommonConstants.ERROR, messageMap);
                        CmsFileUtils.delete(filepath);
                    }
                } catch (IllegalStateException | IOException e) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put(CommonConstants.MESSAGE, e.getMessage());
                    map.put(CommonConstants.ERROR, messageMap);
                }
            } else {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put(CommonConstants.MESSAGE, LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                        "verify.custom.fileType"));
                map.put(CommonConstants.ERROR, messageMap);
            }
        } else {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put(CommonConstants.MESSAGE, 
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(), "verify.notEmpty.file"));
            map.put(CommonConstants.ERROR, messageMap);
        }
        map.put(RESULT_UPLOADED, uploaded);
        return map;
    }
}