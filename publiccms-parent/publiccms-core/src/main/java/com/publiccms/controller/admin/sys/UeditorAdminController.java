package com.publiccms.controller.admin.sys;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.views.pojo.entities.FileSize;
import com.publiccms.views.pojo.entities.UeditorConfig;

/**
 *
 * UeditorAdminController
 * 
 */
@Controller
@RequestMapping("ueditor")
public class UeditorAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    protected LogUploadService logUploadService;
    @Autowired
    protected SiteComponent siteComponent;

    protected static final String ACTION_CONFIG = "config";
    protected static final String ACTION_UPLOAD = "upload";
    protected static final String ACTION_UPLOAD_SCRAW = "uploadScraw";
    protected static final String ACTION_CATCHIMAGE = "catchimage";
    protected static final String ACTION_LISTFILE = "listfile";

    protected static final String FIELD_NAME = "file";
    protected static final String SCRAW_TYPE = ".jpg";

    protected static final String[] IMAGE_ALLOW_FILES = new String[] { ".png", ".jpg", ".jpeg", ".gif", ".bmp" };

    protected static final String[] VIDEO_ALLOW_FILES = new String[] { ".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg",
            ".mpg", ".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid" };
    public static final String[] ALLOW_FILES = ArrayUtils.addAll(ArrayUtils.addAll(VIDEO_ALLOW_FILES, IMAGE_ALLOW_FILES),
            new String[] { ".rar", ".zip", ".tar", ".gz", ".7z", ".bz2", ".cab", ".iso", ".doc", ".docx", ".xls", ".xlsx", ".ppt",
                    ".pptx", ".pdf", ".txt", ".md", ".xml" });

    /**
     * @param request
     * @return view name
     */
    @RequestMapping(params = "action=" + ACTION_CONFIG)
    @ResponseBody
    public UeditorConfig config(HttpServletRequest request) {
        String urlPrefix = siteComponent.getSite(request.getServerName()).getSitePath();
        UeditorConfig config = new UeditorConfig();
        config.setImageActionName(ACTION_UPLOAD);
        config.setSnapscreenActionName(ACTION_UPLOAD);
        config.setScrawlActionName(ACTION_UPLOAD_SCRAW);
        config.setVideoActionName(ACTION_UPLOAD);
        config.setFileActionName(ACTION_UPLOAD);
        config.setCatcherActionName(ACTION_CATCHIMAGE);
        config.setImageManagerActionName(ACTION_LISTFILE);
        config.setFileManagerActionName(ACTION_LISTFILE);
        config.setImageFieldName(FIELD_NAME);
        config.setScrawlFieldName(FIELD_NAME);
        config.setCatcherFieldName(FIELD_NAME);
        config.setVideoFieldName(FIELD_NAME);
        config.setFileFieldName(FIELD_NAME);
        config.setImageUrlPrefix(urlPrefix);
        config.setScrawlUrlPrefix(urlPrefix);
        config.setSnapscreenUrlPrefix(urlPrefix);
        config.setCatcherUrlPrefix(urlPrefix);
        config.setVideoUrlPrefix(urlPrefix);
        config.setFileUrlPrefix(urlPrefix);
        config.setImageManagerUrlPrefix(urlPrefix);
        config.setFileManagerUrlPrefix(urlPrefix);
        config.setImageAllowFiles(IMAGE_ALLOW_FILES);
        config.setCatcherAllowFiles(IMAGE_ALLOW_FILES);
        config.setVideoAllowFiles(VIDEO_ALLOW_FILES);
        config.setFileAllowFiles(ALLOW_FILES);
        config.setImageManagerAllowFiles(IMAGE_ALLOW_FILES);
        config.setFileManagerAllowFiles(ALLOW_FILES);
        return config;
    }

    /**
     * @param site
     * @param admin
     * @param file
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(params = "action=" + ACTION_UPLOAD)
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file,
            HttpServletRequest request, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(ALLOW_FILES, suffix)) {
                String fileName = CmsFileUtils.getUploadFileName(suffix);
                String filePath = siteComponent.getWebFilePath(site, fileName);
                try {
                    CmsFileUtils.upload(file, filePath);
                    FileSize fileSize = CmsFileUtils.getFileSize(filePath, suffix);
                    logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            originalName, CmsFileUtils.getFileType(suffix), file.getSize(), fileSize.getWidth(),
                            fileSize.getHeight(), RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                    Map<String, Object> map = getResultMap(true);
                    map.put("size", file.getSize());
                    map.put("title", originalName);
                    map.put("url", fileName);
                    map.put("type", suffix);
                    map.put("original", originalName);
                    return map;
                } catch (IllegalStateException | IOException e) {
                }
            }
        }
        return getResultMap(false);
    }

    /**
     * @param site
     * @param admin
     * @param file
     * @param request
     * @param session
     * @return view name
     */
    @RequestMapping(params = "action=" + ACTION_UPLOAD_SCRAW)
    @ResponseBody
    public Map<String, Object> uploadScraw(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String file,
            HttpServletRequest request, HttpSession session) {
        if (CommonUtils.notEmpty(file)) {
            byte[] data = VerificationUtils.base64Decode(file);
            String fileName = CmsFileUtils.getUploadFileName(SCRAW_TYPE);
            String filePath = siteComponent.getWebFilePath(site, fileName);
            try {
                CmsFileUtils.writeByteArrayToFile(filePath, data);
                FileSize fileSize = CmsFileUtils.getFileSize(filePath, SCRAW_TYPE);
                logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        CommonConstants.BLANK, CmsFileUtils.FILE_TYPE_IMAGE, data.length, fileSize.getWidth(),
                        fileSize.getHeight(), RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                Map<String, Object> map = getResultMap(true);
                map.put("size", data.length);
                map.put("title", fileName);
                map.put("url", fileName);
                map.put("type", SCRAW_TYPE);
                map.put("original", "scraw" + SCRAW_TYPE);
                return map;
            } catch (IllegalStateException | IOException e) {
                log.error(e.getMessage(), e);
                return getResultMap(false);
            }
        }
        return getResultMap(false);
    }

    /**
     * @param site
     * @param admin
     * @param request
     * @param session
     * @return view name
     */
    @RequestMapping(params = "action=" + ACTION_CATCHIMAGE)
    @ResponseBody
    public Map<String, Object> catchimage(@RequestAttribute SysSite site, @SessionAttribute SysUser admin,
            HttpServletRequest request, HttpSession session) {
        try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(CommonConstants.defaultRequestConfig)
                .build();) {
            String[] files = request.getParameterValues(FIELD_NAME + "[]");
            if (CommonUtils.notEmpty(files)) {
                List<Map<String, Object>> list = new ArrayList<>();
                for (String image : files) {
                    HttpGet httpget = new HttpGet(image);
                    CloseableHttpResponse response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    if (null != entity) {
                        BufferedInputStream inputStream = new BufferedInputStream(entity.getContent());
                        FileType fileType = FileTypeDetector.detectFileType(inputStream);
                        String suffix = fileType.getCommonExtension();
                        if (CommonUtils.notEmpty(suffix)) {
                            String fileName = CmsFileUtils.getUploadFileName(suffix);
                            String filePath = siteComponent.getWebFilePath(site, fileName);
                            CmsFileUtils.copyInputStreamToFile(inputStream, filePath);
                            FileSize fileSize = CmsFileUtils.getFileSize(filePath, suffix);
                            logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                    CommonConstants.BLANK, CmsFileUtils.getFileType(suffix), entity.getContentLength(),
                                    fileSize.getWidth(), fileSize.getHeight(), RequestUtils.getIpAddress(request),
                                    CommonUtils.getDate(), fileName));
                            Map<String, Object> map = getResultMap(true);
                            map.put("size", entity.getContentLength());
                            map.put("title", fileName);
                            map.put("url", fileName);
                            map.put("source", image);
                            list.add(map);
                        }

                    }
                    EntityUtils.consume(entity);
                }
                Map<String, Object> map = getResultMap(true);
                map.put("list", list);
                return map;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResultMap(false);
        }
        return getResultMap(false);
    }

    /**
     * @param admin
     * @param start
     * @param request
     * @param session
     * @return view name
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(params = "action=" + ACTION_LISTFILE)
    @ResponseBody
    public Map<String, Object> listfile(@SessionAttribute SysUser admin, Integer start, HttpServletRequest request,
            HttpSession session) {
        if (CommonUtils.empty(start)) {
            start = 0;
        }
        PageHandler page = logUploadService.getPage(siteComponent.getSite(request.getServerName()).getId(), admin.getId(), null,
                null, null, null, null, null, start / 20 + 1, 20);

        Map<String, Object> map = getResultMap(true);
        List<Map<String, Object>> list = new ArrayList<>();
        for (LogUpload logUpload : ((List<LogUpload>) page.getList())) {
            Map<String, Object> tempMap = getResultMap(true);
            tempMap.put("url", logUpload.getFilePath());
            list.add(tempMap);
        }
        map.put("list", list);
        map.put("start", start);
        map.put("total", page.getTotalCount());
        return map;
    }

    protected static Map<String, Object> getResultMap(boolean success) {
        Map<String, Object> map = new HashMap<>();
        if (success) {
            map.put("state", "SUCCESS");
        } else {
            map.put("state", "error");
        }
        return map;
    }
}