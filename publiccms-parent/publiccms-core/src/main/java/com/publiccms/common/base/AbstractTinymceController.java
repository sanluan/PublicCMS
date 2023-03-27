package com.publiccms.common.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.views.pojo.entities.FileSize;

/**
 * AbstractTinymceController tinymce编辑器基类
 * 
 */
public class AbstractTinymceController {
    @Resource
    protected LogUploadService logUploadService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected SafeConfigComponent safeConfigComponent;

    private static final String RESULT_URL = "location";

    /**
     * @param site
     * @param user
     * @param file
     * @param channel
     * @param request
     * @return view name
     */
    protected Map<String, Object> upload(SysSite site, SysUser user, MultipartFile file, String channel,
            HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put(CommonConstants.ERROR, 0);
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(safeConfigComponent.getSafeSuffix(site), suffix)) {
                String fileName = CmsFileUtils.getUploadFileName(suffix);
                String filepath = siteComponent.getWebFilePath(site.getId(), fileName);
                try {
                    CmsFileUtils.upload(file, filepath);
                    if (CmsFileUtils.isSafe(filepath, suffix)) {
                        FileSize fileSize = CmsFileUtils.getFileSize(filepath, suffix);
                        logUploadService.save(new LogUpload(site.getId(), user.getId(), channel, originalName, false,
                                CmsFileUtils.getFileType(suffix), file.getSize(), fileSize.getWidth(), fileSize.getHeight(),
                                RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                        map.put(RESULT_URL, fileName);
                        return map;
                    } else {
                        map.put(CommonConstants.MESSAGE, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                                request.getLocale(), "verify.custom.file.unsafe"));
                        CmsFileUtils.delete(filepath);
                    }
                } catch (IllegalStateException | IOException e) {
                    map.put(CommonConstants.MESSAGE, e.getMessage());
                }
            } else {
                map.put(CommonConstants.MESSAGE, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                        request.getLocale(), "verify.custom.fileType"));
            }
        } else {
            map.put(CommonConstants.MESSAGE,
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(), "verify.notEmpty.file"));
        }
        map.put(CommonConstants.ERROR, 1);
        return map;
    }

    /**
     * @param site
     * @param user
     * @return view name
     */
    @SuppressWarnings("unchecked")
    protected List<Map<String, String>> imageList(SysSite site, SysUser user) {
        PageHandler page = logUploadService.getPage(site.getId(), user.getId(), null, false, CmsFileUtils.IMAGE_FILETYPES, null,
                null, null, null, 1, 20);
        List<Map<String, String>> result = new ArrayList<>();
        for (LogUpload logUpload : ((List<LogUpload>) page.getList())) {
            Map<String, String> map = new HashMap<>();
            map.put("value", logUpload.getFilePath());
            map.put("title", logUpload.getOriginalName());
            result.add(map);
        }
        return result;
    }
}