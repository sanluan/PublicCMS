package com.publiccms.common.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * AbstractUeditorController 百度编辑器基类
 * 
 */
public class AbstractUeditorController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    protected LogUploadService logUploadService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected SafeConfigComponent safeConfigComponent;

    protected static final String ACTION_CONFIG = "config";
    protected static final String ACTION_UPLOAD = "upload";
    protected static final String ACTION_UPLOAD_SCRAW = "uploadScraw";
    protected static final String ACTION_CATCHIMAGE = "catchimage";
    protected static final String ACTION_LISTIMAGE = "listimage";
    protected static final String ACTION_LISTFILE = "listfile";
    protected static final String ACTION_LISTVIDEO = "listvideo";

    protected static final String FIELD_NAME = "file";
    protected static final String SCRAW_TYPE = ".jpg";

    protected Map<String, Object> upload(SysSite site, SysUser user, MultipartFile file, String channel,
            HttpServletRequest request) {
        if (null != file) {
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
                        Map<String, Object> map = getResultMap();
                        map.put("size", file.getSize());
                        map.put("title", originalName);
                        map.put("url", fileName);
                        map.put("type", suffix);
                        map.put("original", originalName);
                        return map;
                    } else {
                        CmsFileUtils.delete(filepath);
                        return getResultMap(false, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                                request.getLocale(), "verify.custom.file.unsafe"));
                    }
                } catch (IllegalStateException | IOException e) {
                    log.error(e.getMessage(), e);
                    return getResultMap(false, e.getMessage());
                }
            } else {
                return getResultMap(false, LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                        "verify.custom.fileType"));
            }
        } else {
            return getResultMap(false,
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(), "verify.notEmpty.file"));
        }
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> listfile(SysSite site, SysUser user, String[] fileTyps, Integer start) {
        if (CommonUtils.empty(start)) {
            start = 0;
        }
        PageHandler page = logUploadService.getPage(site.getId(), user.getId(), null, false, fileTyps, null, null, null, null,
                start / 20 + 1, 20);

        Map<String, Object> map = getResultMap();
        List<Map<String, Object>> list = new ArrayList<>();
        for (LogUpload logUpload : ((List<LogUpload>) page.getList())) {
            Map<String, Object> tempMap = getResultMap();
            tempMap.put("url", logUpload.getFilePath());
            tempMap.put("original", logUpload.getOriginalName());
            list.add(tempMap);
        }
        map.put("list", list);
        map.put("start", start);
        map.put("total", page.getTotalCount());
        return map;
    }

    protected static Map<String, Object> getResultMap() {
        return getResultMap(true, null);
    }

    protected static Map<String, Object> getResultMap(boolean success, String message) {
        Map<String, Object> map = new HashMap<>();
        if (success) {
            map.put("state", "SUCCESS");
        } else {
            if (CommonUtils.notEmpty(message)) {
                map.put("state", message);
            } else {
                map.put("state", "error");
            }
        }
        return map;
    }
}
