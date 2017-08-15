package org.publiccms.controller.admin.sys;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.http.util.EntityUtils.consume;
import static org.publiccms.logic.service.log.LogLoginService.CHANNEL_WEB_MANAGER;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogUpload;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.site.FileComponent;
import org.publiccms.logic.service.log.LogUploadService;
import org.publiccms.views.pojo.UeditorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.handler.PageHandler;

/**
 *
 * UeditorAdminController
 * 
 */
@Controller
@RequestMapping("ueditor")
public class UeditorAdminController extends AbstractController {
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    protected LogUploadService logUploadService;

    private static final String ACTION_CONFIG = "config";
    private static final String ACTION_UPLOAD = "upload";
    private static final String ACTION_UPLOAD_SCRAW = "uploadScraw";
    private static final String ACTION_CATCHIMAGE = "catchimage";
    private static final String ACTION_LISTFILE = "listfile";

    private static final String FIELD_NAME = "file";
    private static final String SCRAW_TYPE = ".jpg";

    private static final String[] IMAGE_ALLOW_FILES = new String[] { ".png", ".jpg", ".jpeg", ".gif", ".bmp" };

    private static final String[] VIDEO_ALLOW_FILES = new String[] { ".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg",
            ".mpg", ".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid" };
    private static final String[] ALLOW_FILES = addAll(addAll(VIDEO_ALLOW_FILES, IMAGE_ALLOW_FILES),
            new String[] { ".rar", ".zip", ".tar", ".gz", ".7z", ".bz2", ".cab", ".iso", ".doc", ".docx", ".xls", ".xlsx", ".ppt",
                    ".pptx", ".pdf", ".txt", ".md", ".xml" });
    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("image/gif", ".gif");
            put("image/jpeg", ".jpg");
            put("image/jpg", ".jpg");
            put("image/png", ".png");
            put("image/bmp", ".bmp");
        }
    };

    /**
     * @param request
     * @return
     */
    @RequestMapping(params = "action=" + ACTION_CONFIG)
    @ResponseBody
    public UeditorConfig config(HttpServletRequest request) {
        String urlPrefix = getSite(request).getSitePath();
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
     * @param file
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(params = "action=" + ACTION_UPLOAD)
    public String upload(MultipartFile file, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String suffix = fileComponent.getSuffix(originalName);
            String fileName = fileComponent.getUploadFileName(suffix);
            try {
                fileComponent.upload(file, siteComponent.getWebFilePath(site, fileName));
                logUploadService.save(new LogUpload(site.getId(), getAdminFromSession(session).getId(), CHANNEL_WEB_MANAGER,
                        false, file.getSize(), getIpAddress(request), getDate(), fileName));
                Map<String, Object> map = getResultMap(true);
                map.put("size", file.getSize());
                map.put("title", originalName);
                map.put("url", fileName);
                map.put("type", suffix);
                map.put("original", originalName);
                model.put("result", map);
            } catch (IllegalStateException | IOException e) {
                model.put("result", getResultMap(false));
            }
        } else {
            model.put("result", getResultMap(false));
        }
        return "common/mapResult";
    }

    /**
     * @param file
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(params = "action=" + ACTION_UPLOAD_SCRAW)
    @ResponseBody
    public Map<String, Object> uploadScraw(String file, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(file)) {
            byte[] data = decodeBase64(file);
            String fileName = fileComponent.getUploadFileName(SCRAW_TYPE);
            try {
                File dest = new File(siteComponent.getWebFilePath(site, fileName));
                writeByteArrayToFile(dest, data);
                dest.setReadable(true, false);
                dest.setWritable(true, false);
                logUploadService.save(new LogUpload(site.getId(), getAdminFromSession(session).getId(), CHANNEL_WEB_MANAGER, true,
                        dest.length(), getIpAddress(request), getDate(), fileName));
                Map<String, Object> map = getResultMap(true);
                map.put("size", data.length);
                map.put("title", dest.getName());
                map.put("url", fileName);
                map.put("type", SCRAW_TYPE);
                map.put("original", "scraw" + SCRAW_TYPE);
                return map;
            } catch (IllegalStateException | IOException e) {
                log.error(e.getMessage());
                return getResultMap(false);
            }
        }
        return getResultMap(false);
    }

    /**
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(params = "action=" + ACTION_CATCHIMAGE)
    @ResponseBody
    public Map<String, Object> catchimage(HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
            String[] files = request.getParameterValues(FIELD_NAME + "[]");
            if (notEmpty(files)) {
                List<Map<String, Object>> list = new ArrayList<>();
                for (String image : files) {
                    HttpGet httpget = new HttpGet(image);
                    CloseableHttpResponse response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    if (null != entity) {
                        String suffix = null;
                        if (notEmpty(entity.getContentType().getElements())) {
                            suffix = CONTENT_TYPE_MAP.get(entity.getContentType().getElements()[0].getName());
                        }
                        if (empty(suffix)) {
                            suffix = ".jpg";
                        }
                        String fileName = fileComponent.getUploadFileName(suffix);
                        File dest = new File(siteComponent.getWebFilePath(site, fileName));
                        copyInputStreamToFile(entity.getContent(), dest);
                        dest.setReadable(true, false);
                        dest.setWritable(true, false);
                        logUploadService.save(new LogUpload(site.getId(), getAdminFromSession(session).getId(),
                                CHANNEL_WEB_MANAGER, true, dest.length(), getIpAddress(request), getDate(), fileName));
                        Map<String, Object> map = getResultMap(true);
                        map.put("size", entity.getContentLength());
                        map.put("title", dest.getName());
                        map.put("url", fileName);
                        map.put("source", image);
                        list.add(map);
                    }
                    consume(entity);
                }
                Map<String, Object> map = getResultMap(true);
                map.put("list", list);
                return map;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return getResultMap(false);
        }
        return getResultMap(false);
    }

    /**
     * @param start
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(params = "action=" + ACTION_LISTFILE)
    @ResponseBody
    public Map<String, Object> listfile(Integer start, HttpServletRequest request, HttpSession session) {
        if (empty(start)) {
            start = 0;
        }
        PageHandler page = logUploadService.getPage(getSite(request).getId(), getAdminFromSession(session).getId(), null, null,
                null, null, null, start / 20 + 1, 20);

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

    private Map<String, Object> getResultMap(boolean success) {
        Map<String, Object> map = new HashMap<>();
        if (success) {
            map.put("state", "SUCCESS");
        } else {
            map.put("state", "error");
        }
        return map;
    }
}