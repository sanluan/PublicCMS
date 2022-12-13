package com.publiccms.controller.web.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.SiteConfigComponent;
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
    @Autowired
    protected LogUploadService logUploadService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    private LockComponent lockComponent;
    @Autowired
    protected SiteConfigComponent siteConfigComponent;

    /**
     * @param site
     * @param user
     * @param file
     * @param request
     * @return view name
     */
    @RequestMapping(value = "doUpload", method = RequestMethod.POST)
    @Csrf
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser user, MultipartFile file,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()),
                null);
        if (ControllerUtils.errorCustom("locked.user", locked, result)) {
            lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()), null, true);
            return result;
        }
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(siteConfigComponent.getSafeSuffix(site), suffix)) {
                String fileName = CmsFileUtils.getUploadFileName(suffix);
                String filepath = siteComponent.getWebFilePath(site, fileName);
                try {
                    CmsFileUtils.upload(file, filepath);
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
                                fileType, file.getSize(), fileSize.getWidth(), fileSize.getHeight(),
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
}
