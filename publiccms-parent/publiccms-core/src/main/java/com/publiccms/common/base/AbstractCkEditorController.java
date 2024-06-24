package com.publiccms.common.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.views.pojo.entities.FileUploadResult;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * AbstractCkEditorController Ck编辑器基类
 * 
 */
public class AbstractCkEditorController {
    @Resource
    protected LogUploadService logUploadService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected SafeConfigComponent safeConfigComponent;
    @Resource
    protected FileUploadComponent fileUploadComponent;
    @Resource
    protected LocaleResolver localeResolver;

    private static final String RESULT_UPLOADED = "uploaded";
    private static final String RESULT_FILENAME = "fileName";
    private static final String RESULT_URL = "url";

    protected Map<String, Object> upload(SysSite site, SysUser user, MultipartFile upload, String ckCsrfToken, String channel,
            String csrfToken, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        int uploaded = 0;
        if (null != upload && !upload.isEmpty() && csrfToken.equals(ckCsrfToken)) {
            String originalName = upload.getOriginalFilename();
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(safeConfigComponent.getSafeSuffix(site), suffix)) {
                try {
                    FileUploadResult uploadResult = fileUploadComponent.upload(site.getId(), upload, false, suffix,
                            localeResolver.resolveLocale(request));
                    logUploadService.save(new LogUpload(site.getId(), user.getId(), channel, originalName, false,
                            CmsFileUtils.getFileType(suffix), upload.getSize(), uploadResult.getWidth(), uploadResult.getHeight(),
                            RequestUtils.getIpAddress(request), CommonUtils.getDate(), uploadResult.getFilename()));
                    map.put(RESULT_FILENAME, originalName);
                    map.put(RESULT_URL,
                            CommonUtils.joinString(fileUploadComponent.getPrefix(site), uploadResult.getFilename()));
                    uploaded++;
                } catch (IOException e) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put(CommonConstants.MESSAGE, e.getMessage());
                    map.put(CommonConstants.ERROR, messageMap);
                }
            } else {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put(CommonConstants.MESSAGE, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                        localeResolver.resolveLocale(request), "verify.custom.fileType"));
                map.put(CommonConstants.ERROR, messageMap);
            }
        } else {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put(CommonConstants.MESSAGE,
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, localeResolver.resolveLocale(request), "verify.notEmpty.file"));
            map.put(CommonConstants.ERROR, messageMap);
        }
        map.put(RESULT_UPLOADED, uploaded);
        return map;
    }
}
