package com.publiccms.controller.admin.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
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
@RequestMapping("kindeditor")
public class KindEditorAdminController {
    @Resource
    protected LogUploadService logUploadService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected SiteConfigComponent siteConfigComponent;

    private static final String RESULT_URL = "url";

    /**
     * @param site
     * @param admin
     * @param imgFile
     * @param request
     * @return view name
     */
    @RequestMapping("upload")
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile imgFile,
            HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put(CommonConstants.ERROR, 0);
        if (null != imgFile && !imgFile.isEmpty()) {
            String originalName = imgFile.getOriginalFilename();
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(siteConfigComponent.getSafeSuffix(site), suffix)) {
                String fileName = CmsFileUtils.getUploadFileName(suffix);
                String filePath = siteComponent.getWebFilePath(site, fileName);
                try {
                    CmsFileUtils.upload(imgFile, filePath);
                    FileSize fileSize = CmsFileUtils.getFileSize(filePath, suffix);
                    logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            originalName, CmsFileUtils.getFileType(suffix), imgFile.getSize(), fileSize.getWidth(),
                            fileSize.getHeight(), RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                    map.put(RESULT_URL, site.getSitePath() + fileName);
                    return map;
                } catch (IllegalStateException | IOException e) {
                    map.put(CommonConstants.MESSAGE, e.getMessage());
                }
            } else {
                map.put(CommonConstants.MESSAGE, "unsafe file");
            }
        } else {
            map.put(CommonConstants.MESSAGE, "no file");
        }
        map.put(CommonConstants.ERROR, 1);
        return map;
    }
}